const restaurantUrl = "/v1/restaurants";

class RestaurantApi {
  static createRestaurant(accessToken) {
    return cy.request({
      method: "POST",
      url: restaurantUrl,
      headers: {
        Authorization: `Bearer ${accessToken}`,
        "Content-Type": "application/json",
      },
      body: {
        name: "Pizzaria Fredbear",
        shippingFee: 10.1,
        address: {
          zipCode: "13068-603",
          street: "Rua Sta. Luzia",
          number: "109",
          complement: "Caixa d'agua Sanasa",
          neighborhood: "Jardim Aparecida",
          city: {
            id: "0e0362cc-db84-4484-9909-d6977b96b619",
          },
        },
      },
      failOnStatusCode: false,
    });
  }

  static createRestaurantWithCustomBody(accessToken, body) {
    return cy.request({
      method: "POST",
      url: restaurantUrl,
      headers: {
        Authorization: `Bearer ${accessToken}`,
        "Content-Type": "application/json",
      },
      body: body,
      failOnStatusCode: false,
    });
  }

  // ================= GET =================
  static getRestaurants(accessToken) {
    return cy.request({
      method: "GET",
      url: restaurantUrl,
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
      failOnStatusCode: false,
    });
  }

  static getRestaurantById(accessToken, restaurantId) {
    return cy.request({
      method: "GET",
      url: `${restaurantUrl}/${restaurantId}`,
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
      failOnStatusCode: false,
    });
  }

  static getRestaurantByIdWithAcceptHeader(
    accessToken,
    restaurantId,
    acceptHeader,
  ) {
    return cy.request({
      method: "GET",
      url: `${restaurantUrl}/${restaurantId}`,
      headers: {
        Authorization: `Bearer ${accessToken}`,
        Accept: acceptHeader,
      },
      failOnStatusCode: false,
    });
  }

  static getResponsibleUsers(accessToken, restaurantId) {
    return cy.request({
      method: "GET",
      url: `${restaurantUrl}/${restaurantId}/responsible-users`,
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
      failOnStatusCode: false,
    });
  }

  static getPaymentMethods(accessToken, restaurantId) {
    return cy.request({
      method: "GET",
      url: `${restaurantUrl}/${restaurantId}/payment-methods`,
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
      failOnStatusCode: false,
    });
  }

  // ================= PUT =================
  static updateRestaurant(accessToken, restaurantId, body) {
    return cy.request({
      method: "PUT",
      url: `${restaurantUrl}/${restaurantId}`,
      headers: {
        Authorization: `Bearer ${accessToken}`,
        "Content-Type": "application/json",
      },
      body: body,
      failOnStatusCode: false,
    });
  }

  static openRestaurant(accessToken, restaurantId) {
    return cy.request({
      method: "PUT",
      url: `${restaurantUrl}/${restaurantId}/opening`,
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
      failOnStatusCode: false,
    });
  }

  static closeRestaurant(accessToken, restaurantId) {
    return cy.request({
      method: "PUT",
      url: `${restaurantUrl}/${restaurantId}/closing`,
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
      failOnStatusCode: false,
    });
  }

  static activateRestaurant(accessToken, restaurantId) {
    return cy.request({
      method: "PUT",
      url: `${restaurantUrl}/${restaurantId}/active`,
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
      failOnStatusCode: false,
    });
  }

  static deactivateRestaurant(accessToken, restaurantId) {
    return cy.request({
      method: "DELETE",
      url: `${restaurantUrl}/${restaurantId}/active`,
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
      failOnStatusCode: false,
    });
  }

  static activateMultipleRestaurants(accessToken, ids) {
    return cy.request({
      method: "PUT",
      url: `${restaurantUrl}/activations`,
      headers: {
        Authorization: `Bearer ${accessToken}`,
        "Content-Type": "application/json",
      },
      body: ids,
      failOnStatusCode: false,
    });
  }

  static deactivateMultipleRestaurants(accessToken, ids) {
    return cy.request({
      method: "DELETE",
      url: `${restaurantUrl}/activations`,
      headers: {
        Authorization: `Bearer ${accessToken}`,
        "Content-Type": "application/json",
      },
      body: ids,
      failOnStatusCode: false,
    });
  }

  // ================= RESPONSIBLE USERS =================
  static responsibleUserMethod(accessToken, method, restaurantId, userId) {
    return cy.request({
      method: method,
      url: `${restaurantUrl}/${restaurantId}/responsible-users/${userId}`,
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
      failOnStatusCode: false,
    });
  }

  // ================= PAYMENT METHODS =================
  static paymentMethodAssociationMethod(
    accessToken,
    method,
    restaurantId,
    paymentMethodId,
  ) {
    return cy.request({
      method: method,
      url: `${restaurantUrl}/${restaurantId}/payment-methods/${paymentMethodId}`,
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
      failOnStatusCode: false,
    });
  }
}

export default RestaurantApi;
