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
public class RestaurantPhoto {

    @Id
    @GeneratedValue
    @EqualsAndHashCode.Include
    @JdbcTypeCode(SqlTypes.BINARY)
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    private Restaurant restaurant;

    @Column(name = "archive_name")
    private String fileName;

    private String description;

    @Column(name = "content_type")
    private String contentType;

    private Long size;

//    @UpdateTimestamp
//    @Column(name = "update_date")
//    private OffsetDateTime updateDate ;

    public UUID getRestaurantId() {
        if (getRestaurant() != null && getRestaurant().getId() != null) {
            return getRestaurant().getId();
        }
        return null;
    }
}
