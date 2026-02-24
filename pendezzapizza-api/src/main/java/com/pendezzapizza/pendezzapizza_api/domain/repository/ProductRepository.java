package com.pendezzapizza.pendezzapizza_api.domain.repository;

import com.pendezzapizza.pendezzapizza_api.domain.model.Product;
import com.pendezzapizza.pendezzapizza_api.domain.model.ProductPhoto;
import com.pendezzapizza.pendezzapizza_api.domain.model.Restaurant;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends CustomJPARepository<Product, UUID> , ProductRepositoryQueries {

    @Query("from Product where restaurant.id = :restaurantId and id = :productId")
    Optional<Product> findById (@Param("restaurantId") UUID restaurantId , @Param("productId") UUID productId);

    List<Product> findByRestaurant (Restaurant restaurant);

    @Query("from Product p where p.active=true and p.restaurant = :restaurant")
    List<Product>findActivesByRestaurant(Restaurant restaurant) ;

    @Query("from ProductPhoto f join f.product p where p.restaurant.id = :restaurantId and " +
            "f.product.id = :productId")
    Optional<ProductPhoto> findProductPhotoById (UUID restaurantId , UUID productId) ;
}
