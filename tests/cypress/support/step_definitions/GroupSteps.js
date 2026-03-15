import {
  Given,
  When,
  Then,
  Before,
} from "@badeball/cypress-cucumber-preprocessor";
import GroupApi from "../pageObjects/GroupApi";

const groupId = "4a3fdd17-542f-4f6c-b450-871ff0f21092";
const permissionId = "1925eff2-a761-49ff-ab2a-fd471828cb9d";



// ================= POST =================
Given("I make a POST request to endpoint groups with a valid body", () => {
  cy.get("@token").then((token) => {
    GroupApi.createGroup(token).then((response) => {
      cy.wrap(response).as("apiResponse");
      cy.wrap(response.body.id).as("createdGroup");
      cy.wrap(response.body.id).as("createdGroupId");
    });
  });
});

Given("I make a POST request to endpoint groups with empty name", () => {
  cy.get("@token").then((token) => {
    GroupApi.createGroupWithCustomName(" ", token).then((response) => {
      cy.wrap(response).as("apiResponse");
    });
  });
});

// ================= GET =================
When("I make a GET request to endpoint groups with a valid id", () => {
  cy.get("@token").then((token) => {
    cy.get("@createdGroup").then((groupId) => {
      GroupApi.getGroupById(token, groupId).then((response) => {
        cy.wrap(response).as("apiResponse");
      });
    });
  });
});

Given("I make a GET request to endpoint groups with a {string}", (id) => {
  cy.get("@token").then((token) => {
    GroupApi.getGroupById(token, id).then((response) => {
      cy.wrap(response).as("apiResponse");
    });
  });
});

Given("I make a GET request to endpoint groups with a deleted id", () => {
  cy.get("@token").then((token) => {
    cy.get("@createdGroupId").then((groupId) => {
      GroupApi.getGroupById(token, groupId).then((response) => {
        cy.wrap(response).as("apiResponse");
      });
    });
  });
});

Given(
  "I make a GET request to endpoint groups permission with a valid groupId",
  () => {
    cy.get("@token").then((token) => {
      GroupApi.getPermissionsByGroupId(token, groupId).then((response) => {
        cy.wrap(response).as("apiResponse");
      });
    });
  },
);

// ================= PUT =================
When("I make a PUT request to endpoint groups with a valid body and id", () => {
  cy.get("@token").then((token) => {
    cy.get("@createdGroupId").then((groupId) => {
      const updateBody = {
        name: "Cafetão Atualizado",
      };
      GroupApi.updateGroup(token, groupId, updateBody).then((response) => {
        cy.wrap(response).as("apiResponse");
      });
    });
  });
});

Given(
  "I make a PUT request to endpoint groups with id {string} and name {string}",
  (id, name) => {
    cy.get("@token").then((token) => {
      const updateBody = { name: name };
      GroupApi.updateGroup(token, id, updateBody).then((response) => {
        cy.wrap(response).as("apiResponse");
      });
    });
  },
);

// Associate group and permission
Given(
  "I make a PUT request to endpoint groups with a valid id and valid permissionId",
  () => {
    cy.get("@token").then((token) => {
      GroupApi.groupAndPermissionMethod(
        token,
        "PUT",
        groupId,
        permissionId,
      ).then((response) => {
        cy.wrap(response).as("apiResponse");
      });
    });
  },
);
Given(
  "I make a DELETE request to endpoint groups with a valid id and valid permissionId",
  () => {
    cy.get("@token").then((token) => {
      GroupApi.groupAndPermissionMethod(
        token,
        "DELETE",
        groupId,
        permissionId,
      ).then((response) => {
        cy.wrap(response).as("apiResponse");
      });
    });
  },
);
Given(
  "I make a {string} request to endpoint groups with id {string} and permissionId {string}",
  (method, groupId, permissionId) => {
    cy.get("@token").then((token) => {
      GroupApi.groupAndPermissionMethod(
        token,
        method,
        groupId,
        permissionId,
      ).then((response) => {
        cy.wrap(response).as("apiResponse");
      });
    });
  },
);

// ================= DELETE =================
When("I make a DELETE request to endpoint groups with a valid id", () => {
  cy.get("@token").then((token) => {
    cy.get("@createdGroupId").then((createdGroupId) => {
      GroupApi.deleteGroup(token, createdGroupId).then((response) => {
        cy.wrap(response).as("apiResponse");
      });
    });
  });
});

Given(
  "I make a DELETE request to endpoint groups with an id {string}",
  (id) => {
    cy.get("@token").then((token) => {
      GroupApi.deleteGroup(token, id).then((response) => {
        cy.wrap(response).as("apiResponse");
      });
    });
  },
);

Given("I make a DELETE request to endpoint groups with a using groups", () => {
  cy.get("@token").then((token) => {
    GroupApi.deleteGroup(token, groupId).then((response) => {
      cy.wrap(response).as("apiResponse");
    });
  });
});

// ================ DELETE - test 409 error =================
When("I make a DELETE request to endpoint groups with a using group", () => {
  cy.get("@token").then((token) => {
    GroupApi.deleteGroup(token, groupId).then((response) => {
      cy.wrap(response).as("apiResponse");
    });
  });
});

// ================ GET - test 406 error =================
Given(
  "I make a GET request to endpoint groups with a valid id and accept header {string}",
  (acceptHeader) => {
    cy.get("@token").then((token) => {
      GroupApi.getGroupByIdWithAcceptHeader(token, groupId, acceptHeader).then(
        (response) => {
          cy.wrap(response).as("apiResponse");
        },
      );
    });
  },
);
