import {
  Given,
  When,
  Then,
  Before,
} from "@badeball/cypress-cucumber-preprocessor";
import CityApi from "../pageObjects/CityApi";

const stateID = "ac3bb31f-4c4f-44ff-88e8-92646ba56240";

Before(() => {
  cy.task("unSerializeData").then((token) => {
    cy.wrap(token).as("token");
  });
});

// ================= POST =================
Given(
  "I make a POST request to endpoint cities with a valid body",
  () => {
    cy.get("@token").then((token) => {
      CityApi.createCity(stateID, token).then((response) => {
        cy.wrap(response).as("apiResponse");
        cy.wrap(response.body.id).as("createdCity");
        cy.wrap(response.body.id).as("createdCityId");
      });
    });
  },
);
Given(
  "I make a POST request to endpoint cities with valid name but an invalid stateId {string}",
  (invalidStateId) => {
    cy.get("@token").then((token) => {
      CityApi.createCity(invalidStateId, token).then((response) => {
        cy.wrap(response).as("apiResponse");
      });
    });
  },
);

Given(
  "I make a POST request to endpoint cities with empty name and a valid stateId",
  (name) => {
    cy.get("@token").then((token) => {
      CityApi.createCityWithCustomName(name, stateID, token).then(
        (response) => {
          cy.wrap(response).as("apiResponse");
        },
      );
    });
  },
);

// ================= GET =================
When("I make a GET request to endpoint cities with a valid id", () => {
  cy.get("@token").then((token) => {
    cy.get("@createdCity").then((cityId) => {
      CityApi.getCityById(token, cityId).then((response) => {
        cy.wrap(response).as("apiResponse");
      });
    });
  });
});
Given("I make a GET request to endpoint cities with a {string}", function (id) {
  cy.get("@token").then((token) => {
    CityApi.getCityById(token, id).then((response) => {
      cy.wrap(response).as("apiResponse");
    });
  });
});

Given("I make a GET request to endpoint cities with a deleted id", () => {
  cy.get("@token").then((token) => {
    cy.get("@createdCityId").then((cityId) => {
      CityApi.getCityById(token, cityId).then((response) => {
        cy.wrap(response).as("apiResponse");
      });
    });
  });
});

// ================= PUT =================
When(
  "I make a PUT request to endpoint cities with a valid body and id",
  () => {
    cy.get("@token").then((token) => {
      cy.get("@createdCityId").then((cityId) => {
        const updateBody = {
          name: "São Carlos Atualizada",
          stateId: { id: stateID },
        };
        CityApi.updateCity(token, cityId, updateBody).then((response) => {
          cy.wrap(response).as("apiResponse");
        });
      });
    });
  },
);

Given(
  "I make a PUT request to endpoint cities with id {string} and name {string}",
  (id, name) => {
    cy.get("@token").then((token) => {
      const updateBody = { name: name, stateId: { id: stateID } };
      CityApi.updateCity(token, id, updateBody).then((response) => {
        cy.wrap(response).as("apiResponse");
      });
    });
  },
);

// ================= DELETE =================
When(
  'I make a DELETE request to endpoint cities with a valid id',
  () => {
    cy.get("@token").then((token) => {
      cy.get("@createdCityId").then((cityId) => {
        CityApi.deleteCity(token, cityId).then((response) => {
          cy.wrap(response).as("apiResponse");
        });
      });
    });
  },
);

Given(
  "I make a DELETE request to endpoint cities with an id {string}",
  (id) => {
    cy.get("@token").then((token) => {
      CityApi.deleteCity(token, id).then((response) => {
        cy.wrap(response).as("apiResponse");
      });
    });
  },
);

// ================ GET - test 406 error =================

Given(
  "I make a GET request to endpoint cities with a valid id and accept header {string}",
  (acceptHeader) => {
    const cityId = "0e0362cc-db84-4484-9909-d6977b96b619";
    cy.get("@token").then((token) => {
      CityApi.getCityByIdWithAcceptHeader(token, cityId, acceptHeader).then(
        (response) => {
          cy.wrap(response).as("apiResponse");
        },
      );
    });
  },
);

// ================= ASSERTIONS (THEN) =================
Then("I should receive a response with status code from endpoint cities {string}", (statusCode) => {
  cy.get("@apiResponse").then((response) => {
    expect(response.status).to.eq(parseInt(statusCode));
  });
});

// Body -> title
Then(
  "I should receive a response body with title from endpoint cities {string}",
  (title) => {
    cy.get("@apiResponse").then((response) => {
      expect(response.body.title).to.include(title);
    });
  },
);
// Body -> name
Then("I should receive a response body with name from endpoint cities {string}", (name) => {
  cy.get("@apiResponse").then((response) => {
    expect(response.body.name).to.include(name);

  });
});
// Body -> Detail
Then("I should receive a response body with detail from endpoint cities {string}", (detail) => {
  cy.get("@apiResponse").then((response) => {
    expect(response.body.detail).to.include(detail);
  });
});
// StatusText
Then("I should receive a response with statusText from endpoint cities {string}", (statusText) => {
  cy.get("@apiResponse").then((response) => {
    expect(response.statusText).to.include(statusText);
  });
});


// Objects 
Then("I should receive a response body with objects name from endpoint cities {string}", (objectName) => {
  cy.get("@apiResponse").then((response) => {
    expect(response.body.objects[0].name).to.include(objectName);
  });
});
Then("I should receive a response body with objects userMessage from endpoint cities {string}", (objectUserMessage) => {
  cy.get("@apiResponse").then((response) => {
    expect(response.body.objects[0].userMessage).to.include(
      objectUserMessage,
    );
  });
});


