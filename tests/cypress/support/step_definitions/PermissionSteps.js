
import {
  Given,
  When,
  Then,
  Before,
} from "@badeball/cypress-cucumber-preprocessor";
import PermissionApi from "../pageObjects/PermissionApi";


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
