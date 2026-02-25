
import {
  Given,
  When,
  Then,
  Before,
} from "@badeball/cypress-cucumber-preprocessor";
import PermissionApi from "../pageObjects/PermissionApi";


Before(() => {
  cy.task("unSerializeData").then((token) => {
    cy.wrap(token).as("token");
  });
});

// ================= GET =================
When("I make a GET request to endpoint permissions", () => {
  cy.get("@token").then((token) => {
    PermissionApi.getPermission(token).then((response) => {
      cy.wrap(response).as("apiResponse");
    });
  });
});

// ================ GET - test 406 error =================
Given(
  "I make a GET request to endpoint permissions with accept header {string}",
  (acceptHeader) => {
    cy.get("@token").then((token) => {
      PermissionApi.getPermissionByIdWithAcceptHeader(token, acceptHeader).then(
        (response) => {
          cy.wrap(response).as("apiResponse");
        },
      );
    });
  },
);

// ================= ASSERTIONS (THEN) =================
Then(
  "I should receive a response with status code from endpoint permissions {string}",
  (statusCode) => {
    cy.get("@apiResponse").then((response) => {
      expect(response.status).to.eq(parseInt(statusCode));
    });
  },
);

Then(
  "I should receive a response body from endpoint permissions as an array",
  () => {
    cy.get("@apiResponse").then((response) => {
      expect(response.body._embedded.permissions).to.be.an("array");
    });
  },
);

Then(
  "I should receive a response with statusText from endpoint permissions {string}",
  (statusText) => {
    cy.get("@apiResponse").then((response) => {
      expect(response.statusText).to.include(statusText);
    });
  },
);


