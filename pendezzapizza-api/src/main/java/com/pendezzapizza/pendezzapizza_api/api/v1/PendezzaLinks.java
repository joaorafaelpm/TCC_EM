package com.pendezzapizza.pendezzapizza_api.api.v1;

import com.pendezzapizza.pendezzapizza_api.api.v1.controller.*;
import org.springframework.hateoas.*;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class PendezzaLinks {


    public static final TemplateVariables PAGE_VARIABLES = new TemplateVariables(
            new TemplateVariable("page", TemplateVariable.VariableType.REQUEST_PARAM),
            new TemplateVariable("size", TemplateVariable.VariableType.REQUEST_PARAM),
            new TemplateVariable("sort", TemplateVariable.VariableType.REQUEST_PARAM)
    );

    public static final TemplateVariables PROJECTION_VARIABLES = new TemplateVariables(
            new TemplateVariable("projection", TemplateVariable.VariableType.REQUEST_PARAM)
    );

    // ============================
    // ORDERS
    // ============================

    public Link linkToOrders(String rel) {
        TemplateVariables filterVariables = new TemplateVariables(
                new TemplateVariable("clientId", TemplateVariable.VariableType.REQUEST_PARAM),
                new TemplateVariable("restaurantId", TemplateVariable.VariableType.REQUEST_PARAM),
                new TemplateVariable("creationDateStart", TemplateVariable.VariableType.REQUEST_PARAM),
                new TemplateVariable("creationDateEnd", TemplateVariable.VariableType.REQUEST_PARAM)
        );

        String uri = linkTo(OrderController.class).toUri().toString();

        return Link.of(UriTemplate.of(uri, PAGE_VARIABLES.concat(filterVariables)), LinkRelation.of(rel));
    }

    public Link linkToConfirmOrder(UUID id, String rel) {
        return linkTo(methodOn(OrderFlowController.class).confirm(id)).withRel(rel);
    }

    public Link linkToDeliverOrder(UUID id, String rel) {
        return linkTo(methodOn(OrderFlowController.class).deliver(id)).withRel(rel);
    }

    public Link linkToCancelOrder(UUID id, String rel) {
        return linkTo(methodOn(OrderFlowController.class).cancel(id)).withRel(rel);
    }

    public Link linkToOrder(UUID id) {
        return linkTo(methodOn(OrderController.class).findById(id)).withSelfRel();
    }

    // ============================
    // RESTAURANTS
    // ============================

    public Link linkToRestaurants(String rel) {
        String uri = linkTo(RestaurantController.class).toUri().toString();
        return Link.of(UriTemplate.of(uri, PROJECTION_VARIABLES), LinkRelation.of(rel));
    }

    public Link linkToRestaurant() {
        return linkToRestaurants(IanaLinkRelations.SELF.value());
    }

    public Link linkToRestaurantActivation(UUID restaurantId, String rel) {
        return linkTo(methodOn(RestaurantController.class).activate(restaurantId)).withRel(rel);
    }

    public Link linkToRestaurantActivation(UUID restaurantId) {
        return linkToRestaurantActivation(restaurantId, IanaLinkRelations.SELF.value());
    }

    public Link linkToRestaurantInactivation(UUID restaurantId, String rel) {
        return linkTo(methodOn(RestaurantController.class).deactivate(restaurantId)).withRel(rel);
    }

    public Link linkToRestaurantInactivation(UUID restaurantId) {
        return linkToRestaurantInactivation(restaurantId, IanaLinkRelations.SELF.value());
    }

    public Link linkToRestaurantOpening(UUID restaurantId, String rel) {
        return linkTo(methodOn(RestaurantController.class).open(restaurantId)).withRel(rel);
    }

    public Link linkToRestaurantOpening(UUID restaurantId) {
        return linkToRestaurantOpening(restaurantId, IanaLinkRelations.SELF.value());
    }

    public Link linkToRestaurantClosing(UUID restaurantId, String rel) {
        return linkTo(methodOn(RestaurantController.class).close(restaurantId)).withRel(rel);
    }

    public Link linkToRestaurantClosing(UUID restaurantId) {
        return linkToRestaurantClosing(restaurantId, IanaLinkRelations.SELF.value());
    }

    // ============================
    // RESTAURANT PAYMENT METHODS
    // ============================

    public Link linkToRestaurantPaymentMethodDissociate(UUID restaurantId, UUID paymentMethodId, String rel) {
        return linkTo(methodOn(RestaurantPaymentMethodController.class)
                .disassociate(restaurantId, paymentMethodId)).withRel(rel);
    }

    public Link linkToRestaurantPaymentMethodAssociate(UUID restaurantId, String rel) {
        return linkTo(methodOn(RestaurantPaymentMethodController.class)
                .associate(restaurantId, null)).withRel(rel);
    }

    public Link linkToRestaurant(UUID restaurantId, String rel) {
        return linkTo(methodOn(RestaurantController.class)
                .findById(restaurantId)).withRel(rel);
    }

    public Link linkToRestaurant(UUID restaurantId) {
        return linkToRestaurant(restaurantId, IanaLinkRelations.SELF.value());
    }

    // ============================
    // RESPONSIBLE USERS OF RESTAURANT
    // ============================

    public Link linkToRestaurantManagers(UUID restaurantId, String rel) {
        return linkTo(methodOn(RestaurantUserController.class)
                .list(restaurantId)).withRel(rel);
    }

    public Link linkToRestaurantManagers(UUID restaurantId) {
        return linkToRestaurantManagers(restaurantId, IanaLinkRelations.SELF.value());
    }

    public Link linkToRestaurantManagersAssociation(UUID restaurantId, String rel) {
        return linkTo(methodOn(RestaurantUserController.class)
                .associate(restaurantId, null)).withRel(rel);
    }

    public Link linkToRestaurantManagersDissociation(UUID restaurantId, UUID userId, String rel) {
        return linkTo(methodOn(RestaurantUserController.class)
                .disassociate(restaurantId, userId)).withRel(rel);
    }

    // ============================
    // PRODUCTS
    // ============================

    public Link linkToRestaurantProducts(UUID restaurantId, String rel) {
        return linkTo(methodOn(RestaurantProductController.class)
                .findAllByRestaurant(restaurantId, null)).withRel(rel);
    }

    public Link linkToRestaurantProducts(UUID restaurantId) {
        return linkToRestaurantProducts(restaurantId, IanaLinkRelations.SELF.value());
    }

    public Link linkToProduct(UUID restaurantId, UUID productId, String rel) {
        return linkTo(methodOn(RestaurantProductController.class)
                .findById(restaurantId, productId)).withRel(rel);
    }

    public Link linkToProduct(UUID restaurantId, UUID productId) {
        return linkToProduct(restaurantId, productId, IanaLinkRelations.SELF.value());
    }

    public Link linkToProductPhoto(UUID restaurantId, UUID productId, String rel) {
        return linkTo(methodOn(RestaurantProductPhotoController.class).findPhoto(restaurantId, productId))
                .withRel(rel);
    }

    public Link linkToProductPhoto(UUID restaurantId, UUID productId) {
        return linkToProductPhoto(restaurantId, productId, IanaLinkRelations.SELF.value());
    }

    // ============================
    // USERS
    // ============================

    public Link linkToUser(UUID userId, String rel) {
        return linkTo(methodOn(UserController.class)
                .findById(userId)).withRel(rel);
    }

    public Link linkToUser(UUID userId) {
        return linkToUser(userId, IanaLinkRelations.SELF.value());
    }

    public Link linkToUsers(String rel) {
        return linkTo(UserController.class).withRel(rel);
    }

    public Link linkToUsers() {
        return linkToUsers(IanaLinkRelations.COLLECTION.value());
    }

    public Link linkToUserGroups(UUID userId, String rel) {
        return linkTo(methodOn(UserGroupController.class)
                .getAllGroupsFromUser(userId)).withRel(rel);
    }

    public Link linkToUserGroups(UUID userId) {
        return linkToUserGroups(userId, IanaLinkRelations.SELF.value());
    }

    // ============================
    // PAYMENT METHODS
    // ============================

    public Link linkToRestaurantPaymentMethods(UUID restaurantId, String rel) {
        return linkTo(methodOn(RestaurantPaymentMethodController.class)
                .all(restaurantId)).withRel(rel);
    }

    public Link linkToRestaurantPaymentMethods(UUID restaurantId) {
        return linkToRestaurantPaymentMethods(restaurantId, IanaLinkRelations.SELF.value());
    }

    public Link linkToPaymentMethods(String rel) {
        return linkTo(PaymentMethodController.class).withRel(rel);
    }

    public Link linkToPaymentMethods() {
        return linkToPaymentMethods(IanaLinkRelations.COLLECTION.value());
    }

    public Link linkToPaymentMethod(UUID paymentMethodId, String rel) {
        return linkTo(methodOn(PaymentMethodController.class)
                .findById(paymentMethodId, null)).withRel(rel);
    }

    public Link linkToPaymentMethod(UUID paymentMethodId) {
        return linkToPaymentMethod(paymentMethodId, IanaLinkRelations.SELF.value());
    }

    // ============================
    // CITIES & STATES
    // ============================

    public Link linkToCity(UUID cityId, String rel) {
        return linkTo(methodOn(CityController.class)
                .findById(cityId)).withRel(rel);
    }

    public Link linkToCity(UUID cityId) {
        return linkToCity(cityId, IanaLinkRelations.SELF.value());
    }

    public Link linkToCities(String rel) {
        return linkTo(CityController.class).withRel(rel);
    }

    public Link linkToCities() {
        return linkToCities(IanaLinkRelations.COLLECTION.value());
    }

    public Link linkToState(UUID stateId, String rel) {
        return linkTo(methodOn(StateController.class)
                .findById(stateId)).withRel(rel);
    }

    public Link linkToState(UUID stateId) {
        return linkToState(stateId, IanaLinkRelations.SELF.value());
    }

    public Link linkToStates(String rel) {
        return linkTo(StateController.class).withRel(rel);
    }

    public Link linkToStates() {
        return linkToStates(IanaLinkRelations.COLLECTION.value());
    }

    // ============================
    // GROUPS & PERMISSIONS
    // ============================

    public Link linkToGroups(String rel) {
        return linkTo(GroupController.class).withRel(rel);
    }

    public Link linkToGroups() {
        return linkToGroups(IanaLinkRelations.COLLECTION.value());
    }

    public Link linkToGroup(UUID groupId, String rel) {
        return linkTo(methodOn(GroupController.class).findById(groupId)).withRel(rel);
    }

    public Link linkToGroup(UUID groupId) {
        return linkToGroup(groupId, IanaLinkRelations.SELF.value());
    }

    public Link linkToGroupPermissions(UUID groupId, String rel) {
        return linkTo(methodOn(GroupPermissionController.class).listPermissions(groupId)).withRel(rel);
    }

    public Link linkToGroupPermissionAssociation(UUID groupId, UUID permissionId, String rel) {
        return linkTo(methodOn(GroupPermissionController.class)
                .associatePermission(groupId, permissionId)).withRel(rel);
    }

    public Link linkToGroupPermissionDissociation(UUID groupId, UUID permissionId, String rel) {
        return linkTo(methodOn(GroupPermissionController.class)
                .disassociatePermission(groupId, permissionId)).withRel(rel);
    }

    public Link linkToUserGroupAssociation(UUID userId, UUID groupId, String rel) {
        return linkTo(methodOn(UserGroupController.class)
                .associate(userId, groupId)).withRel(rel);
    }

    public Link linkToUserGroupDissociation(UUID userId, UUID groupId, String rel) {
        return linkTo(methodOn(UserGroupController.class)
                .disassociate(userId, groupId)).withRel(rel);
    }

    public Link linkToPermissions(String rel) {
        return linkTo(PermissionController.class).withRel(rel);
    }

    public Link linkToPermissions() {
        return linkToPermissions(IanaLinkRelations.COLLECTION.value());
    }

    // ============================
    // STATISTICS
    // ============================

    public Link linkToStatistics(String rel) {
        return linkTo(StatisticsController.class).withRel(rel);
    }

    public Link linkToDailySalesStatistics(String rel) {
        TemplateVariables filters = new TemplateVariables(
                new TemplateVariable("restaurantId", TemplateVariable.VariableType.REQUEST_PARAM),
                new TemplateVariable("creationDateStart", TemplateVariable.VariableType.REQUEST_PARAM),
                new TemplateVariable("creationDateEnd", TemplateVariable.VariableType.REQUEST_PARAM),
                new TemplateVariable("timeOffset", TemplateVariable.VariableType.REQUEST_PARAM)
        );

        String link = linkTo(methodOn(StatisticsController.class)
                .consultDailySales(null, null)).toUri().toString();

        return Link.of(UriTemplate.of(link, filters), rel);
    }

}