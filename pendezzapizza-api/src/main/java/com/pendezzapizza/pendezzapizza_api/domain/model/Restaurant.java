package com.pendezzapizza.pendezzapizza_api.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Restaurant {

    @Id
    @GeneratedValue
    @EqualsAndHashCode.Include
    @JdbcTypeCode(SqlTypes.BINARY)
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(name = "shipping_fee", nullable = false)
    private BigDecimal shippingFee;

    @Embedded
    private Address address;

    private Boolean active = Boolean.TRUE;
    private Boolean open = Boolean.FALSE;

    @CreationTimestamp
    @Column(columnDefinition = "datetime", name = "registration_date")
    private OffsetDateTime registrationDate;

    @UpdateTimestamp
    @Column(columnDefinition = "datetime", name = "update_date", nullable = false)
    private OffsetDateTime updateDate;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Product> products = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "restaurant_payment_method",
            joinColumns = @JoinColumn(name = "restaurant_id"),
            inverseJoinColumns = @JoinColumn(name = "payment_method_id")
    )
    private Set<PaymentMethod> paymentMethods = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "restaurant_user_responsible",
            joinColumns = @JoinColumn(name = "restaurant_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> responsibleUsers = new HashSet<>();

    public void activate() { this.active = true; }
    public void deactivate() { this.active = false; }

    public void open() { this.open = true; }
    public void close() { this.open = false; }

    public boolean associatePaymentMethod(PaymentMethod method) {
        return this.paymentMethods.add(method);
    }

    public boolean disassociatePaymentMethod(PaymentMethod method) {
        return this.paymentMethods.remove(method);
    }

    public boolean associateResponsibleUser(User user) {
        return this.responsibleUsers.add(user);
    }

    public boolean disassociateResponsibleUser(User user) {
        return this.responsibleUsers.remove(user);
    }

    public boolean addProduct(Product product) {
        return this.products.add(product);
    }

    public boolean removeProduct(Product product) {
        return this.products.remove(product);
    }

    public boolean acceptsPaymentMethod(PaymentMethod method) {
        return this.paymentMethods.contains(method);
    }

    public boolean doesNotAcceptPaymentMethod(PaymentMethod method) {
        return !acceptsPaymentMethod(method);
    }

    public boolean isActive() { return this.active; }
    public boolean isInactive() { return !isActive(); }

    public boolean isOpen() { return this.open; }
    public boolean isClosed() { return !isOpen(); }

    public boolean canOpen() {
        return isActive() && isClosed();
    }

    public boolean canClose() {
        return isOpen();
    }

    public boolean canActivate() {
        return isInactive();
    }

    public boolean canDeactivate() {
        return isActive();
    }
}
