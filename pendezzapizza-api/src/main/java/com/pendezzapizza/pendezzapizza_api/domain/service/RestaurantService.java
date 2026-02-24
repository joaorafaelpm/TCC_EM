package com.pendezzapizza.pendezzapizza_api.domain.service;

import com.pendezzapizza.pendezzapizza_api.domain.exception.EntityInUseException;
import com.pendezzapizza.pendezzapizza_api.domain.exception.RestaurantNotFoundException;
import com.pendezzapizza.pendezzapizza_api.domain.model.PaymentMethod;
import com.pendezzapizza.pendezzapizza_api.domain.model.Restaurant;
import com.pendezzapizza.pendezzapizza_api.domain.model.User;
import com.pendezzapizza.pendezzapizza_api.domain.repository.RestaurantRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class RestaurantService {

    RestaurantRepository restaurantRepository;
    CityService cityService ;
    PaymentMethodService paymentMethodService ;
    UserService userService ;

    public List<Restaurant> findAll() {
        return restaurantRepository.findAll();
    }

    public Restaurant findById (UUID id ) {
        return restaurantRepository.findByIdOrThrowException(id);
    }
    public Restaurant findByIdWithAllDependencies (UUID id ) {
        return restaurantRepository.findByIdMapperResolved(id).orElseThrow(() ->
                new RestaurantNotFoundException(id));
    }



    @Transactional
    public Restaurant save (Restaurant restaurant) {
        UUID cityId = restaurant.getAddress().getCity().getId();
        restaurant.getAddress().setCity(cityService.findById(cityId));

        Restaurant savedRestaurant = restaurantRepository.saveAndFlush(restaurant);
        return  findByIdWithAllDependencies(savedRestaurant.getId());

    }

    @Transactional
    public void remove (UUID id) {
        try {
            restaurantRepository.delete(findById(id));
        }
        catch (DataIntegrityViolationException e) {
            throw new EntityInUseException(
                    String.format("Restaurante de código %d tem produtos ativos, logo, não pode ser removida!" , id)
            ) ;
        }
    }



    @Transactional
    public void activate (UUID id) {
        Restaurant restaurant = findById(id);
        restaurant.activate();
    }
    @Transactional
    public void deactivate (UUID id) {
        Restaurant restaurant = findById(id);
        restaurant.deactivate();
    }

    @Transactional
    public void activate (List<UUID> restaurantIds) {
        restaurantIds.forEach(this::activate);
    }

    @Transactional
    public void deactivate (List<UUID> restaurantIds) {
        restaurantIds.forEach(this::deactivate);
    }

    @Transactional
    public void open (UUID id) {
        Restaurant restaurant = findById(id);
        restaurant.open();
    }
    @Transactional
    public void close (UUID id) {
        Restaurant restaurant = findById(id);
        restaurant.close();
    }

    @Transactional
    public void disassociatePaymentMethod(UUID restaurantId , UUID paymentMethodId) {
        Restaurant restaurant = findById(restaurantId);
        PaymentMethod paymentMethod = paymentMethodService.findById(paymentMethodId);

        restaurant.disassociatePaymentMethod(paymentMethod);
    }
    @Transactional
    public void associatePaymentMethod(UUID restaurantId , UUID paymentMethodId) {
        Restaurant restaurant = findById(restaurantId);
        PaymentMethod paymentMethod = paymentMethodService.findById(paymentMethodId);

        restaurant.associatePaymentMethod(paymentMethod);
    }

    @Transactional
    public void disassociateResponsibleUser(UUID restaurantId , UUID userId) {
        Restaurant restaurant = findById(restaurantId);
        User user = userService.findById(userId);

        restaurant.disassociateResponsibleUser(user);
    }
    @Transactional
    public void associateResponsibleUser(UUID restaurantId , UUID userId) {
        Restaurant restaurant = findById(restaurantId);
        User user = userService.findById(userId);

        restaurant.associateResponsibleUser(user);
    }


}