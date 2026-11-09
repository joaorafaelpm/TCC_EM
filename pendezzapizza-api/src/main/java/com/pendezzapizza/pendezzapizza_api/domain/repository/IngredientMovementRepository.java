package com.pendezzapizza.pendezzapizza_api.domain.repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pendezzapizza.pendezzapizza_api.domain.model.IngredientMovement;
import com.pendezzapizza.pendezzapizza_api.domain.model.enums.MovementType;

public interface IngredientMovementRepository extends JpaRepository<IngredientMovement, UUID> {

    List<IngredientMovement> findByOrderIdAndType(UUID orderId, MovementType type);

    boolean existsByOrderIdAndType(UUID orderId, MovementType type);

    /**
     * Linha do relatório de consumo: quanto de cada ingrediente saiu no período.
     */
    interface ConsumptionRow {
        UUID getIngredientId();

        Long getConsumed();
    }

    /**
     * Consumo líquido por ingrediente no período: saídas de pedido menos estornos.
     * Entradas e ajustes não contam como consumo.
     */
    @Query("""
            SELECT m.ingredient.id AS ingredientId,
                   SUM(CASE
                         WHEN m.type = com.pendezzapizza.pendezzapizza_api.domain.model.enums.MovementType.SAIDA_PEDIDO THEN m.quantity
                         WHEN m.type = com.pendezzapizza.pendezzapizza_api.domain.model.enums.MovementType.ESTORNO THEN -m.quantity
                         ELSE 0L
                       END) AS consumed
            FROM IngredientMovement m
            WHERE m.ingredient.restaurant.id = :restaurantId
              AND m.createdAt >= :periodStart
              AND m.createdAt < :periodEnd
            GROUP BY m.ingredient.id
            """)
    List<ConsumptionRow> consumptionByIngredient(@Param("restaurantId") UUID restaurantId,
            @Param("periodStart") Instant periodStart,
            @Param("periodEnd") Instant periodEnd);

    @Modifying(flushAutomatically = true)
    @Query("DELETE FROM IngredientMovement m WHERE m.order.id = :orderId")
    int deleteByOrderId(@Param("orderId") UUID orderId);
}