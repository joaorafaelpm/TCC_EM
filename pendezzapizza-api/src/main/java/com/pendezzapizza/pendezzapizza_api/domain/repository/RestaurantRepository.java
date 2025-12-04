package com.pendezzapizza.pendezzapizza_api.domain.repository;

import com.pendezzapizza.pendezzapizza_api.domain.model.Restaurant;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RestaurantRepository
        extends CustomJPARepository<Restaurant , UUID> , RestaurantRepositoryQueries,
        JpaSpecificationExecutor<Restaurant> {

    @Query("from Restaurant r join fetch r.paymentMethods")
    List<Restaurant> findAll ();

    @Query("""
    SELECT r FROM Restaurant r
    JOIN FETCH r.address.city c
    JOIN FETCH c.state
    WHERE r.id = :id
""")
    Optional<Restaurant> findByIdMapperResolved(UUID id);

    List<Restaurant> findByShippingFeeBetween (BigDecimal lowerShippingFee , BigDecimal higherShippingFee);

    Optional<Restaurant> findFirstByNameContaining (String nome);

    List<Restaurant> findTop2ByNameContaining (String nome) ;

    List<Restaurant> find (String name , BigDecimal startShippingFee , BigDecimal endShippingFee) ;

}
