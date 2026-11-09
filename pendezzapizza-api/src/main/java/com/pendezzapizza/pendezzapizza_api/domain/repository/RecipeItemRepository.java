package com.pendezzapizza.pendezzapizza_api.domain.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pendezzapizza.pendezzapizza_api.domain.model.RecipeItem;

public interface RecipeItemRepository extends JpaRepository<RecipeItem, UUID> {

    /**
     * Receitas de todos os produtos informados, já com o ingrediente carregado
     * (evita N+1).
     */
    @Query("SELECT r FROM RecipeItem r JOIN FETCH r.ingredient WHERE r.product.id IN :productIds")
    List<RecipeItem> findByProductIds(@Param("productIds") Collection<UUID> productIds);

    /** Ficha técnica de um produto, com o ingrediente carregado. */
    @Query("SELECT r FROM RecipeItem r JOIN FETCH r.ingredient i WHERE r.product.id = :productId ORDER BY i.name")
    List<RecipeItem> findByProductIdWithIngredient(@Param("productId") UUID productId);

    Optional<RecipeItem> findByProductIdAndIngredientId(UUID productId, UUID ingredientId);

    /**
     * Todas as fichas técnicas dos ingredientes de um restaurante (produto e
     * ingrediente carregados).
     */
    @Query("SELECT r FROM RecipeItem r JOIN FETCH r.ingredient i JOIN FETCH r.product p "
            + "WHERE i.restaurant.id = :restaurantId")
    List<RecipeItem> findAllByRestaurantId(@Param("restaurantId") UUID restaurantId);
}