/// <reference types="cypress" />

import {
  Given,
  When,
  Then,
} from "@badeball/cypress-cucumber-preprocessor";
import LoginPage from "../pageObjects/LoginApi";
import BussinessApi from "../pageObjects/BussinessApi";

const otherRestaurantId = "d4e5f6a7-b8c9-4d0e-ab1f-3b4c5d6e7f8a";
const otherOrderId = "b4c5d6e7-f8a9-4b0c-ef1d-3f4a5b6c7d8e";
const paymentMethodId = "d0e1f2a3-b4c5-4d6e-ab7f-9b0c1d2e3f4a";
const otherProductId = "a7b8c9d0-e1f2-4a3b-de4c-6e7f8a9b0c1d";
const otherUserId = "c7d8e9f0-a1b2-4c3d-fa4e-6a7b8c9d0e1f";
const productPhotoFileName = "exampleImage.jpg";
// Login (Valid Login as owner of restaurant) ================================================================================================
When("I type a registered email and password of a restaurant owner", () => {
  cy.getRestaurantOwnerInfo().then((info) => {
    LoginPage.fillUsername(info.restaurantUsername);
    LoginPage.fillPassword(info.restaurantPassword);
    LoginPage.submit();
  });
});

// (I hit "PUT" in endpoint "/v1/restaurants" with valid body that isn't mine) ================================================================================================
Given(
  "I hit {string} in endpoint {string} with valid body that isn't mine",
  (method, endpoint) => {
    cy.get("@token").then((token) => {
      BussinessApi.updateRestaurant(
        token,
        method,
        endpoint,
        otherRestaurantId,
      ).then((response) => {
        cy.wrap(response).as("apiResponse");
      });
    });
  },
);

// (I hit <method> in endpoint <endpoint> with valid id that isn't mine to open or close it) ================================================================================================
Given(
  "I hit {string} in endpoint {string} with valid id that isn't mine to open or close it",
  (method, endpoint) => {
    cy.get("@token").then((token) => {
      BussinessApi.openOrCloseRestaurant(
        token,
        method,
        endpoint,
        otherRestaurantId,
      ).then((response) => {
        cy.wrap(response).as("apiResponse");
      });
    });
  },
);

// (I hit <method> in endpoint <endpoint> with valid id that isn't mine to manage the payment methods) ================================================================================================

Given(
  "I hit {string} in endpoint {string} with valid id that isn't mine to manage the payment methods",
  (method, endpoint) => {
    cy.get("@token").then((token) => {
      BussinessApi.addOrRemovePaymentMethodsToRestaurant(
        token,
        method,
        endpoint,
        otherRestaurantId,
        paymentMethodId,
      ).then((response) => {
        cy.wrap(response).as("apiResponse");
      });
    });
  },
);
// (I hit <method> in endpoint <endpoint> with valid body that isn't mine to manage the products) ================================================================================================
Given(
  "I hit {string} in endpoint {string} with valid body that isn't mine to manage the products",
  (method, endpoint) => {
    cy.get("@token").then((token) => {
      BussinessApi.addOrSaveProducts(
        token,
        method,
        endpoint,
        otherRestaurantId,
        otherProductId,
      ).then((response) => {
        cy.wrap(response).as("apiResponse");
      });
    });
  },
);

// (I hit <method> in endpoint <endpoint> with valid id that isn't mine to activate or deactivate the products) ================================================================================================

Given(
  "I hit {string} in endpoint {string} with valid id that isn't mine to activate or deactivate the products",
  (method, endpoint) => {
    cy.get("@token").then((token) => {
      BussinessApi.activateOrDeactivateProducts(
        token,
        method,
        endpoint,
        otherRestaurantId,
        otherProductId,
      ).then((response) => {
        cy.wrap(response).as("apiResponse");
      });
    });
  },
);

// (I hit <method> in endpoint <endpoint> with valid id that isn't mine to activate or deactivate the products) ================================================================================================

Given(
  "I hit {string} in endpoint {string} with valid body that isn't mine to add or remove product photos",
  (method, endpoint) => {
    cy.get("@token").then((token) => {
      BussinessApi.addOrRemoveProductPhotos(
        token,
        method,
        endpoint,
        otherRestaurantId,
        otherProductId,
        productPhotoFileName,
      ).then((response) => {
        cy.wrap(response).as("apiResponse");
      });
    });
  },
);

// (I hit "GET" in endpoint "v1/orders" with valid id that isn't mine) ================================================================================================

Given(
  "I hit {string} in endpoint {string} with valid id that isn't mine",
  (method, endpoint) => {
    cy.get("@token").then((token) => {
      BussinessApi.getOrderById(token, method, endpoint, otherOrderId).then(
        (response) => {
          cy.wrap(response).as("apiResponse");
        },
      );
    });
  },
);
// (I hit "PUT" in endpoint "v1/users/userId/password" with valid id that isn't mine to change his password) ================================================================================================
Given(
  "I hit {string} in endpoint {string} with valid id that isn't mine to change his password",
  (method, endpoint) => {
    cy.get("@token").then((token) => {
      BussinessApi.updateUserPassword(token, method, endpoint, otherUserId).then(
        (response) => {
          cy.wrap(response).as("apiResponse");
        },
      );
    });
  },
);

// Assertions
Then("I have a successful login and see my access_token as an admin", () => {
  cy.getAccessTokenRequestEnv().then((env) => {
    cy.makeAccessTokenRequestAndWriteIt(env , "admin_access_token");
  });
});

