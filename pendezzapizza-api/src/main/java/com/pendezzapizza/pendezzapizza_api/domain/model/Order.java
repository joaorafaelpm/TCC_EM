package com.pendezzapizza.pendezzapizza_api.domain.model;

import com.pendezzapizza.pendezzapizza_api.domain.event.ConfirmationOrderEvent;
import com.pendezzapizza.pendezzapizza_api.domain.event.OrderCancellationEvent;
import com.pendezzapizza.pendezzapizza_api.domain.exception.BusinessException;
import com.pendezzapizza.pendezzapizza_api.domain.model.enuns.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.domain.AbstractAggregateRoot;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Entity
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true , callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "`order`")
public class Order extends AbstractAggregateRoot<Order> {

    @Id
    @GeneratedValue
    @EqualsAndHashCode.Include
    @JdbcTypeCode(SqlTypes.BINARY)
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    private BigDecimal subtotal ;
    private BigDecimal shippingFee ;
    private BigDecimal totalCost ;

    @CreationTimestamp
    private OffsetDateTime creationDate;
    private OffsetDateTime confirmationDate ;
    private OffsetDateTime cancellationDate;
    private OffsetDateTime deliveryDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status" , nullable = false)
    private OrderStatus orderStatus = OrderStatus.CREATED ;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private PaymentMethod paymentMethods ;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Restaurant restaurant;

    @ManyToOne
    @JoinColumn(nullable = false , name = "client_user_id")
    private User customer ;

    @Embedded
    private Address deliveryAddress ;

    @OneToMany(mappedBy = "order" , cascade = CascadeType.ALL)
    private List<OrderItem> items = new ArrayList<>();

    public void calculateTotalOrderCost() {
        getItems().forEach(OrderItem::calculateTotalPrice);

        this.subtotal = getItems().stream()
                .map(OrderItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        this.totalCost = this.subtotal.add(this.shippingFee);
    }


    public void confirm () {
        setOrderStatus(OrderStatus.CONFIRMED);
        setConfirmationDate(OffsetDateTime.now());

        registerEvent(new ConfirmationOrderEvent(this));
    }
    public void deliver () {
        setOrderStatus(OrderStatus.DELIVERED);
        setDeliveryDate(OffsetDateTime.now());
    }
    public void cancel () {
        setOrderStatus(OrderStatus.CANCELED);
        setCancellationDate(OffsetDateTime.now());

        registerEvent(new OrderCancellationEvent(this));
    }

    public boolean canBeConfirmed() {
        return getOrderStatus().canChangeTo(OrderStatus.CONFIRMED);
    }
    public boolean canBeDelivered() {
        return getOrderStatus().canChangeTo(OrderStatus.DELIVERED);
    }
    public boolean canBeCanceled() {
        return getOrderStatus().canChangeTo(OrderStatus.CANCELED);
    }

    private void setOrderStatus(OrderStatus newStatus){
        if (getOrderStatus().cannotChangeTo(newStatus)) {
            throw new BusinessException(String.format(
                    "Status do pedido '%s' não pode ser alterado de '%s' para '%s'" ,
                    getId(), getOrderStatus().getDescription() , newStatus.getDescription()
            ));
        }

        this.orderStatus = newStatus;
    }


}