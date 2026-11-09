package com.pendezzapizza.pendezzapizza_api.domain.service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.pendezzapizza.pendezzapizza_api.domain.exception.BusinessException;
import com.pendezzapizza.pendezzapizza_api.domain.model.IngredientMovement;
import com.pendezzapizza.pendezzapizza_api.domain.model.Order;
import com.pendezzapizza.pendezzapizza_api.domain.model.OrderItem;
import com.pendezzapizza.pendezzapizza_api.domain.model.RecipeItem;
import com.pendezzapizza.pendezzapizza_api.domain.model.enums.MovementType;
import com.pendezzapizza.pendezzapizza_api.domain.model.enums.OrderStatus;
import com.pendezzapizza.pendezzapizza_api.domain.repository.IngredientMovementRepository;
import com.pendezzapizza.pendezzapizza_api.domain.repository.IngredientRepository;
import com.pendezzapizza.pendezzapizza_api.domain.repository.RecipeItemRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor 
public class StockService {

    private final IngredientRepository ingredientRepository;
    private final RecipeItemRepository recipeItemRepository;
    private final IngredientMovementRepository movementRepository;

    /**
     * Registra a compra/entrada de um ingrediente (quantidade na unidade base: g,
     * ml ou un).
     * A validação de que o ingrediente pertence ao restaurante do usuário fica na
     * camada de API.
     */
    @Transactional
    public void registerEntry(UUID ingredientId, long quantity) {
        if (quantity <= 0) {
            throw new BusinessException("A quantidade de entrada deve ser maior que zero.");
        }
        if (ingredientRepository.increment(ingredientId, quantity) == 0) {
            throw new BusinessException("Ingrediente não encontrado.");
        }
        saveMovement(ingredientId, MovementType.ENTRADA, quantity, null);
    }

    /**
     * Baixa os ingredientes consumidos pelo pedido. Deve ser chamado DEPOIS de
     * salvar o pedido
     * (precisa do id) e dentro da mesma transação: se faltar estoque, lança
     * BusinessException
     * e o pedido inteiro sofre rollback.
     * Produtos sem receita não consomem estoque.
     */
    @Transactional
    public void consumeForOrder(Order order) {
        Map<UUID, List<RecipeItem>> recipesByProduct = loadRecipesByProduct(order);
        if (recipesByProduct.isEmpty()) {
            return;
        }

        // Soma o consumo por ingrediente no pedido inteiro (itens diferentes podem usar
        // a mesma farinha).
        // TreeMap => ordem estável por id, o que evita deadlock entre pedidos
        // concorrentes.
        Map<UUID, Long> consumption = new TreeMap<>();
        for (OrderItem item : order.getItems()) {
            int quantity = item.getQuantity() == null ? 0 : item.getQuantity();
            if (quantity <= 0) {
                continue;
            }
            for (RecipeItem recipe : recipesByProduct.getOrDefault(item.getProduct().getId(), List.of())) {
                consumption.merge(recipe.getIngredient().getId(), recipe.getQuantityPerUnit() * quantity, Long::sum);
            }
        }

        for (Map.Entry<UUID, Long> entry : consumption.entrySet()) {
            UUID ingredientId = entry.getKey();
            long needed = entry.getValue();

            // Update atômico: 0 linhas afetadas = saldo insuficiente
            if (ingredientRepository.decrementIfAvailable(ingredientId, needed) == 0) {
                throw new BusinessException(insufficientStockMessage(order, recipesByProduct, ingredientId));
            }
            saveMovement(ingredientId, MovementType.SAIDA_PEDIDO, needed, order);
        }
    }

    /**
     * Devolve ao estoque o que o pedido consumiu, a partir do histórico (não da
     * receita atual,
     * que pode ter mudado). Idempotente: se o pedido já foi estornado, não faz
     * nada.
     */
    @Transactional
    public void restoreForOrder(Order order) {
        UUID orderId = order.getId();

        if (movementRepository.existsByOrderIdAndType(orderId, MovementType.ESTORNO)) {
            return;
        }

        List<IngredientMovement> consumed = movementRepository
                .findByOrderIdAndType(orderId, MovementType.SAIDA_PEDIDO).stream()
                .sorted(Comparator.comparing(m -> m.getIngredient().getId()))
                .toList();

        for (IngredientMovement movement : consumed) {
            UUID ingredientId = movement.getIngredient().getId();
            ingredientRepository.increment(ingredientId, movement.getQuantity());
            saveMovement(ingredientId, MovementType.ESTORNO, movement.getQuantity(), movement.getOrder());
        }
    }

    private Map<UUID, List<RecipeItem>> loadRecipesByProduct(Order order) {
        Set<UUID> productIds = order.getItems().stream()
                .map(item -> item.getProduct().getId())
                .collect(Collectors.toSet());

        if (productIds.isEmpty()) {
            return Map.of();
        }

        return recipeItemRepository.findByProductIds(productIds).stream()
                .collect(Collectors.groupingBy(recipe -> recipe.getProduct().getId()));
    }

    /** Devolve o estoque (se ainda não devolvido) e remove as movimentações do pedido, para ele poder ser excluído. */
    @Transactional
    public void releaseOrder(Order order) {
        if (order.getOrderStatus() == OrderStatus.CREATED) {
            restoreForOrder(order);
        }
        movementRepository.deleteByOrderId(order.getId());
    }

    /**
     * Mensagem para o cliente: cita os produtos afetados, não os ingredientes
     * (informação interna).
     */
    private String insufficientStockMessage(Order order, Map<UUID, List<RecipeItem>> recipesByProduct,
            UUID ingredientId) {
        String products = order.getItems().stream()
                .filter(item -> recipesByProduct.getOrDefault(item.getProduct().getId(), List.of()).stream()
                        .anyMatch(recipe -> recipe.getIngredient().getId().equals(ingredientId)))
                .map(item -> item.getProduct().getName())
                .distinct()
                .collect(Collectors.joining(", "));

        return String.format("Estoque insuficiente para: %s. Reduza a quantidade ou escolha outro item.", products);
    }

    private void saveMovement(UUID ingredientId, MovementType type, long quantity, Order order) {
        IngredientMovement movement = new IngredientMovement();
        movement.setIngredient(ingredientRepository.getReferenceById(ingredientId));
        movement.setType(type);
        movement.setQuantity(quantity);
        movement.setOrder(order);
        movementRepository.save(movement);
    }
}
