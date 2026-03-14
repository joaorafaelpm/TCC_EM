package com.pendezzapizza.pendezzapizza_api.domain.repository;

import com.pendezzapizza.pendezzapizza_api.domain.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends CustomJPARepository<Order, UUID> ,
        JpaSpecificationExecutor<Order> {

    @Query("""
        SELECT p FROM Order p
        JOIN FETCH p.restaurant r
        JOIN FETCH p.deliveryAddress.city c
        JOIN FETCH c.state
        WHERE p.id = :id
    """)
    Optional<Order> findByIdMapperResolved(UUID id);

    @Override
    @Query("from Order p join fetch p.customer")
    Page<Order> findAll (Pageable pageable);

    boolean isOrderManagedBy (UUID orderId , UUID userId);

    @Query("select max(o.updateDate) from Order o")
    OffsetDateTime getLastUpdateDate();

    @Query("select max(o.updateDate) from Order o where o.id = :orderId")
    OffsetDateTime getLastUpdateDateById(UUID orderId);

}
