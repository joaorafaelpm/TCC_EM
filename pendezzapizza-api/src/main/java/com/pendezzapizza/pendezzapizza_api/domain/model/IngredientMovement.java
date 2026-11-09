package com.pendezzapizza.pendezzapizza_api.domain.model;

import java.time.Instant;
import java.util.UUID;

import com.pendezzapizza.pendezzapizza_api.domain.model.enums.MovementType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true) // Apenas o campo "id" (marcado com @EqualsAndHashCode.Include) entra
                                                  // na comparação
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "ingredient_movement")
public class IngredientMovement { // histórico (ledger)

    @Id
    @GeneratedValue
    @EqualsAndHashCode.Include
    @JdbcTypeCode(SqlTypes.BINARY)
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ingredient_id", nullable = false)
    @ToString.Exclude
    private Ingredient ingredient;

    // ENTRADA, SAIDA_PEDIDO, ESTORNO, AJUSTE
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private MovementType type;

    // ENTRADA/SAIDA_PEDIDO/ESTORNO: positivo. AJUSTE: pode ser negativo (regras no
    // CHECK da migration).
    @Column(nullable = false)
    private long quantity;

    // Obrigatório em SAIDA_PEDIDO/ESTORNO; nulo em ENTRADA/AJUSTE
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    @ToString.Exclude
    private Order order;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    void onCreate() {
        if (createdAt == null) {
            createdAt = Instant.now();
        }
    }
}