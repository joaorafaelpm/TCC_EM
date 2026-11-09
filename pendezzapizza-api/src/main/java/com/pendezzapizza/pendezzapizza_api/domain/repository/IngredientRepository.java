package com.pendezzapizza.pendezzapizza_api.domain.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pendezzapizza.pendezzapizza_api.domain.model.Ingredient;

public interface IngredientRepository extends JpaRepository<Ingredient, UUID> {

    /**
     * Baixa atômica: só altera se houver saldo suficiente.
     * Retorna o número de linhas afetadas (0 = estoque insuficiente ou ingrediente
     * inexistente).
     *
     * flushAutomatically = true: envia ao banco o que estiver pendente (ex.: o
     * pedido recém-salvo).
     * clearAutomatically NÃO é usado de propósito: limparia o contexto e
     * desanexaria o pedido em andamento.
     */
    @Modifying(flushAutomatically = true)
    @Query("UPDATE Ingredient i SET i.stockQuantity = i.stockQuantity - :qty "
            + "WHERE i.id = :id AND i.stockQuantity >= :qty")
    int decrementIfAvailable(@Param("id") UUID id, @Param("qty") long qty);

    /**
     * Soma ao saldo (entradas e estornos). Retorna 0 se o ingrediente não existir.
     */
    @Modifying(flushAutomatically = true)
    @Query("UPDATE Ingredient i SET i.stockQuantity = i.stockQuantity + :qty WHERE i.id = :id")
    int increment(@Param("id") UUID id, @Param("qty") long qty);

    /** Todos os ingredientes do restaurante, em ordem alfabética. */
    List<Ingredient> findByRestaurantIdOrderByNameAsc(UUID restaurantId);

    /** Ingredientes com saldo igual ou abaixo do mínimo configurado. */
    @Query("SELECT i FROM Ingredient i WHERE i.restaurant.id = :restaurantId "
            + "AND i.stockQuantity <= i.lowStockThreshold ORDER BY i.name")
    List<Ingredient> findLowStock(@Param("restaurantId") UUID restaurantId);
}