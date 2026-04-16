package com.pendezzapizza.pendezzapizza_api.core.cache;

import lombok.AllArgsConstructor;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@AllArgsConstructor
public class CacheInvalidationAspect {

    private final CacheInvalidatorUtil cacheInvalidatorUtil;

    // "Sempre que um método for anotado com @GroupsCacheEvict, execute isso após o sucesso"
    @AfterReturning("@annotation(com.pendezzapizza.pendezzapizza_api.core.cache.cacheannotations.GroupsCacheEvict)")
    public void afterGroupsUpdate() {
        cacheInvalidatorUtil.publishCacheInvalidation("groups", "group", "groupsLastUpdate", "groupsLastUpdateById");
    }

    @AfterReturning("@annotation(com.pendezzapizza.pendezzapizza_api.core.cache.cacheannotations.UsersCacheEvict)")
    public void afterUsersUpdate() {
        cacheInvalidatorUtil.publishCacheInvalidation("users", "user", "usersLastUpdate", "usersLastUpdateById" , "userGroup");
    }
    @AfterReturning("@annotation(com.pendezzapizza.pendezzapizza_api.core.cache.cacheannotations.PermissionsCacheEvict)")
    public void afterPermissionsUpdate() {
        cacheInvalidatorUtil.publishCacheInvalidation("permissions", "permission", "permissionsLastUpdate", "permissionsLastUpdateById");
    }
    @AfterReturning("@annotation(com.pendezzapizza.pendezzapizza_api.core.cache.cacheannotations.StatesCacheEvict)")
    public void afterStatesUpdate() {
        cacheInvalidatorUtil.publishCacheInvalidation("states", "state" , "statesLastUpdate" , "statesLastUpdateById");
    }
    @AfterReturning("@annotation(com.pendezzapizza.pendezzapizza_api.core.cache.cacheannotations.CitiesCacheEvict)")
    public void afterCitiesUpdate() {
        cacheInvalidatorUtil.publishCacheInvalidation("cities", "city" , "citiesLastUpdate" , "citiesLastUpdateById");
    }
    @AfterReturning("@annotation(com.pendezzapizza.pendezzapizza_api.core.cache.cacheannotations.PaymentMethodsCacheEvict)")
    public void afterPaymentMethodsUpdate() {
        cacheInvalidatorUtil.publishCacheInvalidation("paymentMethods", "paymentMethod" , "paymentMethodsLastUpdate" , "paymentMethodsLastUpdateById");
    }
    @AfterReturning("@annotation(com.pendezzapizza.pendezzapizza_api.core.cache.cacheannotations.OrdersCacheEvict)")
    public void afterOrdersUpdate() {
        cacheInvalidatorUtil.publishCacheInvalidation("orders", "order" , "ordersLastUpdate" , "ordersLastUpdateById");
    }
    @AfterReturning("@annotation(com.pendezzapizza.pendezzapizza_api.core.cache.cacheannotations.ProductCacheEvict)")
    public void afterProductUpdate() {
        cacheInvalidatorUtil.publishCacheInvalidation("product","allProducts","productsActive", "productsByRestaurant" , "productsActivesByRestaurant" ,"productsAllLastUpdateDateActives", "productsLastUpdateDateActivesByRestaurantId" , "productsLastUpdateDateByRestaurantId");
    }
    @AfterReturning("@annotation(com.pendezzapizza.pendezzapizza_api.core.cache.cacheannotations.RestaurantsCacheEvict)")
    public void afterRestaurantUpdate() {
        cacheInvalidatorUtil.publishCacheInvalidation("restaurants", "restaurant" , "restaurantsLastUpdate" , "restaurantsLastUpdateById" , "restaurantsResponsibleUsers" , "restaurantsResponsibleUsers");
    }
}