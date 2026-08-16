package com.pendezzapizza.pendezzapizza_api.domain.service;

import com.pendezzapizza.pendezzapizza_api.core.cache.cacheannotations.action.RestaurantsActionCacheEvict;
import com.pendezzapizza.pendezzapizza_api.core.cache.cacheannotations.action.UsersActionCacheEvict;
import com.pendezzapizza.pendezzapizza_api.core.cache.cacheannotations.save.RestaurantsSaveCacheEvict;
import com.pendezzapizza.pendezzapizza_api.core.cache.cacheannotations.save.UsersSaveCacheEvict;
import com.pendezzapizza.pendezzapizza_api.core.security.PendezzaPizzaSecurity;
import com.pendezzapizza.pendezzapizza_api.domain.exception.EntityInUseException;
import com.pendezzapizza.pendezzapizza_api.domain.exception.GroupNotFoundException;
import com.pendezzapizza.pendezzapizza_api.domain.exception.RestaurantNotFoundException;
import com.pendezzapizza.pendezzapizza_api.domain.model.*;
import com.pendezzapizza.pendezzapizza_api.domain.repository.GroupRepository;
import com.pendezzapizza.pendezzapizza_api.domain.repository.RestaurantRepository;
import com.pendezzapizza.pendezzapizza_api.domain.repository.UserRepository;
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

    private final RestaurantRepository restaurantRepository;
    private final RestaurantOwnerProfileService restaurantOwnerProfileService;
    private final CityService cityService ;
    private final PaymentMethodService paymentMethodService ;
    private final UserRepository userRepository ;
    private final GroupRepository groupRepository ;
    private final GroupService groupService ;
    private final PendezzaPizzaSecurity pendezzaPizzaSecurity ;

    @Cacheable(value = "restaurants")
    public Page<Restaurant> findAll(Pageable pageable) {
        return restaurantRepository.findAll(pageable);
    }

    @Cacheable(value = "restaurantName")
    public Page<Restaurant> findAllByName(String restaurantName , Pageable pageable) {
        return restaurantRepository.findByName(restaurantName, pageable);
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
    @UsersSaveCacheEvict
    @Transactional
    public Restaurant save (Restaurant restaurant , String ownerCpf) {
        UUID cityId = restaurant.getAddress().getCity().getId();
        restaurant.getAddress().setCity(cityService.findById(cityId));
        UUID userId = pendezzaPizzaSecurity.getUserId();

        Group newGroup = groupRepository.findByName("Dono_de_Restaurante").orElseThrow(()-> new GroupNotFoundException("Dono_de_Restaurante"));

        if (!restaurantRepository.existsById(userId)) {
            User user = userRepository.findByIdOrThrowException(userId);
            var profile = new RestaurantOwnerProfile();
            profile.setUser(user);
            profile.setCpf(ownerCpf);
            restaurantOwnerProfileService.save(profile);
//            Depois de salvar o novo perfil de usuário como dono de restaurante, a gente já promove ele para um dono de restaurante
            if (!user.getGroups().contains(newGroup)) {
                UUID groupId = newGroup.getId();
                groupService.associateGroup(userId , groupId);
                userRepository.save(user);
            }
        }

        Restaurant savedRestaurant = restaurantRepository.saveAndFlush(restaurant);
        associateResponsibleUser(savedRestaurant.getId() , userId);
        return  findByIdWithAllDependencies(savedRestaurant.getId());
    }

    @RestaurantsSaveCacheEvict
    @Transactional
    public Restaurant update (Restaurant restaurant) {
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
        User user = userRepository.findByIdOrThrowException(userId);

        restaurant.disassociateResponsibleUser(user);
    }
    @UsersActionCacheEvict
    @RestaurantsActionCacheEvict
    @Transactional
    public void associateResponsibleUser(UUID restaurantId , UUID userId) {
        Restaurant restaurant = findById(restaurantId);
        User user = userRepository.findByIdOrThrowException(userId);

        restaurant.associateResponsibleUser(user);
    }

    public Boolean existsResponsible (UUID restaurantId) {
        return restaurantRepository.existsResponsible(restaurantId, pendezzaPizzaSecurity.getUserId()) ;
    }
    public Boolean checkIfUserIsResponsible (UUID restaurantId , UUID userId) {
        return restaurantRepository.existsResponsible(restaurantId, pendezzaPizzaSecurity.getUserId()) && pendezzaPizzaSecurity.getUserId().equals(userId);
    }


}