package com.pendezzapizza.pendezzapizza_api.domain.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class ProductPhoto {

    @Id
    @GeneratedValue
    @EqualsAndHashCode.Include
    @JdbcTypeCode(SqlTypes.BINARY)
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    private Product product;

    @Column(name = "archive_name")
    private String fileName;

    private String description;

    @Column(name = "content_type")
    private String contentType;

    private Long size;

    public UUID getRestaurantId() {
        if (getProduct() != null && getProduct().getRestaurant() != null) {
            return getProduct().getRestaurant().getId();
        }
        return null;
    }
}
