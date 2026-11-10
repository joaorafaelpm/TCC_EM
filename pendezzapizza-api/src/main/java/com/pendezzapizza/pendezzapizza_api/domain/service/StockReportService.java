package com.pendezzapizza.pendezzapizza_api.domain.service;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pendezzapizza.pendezzapizza_api.domain.model.RecipeItem;
import com.pendezzapizza.pendezzapizza_api.domain.model.enums.UnitType;
import com.pendezzapizza.pendezzapizza_api.domain.repository.IngredientMovementRepository;
import com.pendezzapizza.pendezzapizza_api.domain.repository.IngredientRepository;
import com.pendezzapizza.pendezzapizza_api.domain.repository.RecipeItemRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class StockReportService {

    private final IngredientRepository ingredientRepository;
    private final IngredientMovementRepository movementRepository;
    private final RecipeItemRepository recipeItemRepository;

    /**
     * Linha do relatório de reposição (tudo na unidade base do ingrediente).
     * consumed = quanto saiu no período (saídas - estornos)
     * suggestedPurchase = quanto comprar para repor o consumo e ficar acima do
     * estoque mínimo
     */
    public record ReplenishmentLine(UUID ingredientId, String name, UnitType unit, long stock,
            long lowStockThreshold, long consumed, long suggestedPurchase) {
    }

    /** Quantas unidades do produto ainda dá para produzir com o estoque atual. */
    public record ProductCapacity(UUID productId, String productName, long portions) {
    }

    /**
     * Relatório de reposição do período. Fórmula da sugestão (fácil de trocar):
     * max(0, consumo do período + estoque mínimo - estoque atual)
     */
    public List<ReplenishmentLine> replenishment(UUID restaurantId, Instant start, Instant end) {
        Map<UUID, Long> consumedByIngredient = movementRepository
                .consumptionByIngredient(restaurantId, start, end).stream()
                .collect(Collectors.toMap(
                        IngredientMovementRepository.ConsumptionRow::getIngredientId,
                        row -> row.getConsumed() == null ? 0L : row.getConsumed()));

        return ingredientRepository.findByRestaurantIdOrderByNameAsc(restaurantId).stream()
                .map(ingredient -> {
                    long consumed = Math.max(0L, consumedByIngredient.getOrDefault(ingredient.getId(), 0L));
                    long suggested = Math.max(0L,
                            consumed + ingredient.getLowStockThreshold() - ingredient.getStockQuantity());
                    return new ReplenishmentLine(ingredient.getId(), ingredient.getName(), ingredient.getUnit(),
                            ingredient.getStockQuantity(), ingredient.getLowStockThreshold(), consumed, suggested);
                })
                .sorted(Comparator.comparingLong(ReplenishmentLine::suggestedPurchase).reversed()
                        .thenComparing(ReplenishmentLine::name))
                .toList();
    }

    /**
     * Capacidade de produção por produto: o ingrediente mais escasso da receita
     * limita a quantidade.
     * Cada produto é calculado isoladamente (ingredientes compartilhados entre
     * produtos não são
     * divididos entre eles). Produtos sem ficha técnica não aparecem.
     */
    public List<ProductCapacity> capacity(UUID restaurantId) {
        Map<UUID, List<RecipeItem>> recipesByProduct = recipeItemRepository.findAllByRestaurantId(restaurantId)
                .stream()
                .collect(Collectors.groupingBy(recipe -> recipe.getProduct().getId()));

        return recipesByProduct.values().stream()
                .map(items -> {
                    long portions = items.stream()
                            .mapToLong(r -> r.getIngredient().getStockQuantity() / r.getQuantityPerUnit())
                            .min().orElse(0L);
                    return new ProductCapacity(items.get(0).getProduct().getId(),
                            items.get(0).getProduct().getName(), portions);
                })
                .sorted(Comparator.comparing(ProductCapacity::productName))
                .toList();
    }

    /**
     * Linha do resumo "antes e depois" (tudo na unidade base do ingrediente).
     * closingBalance = openingBalance + entries - consumed + adjustments
     * consumed = saídas de pedido - estornos (pode ficar negativo se estornos > saídas no período)
     */
    public record StockSummaryLine(UUID ingredientId, String name, UnitType unit,
                                   long openingBalance, long entries, long consumed, long adjustments,
                                   long closingBalance, long currentStock, long lowStockThreshold, long suggestedPurchase) {
    }

    public List<StockSummaryLine> summary(UUID restaurantId, Instant start, Instant end) {
        Map<UUID, IngredientMovementRepository.MovementSummaryRow> rows = movementRepository
                .movementSummary(restaurantId, start, end).stream()
                .collect(Collectors.toMap(
                        IngredientMovementRepository.MovementSummaryRow::getIngredientId,
                        row -> row));

        return ingredientRepository.findByRestaurantIdOrderByNameAsc(restaurantId).stream()
                .map(ingredient -> {
                    IngredientMovementRepository.MovementSummaryRow row = rows.get(ingredient.getId());
                    long current = ingredient.getStockQuantity();

                    long netSinceStart = row == null ? 0L : nz(row.getNetSinceStart());
                    long netSinceEnd = row == null ? 0L : nz(row.getNetSinceEnd());
                    long entries = row == null ? 0L : nz(row.getEntries());
                    long consumed = row == null ? 0L : nz(row.getOutputs()) - nz(row.getReversals());
                    long adjustments = row == null ? 0L : nz(row.getAdjustments());

                    // mesma fórmula do relatório de reposição
                    long suggested = Math.max(0L,
                            Math.max(0L, consumed) + ingredient.getLowStockThreshold() - current);

                    return new StockSummaryLine(ingredient.getId(), ingredient.getName(), ingredient.getUnit(),
                            current - netSinceStart, entries, consumed, adjustments,
                            current - netSinceEnd, current, ingredient.getLowStockThreshold(), suggested);
                })
                .toList();
    }

    private static long nz(Long value) {
        return value == null ? 0L : value;
    }
}