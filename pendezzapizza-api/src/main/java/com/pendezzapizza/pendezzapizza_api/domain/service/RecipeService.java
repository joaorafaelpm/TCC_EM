package com.pendezzapizza.pendezzapizza_api.domain.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pendezzapizza.pendezzapizza_api.domain.exception.BusinessException;
import com.pendezzapizza.pendezzapizza_api.domain.model.Ingredient;
import com.pendezzapizza.pendezzapizza_api.domain.model.Product;
import com.pendezzapizza.pendezzapizza_api.domain.model.RecipeItem;
import com.pendezzapizza.pendezzapizza_api.domain.repository.RecipeItemRepository;

import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;

/**
 * Ficha técnica: quanto de cada ingrediente (na unidade base) uma unidade do
 * produto consome.
 *
 * Validação que fica na camada de API: o produto pertencer ao restaurante do
 * usuário.
 */
@Service
@AllArgsConstructor
@Transactional
public class RecipeService {

    private final RecipeItemRepository recipeItemRepository;
    private final IngredientService ingredientService;
    private final EntityManager entityManager;

   @Transactional(readOnly = true)
    public List<RecipeItem> findByProduct(UUID restaurantId, UUID productId) {
        assertProductOwned(restaurantId, productId);
        return recipeItemRepository.findByProductIdWithIngredient(productId);
    }


    /**
     * Cria o item da receita ou atualiza a quantidade se o ingrediente já estiver
     * nela.
     */
    public RecipeItem save(UUID restaurantId, UUID productId, UUID ingredientId, long quantityPerUnit) {
        assertProductOwned(restaurantId, productId);
        if (quantityPerUnit <= 0) {
            throw new BusinessException("A quantidade por unidade deve ser maior que zero.");
        }
        Ingredient ingredient = ingredientService.findOwned(restaurantId, ingredientId);

        RecipeItem item = recipeItemRepository.findByProductIdAndIngredientId(productId, ingredientId)
                .orElseGet(() -> {
                    RecipeItem created = new RecipeItem();
                    created.setProduct(entityManager.getReference(Product.class, productId));
                    created.setIngredient(ingredient);
                    return created;
                });
        item.setQuantityPerUnit(quantityPerUnit);
        return recipeItemRepository.save(item);
    }

    public void remove(UUID restaurantId, UUID productId, UUID ingredientId) {
        assertProductOwned(restaurantId, productId);
        recipeItemRepository.findByProductIdAndIngredientId(productId, ingredientId)
                .ifPresent(recipeItemRepository::delete);
    }

    private void assertProductOwned(UUID restaurantId, UUID productId) {
    boolean owned = entityManager.createQuery(
            "SELECT COUNT(p) > 0 FROM Product p WHERE p.id = :pid AND p.restaurant.id = :rid",
            Boolean.class)
        .setParameter("pid", productId)
        .setParameter("rid", restaurantId)
        .getSingleResult();
    if (!owned) {
        throw new BusinessException("Produto não encontrado.");
    }
}
}