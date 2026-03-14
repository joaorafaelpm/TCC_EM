package com.pendezzapizza.pendezzapizza_api.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class OrderItem {

    @Id
    @GeneratedValue
    @EqualsAndHashCode.Include
    @JdbcTypeCode(SqlTypes.BINARY)
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;
    private String note;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    public BigDecimal calculateTotalPrice() {
        BigDecimal price = this.getUnitPrice();
        Integer qtt = this.getQuantity();

        if (price == null) {
            price = BigDecimal.ZERO;
        }

        if (qtt == null) {
            qtt = 0;
        }

        BigDecimal total = price.multiply(BigDecimal.valueOf(qtt));
        setTotalPrice(total);

        return total;
    }
}
