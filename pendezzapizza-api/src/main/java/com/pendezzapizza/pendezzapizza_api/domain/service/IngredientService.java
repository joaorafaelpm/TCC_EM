package com.pendezzapizza.pendezzapizza_api.domain.service;

import java.util.List;
import java.util.UUID;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pendezzapizza.pendezzapizza_api.domain.exception.BusinessException;
import com.pendezzapizza.pendezzapizza_api.domain.exception.EntityInUseException;
import com.pendezzapizza.pendezzapizza_api.domain.model.Ingredient;
import com.pendezzapizza.pendezzapizza_api.domain.model.Restaurant;
import com.pendezzapizza.pendezzapizza_api.domain.model.enums.UnitType;
import com.pendezzapizza.pendezzapizza_api.domain.repository.IngredientRepository;

import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;

/**
 * Cadastro de ingredientes e entradas de estoque.
 *
 * IMPORTANTE: adicione @DynamicUpdate (org.hibernate.annotations) na entidade
 * Ingredient.
 * Sem isso, ao editar nome/mínimo o Hibernate regrava também stock_quantity com
 * o valor
 * lido antes, podendo desfazer uma baixa feita por um pedido no meio tempo.
 */
@Service
@AllArgsConstructor
@Transactional
public class IngredientService {

    private static final String NOT_FOUND = "Ingrediente não encontrado.";

    private final IngredientRepository ingredientRepository;
    private final StockService stockService;
    private final EntityManager entityManager;

    @Transactional(readOnly = true)
    public List<Ingredient> findAll(UUID restaurantId) {
        return ingredientRepository.findByRestaurantIdOrderByNameAsc(restaurantId);
    }

    @Transactional(readOnly = true)
    public List<Ingredient> findLowStock(UUID restaurantId) {
        return ingredientRepository.findLowStock(restaurantId);
    }

    /** Busca o ingrediente garantindo que ele pertence ao restaurante da URL. */
    @Transactional(readOnly = true)
    public Ingredient findOwned(UUID restaurantId, UUID ingredientId) {
        Ingredient ingredient = ingredientRepository.findById(ingredientId)
                .orElseThrow(() -> new BusinessException(NOT_FOUND));
        if (!ingredient.getRestaurant().getId().equals(restaurantId)) {
            throw new BusinessException(NOT_FOUND);
        }
        return ingredient;
    }
    private RuntimeException translateIntegrityViolation(DataIntegrityViolationException e) {
        String cause = String.valueOf(e.getMostSpecificCause().getMessage());
        if (cause.contains("uk_ingredient_restaurant_name")) {
            return new BusinessException("Já existe um ingrediente com esse nome neste restaurante.");
        }
        return e;
    }

    private void assertRestaurantExists(UUID restaurantId) {
        boolean exists = entityManager.createQuery(
                        "SELECT COUNT(r) > 0 FROM Restaurant r WHERE r.id = :id", Boolean.class)
                .setParameter("id", restaurantId)
                .getSingleResult();
        if (!exists) {
            throw new BusinessException("Restaurante não encontrado.");
        }
    }
    public Ingredient create(UUID restaurantId, Ingredient ingredient, long initialQuantity) {
        validate(ingredient.getName(), ingredient.getUnit(), ingredient.getLowStockThreshold());
        if (initialQuantity < 0) {
            throw new BusinessException("A quantidade inicial não pode ser negativa.");
        }
        assertRestaurantExists(restaurantId);

        ingredient.setRestaurant(entityManager.getReference(Restaurant.class, restaurantId));
        ingredient.setName(ingredient.getName().trim());
        ingredient.setStockQuantity(0);

        try {
            ingredientRepository.saveAndFlush(ingredient); // gera o id: precisa vir ANTES da entrada
        } catch (DataIntegrityViolationException e) {
            throw translateIntegrityViolation(e);
        }

        if (initialQuantity > 0) {
            stockService.registerEntry(ingredient.getId(), initialQuantity);
            entityManager.refresh(ingredient);
        }
        return ingredient;
    }

    public Ingredient update(UUID restaurantId, UUID ingredientId, Ingredient changes) {
        Ingredient ingredient = findOwned(restaurantId, ingredientId);
        validate(changes.getName(), ingredient.getUnit(), changes.getLowStockThreshold());
        ingredient.setName(changes.getName().trim());
        ingredient.setLowStockThreshold(changes.getLowStockThreshold());
        try {
            return ingredientRepository.saveAndFlush(ingredient);
        } catch (DataIntegrityViolationException e) {
            throw translateIntegrityViolation(e);
        }
    }

    /** Registra a compra/entrada (quantidade na unidade base: g, ml ou un). */
    public Ingredient addEntry(UUID restaurantId, UUID ingredientId, long quantity) {
        Ingredient ingredient = findOwned(restaurantId, ingredientId);
        stockService.registerEntry(ingredientId, quantity);
        entityManager.refresh(ingredient);
        return ingredient;
    }

    public void delete(UUID restaurantId, UUID ingredientId) {
        findOwned(restaurantId, ingredientId);
        try {
            ingredientRepository.deleteById(ingredientId);
            ingredientRepository.flush();
        } catch (DataIntegrityViolationException e) {
            // já usado em ficha técnica ou com histórico de movimentações
            throw new EntityInUseException(ingredientId);
        }
    }

    private void validate(String name, UnitType unit, long lowStockThreshold) {
        if (name == null || name.isBlank()) {
            throw new BusinessException("O nome do ingrediente é obrigatório.");
        }
        if (unit == null) {
            throw new BusinessException("A unidade do ingrediente é obrigatória (G, ML ou UN).");
        }
        if (lowStockThreshold < 0) {
            throw new BusinessException("O estoque mínimo não pode ser negativo.");
        }
    }
}