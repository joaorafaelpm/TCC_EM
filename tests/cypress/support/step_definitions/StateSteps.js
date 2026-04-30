import {
  Given,
  When,
  Then,
  Before,
} from "@badeball/cypress-cucumber-preprocessor";
import StateApi from "../pageObjects/StateApi";

const stateId = "ac3bb31f-4c4f-44ff-88e8-92646ba56240";

// ================= POST =================
Given("I make a POST request to endpoint states with a valid body", () => {
  cy.get("@token").then((token) => {
    StateApi.createState(token).then((response) => {
      cy.wrap(response).as("apiResponse");
      cy.wrap(response.body.id).as("createdState");
      cy.wrap(response.body.id).as("createdStateId");
    });
  });
});

Given("I make a POST request to endpoint states with empty name", () => {
  cy.get("@token").then((token) => {
    StateApi.createStateWithCustomName(" ", token).then((response) => {
      cy.wrap(response).as("apiResponse");
    });
  });
});

// ================= GET =================
When("I make a GET request to endpoint states with a valid id", () => {
  cy.get("@token").then((token) => {
    cy.get("@createdState").then((stateId) => {
      StateApi.getStateById(token, stateId).then((response) => {
        cy.wrap(response).as("apiResponse");
      });
    });
  });
});

Given("I make a GET request to endpoint states with a {string}", function (id) {
  cy.get("@token").then((token) => {
    StateApi.getStateById(token, id).then((response) => {
      cy.wrap(response).as("apiResponse");
    });
  });
});

Given("I make a GET request to endpoint states with a deleted id", () => {
  cy.get("@token").then((token) => {
    cy.get("@createdStateId").then((stateId) => {
      StateApi.getStateById(token, stateId).then((response) => {
        cy.wrap(response).as("apiResponse");
      });
    });
  });
});

// ================= PUT =================
When("I make a PUT request to endpoint states with a valid body and id", () => {
  cy.get("@token").then((token) => {
    cy.get("@createdStateId").then((stateId) => {
      const updateBody = {
        name: "São Paulo Atualizada",
      };
      StateApi.updateState(token, stateId, updateBody).then((response) => {
        cy.wrap(response).as("apiResponse");
      });
    });
  });
});

Given(
  "I make a PUT request to endpoint states with id {string} and name {string}",
  (id, name) => {
    cy.get("@token").then((token) => {
      const updateBody = { name: name };
      StateApi.updateState(token, id, updateBody).then((response) => {
        cy.wrap(response).as("apiResponse");
      });
    });
  },
);

// ================= DELETE =================
When("I make a DELETE request to endpoint states with a valid id", () => {
  cy.get("@token").then((token) => {
    cy.get("@createdStateId").then((stateId) => {
      StateApi.deleteState(token, stateId).then((response) => {
        cy.wrap(response).as("apiResponse");
      });
    });
  });
});

Given(
  "I make a DELETE request to endpoint states with an id {string}",
  (id) => {
    cy.get("@token").then((token) => {
      StateApi.deleteState(token, id).then((response) => {
        cy.wrap(response).as("apiResponse");
      });
    });
  },
);

// ================ DELETE - test 409 error =================
When("I make a DELETE request to endpoint states with a using state", () => {
  cy.get("@token").then((token) => {
    StateApi.deleteState(token, stateId).then((response) => {
      cy.wrap(response).as("apiResponse");
    });
  });
});

// ================ GET - test 406 error =================
Given(
  "I make a GET request to endpoint states with a valid id and accept header {string}",
  (acceptHeader) => {
    cy.get("@token").then((token) => {
      StateApi.getStateByIdWithAcceptHeader(token, stateId, acceptHeader).then(
        (response) => {
          cy.wrap(response).as("apiResponse");
        },
      );
    });
  },
);
