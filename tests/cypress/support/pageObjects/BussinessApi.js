class BussinessApi {
  static visitLoginPage() {
    cy.getAuthUrlEnv().then((env) => {
      cy.visit(env.authUrl);
    });
  }
  static updateRestaurant(token, method, endpoint, restaurantId) {
    return cy.request({
      method: method,
      url: `${endpoint}/${restaurantId}`,
      headers: {
        Authorization: `Bearer ${token}`,
        "Content-Type": "application/json",
      },
      body: {
        name: "Pizzaria fredbear",
        shippingFee: 10.1,
        address: {
          zipCode: "13068-603",
          street: "Rua Sta. Luzia",
          number: "109",
          complement: "Caixa d'gua Sanasa",
          neighborhood: "Jardim Aparecida",
          city: {
            id: "0e0362cc-db84-4484-9909-d6977b96b619",
          },
        },
      },
      failOnStatusCode: false,
    });
  }

  static openOrCloseRestaurant(token, method, endpoint, restaurantId) {
    endpoint = endpoint.replace("id", restaurantId);
    return cy.request({
      method: method,
      url: `${endpoint}`,
      headers: {
        Authorization: `Bearer ${token}`,
        "Content-Type": "application/json",
      },
      failOnStatusCode: false,
    });
  }

  static addOrRemovePaymentMethodsToRestaurant(
    token,
    method,
    endpoint,
    restaurantId,
    paymentMethodId,
  ) {
    endpoint = endpoint.replace("restaurantId", restaurantId);
    endpoint = endpoint.replace("paymentMethodId", paymentMethodId);
    return cy.request({
      method: method,
      url: `${endpoint}`,
      headers: {
        Authorization: `Bearer ${token}`,
        "Content-Type": "application/json",
      },
      failOnStatusCode: false,
    });
  }

  static addOrSaveProducts(token, method, endpoint, restaurantId, productId) {
    endpoint = endpoint.replace("restaurantId", restaurantId);
    endpoint = endpoint.replace("productId", productId);
    return cy.request({
      method: method,
      url: `${endpoint}`,
      headers: {
        Authorization: `Bearer ${token}`,
        "Content-Type": "application/json",
      },
      body: {
        name: "Pizza Margherita",
        description: "Pizza clássica com molho de tomate e queijo",
        price: 49.9,
      },
      failOnStatusCode: false,
    });
  }

  static activateOrDeactivateProducts(
    token,
    method,
    endpoint,
    restaurantId,
    productId,
  ) {
    endpoint = endpoint.replace("restaurantId", restaurantId);
    endpoint = endpoint.replace("productId", productId);
    return cy.request({
      method: method,
      url: `${endpoint}`,
      headers: {
        Authorization: `Bearer ${token}`,
        "Content-Type": "application/json",
      },
      failOnStatusCode: false,
    });
  }

  static addOrRemoveProductPhotos(
    token,
    method,
    endpoint,
    restaurantId,
    productId,
    productPhotoFileName,
  ) {
    endpoint = endpoint.replace("restaurantId", restaurantId);
    endpoint = endpoint.replace("productId", productId);
    return cy
      .fixture(productPhotoFileName, "binary")
      .then(Cypress.Blob.binaryStringToBlob)
      .then((blob) => {
        const formData = new FormData();
        const file = new File([blob], productPhotoFileName, {
          type: "image/jpeg",
        });

        formData.append("file", file);
        formData.append("description", "Descrição");

        return cy.window().then((win) => {
          return win
            .fetch(endpoint, {
              method: method,
              headers: {
                Authorization: `Bearer ${token}`,
                Accept: "application/json",
              },
              body: formData,
              failOnStatusCode: false,
            })
            .then(async (response) => {
              const bodyJson = await response.json().catch(() => ({}));
              return {
                status: response.status,
                statusText: response.statusText,
                body: bodyJson,
              };
            });
        });
      });
  }

  static getOrderById(token, method, endpoint, orderId) {
    endpoint = endpoint.replace("orderId", orderId);
    return cy.request({
      method: method,
      url: `${endpoint}`,
      headers: {
        Authorization: `Bearer ${token}`,
        "Content-Type": "application/json",
      },
      failOnStatusCode: false,
    });
  }

  static updateUserPassword(token, method, endpoint, userId) {
    endpoint = endpoint.replace("userId", userId);
    return cy.getLoginTestEnv().then((env) => {
      cy.request({
        method: method,
        url: `${endpoint}`,
        headers: {
          Authorization: `Bearer ${token}`,
          "Content-Type": "application/json",
        },
        body: {
          currentPassword: env.password,
          newPassword: "abc",
        },
        failOnStatusCode: false,
      });
    });
  }
}

export default BussinessApi;
