package com.pendezzapizza.pendezzapizza_api.domain.service;


import com.pendezzapizza.pendezzapizza_api.domain.model.Product;
import com.pendezzapizza.pendezzapizza_api.domain.model.Restaurant;
import com.pendezzapizza.pendezzapizza_api.domain.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ProductService {

    RestaurantService restaurantService ;
    ProductRepository productRepository;

    public List<Product> findAll () {
        return productRepository.findAll();
    }

    public List<Product> findByRestaurant (Restaurant restaurant) {
        return productRepository.findByRestaurant(restaurant);
    }
    public List<Product> findActiveByRestaurant (Restaurant restaurant) {
        return productRepository.findActivesByRestaurant(restaurant);
    }

    public Product findById (UUID restaurantId , UUID productId ) {
        Restaurant restaurant = restaurantService.findById(restaurantId);
        restaurantService.findById(restaurantId);
        return productRepository.findByIdOrThrowException(restaurant ,restaurantId , productId);
    }

    @Transactional
    public Product save (UUID restaurantId , Product product) {
        Restaurant restaurant = restaurantService.findById(restaurantId);
        product.setRestaurant(restaurant);
        restaurant.addProduct(product);
        return productRepository.save(product) ;
    }

    @Transactional
    public void remove (UUID restaurantId , UUID productId) {
        Restaurant restaurant = restaurantService.findById(restaurantId);
        Product product = findById(restaurantId , productId);

        restaurant.removeProduct(product);
        productRepository.delete(product);
        productRepository.flush();
    }

    @Transactional
    public void active (UUID restaurantId , UUID productId) {
        Product product = findById(restaurantId, productId);
        if (product.canActivate()) {
            product.activate();
        }
    }
    @Transactional
    public void deactivate (UUID restaurantId, UUID productId) {
        Product product = findById(restaurantId, productId);
        if (product.canDeactivate()) {
            product.deactivate();
        }
    }

}