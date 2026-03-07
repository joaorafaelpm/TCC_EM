package com.pendezzapizza.pendezzapizza_api.domain.repository;

import com.pendezzapizza.pendezzapizza_api.domain.model.Restaurant;
import com.pendezzapizza.pendezzapizza_api.domain.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RestaurantRepository
        extends CustomJPARepository<Restaurant , UUID> , RestaurantRepositoryQueries,
        JpaSpecificationExecutor<Restaurant> {

    @EntityGraph(attributePaths = "paymentMethods")
    Page<Restaurant> findAll (Pageable pageable);

    @Query("""
    SELECT r FROM Restaurant r
    JOIN FETCH r.address.city c
    JOIN FETCH c.state
    WHERE r.id = :id
""")
    Optional<Restaurant> findByIdMapperResolved(UUID id);

    List<Restaurant> find (String name , BigDecimal startShippingFee , BigDecimal endShippingFee) ;

    boolean existsResponsible(UUID restaurantId , UUID userId);

    @Query("select max(r.updateDate) from Restaurant r")
    OffsetDateTime getLastUpdateDate();

    @Query("select max(r.updateDate) from Restaurant r where r.id = :restaurantId")
    OffsetDateTime getLastUpdateDateById(UUID restaurantId);

    @Query("SELECT u FROM Restaurant r JOIN r.responsibleUsers u WHERE r.id = :restaurantId")
    Page<User> findResponsibleUsersByRestaurantId(@Param("restaurantId") UUID restaurantId, Pageable pageable);

}
