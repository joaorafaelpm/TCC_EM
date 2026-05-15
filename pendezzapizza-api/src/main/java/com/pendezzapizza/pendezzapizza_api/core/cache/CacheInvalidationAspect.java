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

    // ==================== CITIES ====================
    @AfterReturning("@annotation(com.pendezzapizza.pendezzapizza_api.core.cache.cacheannotations.CitiesCacheEvict)")
    public void afterCitiesUpdate() {
        cacheInvalidatorUtil.publishCacheInvalidation("cities", "city", "citiesLastUpdate", "citiesLastUpdateById", "cityAndStateName");
    }

    @AfterReturning("@annotation(com.pendezzapizza.pendezzapizza_api.core.cache.cacheannotations.save.CitiesSaveCacheEvict)")
    public void afterCitiesSave() {
        cacheInvalidatorUtil.publishCacheInvalidation("cities", "city", "citiesLastUpdate", "citiesLastUpdateById", "cityAndStateName");
    }

    @AfterReturning("@annotation(com.pendezzapizza.pendezzapizza_api.core.cache.cacheannotations.action.CitiesActionCacheEvict)")
    public void afterCitiesAction() {
        cacheInvalidatorUtil.publishCacheInvalidation("cities", "city", "citiesLastUpdate", "citiesLastUpdateById", "cityAndStateName");
    }

    // ==================== GROUPS ====================
    @AfterReturning("@annotation(com.pendezzapizza.pendezzapizza_api.core.cache.cacheannotations.GroupsCacheEvict)")
    public void afterGroupsUpdate() {
        cacheInvalidatorUtil.publishCacheInvalidation("groups", "group", "groupsLastUpdate", "groupsLastUpdateById");
    }

    @AfterReturning("@annotation(com.pendezzapizza.pendezzapizza_api.core.cache.cacheannotations.save.GroupsSaveCacheEvict)")
    public void afterGroupsSave() {
        cacheInvalidatorUtil.publishCacheInvalidation("groups", "group", "groupsLastUpdate", "groupsLastUpdateById");
    }

    @AfterReturning("@annotation(com.pendezzapizza.pendezzapizza_api.core.cache.cacheannotations.action.GroupsActionCacheEvict)")
    public void afterGroupsAction() {
        cacheInvalidatorUtil.publishCacheInvalidation("groups", "group", "groupsLastUpdate", "groupsLastUpdateById");
    }

    // ==================== ORDERS ====================
    @AfterReturning("@annotation(com.pendezzapizza.pendezzapizza_api.core.cache.cacheannotations.OrdersCacheEvict)")
    public void afterOrdersUpdate() {
        cacheInvalidatorUtil.publishCacheInvalidation("orders", "order", "ordersLastUpdate", "ordersLastUpdateById");
    }

    @AfterReturning("@annotation(com.pendezzapizza.pendezzapizza_api.core.cache.cacheannotations.save.OrdersSaveCacheEvict)")
    public void afterOrdersSave() {
        cacheInvalidatorUtil.publishCacheInvalidation("orders", "order", "ordersLastUpdate", "ordersLastUpdateById");
    }

    @AfterReturning("@annotation(com.pendezzapizza.pendezzapizza_api.core.cache.cacheannotations.action.OrdersActionCacheEvict)")
    public void afterOrdersAction() {
        cacheInvalidatorUtil.publishCacheInvalidation("orders", "order", "ordersLastUpdate", "ordersLastUpdateById");
    }

    // ==================== PAYMENT METHODS ====================
    @AfterReturning("@annotation(com.pendezzapizza.pendezzapizza_api.core.cache.cacheannotations.PaymentMethodsCacheEvict)")
    public void afterPaymentMethodsUpdate() {
        cacheInvalidatorUtil.publishCacheInvalidation("paymentMethods", "paymentMethod", "paymentMethodsLastUpdate", "paymentMethodsLastUpdateById");
    }

    @AfterReturning("@annotation(com.pendezzapizza.pendezzapizza_api.core.cache.cacheannotations.save.PaymentMethodsSaveCacheEvict)")
    public void afterPaymentMethodsSave() {
        cacheInvalidatorUtil.publishCacheInvalidation("paymentMethods", "paymentMethod", "paymentMethodsLastUpdate", "paymentMethodsLastUpdateById");
    }

    @AfterReturning("@annotation(com.pendezzapizza.pendezzapizza_api.core.cache.cacheannotations.action.PaymentMethodsActionCacheEvict)")
    public void afterPaymentMethodsAction() {
        cacheInvalidatorUtil.publishCacheInvalidation("paymentMethods", "paymentMethod", "paymentMethodsLastUpdate", "paymentMethodsLastUpdateById");
    }

    // ==================== PERMISSIONS ====================
    @AfterReturning("@annotation(com.pendezzapizza.pendezzapizza_api.core.cache.cacheannotations.PermissionsCacheEvict)")
    public void afterPermissionsUpdate() {
        cacheInvalidatorUtil.publishCacheInvalidation("permissions", "permission", "permissionsLastUpdate", "permissionsLastUpdateById");
    }

    @AfterReturning("@annotation(com.pendezzapizza.pendezzapizza_api.core.cache.cacheannotations.save.PermissionsSaveCacheEvict)")
    public void afterPermissionsSave() {
        cacheInvalidatorUtil.publishCacheInvalidation("permissions", "permission", "permissionsLastUpdate", "permissionsLastUpdateById");
    }

    @AfterReturning("@annotation(com.pendezzapizza.pendezzapizza_api.core.cache.cacheannotations.action.PermissionsActionCacheEvict)")
    public void afterPermissionsAction() {
        cacheInvalidatorUtil.publishCacheInvalidation("permissions", "permission", "permissionsLastUpdate", "permissionsLastUpdateById");
    }

    // ==================== PRODUCTS ====================
    @AfterReturning("@annotation(com.pendezzapizza.pendezzapizza_api.core.cache.cacheannotations.ProductCacheEvict)")
    public void afterProductUpdate() {
        cacheInvalidatorUtil.publishCacheInvalidation("product", "allProducts", "productId", "productsActive", "productsByRestaurant", "productsLastUpdateDateById", "productsActivesByRestaurant", "productsAllLastUpdateDateActives", "productsLastUpdateDateActivesByRestaurantId", "productsLastUpdateDateByRestaurantId");
    }

    @AfterReturning("@annotation(com.pendezzapizza.pendezzapizza_api.core.cache.cacheannotations.save.ProductsSaveCacheEvict)")
    public void afterProductsSave() {
        cacheInvalidatorUtil.publishCacheInvalidation("product", "allProducts", "productId", "productsActive", "productsByRestaurant", "productsLastUpdateDateById", "productsActivesByResteurant", "productsAllLastUpdateDateActives", "productsLastUpdateDateActivesByRestaurantId", "productsLastUpdateDateByRestaurantId");
    }

    @AfterReturning("@annotation(com.pendezzapizza.pendezzapizza_api.core.cache.cacheannotations.action.ProductsActionCacheEvict)")
    public void afterProductsAction() {
        cacheInvalidatorUtil.publishCacheInvalidation("product", "allProducts", "productId", "productsActive", "productsByRestaurant", "productsLastUpdateDateById", "productsActivesByRestaurant", "productsAllLastUpdateDateActives", "productsLastUpdateDateActivesByRestaurantId", "productsLastUpdateDateByRestaurantId");
    }

    // ==================== RESTAURANTS ====================
    @AfterReturning("@annotation(com.pendezzapizza.pendezzapizza_api.core.cache.cacheannotations.RestaurantsCacheEvict)")
    public void afterRestaurantUpdate() {
        cacheInvalidatorUtil.publishCacheInvalidation("restaurants", "restaurant", "restaurantsLastUpdate", "restaurantsLastUpdateById", "restaurantsResponsibleUsers", "restaurantsPaymentMethods");
    }

    @AfterReturning("@annotation(com.pendezzapizza.pendezzapizza_api.core.cache.cacheannotations.save.RestaurantsSaveCacheEvict)")
    public void afterRestaurantsSave() {
        cacheInvalidatorUtil.publishCacheInvalidation("restaurants", "restaurant", "restaurantsLastUpdate", "restaurantsLastUpdateById", "restaurantsResponsibleUsers", "restaurantsPaymentMethods");
    }

    @AfterReturning("@annotation(com.pendezzapizza.pendezzapizza_api.core.cache.cacheannotations.action.RestaurantsActionCacheEvict)")
    public void afterRestaurantsAction() {
        cacheInvalidatorUtil.publishCacheInvalidation("restaurants", "restaurant", "restaurantsLastUpdate", "restaurantsLastUpdateById", "restaurantsResponsibleUsers", "restaurantsPaymentMethods");
    }

    // ==================== STATES ====================
    @AfterReturning("@annotation(com.pendezzapizza.pendezzapizza_api.core.cache.cacheannotations.StatesCacheEvict)")
    public void afterStatesUpdate() {
        cacheInvalidatorUtil.publishCacheInvalidation("states", "state", "statesLastUpdate", "statesLastUpdateById");
    }

    @AfterReturning("@annotation(com.pendezzapizza.pendezzapizza_api.core.cache.cacheannotations.save.StatesSaveCacheEvict)")
    public void afterStatesSave() {
        cacheInvalidatorUtil.publishCacheInvalidation("states", "state", "statesLastUpdate", "statesLastUpdateById");
    }

    @AfterReturning("@annotation(com.pendezzapizza.pendezzapizza_api.core.cache.cacheannotations.action.StatesActionCacheEvict)")
    public void afterStatesAction() {
        cacheInvalidatorUtil.publishCacheInvalidation("states", "state", "statesLastUpdate", "statesLastUpdateById");
    }

    // ==================== USERS ====================
    @AfterReturning("@annotation(com.pendezzapizza.pendezzapizza_api.core.cache.cacheannotations.UsersCacheEvict)")
    public void afterUsersUpdate() {
        cacheInvalidatorUtil.publishCacheInvalidation("users", "user", "usersLastUpdate", "usersLastUpdateById", "userGroup", "usersRestaurants");
    }

    @AfterReturning("@annotation(com.pendezzapizza.pendezzapizza_api.core.cache.cacheannotations.save.UsersSaveCacheEvict)")
    public void afterUsersSave() {
        cacheInvalidatorUtil.publishCacheInvalidation("users", "user", "usersLastUpdate", "usersLastUpdateById", "userGroup", "usersRestaurants");
    }

    @AfterReturning("@annotation(com.pendezzapizza.pendezzapizza_api.core.cache.cacheannotations.action.UsersActionCacheEvict)")
    public void afterUsersAction() {
        cacheInvalidatorUtil.publishCacheInvalidation("users", "user", "usersLastUpdate", "usersLastUpdateById", "userGroup", "usersRestaurants");
    }
}