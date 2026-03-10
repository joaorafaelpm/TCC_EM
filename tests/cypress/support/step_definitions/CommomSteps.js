import {
  Given,
  When,
  Then,
  Before,
} from "@badeball/cypress-cucumber-preprocessor";
import CommomApi from "../pageObjects/CommomApi";

// ------------------------ Access Token ------------------------

Before(() => {
  cy.task("unSerializeData").then((token) => {
    cy.wrap(token).as("token");
  });
});

// ------------------------  GET ------------------------

When("I make a GET request to endpoint {string} with id {string}", (endpoint , id) => {
    cy.get("@token").then((token) => {
        CommomApi.getById(token, id, endpoint).then((response) => {
          cy.wrap(response).as("apiResponse");
        });
    });
});

// ------------------------  Assertions (Then) ------------------------

Then("I should receive a response with status code {string}", (statusCode) => {
  cy.get("@apiResponse").then((response) => {
    expect(response.status).to.eq(parseInt(statusCode));
  });
});

// Body -> title
Then(
  "I should receive a response body with title {string}",
  (title) => {
    cy.get("@apiResponse").then((response) => {
      expect(response.body.title).to.include(title);
    });
  },
);
// Body -> name
Then("I should receive a response body with name {string}", (name) => {
  cy.get("@apiResponse").then((response) => {
    expect(response.body.name).to.include(name);

  });
});
// Body -> Detail
Then("I should receive a response body with detail {string}", (detail) => {
  cy.get("@apiResponse").then((response) => {
    expect(response.body.detail).to.include(detail);
  });
});

// Date
// TODO: Consertar isso aqui
// Then("I should receive a response body with specific date",
//   () => {
//     const dataFormatada = new Date().toISOString().split("T")[0];
//     cy.get("@apiResponse").then((response) => {
//       expect(response.body[0].dataFormatada).to.include(dataFormatada);
//     });
// });

// StatusText
Then("I should receive a response with statusText {string}", (statusText) => {
  cy.get("@apiResponse").then((response) => {
    expect(response.statusText).to.include(statusText);
  });
});

Then(
  "I should receive a response body with status {string}",
  (status) => {
    cy.get("@apiResponse").then((response) => {
      expect(response.body.status).to.include(status);
    });
  },
);

// USerMessage

Then(
  "I should receive a response body with userMessage {string}",
  (msg) => {
    cy.get("@apiResponse").then((response) => {
      expect(response.body.userMessage).to.include(msg);
    });
  },
);

// Description

Then(
  "I should receive a response body with description {string}",
  (desc) => {
    cy.get("@apiResponse").then((response) => {
      expect(response.body.description).to.include(desc);
    });
  },
);

// Objects 
Then("I should receive a response body with objects name {string}", (objectName) => {
  cy.get("@apiResponse").then((response) => {
    expect(response.body.objects[0].name).to.include(objectName);
  });
});
Then("I should receive a response body with objects userMessage {string}", (objectUserMessage) => {
  cy.get("@apiResponse").then((response) => {
    expect(response.body.objects[0].userMessage).to.include(
      objectUserMessage,
    );
  });
});

// Array

Then("I should receive an array", () => {
  cy.get("@apiResponse").then((response) => {
    expect(response.body.content).to.be.an("array");
  });
});

Then("I should receive an array but not as a page", () => {
  cy.get("@apiResponse").then((response) => {
    expect(response.body).to.be.an("array");
  });
});

