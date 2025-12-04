package com.pendezzapizza.pendezzapizza_api.domain.repository;

import com.pendezzapizza.pendezzapizza_api.domain.model.Order;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
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

    @Query("from Order p join fetch p.client")
    List<Order> findAll ();


}
