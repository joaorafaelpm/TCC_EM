
const restaurantUrl = "/v1/restaurants";

class ProductApi {
  static createProduct(accessToken, restaurantId) {
    return cy.request({
      method: "POST",
      url: `${restaurantUrl}/${restaurantId}/products`,
      headers: {
        Authorization: `Bearer ${accessToken}`,
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

  static createProductWithCustomBody(accessToken, restaurantId, body) {
    return cy.request({
      method: "POST",
      url: `${restaurantUrl}/${restaurantId}/products`,
      headers: {
        Authorization: `Bearer ${accessToken}`,
        "Content-Type": "application/json",
      },
      body: body,
      failOnStatusCode: false,
    });
  }

  static getProductById(accessToken, restaurantId, productId) {
    return cy.request({
      method: "GET",
      url: `${restaurantUrl}/${restaurantId}/products/${productId}`,
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
      failOnStatusCode: false,
    });
  }

  static getProductPhoto(accessToken, restaurantId, productId) {
    return cy.request({
      method: "GET",
      url: `${restaurantUrl}/${restaurantId}/products/${productId}/photo`,
      headers: {
        Authorization: `Bearer ${accessToken}`,
        Accept: "application/json",
      },
      failOnStatusCode: false,
    });
  }

  static removeProductPhoto(accessToken, restaurantId, productId) {
    return cy.request({
      method: "DELETE",
      url: `${restaurantUrl}/${restaurantId}/products/${productId}/photo`,
      headers: {
        Authorization: `Bearer ${accessToken}`,
        Accept: "application/json",
      },
      failOnStatusCode: false,
    });
  }

  static getProductByIdWithAcceptHeader(
    accessToken,
    restaurantId,
    acceptHeader,
  ) {
    return cy.request({
      method: "GET",
      url: `${restaurantUrl}/${restaurantId}/products`,
      headers: {
        Authorization: `Bearer ${accessToken}`,
        Accept: acceptHeader,
      },
      failOnStatusCode: false,
    });
  }

  static getAllProducts(accessToken, restaurantId) {
    return cy.request({
      method: "GET",
      url: `${restaurantUrl}/${restaurantId}/products`,
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
      failOnStatusCode: false,
    });
  }

  static updateProduct(accessToken, restaurantId, productId, body) {
    return cy.request({
      method: "PUT",
      url: `${restaurantUrl}/${restaurantId}/products/${productId}`,
      headers: {
        Authorization: `Bearer ${accessToken}`,
        "Content-Type": "application/json",
      },
      body: body,
      failOnStatusCode: false,
    });
  }

  static activateProduct(accessToken, restaurantId, productId) {
    return cy.request({
      method: "PUT",
      url: `${restaurantUrl}/${restaurantId}/products/${productId}/active`,
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
      failOnStatusCode: false,
    });
  }

  static deactivateProduct(accessToken, restaurantId, productId) {
    return cy.request({
      method: "DELETE",
      url: `${restaurantUrl}/${restaurantId}/products/${productId}/active`,
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
      failOnStatusCode: false,
    });
  }

  static addPhotoToProduct(accessToken, restaurantId, productId, fileName) {
    return cy
      .fixture(fileName, "binary")
      .then(Cypress.Blob.binaryStringToBlob)
      .then((blob) => {
        const formData = new FormData();
        const file = new File([blob], fileName, { type: "image/jpeg" });

        formData.append("file", file);
        formData.append("description", "Descrição");

        return cy.window().then((win) => {
          return win
            .fetch(
              `${restaurantUrl}/${restaurantId}/products/${productId}/photo`,
              {
                method: "PUT",
                headers: {
                  Authorization: `Bearer ${accessToken}`,
                  Accept: "application/json",
                },
                body: formData,
              },
            )
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
  static addInvalidPhotoToProduct(
    accessToken,
    restaurantId,
    productId,
    fileName,
  ) {

    return cy
      .fixture(fileName, "binary")
      .then(Cypress.Blob.binaryStringToBlob)
      .then((blob) => {
        const formData = new FormData();

        const file = new File([blob], fileName, { type: "application/pdf" });

        formData.append("file", file);
        formData.append("description", "Tentativa de envio de PDF");

        return cy.window().then((win) => {
          return win
            .fetch(
              `${restaurantUrl}/${restaurantId}/products/${productId}/photo`,
              {
                method: "PUT",
                headers: {
                  Authorization: `Bearer ${accessToken}`,
                  Accept: "application/json",
                },
                body: formData,
              },
            )
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
}

export default ProductApi;
