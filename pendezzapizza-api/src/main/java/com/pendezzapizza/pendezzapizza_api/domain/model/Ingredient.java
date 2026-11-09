package com.pendezzapizza.pendezzapizza_api.domain.model;

import java.util.UUID;

import org.hibernate.annotations.DynamicUpdate;

import com.pendezzapizza.pendezzapizza_api.domain.model.enums.UnitType;

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
@DynamicUpdate
@NoArgsConstructor
@Table(name = "ingredient")
public class Ingredient {

    @Id
    @GeneratedValue
    @EqualsAndHashCode.Include
    @JdbcTypeCode(SqlTypes.BINARY)
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    // Pertence a um restaurante
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @ToString.Exclude
    private Restaurant restaurant;

    @Column(nullable = false, length = 120)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private UnitType unit; // G, ML ou UN

    // Sempre na unidade base (g, ml ou un).
    // Alterar o saldo apenas via update atômico no repositório, não pelo setter.
    @Column(name = "stock_quantity", nullable = false)
    private long stockQuantity;

    @Column(name = "low_stock_threshold", nullable = false)
    private long lowStockThreshold;
}