const orderUrl = "/v1/orders";

const RESTAURANT_ID = "52ec094f-3e34-42d4-845a-bc1c178259c1";
const PAYMENT_METHOD_ID = "3ee42ee7-3d35-4680-afe0-e01a24e649dc";
const PRODUCT_ID = "72e58c00-e73f-41ee-bdd7-acf75341a7a7";
const CITY_ID = "0e0362cc-db84-4484-9909-d6977b96b619";

class OrderApi {
  static createOrder(accessToken) {
    return cy.request({
      method: "POST",
      url: orderUrl,
      headers: {
        Authorization: `Bearer ${accessToken}`,
        "Content-Type": "application/json",
      },
      body: {
        restaurantId: { id: RESTAURANT_ID },
        paymentMethodId: { id: PAYMENT_METHOD_ID },
        deliveryAddress: {
          zipCode: "13068-603",
          street: "Rua Sta. Luzia",
          number: "109",
          complement: "Caixa d'agua Sanasa",
          neighborhood: "Jardim Aparecida",
          city: { id: CITY_ID },
        },
        items: [
          {
            productId: PRODUCT_ID,
            quantity: 1,
            note: "Sem pimenta, por favor",
          },
        ],
      },
      failOnStatusCode: false,
    });
  }

  static createOrderWithCustomBody(accessToken, body) {
    return cy.request({
      method: "POST",
      url: orderUrl,
      headers: {
        Authorization: `Bearer ${accessToken}`,
        "Content-Type": "application/json",
      },
      body: body,
      failOnStatusCode: false,
    });
  }

  static getOrders(accessToken) {
    return cy.request({
      method: "GET",
      url: orderUrl,
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
      failOnStatusCode: false,
    });
  }

  static getOrderById(accessToken, orderId) {
    return cy.request({
      method: "GET",
      url: `${orderUrl}/${orderId}`,
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
      failOnStatusCode: false,
    });
  }

  static getOrderByIdWithAcceptHeader(accessToken, orderId, acceptHeader) {
    return cy.request({
      method: "GET",
      url: `${orderUrl}/${orderId}`,
      headers: {
        Authorization: `Bearer ${accessToken}`,
        Accept: acceptHeader,
      },
      failOnStatusCode: false,
    });
  }

  static confirmOrder(accessToken, orderId) {
    return cy.request({
      method: "PUT",
      url: `${orderUrl}/${orderId}/confirmation`,
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
      failOnStatusCode: false,
    });
  }

  static deliverOrder(accessToken, orderId) {
    return cy.request({
      method: "PUT",
      url: `${orderUrl}/${orderId}/delivery`,
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
      failOnStatusCode: false,
    });
  }

  static cancelOrder(accessToken, orderId) {
    return cy.request({
      method: "PUT",
      url: `${orderUrl}/${orderId}/cancellation`,
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
      failOnStatusCode: false,
    });
  }
}

export default OrderApi;
