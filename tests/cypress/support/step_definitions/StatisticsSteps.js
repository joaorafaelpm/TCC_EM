
import {
  When,
  Given,
  Then,
  Before,
} from "@badeball/cypress-cucumber-preprocessor";
import StatisticsApi from "../pageObjects/StatisticsApi";

const restaurantId = "52ec094f-3e34-42d4-845a-bc1c178259c1";

Before(() => {
  cy.task("unSerializeData").then((token) => {
    cy.wrap(token).as("token");
  });
});

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

// ================= ASSERTIONS (THEN) =================
Then("I should receive a response with status code from endpoint statistics {string}",
  (statusCode) => {
    cy.get("@apiResponse").then((response) => {
      expect(response.status).to.eq(parseInt(statusCode));
    });
  },
);
Then("I should receive a response body from endpoint statistics with date {string}",
  (date) => {
    cy.get("@apiResponse").then((response) => {
      expect(response.body[0].date).to.include(date);
    });
  });
Then("I should receive a response body from endpoint statistics with totalBilled {string}",
  (totalBilled) => {
    cy.get("@apiResponse").then((response) => {
      expect(response.body[0].totalBilled).to.eq(parseFloat(totalBilled));
    });
  });
Then("I should receive a response body from endpoint statistics with totalSales {string}",
  (totalSales) => {
    cy.get("@apiResponse").then((response) => {
      expect(response.body[0].totalSales).to.eq(parseInt(totalSales));
    });
  });

    Then("I should receive a response body with an array from endpoint statistics",
      () => {
        cy.get("@apiResponse").then((response) => {
          expect(response.body).to.be.an("array");
        });
      },
    );

    Then("I should receive a response with statusText from endpoint statistics ronaldo {string}",
      (statusText) => {
        cy.get("@apiResponse").then((response) => {
          expect(response.statusText).to.include(statusText);
        });
      },
    );
