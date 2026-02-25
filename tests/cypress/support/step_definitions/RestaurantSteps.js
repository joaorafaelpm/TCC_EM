import {
  Given,
  When,
  Then,
  Before,
} from "@badeball/cypress-cucumber-preprocessor";
import RestaurantApi from "../pageObjects/RestaurantApi";

// ⚠️ Substitua pelos IDs reais do seu banco antes de rodar
const USER_ID = "a6162eb1-df44-471b-aef3-9feee0d9d267";
const PAYMENT_METHOD_ID = "3ee42ee7-3d35-4680-afe0-e01a24e649dc";
const RESTAURANT_ID = "52ec094f-3e34-42d4-845a-bc1c178259c1";

Before(() => {
  cy.task("unSerializeData").then((token) => {
    cy.wrap(token).as("token");
  });
});

// ================= POST =================
Given("I make a POST request to endpoint restaurants with a valid body", () => {
  cy.get("@token").then((token) => {
    RestaurantApi.createRestaurant(token).then((response) => {
      cy.wrap(response).as("apiResponse");
      cy.wrap(response.body.id).as("createdRestaurantId");
    });
  });
});

// ================= GET =================
When("I make a GET request to endpoint restaurants with a valid id", () => {
  cy.get("@token").then((token) => {
    cy.get("@createdRestaurantId").then((restaurantId) => {
      RestaurantApi.getRestaurantById(token, restaurantId).then((response) => {
        cy.wrap(response).as("apiResponse");
      });
    });
  });
});

When("I make a GET request to list all restaurants", () => {
  cy.get("@token").then((token) => {
    RestaurantApi.getRestaurants(token).then((response) => {
      cy.wrap(response).as("apiResponse");
    });
  });
});

Given(
  "I make a GET request to endpoint restaurants with id {string}",
  (id) => {
    cy.get("@token").then((token) => {
      RestaurantApi.getRestaurantById(token, id).then((response) => {
        cy.wrap(response).as("apiResponse");
      });
    });
  },
);

Given(
  "I make a GET request to endpoint restaurants with a valid id and accept header {string}",
  (acceptHeader) => {
    cy.get("@token").then((token) => {
      cy.get("@createdRestaurantId").then((restaurantId) => {
        RestaurantApi.getRestaurantByIdWithAcceptHeader(token, restaurantId, acceptHeader).then(
          (response) => {
            cy.wrap(response).as("apiResponse");
          },
        );
      });
    });
  },
);

When("I make a GET request to list responsible users of the restaurant", () => {
  cy.get("@token").then((token) => {
    cy.get("@createdRestaurantId").then((restaurantId) => {
      RestaurantApi.getResponsibleUsers(token, restaurantId).then((response) => {
        cy.wrap(response).as("apiResponse");
      });
    });
  });
});

When("I make a GET request to list payment methods of the restaurant", () => {
  cy.get("@token").then((token) => {
    cy.get("@createdRestaurantId").then((restaurantId) => {
      RestaurantApi.getPaymentMethods(token, restaurantId).then((response) => {
        cy.wrap(response).as("apiResponse");
      });
    });
  });
});

// ================= PUT =================
When("I make a PUT request to endpoint restaurants with a valid body and id", () => {
  cy.get("@token").then((token) => {
    cy.get("@createdRestaurantId").then((restaurantId) => {
      const updateBody = {
        name: "Pizzaria Fredbear Atualizada",
        shippingFee: 12.5,
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
      };
      RestaurantApi.updateRestaurant(token, restaurantId, updateBody).then((response) => {
        cy.wrap(response).as("apiResponse");
      });
    });
  });
});

When("I make a PUT request to activate the restaurant", () => {
  cy.get("@token").then((token) => {
    cy.get("@createdRestaurantId").then((restaurantId) => {
      RestaurantApi.activateRestaurant(token, restaurantId).then((response) => {
        cy.wrap(response).as("apiResponse");
      });
    });
  });
});

When("I make a PUT request to open the restaurant", () => {
  cy.get("@token").then((token) => {
    cy.get("@createdRestaurantId").then((restaurantId) => {
      RestaurantApi.openRestaurant(token, restaurantId).then((response) => {
        cy.wrap(response).as("apiResponse");
      });
    });
  });
});

When("I make a PUT request to close the restaurant", () => {
  cy.get("@token").then((token) => {
    cy.get("@createdRestaurantId").then((restaurantId) => {
      RestaurantApi.closeRestaurant(token, restaurantId).then((response) => {
        cy.wrap(response).as("apiResponse");
      });
    });
  });
});

When("I make a PUT request to activate multiple restaurants", () => {
  cy.get("@token").then((token) => {
    cy.get("@createdRestaurantId").then((restaurantId) => {
      RestaurantApi.activateMultipleRestaurants(token, [restaurantId]).then((response) => {
        cy.wrap(response).as("apiResponse");
      });
    });
  });
});

// ================= DELETE =================
When("I make a DELETE request to deactivate the restaurant", () => {
  cy.get("@token").then((token) => {
    cy.get("@createdRestaurantId").then((restaurantId) => {
      RestaurantApi.deactivateRestaurant(token, restaurantId).then((response) => {
        cy.wrap(response).as("apiResponse");
      });
    });
  });
});

