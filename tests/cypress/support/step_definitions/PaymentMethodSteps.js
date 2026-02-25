import {
  Given,
  When,
  Then,
  Before,
} from "@badeball/cypress-cucumber-preprocessor";
import PaymentMethodApi from "../pageObjects/PaymentMethodApi";

const usingPaymentId = "3ee42ee7-3d35-4680-afe0-e01a24e649dc";

Before(() => {
  cy.task("unSerializeData").then((token) => {
    cy.wrap(token).as("token");
  });
});

// ================= POST =================
Given(
  "I make a POST request to endpoint payment-methods with a valid body",
  () => {
    cy.get("@token").then((token) => {
      PaymentMethodApi.createPaymentMethod(token).then((response) => {
        cy.wrap(response).as("apiResponse");
        cy.wrap(response.body.id).as("createdPaymentId");
      });
    });
  },
);

Given(
  "I make a POST request to endpoint payment-methods with empty description",
  () => {
    cy.get("@token").then((token) => {
      PaymentMethodApi.createPaymentMethodWithCustomDesc("", token).then(
        (response) => {
          cy.wrap(response).as("apiResponse");
        },
      );
    });
  },
);

// ================= GET =================
When("I make a GET request to endpoint payment-methods with a valid id", () => {
  cy.get("@token").then((token) => {
    cy.get("@createdPaymentId").then((id) => {
      PaymentMethodApi.getPaymentMethodById(token, id).then((response) => {
        cy.wrap(response).as("apiResponse");
      });
    });
  });
});

Given(
  "I make a GET request to endpoint payment-methods with a {string}",
  function (id) {
    cy.get("@token").then((token) => {
      PaymentMethodApi.getPaymentMethodById(token, id).then((response) => {
        cy.wrap(response).as("apiResponse");
      });
    });
  },
);
Given(
  "I make a GET request to endpoint payment-methods with a deleted id",
  () => {
    cy.get("@token").then((token) => {
      cy.get("@createdPaymentId").then((paymentId) => {
        PaymentMethodApi.getPaymentMethodById(token, paymentId).then(
          (response) => {
            cy.wrap(response).as("apiResponse");
          },
        );
      });
    });
  },
);
Given(
  "I make a GET request to endpoint payment-methods with a valid id and accept header {string}",
  (acceptHeader) => {
    cy.get("@token").then((token) => {
      PaymentMethodApi.getPaymentMethodWithCustomAcceptHeader(
        token,
        acceptHeader,
      ).then((response) => {
        cy.wrap(response).as("apiResponse");
      });
    });
  },
);
// ================= PUT =================
When(
  "I make a PUT request to endpoint payment-methods with a valid body and id",
  () => {
    cy.get("@token").then((token) => {
      cy.get("@createdPaymentId").then((id) => {
        const updateBody = { description: "Boleto Atualizado" };
        PaymentMethodApi.updatePaymentMethod(token, id, updateBody).then(
          (response) => {
            cy.wrap(response).as("apiResponse");
          },
        );
      });
    });
  },
);

Given(
  "I make a PUT request to endpoint payment-methods with id {string} and description {string}",
  (id, desc) => {
    cy.get("@token").then((token) => {
      PaymentMethodApi.updatePaymentMethod(token, id, {
        description: desc,
      }).then((response) => {
        cy.wrap(response).as("apiResponse");
      });
    });
  },
);
Given(
  "I make a PUT request to endpoint payment-methods with id {string} and empty description",
  (id) => {
    cy.get("@token").then((token) => {
      PaymentMethodApi.updatePaymentMethod(token, id, {
        description: " ",
      }).then((response) => {
        cy.wrap(response).as("apiResponse");
      });
    });
  },
);

// ================= DELETE =================
When(
  "I make a DELETE request to endpoint payment-methods with a valid id",
  () => {
    cy.get("@token").then((token) => {
      cy.get("@createdPaymentId").then((id) => {
        PaymentMethodApi.deletePaymentMethod(token, id).then((response) => {
          cy.wrap(response).as("apiResponse");
        });
      });
    });
  },
);

When(
  "I make a DELETE request to endpoint payment-methods with a using payment method",
  () => {
    cy.get("@token").then((token) => {
      PaymentMethodApi.deletePaymentMethod(token, usingPaymentId).then(
        (response) => {
          cy.wrap(response).as("apiResponse");
        },
      );
    });
  },
);

When(
  "I make a DELETE request to endpoint payment-methods with an id {string}",
  (id) => {
    cy.get("@token").then((token) => {
      PaymentMethodApi.deletePaymentMethod(token, id).then((response) => {
        cy.wrap(response).as("apiResponse");
      });
    });
  },
);

// ================= ASSERTIONS =================
Then(
  "I should receive a response with status code from endpoint payment-methods {string}",
  (statusCode) => {
    cy.get("@apiResponse").then((response) => {
      expect(response.status).to.eq(parseInt(statusCode));
    });
  },
);

Then(
  "I should receive a response body with description from endpoint payment-methods {string}",
  (desc) => {
    cy.get("@apiResponse").then((response) => {
      expect(response.body.description).to.include(desc);
    });
  },
);

Then(
  "I should receive a response with statusText from endpoint payment-methods {string}",
  (statusText) => {
    cy.get("@apiResponse").then((response) => {
      expect(response.statusText).to.include(statusText);
    });
  },
);

Then(
  "I should receive a response body with detail from endpoint payment-methods {string}",
  (detail) => {
    cy.get("@apiResponse").then((response) => {
      expect(response.body.detail).to.include(detail);
    });
  },
);

Then(
  "I should receive a response body with userMessage from endpoint payment-methods {string}",
  (msg) => {
    cy.get("@apiResponse").then((response) => {
      expect(response.body.userMessage).to.include(msg);
    });
  },
);

Then(
  "I should receive a response body with title from endpoint payment-methods {string}",
  (title) => {
    cy.get("@apiResponse").then((response) => {
      expect(response.body.title).to.include(title);
    });
  },
);

Then(
  "I should receive a response body with objects name from endpoint payment-methods {string}",
  (name) => {
    cy.get("@apiResponse").then((response) => {
      expect(response.body.objects[0].name).to.include(name);
    });
  },
);

Then(
  "I should receive a response body with objects userMessage from endpoint payment-methods {string}",
  (msg) => {
    cy.get("@apiResponse").then((response) => {
      expect(response.body.objects[0].userMessage).to.include(msg);
    });
  },
);
