package com.pendezzapizza.pendezzapizza_api.domain.model;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@NoArgsConstructor
@Table(name = "recipe_item")
public class RecipeItem { // ficha técnica

    @Id
    @GeneratedValue
    @EqualsAndHashCode.Include
    @JdbcTypeCode(SqlTypes.BINARY)
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    @ToString.Exclude
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ingredient_id", nullable = false)
    @ToString.Exclude
    private Ingredient ingredient;

    // Ex.: 500 (g) por pizza. Unique (product_id, ingredient_id) garantido na
    // migration.
    @Column(name = "quantity_per_unit", nullable = false)
    private long quantityPerUnit;
}