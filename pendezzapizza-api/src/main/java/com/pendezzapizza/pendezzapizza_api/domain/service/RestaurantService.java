package com.pendezzapizza.pendezzapizza_api.domain.service;

import com.pendezzapizza.pendezzapizza_api.core.cache.cacheannotations.action.RestaurantsActionCacheEvict;
import com.pendezzapizza.pendezzapizza_api.core.cache.cacheannotations.save.RestaurantsSaveCacheEvict;
import com.pendezzapizza.pendezzapizza_api.domain.exception.EntityInUseException;
import com.pendezzapizza.pendezzapizza_api.domain.exception.RestaurantNotFoundException;
import com.pendezzapizza.pendezzapizza_api.domain.model.PaymentMethod;
import com.pendezzapizza.pendezzapizza_api.domain.model.Restaurant;
import com.pendezzapizza.pendezzapizza_api.domain.model.User;
import com.pendezzapizza.pendezzapizza_api.domain.repository.RestaurantRepository;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class RestaurantService {

    RestaurantRepository restaurantRepository;
    CityService cityService ;
    PaymentMethodService paymentMethodService ;
    UserService userService ;

    @Cacheable(value = "restaurants")
    public Page<Restaurant> findAll(Pageable pageable) {
        return restaurantRepository.findAll(pageable);
    }

    @Cacheable(value = "restaurantsResponsibleUsers" , key = "#restaurantId")
    public Page<User> findResponsibleUsersByRestaurantId(UUID restaurantId , Pageable pageable) {
        return restaurantRepository.findResponsibleUsersByRestaurantId(restaurantId , pageable);
    }
    @Cacheable(value = "restaurantsPaymentMethods" , key = "#restaurantId")
    public Page<PaymentMethod> findPaymentMethodsByRestaurantId(UUID restaurantId , Pageable pageable) {
        return restaurantRepository.findPaymentMethodsByRestaurantId(restaurantId , pageable);
    }
    @Cacheable(value = "restaurant" , key = "#restaurantId")
    public Restaurant findById (UUID restaurantId ) {
//        return restaurantRepository.findByIdOrThrowException(restaurantId);
        return restaurantRepository.findByIdMapperResolved(restaurantId).orElseThrow(() ->
                new RestaurantNotFoundException(restaurantId));
    }
    public Restaurant findByIdWithAllDependencies (UUID restaurantId ) {
        return restaurantRepository.findByIdMapperResolved(restaurantId).orElseThrow(() ->
                new RestaurantNotFoundException(restaurantId));
    }

    @Cacheable("restaurantsLastUpdate")
    public OffsetDateTime getLastUpdateDate() {
        return restaurantRepository.getLastUpdateDate();
    }

    @Cacheable(value = "restaurantsLastUpdateById" , key = "#restaurantId")
    public OffsetDateTime getLastUpdateDateById(UUID restaurantId) {
        return restaurantRepository.getLastUpdateDateById(restaurantId);
    }

    @RestaurantsSaveCacheEvict
    @Transactional
    public Restaurant save (Restaurant restaurant) {
        UUID cityId = restaurant.getAddress().getCity().getId();
        restaurant.getAddress().setCity(cityService.findById(cityId));

        Restaurant savedRestaurant = restaurantRepository.saveAndFlush(restaurant);
        return  findByIdWithAllDependencies(savedRestaurant.getId());

    }

    @RestaurantsActionCacheEvict
    @Transactional
    public void remove (UUID restaurantId) {
        try {
            restaurantRepository.delete(findById(restaurantId));
        }
        catch (DataIntegrityViolationException e) {
            throw new EntityInUseException(
                    String.format("Restaurante de código %d tem produtos ativos, logo, não pode ser removida!" , restaurantId)
            ) ;
        }
    }

    @RestaurantsActionCacheEvict
    @Transactional
    public void activate (UUID restaurantId) {
        Restaurant restaurant = findById(restaurantId);
        restaurant.activate();
    }
    @RestaurantsActionCacheEvict
    @Transactional
    public void deactivate (UUID restaurantId) {
        Restaurant restaurant = findById(restaurantId);
        restaurant.deactivate();
    }

    @RestaurantsActionCacheEvict
    @Transactional
    public void open (UUID restaurantId) {
        Restaurant restaurant = findById(restaurantId);
        restaurant.open();
    }
    @RestaurantsActionCacheEvict
    @Transactional
    public void close (UUID restaurantId) {
        Restaurant restaurant = findById(restaurantId);
        restaurant.close();
    }

    @RestaurantsActionCacheEvict
    @Transactional
    public void disassociatePaymentMethod(UUID restaurantId , UUID paymentMethodId) {
        Restaurant restaurant = findById(restaurantId);
        PaymentMethod paymentMethod = paymentMethodService.findById(paymentMethodId);

        restaurant.disassociatePaymentMethod(paymentMethod);
    }
    @RestaurantsActionCacheEvict
    @Transactional
    public void associatePaymentMethod(UUID restaurantId , UUID paymentMethodId) {
        Restaurant restaurant = findById(restaurantId);
        PaymentMethod paymentMethod = paymentMethodService.findById(paymentMethodId);

        restaurant.associatePaymentMethod(paymentMethod);
    }

    @RestaurantsActionCacheEvict
    @Transactional
    public void disassociateResponsibleUser(UUID restaurantId , UUID userId) {
        Restaurant restaurant = findById(restaurantId);
        User user = userService.findById(userId);

        restaurant.disassociateResponsibleUser(user);
    }
    @RestaurantsActionCacheEvict
    @Transactional
    public void associateResponsibleUser(UUID restaurantId , UUID userId) {
        Restaurant restaurant = findById(restaurantId);
        User user = userService.findById(userId);

        restaurant.associateResponsibleUser(user);
    }


}