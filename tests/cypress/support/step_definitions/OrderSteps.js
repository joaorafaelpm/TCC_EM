import {
  Given,
  When,
  Then,
  Before,
} from "@badeball/cypress-cucumber-preprocessor";
import OrderApi from "../pageObjects/OrderApi";

const RESTAURANT_ID = "52ec094f-3e34-42d4-845a-bc1c178259c1";
const PAYMENT_METHOD_ID = "3ee42ee7-3d35-4680-afe0-e01a24e649dc";
const PRODUCT_ID = "72e58c00-e73f-41ee-bdd7-acf75341a7a7";
const CITY_ID = "0e0362cc-db84-4484-9909-d6977b96b619";

Before(() => {
  cy.task("unSerializeData").then((token) => {
    cy.wrap(token).as("token");
  });
});

// ================= POST =================
Given("I make a POST request to endpoint orders with a valid body", () => {
  cy.get("@token").then((token) => {
    OrderApi.createOrder(token).then((response) => {
      cy.wrap(response).as("apiResponse");
      cy.wrap(response.body.id).as("createdOrderId");
    });
  });
});

Given(
  "I make a POST request to endpoint orders with a null {string}",
  (field) => {
    cy.get("@token").then((token) => {
      const body = {
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
      };
      body[field] = null;
      OrderApi.createOrderWithCustomBody(token, body).then((response) => {
        cy.wrap(response).as("apiResponse");
      });
    });
  },
);

// ================= GET =================
When("I make a GET request to endpoint orders with a valid id", () => {
  cy.get("@token").then((token) => {
    cy.get("@createdOrderId").then((orderCode) => {
      OrderApi.getOrderById(token, orderCode).then((response) => {
        cy.wrap(response).as("apiResponse");
      });
    });
  });
});

When("I make a GET request to list all orders", () => {
  cy.get("@token").then((token) => {
    OrderApi.getOrders(token).then((response) => {
      cy.wrap(response).as("apiResponse");
    });
  });
});

Given("I make a GET request to endpoint orders with id {string}", (id) => {
  cy.get("@token").then((token) => {
    OrderApi.getOrderById(token, id).then((response) => {
      cy.wrap(response).as("apiResponse");
    });
  });
});

Given(
  "I make a GET request to endpoint orders with a valid id and accept header {string}",
  (acceptHeader) => {
    cy.get("@token").then((token) => {
      cy.get("@createdOrderId").then((orderCode) => {
        OrderApi.getOrderByIdWithAcceptHeader(
          token,
          orderCode,
          acceptHeader,
        ).then((response) => {
          cy.wrap(response).as("apiResponse");
        });
      });
    });
  },
);

// ================= FLUXOS =================
When("I make a PUT request to confirm the order", () => {
  cy.get("@token").then((token) => {
    cy.get("@createdOrderId").then((orderCode) => {
      OrderApi.confirmOrder(token, orderCode).then((response) => {
        cy.wrap(response).as("apiResponse");
      });
    });
  });
});

When("I make a PUT request to deliver the order", () => {
  cy.get("@token").then((token) => {
    cy.get("@createdOrderId").then((orderCode) => {
      OrderApi.deliverOrder(token, orderCode).then((response) => {
        cy.wrap(response).as("apiResponse");
      });
    });
  });
});

When("I make a PUT request to cancel the order", () => {
  cy.get("@token").then((token) => {
    cy.get("@createdOrderId").then((orderCode) => {
      OrderApi.cancelOrder(token, orderCode).then((response) => {
        cy.wrap(response).as("apiResponse");
      });
    });
  });
});
