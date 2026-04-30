import {
  Given,
  When,
  Then,
  Before,
} from "@badeball/cypress-cucumber-preprocessor";
import UserApi from "../pageObjects/UserApi";

const userId = "a6162eb1-df44-471b-aef3-9feee0d9d267";
const groupId = "4a3fdd17-542f-4f6c-b450-871ff0f21092";

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
