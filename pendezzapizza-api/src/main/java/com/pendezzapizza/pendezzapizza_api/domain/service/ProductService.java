package com.pendezzapizza.pendezzapizza_api.domain.service;


import com.pendezzapizza.pendezzapizza_api.core.cache.cacheannotations.action.ProductsActionCacheEvict;
import com.pendezzapizza.pendezzapizza_api.core.cache.cacheannotations.save.ProductsSaveCacheEvict;
import com.pendezzapizza.pendezzapizza_api.domain.exception.ProductNotFoundException;
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

    private final RestaurantService restaurantService ;
    private final ProductRepository productRepository;

    @Cacheable(value = "productsByRestaurant", key = "{#restaurant.id, #pageable.pageNumber, #pageable.pageSize}")
    public Page<Product> findByRestaurant(Restaurant restaurant, Pageable pageable) {
        return productRepository.findByRestaurant(restaurant, pageable);
    }
    @Cacheable(value = "productsActive", key = "{#pageable.pageNumber, #pageable.pageSize}")
    public Page<Product> findAllActive(Pageable pageable) {
        return productRepository.findAllActives(pageable);
    }
    //    Adicionar aos caches
    @Cacheable(value = "allProducts", key = "{#pageable.pageNumber, #pageable.pageSize}")
    public Page<Product> findAll(Pageable pageable) {
        return productRepository.findAllActives(pageable);
    }

    @Cacheable(value = "productsName", key = "{#pageable.pageNumber, #pageable.pageSize, #productName}")
    public Page<Product> findAllActivesByName(String productName, Pageable pageable) {
        return productRepository.findAllActivesByName(productName, pageable);
    }

    @Cacheable(value = "productsActivesByRestaurant", key = "{#restaurant.id, #pageable.pageNumber, #pageable.pageSize}")
    public Page<Product> findActiveByRestaurant(Restaurant restaurant, Pageable pageable) {
        return productRepository.findActivesByRestaurant(restaurant, pageable);
    }

    @Cacheable(value = "product", key = "#productId")
    public Product findById(UUID restaurantId, UUID productId) {
        Restaurant restaurant = restaurantService.findById(restaurantId);
        return productRepository.findByIdOrThrowException(restaurant, restaurantId, productId);
    }

    @Cacheable(value = "productId", key = "#productId")
    public Product findByProductId(UUID productId) {
        return productRepository.findByProductIdLazySolver(productId).orElseThrow( () ->
                new ProductNotFoundException(productId)
        );
    }

    @ProductsSaveCacheEvict // Resolve o ID nulo via #result.id
    @Transactional
    public Product save(UUID restaurantId, Product product) {
        Restaurant restaurant = restaurantService.findById(restaurantId);
        product.setRestaurant(restaurant);
        return productRepository.save(product);
    }

    @ProductsActionCacheEvict
    @Transactional
    public void remove(UUID restaurantId, UUID productId) {
        Product product = findById(restaurantId, productId);
        productRepository.delete(product);
        productRepository.flush();
    }

    @ProductsActionCacheEvict
    @Transactional
    public void active(UUID restaurantId, UUID productId) {
        Product product = findById(restaurantId, productId);
        if (product.canActivate()) product.activate();
    }

    @ProductsActionCacheEvict
    @Transactional
    public void deactivate(UUID restaurantId, UUID productId) {
        Product product = findById(restaurantId, productId);
        if (product.canDeactivate()) product.deactivate();
    }

    //    Adicionar aos caches
    @Cacheable(value = "productsAllLastUpdateDateActives")
    public OffsetDateTime getAllLastUpdateDate () {
        return productRepository.getAllLastUpdateDate();
    }
    @Cacheable(value = "productsLastUpdateDateActivesByRestaurantId" , key = "#restaurantId")
    public OffsetDateTime findLastUpdateDateAndActivesByRestaurantId (UUID restaurantId) {
        return productRepository.getLastUpdateDateById(restaurantId);
    }
    @Cacheable(value = "productsLastUpdateDateByRestaurantId" , key = "#restaurantId")
    public OffsetDateTime findLastUpdateDateByRestaurantId (UUID restaurantId) {
        return productRepository.getLastUpdateDateByIdGetAll(restaurantId);
    }
    @Cacheable(value = "productsLastUpdateDateById" , key = "#productId")
    public OffsetDateTime findLastUpdateDateById (UUID productId) {
        return productRepository.getLastUpdateDateByProductId(productId);
    }

}