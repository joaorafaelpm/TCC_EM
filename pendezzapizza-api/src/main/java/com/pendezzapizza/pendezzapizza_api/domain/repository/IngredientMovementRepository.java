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
    /** Movimentação agregada por ingrediente, de :periodStart até agora (base do "antes e depois"). */
    interface MovementSummaryRow {
        UUID getIngredientId();

        Long getNetSinceStart();   // soma assinada de tudo desde o início do período até agora

        Long getNetSinceEnd();     // soma assinada de tudo desde o fim do período até agora

        Long getEntries();         // compras no período

        Long getOutputs();         // saídas de pedido no período

        Long getReversals();       // estornos no período

        Long getAdjustments();     // ajustes no período (com sinal)
    }

    @Query("""
            SELECT m.ingredient.id AS ingredientId,
                   SUM(CASE WHEN m.type = :out THEN -m.quantity ELSE m.quantity END) AS netSinceStart,
                   SUM(CASE WHEN m.createdAt >= :periodEnd
                            THEN (CASE WHEN m.type = :out THEN -m.quantity ELSE m.quantity END)
                            ELSE 0L END) AS netSinceEnd,
                   SUM(CASE WHEN m.createdAt < :periodEnd AND m.type = :entry THEN m.quantity ELSE 0L END) AS entries,
                   SUM(CASE WHEN m.createdAt < :periodEnd AND m.type = :out THEN m.quantity ELSE 0L END) AS outputs,
                   SUM(CASE WHEN m.createdAt < :periodEnd AND m.type = :reversal THEN m.quantity ELSE 0L END) AS reversals,
                   SUM(CASE WHEN m.createdAt < :periodEnd AND m.type = :adjustment THEN m.quantity ELSE 0L END) AS adjustments
            FROM IngredientMovement m
            WHERE m.ingredient.restaurant.id = :restaurantId
              AND m.createdAt >= :periodStart
            GROUP BY m.ingredient.id
            """)
    List<MovementSummaryRow> movementSummaryRaw(@Param("restaurantId") UUID restaurantId,
                                                @Param("periodStart") Instant periodStart,
                                                @Param("periodEnd") Instant periodEnd,
                                                @Param("entry") MovementType entry,
                                                @Param("out") MovementType out,
                                                @Param("reversal") MovementType reversal,
                                                @Param("adjustment") MovementType adjustment);

    default List<MovementSummaryRow> movementSummary(UUID restaurantId, Instant start, Instant end) {
        return movementSummaryRaw(restaurantId, start, end,
                MovementType.ENTRADA, MovementType.SAIDA_PEDIDO, MovementType.ESTORNO, MovementType.AJUSTE);
    }
}