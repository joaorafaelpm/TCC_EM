const paymentMethodURL = "/v1/payment-methods";

class PaymentMethodApi {
  static createPaymentMethod(accessToken) {
    return cy.request({
      method: "POST",
      url: paymentMethodURL,
      headers: {
        Authorization: `Bearer ${accessToken}`,
        "Content-Type": "application/json",
      },
      body: {
        description: "Boleto",
      },
      failOnStatusCode: false,
    });
  }

  static createPaymentMethodWithCustomDesc(description, accessToken) {
    return cy.request({
      method: "POST",
      url: paymentMethodURL,
      headers: {
        Authorization: `Bearer ${accessToken}`,
        "Content-Type": "application/json",
      },
      body: {
        description: description,
      },
      failOnStatusCode: false,
    });
  }

  static getPaymentMethodById(accessToken, id) {
    return cy.request({
      method: "GET",
      url: `${paymentMethodURL}/${id}`,
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
      failOnStatusCode: false,
    });
  }
  static getPaymentMethodWithCustomAcceptHeader(accessToken, acceptHeader) {
    return cy.request({
      method: "GET",
      url: `${paymentMethodURL}`,
      headers: {
        Authorization: `Bearer ${accessToken}`,
        Accept: acceptHeader,
      },
      failOnStatusCode: false,
    });
  }

  static deletePaymentMethod(accessToken, id) {
    return cy.request({
      method: "DELETE",
      url: `${paymentMethodURL}/${id}`,
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
      failOnStatusCode: false,
    });
  }

  static updatePaymentMethod(accessToken, id, body) {
    return cy.request({
      method: "PUT",
      url: `${paymentMethodURL}/${id}`,
      headers: {
        Authorization: `Bearer ${accessToken}`,
        "Content-Type": "application/json",
      },
      body: body,
      failOnStatusCode: false,
    });
  }
}

export default PaymentMethodApi;
