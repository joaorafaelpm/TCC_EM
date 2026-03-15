
import {
  When,
  Given,
  Then,
  Before,
} from "@badeball/cypress-cucumber-preprocessor";
import StatisticsApi from "../pageObjects/StatisticsApi";

const restaurantId = "52ec094f-3e34-42d4-845a-bc1c178259c1";

// ================= GET =================
Given("I make a GET request to endpoint statistics", () => {
  cy.get("@token").then((token) => {
    StatisticsApi.getStatistics(token).then((response) => {
      cy.wrap(response).as("apiResponse");
    });
  });
});
Given("I make a GET request to endpoint statistics with a valid id", () => {
  cy.get("@token").then((token) => {
    StatisticsApi.getStatisticsWithCustomId(token, restaurantId).then((response) => {
      cy.wrap(response).as("apiResponse");
    });
  });
});

// ================ GET - test 406 error =================
Given("I make a GET request to endpoint statistics with accept header {string}",
  (acceptHeader) => {
    cy.get("@token").then((token) => {
      StatisticsApi.getStatisticsByIdWithAcceptHeader(token, acceptHeader).then(
        (response) => {
          cy.wrap(response).as("apiResponse");
        },
      );
    });
  },
);
