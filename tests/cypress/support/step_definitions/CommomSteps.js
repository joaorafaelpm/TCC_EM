import {
  Given,
  When,
  Then,
  Before,
} from "@badeball/cypress-cucumber-preprocessor";
import CommomApi from "../pageObjects/CommomApi";
import LoginPage from "../pageObjects/LoginApi";


Before(() => {
  cy.task("unSerializeData", "admin_access_token").then((token) => {
    cy.wrap(token).as("token");
  });
})

// ------------------------  Commom steps of login ------------------------

Given("I'm on the login page", () => {
  LoginPage.visitLoginPage();
});

When("I click submit button", () => {
  LoginPage.submit();
});

Then("I should see a error page", () => {
  LoginPage.getErrorMessage();
});

Then("I have a successful login and see my access_token", () => {
  LoginPage.makeAccessTokenRequest();
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

// TotalBilled

Then(
  "I should receive a response body with totalBilled {string}",
  (totalBilled) => {
    cy.get("@apiResponse").then((response) => {
      expect(response.body[0].totalBilled).to.eq(parseFloat(totalBilled));
    });
  },
);

// TotalSales
Then(
  "I should receive a response body with totalSales {string}",
  (totalSales) => {
    cy.get("@apiResponse").then((response) => {
      expect(response.body[0].totalSales).to.eq(parseInt(totalSales));
    });
  },
);

// Date
Then("I should receive a response body with specific date",
  () => {
    cy.formatSpecificDate().then(date => {
      cy.get("@apiResponse").then((response) => {
        expect(response.body[0].date).to.include(date);
      });
    });
    
});

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

