import {
  Given,
  When,
  Then,
  Before,
} from "@badeball/cypress-cucumber-preprocessor";
import UserApi from "../pageObjects/UserApi";

const userId = "a6162eb1-df44-471b-aef3-9feee0d9d267";
const groupId = "4a3fdd17-542f-4f6c-b450-871ff0f21092";

Before(() => {
  cy.task("unSerializeData").then((token) => {
    cy.wrap(token).as("token");
  });
});

// ================= POST =================
Given("I make a POST request to endpoint users with a valid body", () => {
  cy.get("@token").then((token) => {
    const body = {
      name: "João Teste",
      email: "joao.teste@gmail.com",
      password: "senha123",
    };

    UserApi.createUser(token, body).then((response) => {
      cy.wrap(response).as("apiResponse");
      cy.wrap(body.password).as("createdUserPassword");
      cy.wrap(response.body.id).as("createdUserId");
    });
  });
});

Given(
  "I make a POST request to endpoint users with a {string} , {string} and {string}",
  (name , email , password) => {
    cy.get("@token").then((token) => {
      const updateBody = {
        name: name,
        email: email,
        password: password,
      };
      UserApi.createUserWithCustomBody(token, updateBody).then((response) => {
        cy.wrap(response).as("apiResponse");
      });
    });
  },
);

// ================= GET =================
When("I make a GET request to endpoint users with a valid id", () => {
  cy.get("@token").then((token) => {
    cy.get("@createdUserId").then((id) => {
      UserApi.getUserById(token, id).then((response) => {
        cy.wrap(response).as("apiResponse");
      });
    });
  });
});

Given("I make a GET request to endpoint users with a {string}", (id) => {
  cy.get("@token").then((token) => {
    UserApi.getUserById(token, id).then((response) => {
      cy.wrap(response).as("apiResponse");
    });
  });
});

Given("I make a GET request to endpoint users with a deleted id", () => {
  cy.get("@token").then((token) => {
    cy.get("@createdUserId").then((id) => {
      UserApi.getUserById(token, id).then((response) => {
        cy.wrap(response).as("apiResponse");
      });
    });
  });
});

// ================= PUT =================
When("I make a PUT request to endpoint users with a valid body and id", () => {
  cy.get("@token").then((token) => {
    cy.get("@createdUserId").then((id) => {
      const updateBody = {
        name: "João Teste Atualizado",
        email: "joao.teste.atualizado@gmail.com",
      };
      UserApi.updateUser(token, id, updateBody).then((response) => {
        cy.wrap(response).as("apiResponse");
      });
    });
  });
});
When(
  "I make a PUT request to endpoint users with a valid body and id to change user password",
  () => {
    cy.get("@token").then((token) => {
      cy.get("@createdUserPassword").then((currentPassword) => {
        const updatePassword = {
          currentPassword: currentPassword,
          newPassword: "novaSenha123",
        };
        cy.get("@createdUserId").then((id) => {
          UserApi.updateUserPassword(token, id, updatePassword).then(
            (response) => {
              cy.wrap(response).as("apiResponse");
            },
          );
        });
      });
    });
  },
);

Given(
  "I make a PUT request to endpoint users with a valid id, {string} and {string}",
  (name, email) => {
    cy.get("@token").then((token) => {
      const updateBody = { name: name, email: email };
      UserApi.updateUser(token, userId, updateBody).then((response) => {
        cy.wrap(response).as("apiResponse");
      });
    });
  },
);

Given(
  "I make a PUT request to endpoint users with an id {string} and name {string}",
  (id , name) => {
    cy.get("@token").then((token) => {
      const updateBody = { name: name, email: "emailgenerico@gmail" };
      UserApi.updateUser(token, id, updateBody).then((response) => {
        cy.wrap(response).as("apiResponse");
      });
    });
  },
);




// Associate user and group
Given(
  "I make a PUT request to endpoint users with a valid id and valid groupId",
  () => {
    cy.get("@token").then((token) => {
      UserApi.userAndGroupMethod(token, "PUT", userId, groupId).then(
        (response) => {
          cy.wrap(response).as("apiResponse");
        },
      );
    });
  },
);

Given(
  "I make a DELETE request to endpoint users with a valid id and valid groupId",
  () => {
    cy.get("@token").then((token) => {
      UserApi.userAndGroupMethod(token, "DELETE", userId, groupId).then(
        (response) => {
          cy.wrap(response).as("apiResponse");
        },
      );
    });
  },
);

Given(
  "I make a {string} request to endpoint users with id {string} and groupId {string}",
  (method, userId, groupId) => {
    cy.get("@token").then((token) => {
      UserApi.userAndGroupMethod(token, method, userId, groupId).then(
        (response) => {
          cy.wrap(response).as("apiResponse");
        },
      );
    });
  },
);

// ================= DELETE =================
When("I make a DELETE request to endpoint users with a valid id", () => {
  cy.get("@token").then((token) => {
    cy.get("@createdUserId").then((id) => {
      UserApi.deleteUser(token, id).then((response) => {
        cy.wrap(response).as("apiResponse");
      });
    });
  });
});

// ================ GET - test 406 error =================
Given(
  "I make a GET request to endpoint users with a valid id and accept header {string}",
  (acceptHeader) => {
    cy.get("@token").then((token) => {
      UserApi.getUserByIdWithAcceptHeader(token, userId, acceptHeader).then(
        (response) => {
          cy.wrap(response).as("apiResponse");
        },
      );
    });
  },
);

// ================= ASSERTIONS (THEN) =================
Then(
  "I should receive a response with status code from endpoint users {string}",
  (statusCode) => {
    cy.get("@apiResponse").then((response) => {
      expect(response.status).to.eq(parseInt(statusCode));
    });
  },
);

Then(
  "I should receive a response body with title from endpoint users {string}",
  (title) => {
    cy.get("@apiResponse").then((response) => {
      expect(response.body.title).to.include(title);
    });
  },
);

Then(
  "I should receive a response body with name from endpoint users {string}",
  (name) => {
    cy.get("@apiResponse").then((response) => {
      expect(response.body.name).to.include(name);
    });
  },
);

Then(
  "I should receive a response body with detail from endpoint users {string}",
  (detail) => {
    cy.get("@apiResponse").then((response) => {
      expect(response.body.detail).to.include(detail);
    });
  },
);

Then("I should receive an array in endpoint users", () => {
  cy.get("@apiResponse").then((response) => {
    expect(response.body).to.be.an("array");
  });
});

Then(
  "I should receive a response with statusText from endpoint users {string}",
  (statusText) => {
    cy.get("@apiResponse").then((response) => {
      expect(response.statusText).to.include(statusText);
    });
  },
);

Then(
  "I should receive a response body with objects name from endpoint users {string}",
  (objectName) => {
    cy.get("@apiResponse").then((response) => {
      expect(response.body.objects[0].name).to.include(objectName);
    });
  },
);

Then(
  "I should receive a response with detail containing the message from users {string}",
  (detail) => {
    cy.get("@apiResponse").then((response) => {
      expect(response.body.detail).to.include(detail);
    });
  },
);

Then(
  "I should receive a response body with objects userMessage from endpoint users {string}",
  (objectUserMessage) => {
    cy.get("@apiResponse").then((response) => {
      expect(response.body.objects[0].userMessage).to.include(objectUserMessage);
    });
  },
);

Then(
  "I should receive a response body with userMessage from endpoint users {string}",
  (objectUserMessage) => {
    cy.get("@apiResponse").then((response) => {
      expect(response.body.userMessage).to.include(objectUserMessage);
    });
  },
);