When("I make a DELETE request to deactivate multiple restaurants", () => {
  cy.get("@token").then((token) => {
    cy.get("@createdRestaurantId").then((restaurantId) => {
      RestaurantApi.deactivateMultipleRestaurants(token, [restaurantId]).then((response) => {
        cy.wrap(response).as("apiResponse");
      });
    });
  });
});

// ================= RESPONSIBLE USERS =================
Given(
  "I make a PUT request to endpoint restaurants with a valid id and valid userId",
  () => {
    cy.get("@token").then((token) => {
      cy.get("@createdRestaurantId").then((restaurantId) => {
        RestaurantApi.responsibleUserMethod(token, "PUT", restaurantId, USER_ID).then(
          (response) => {
            cy.wrap(response).as("apiResponse");
          },
        );
      });
    });
  },
);

Given(
  "I make a DELETE request to endpoint restaurants with a valid id and valid userId",
  () => {
    cy.get("@token").then((token) => {
      cy.get("@createdRestaurantId").then((restaurantId) => {
        RestaurantApi.responsibleUserMethod(token, "DELETE", restaurantId, USER_ID).then(
          (response) => {
            cy.wrap(response).as("apiResponse");
          },
        );
      });
    });
  },
);

Given(
  "I make a {string} request to endpoint restaurants responsible-users with restaurantId {string} and userId {string}",
  (method, restaurantId, userId) => {
    cy.get("@token").then((token) => {
      RestaurantApi.responsibleUserMethod(token, method, restaurantId, userId).then(
        (response) => {
          cy.wrap(response).as("apiResponse");
        },
      );
    });
  },
);

// ================= PAYMENT METHODS =================
Given(
  "I make a PUT request to endpoint restaurants with a valid id and valid paymentMethodId",
  () => {
    cy.get("@token").then((token) => {
      cy.get("@createdRestaurantId").then((restaurantId) => {
        RestaurantApi.paymentMethodAssociationMethod(
          token,
          "PUT",
          restaurantId,
          PAYMENT_METHOD_ID,
        ).then((response) => {
          cy.wrap(response).as("apiResponse");
        });
      });
    });
  },
);

Given(
  "I make a DELETE request to endpoint restaurants with a valid id and valid paymentMethodId",
  () => {
    cy.get("@token").then((token) => {
      cy.get("@createdRestaurantId").then((restaurantId) => {
        RestaurantApi.paymentMethodAssociationMethod(
          token,
          "DELETE",
          restaurantId,
          PAYMENT_METHOD_ID,
        ).then((response) => {
          cy.wrap(response).as("apiResponse");
        });
      });
    });
  },
);

Given(
  "I make a {string} request to endpoint restaurants payment-methods with restaurantId {string} and paymentMethodId {string}",
  (method, restaurantId, paymentMethodId) => {
    cy.get("@token").then((token) => {
      RestaurantApi.paymentMethodAssociationMethod(
        token,
        method,
        restaurantId,
        paymentMethodId,
      ).then((response) => {
        cy.wrap(response).as("apiResponse");
      });
    });
  },
);

// ================= ASSERTIONS (THEN) =================
Then(
  "I should receive a response with status code from endpoint restaurants {string}",
  (statusCode) => {
    cy.get("@apiResponse").then((response) => {
      expect(response.status).to.eq(parseInt(statusCode));
    });
  },
);

Then(
  "I should receive a response body with name from endpoint restaurants {string}",
  (name) => {
    cy.get("@apiResponse").then((response) => {
      expect(response.body.name).to.include(name);
    });
  },
);

Then(
  "I should receive a response body with detail from endpoint restaurants {string}",
  (detail) => {
    cy.get("@apiResponse").then((response) => {
      expect(response.body.detail).to.include(detail);
    });
  },
);

Then(
  "I should receive a response body with title from endpoint restaurants {string}",
  (title) => {
    cy.get("@apiResponse").then((response) => {
      expect(response.body.title).to.include(title);
    });
  },
);

Then("I should receive an array of {string} in endpoint restaurants", (currentCheck) => {
  cy.get("@apiResponse").then((response) => {

    expect(response.body._embedded[currentCheck]).to.be.an("array");
  });
});


Then(
  "I should receive a response with statusText from endpoint restaurants {string}",
  (statusText) => {
    cy.get("@apiResponse").then((response) => {
      expect(response.statusText).to.include(statusText);
    });
  },
);

Then(
  "I should receive a response with detail containing the message from restaurants {string}",
  (detail) => {
    cy.get("@apiResponse").then((response) => {
      expect(response.body.detail).to.include(detail);
    });
  },
);

Then(
  "I should receive a response body with objects name from endpoint restaurants {string}",
  (objectName) => {
    cy.get("@apiResponse").then((response) => {
      expect(response.body.objects[0].name).to.include(objectName);
    });
  },
);

Then(
  "I should receive a response body with objects userMessage from endpoint restaurants {string}",
  (objectUserMessage) => {
    cy.get("@apiResponse").then((response) => {
      expect(response.body.objects[0].userMessage).to.include(objectUserMessage);
    });
  },
);
