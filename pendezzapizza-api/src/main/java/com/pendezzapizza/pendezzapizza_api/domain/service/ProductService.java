package com.pendezzapizza.pendezzapizza_api.domain.service;


import com.pendezzapizza.pendezzapizza_api.core.cache.cacheannotations.ProductsCacheEvict;
import com.pendezzapizza.pendezzapizza_api.domain.model.Product;
import com.pendezzapizza.pendezzapizza_api.domain.model.Restaurant;
import com.pendezzapizza.pendezzapizza_api.domain.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    RestaurantService restaurantService ;
    ProductRepository productRepository;

    @Cacheable(value = "productsByRestaurant" , key = "#restaurant")
    public Page<Product> findByRestaurant (Restaurant restaurant , Pageable pageable) {
        return productRepository.findByRestaurant(restaurant , pageable);
    }
    @Cacheable(value = "productsActivesByRestaurant" , key = "#restaurant")
    public Page<Product> findActiveByRestaurant (Restaurant restaurant , Pageable pageable) {
        return productRepository.findActivesByRestaurant(restaurant , pageable);
    }

    @Cacheable(value = "productsLastUpdateDateActivesByRestaurantId" , key = "#restaurantId")
    public OffsetDateTime findLastUpdateDateAndActivesByRestaurantId (UUID restaurantId) {
        return productRepository.getLastUpdateDateById(restaurantId);
    }
    @Cacheable(value = "productsLastUpdateDateByRestaurantId" , key = "#restaurantId")
    public OffsetDateTime findLastUpdateDateByRestaurantId (UUID restaurantId) {
        return productRepository.getLastUpdateDateByIdGetAll(restaurantId);
    }

    @Cacheable(value = "product" , key = "#restaurantId,#productId")
    public Product findById (UUID restaurantId , UUID productId ) {
        Restaurant restaurant = restaurantService.findById(restaurantId);
        restaurantService.findById(restaurantId);
        return productRepository.findByIdOrThrowException(restaurant ,restaurantId , productId);
    }

    @ProductsCacheEvict
    @Transactional
    public Product save (UUID restaurantId , Product product) {
        Restaurant restaurant = restaurantService.findById(restaurantId);
        product.setRestaurant(restaurant);
        restaurant.addProduct(product);
        return productRepository.save(product) ;
    }

    @ProductsCacheEvict
    @Transactional
    public void remove (UUID restaurantId , UUID productId) {
        Restaurant restaurant = restaurantService.findById(restaurantId);
        Product product = findById(restaurantId , productId);

        restaurant.removeProduct(product);
        productRepository.delete(product);
        productRepository.flush();
    }

    @ProductsCacheEvict
    @Transactional
    public void active (UUID restaurantId , UUID productId) {
        Product product = findById(restaurantId, productId);
        if (product.canActivate()) {
            product.activate();
        }
    }
    @ProductsCacheEvict
    @Transactional
    public void deactivate (UUID restaurantId, UUID productId) {
        Product product = findById(restaurantId, productId);
        if (product.canDeactivate()) {
            product.deactivate();
        }
    }

}