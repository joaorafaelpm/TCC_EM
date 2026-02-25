
import {
  Given,
  When,
  Then,
  Before,
} from "@badeball/cypress-cucumber-preprocessor";
import ProductApi from "../pageObjects/ProductApi";
import RestaurantApi from "../pageObjects/RestaurantApi";

const restaurantId = "52ec094f-3e34-42d4-845a-bc1c178259c1";
const productId = "72e58c00-e73f-41ee-bdd7-acf75341a7a7";

Before(() => {
  cy.task("unSerializeData").then((token) => {
    cy.wrap(token).as("token");
  });
});

// ================= RESTAURANT SETUP =================
Given("I make a POST request to create a restaurant for product tests", () => {
  cy.get("@token").then((token) => {
    RestaurantApi.createRestaurant(token).then((response) => {
      cy.wrap(response).as("apiResponse");
      cy.wrap(response.body.id).as("createdRestaurantId");
    });
  });
});

// ================= POST =================
Given("I make a POST request to endpoint products with a valid body", () => {
  cy.get("@token").then((token) => {
    cy.get("@createdRestaurantId").then((restaurantId) => {
      ProductApi.createProduct(token, restaurantId).then((response) => {
        cy.wrap(response).as("apiResponse");
        cy.wrap(response.body.id).as("createdProductId");
      });
    });
  });
});

Given("I make a POST request to endpoint products with empty name", () => {
  cy.get("@token").then((token) => {
    cy.get("@createdRestaurantId").then((restaurantId) => {
      ProductApi.createProductWithCustomBody(token, restaurantId, {
        name: " ",
        description: "Descrição válida",
        price: 29.9,
    }).then((response) => {
        cy.wrap(response).as("apiResponse");
      });
    });
  });
});

Given(
  "I make a POST request to endpoint products with a {string}, {string} and {float}",
  (name, description, price) => {
    cy.get("@token").then((token) => {
      cy.get("@createdRestaurantId").then((restaurantId) => {
        ProductApi.createProductWithCustomBody(token, restaurantId, {
          name,
          description,
          price,
        }).then((response) => {
          cy.wrap(response).as("apiResponse");
        });
      });
    });
  },
);

// ================= POST - produto com campo nulo =================
Given(
  "I make a POST request to endpoint products with a null {string}",
  (field) => {
    cy.get("@token").then((token) => {
      cy.get("@createdRestaurantId").then((restaurantId) => {
        const body = {
          name: "Nome Valido",
          description: "Descrição válida",
          price: 49.9,
        };
        body[field] = null;
        ProductApi.createProductWithCustomBody(token, restaurantId, body).then(
          (response) => {
            cy.wrap(response).as("apiResponse");
          },
        );
      });
    });
  },
);


// ================= GET =================
When("I make a GET request to endpoint products with a valid id", () => {
  cy.get("@token").then((token) => {
    cy.get("@createdRestaurantId").then((restaurantId) => {
      cy.get("@createdProductId").then((productId) => {
        ProductApi.getProductById(token, restaurantId, productId).then(
          (response) => {
            cy.wrap(response).as("apiResponse");
          },
        );
      });
    });
  });
});

Given(
  "I make a GET request to endpoint products with restaurantId {string} and productId {string}",
  (restaurantId, productId) => {
    cy.get("@token").then((token) => {
      ProductApi.getProductById(token, restaurantId, productId).then(
        (response) => {
          cy.wrap(response).as("apiResponse");
        },
      );
    });
  },
);

When("I make a GET request to list all products from the restaurant", () => {
  cy.get("@token").then((token) => {
    cy.get("@createdRestaurantId").then((restaurantId) => {
      ProductApi.getAllProducts(token, restaurantId).then((response) => {
        cy.wrap(response).as("apiResponse");
      });
    });
  });
});

Given(
  "I make a GET request to endpoint products with a valid id and accept header {string}",
  (acceptHeader) => {
    cy.get("@token").then((token) => {
      cy.get("@createdRestaurantId").then((restaurantId) => {
          ProductApi.getProductByIdWithAcceptHeader(
            token,
            restaurantId,
            acceptHeader,
          ).then((response) => {
            cy.wrap(response).as("apiResponse");
          });
      });
    });
  },
);

Given(
  "I make a GET request to endpoint products to get a photo with {string} and {string}",
  (restaurantId, productId) => {
    cy.get("@token").then((token) => {
      ProductApi.getProductPhoto(token, restaurantId, productId).then(
        (response) => {
          cy.wrap(response).as("apiResponse");
        },
      );
    });
  },
);

Given(
  "I make a GET request to endpoint products with a valid restaurantId and productId in endpoint photo",
  () => {
    cy.get("@token").then((token) => {
      cy.get("@createdRestaurantId").then((restaurantId) => {
        cy.get("@createdProductId").then((productId) => {
          ProductApi.getProductPhoto(
            token,
            restaurantId,
            productId,
          ).then((response) => {
            cy.wrap(response).as("apiResponse");
          });
      });
    });
  });
  },
);

// ================= PUT =================
When(
  "I make a PUT request to endpoint products with a valid body and id",
  () => {
    cy.get("@token").then((token) => {
      cy.get("@createdRestaurantId").then((restaurantId) => {
        cy.get("@createdProductId").then((productId) => {
          const updateBody = {
            name: "Pizza Margherita Atualizada",
            description: "Pizza clássica atualizada",
            price: 59.9,
          };
          ProductApi.updateProduct(
            token,
            restaurantId,
            productId,
            updateBody,
          ).then((response) => {
            cy.wrap(response).as("apiResponse");
          });
        });
      });
    });
  },
);

Given("I make a PUT request to endpoint products with empty name", () => {
  cy.get("@token").then((token) => {
    cy.get("@createdRestaurantId").then((restaurantId) => {
      cy.get("@createdProductId").then((productId) => {
        ProductApi.updateProduct(token, restaurantId, productId, {
          name: " ",
          description: "Descrição válida",
          price: 29.9,
        }).then((response) => {
          cy.wrap(response).as("apiResponse");
        });
      });
    });
  });
});

Given(
  "I make a PUT request to endpoint products with restaurantId {string} and productId {string} and name {string}",
  (restaurantId, productId, name) => {
    cy.get("@token").then((token) => {
      ProductApi.updateProduct(token, restaurantId, productId, {
        name: name,
        description: "Descrição válida",
        price: 29.9,
      }).then((response) => {
        cy.wrap(response).as("apiResponse");
      });
    });
  },
);

Given(
  "I make a PUT request to endpoint products with a {string}, {string} and {float}",
  (name, description, price) => {
    cy.get("@token").then((token) => {
      cy.get("@createdRestaurantId").then((restaurantId) => {
        cy.get("@createdProductId").then((productId) => {
          ProductApi.updateProduct(token, restaurantId, productId, {
            name,
            description,
            price,
          }).then((response) => {
            cy.wrap(response).as("apiResponse");
          });
        });
      });
    });
  },
);

// ================= ACTIVE/INACTIVE =================
When("I make a DELETE request to deactivate the product", () => {
  cy.get("@token").then((token) => {
    cy.get("@createdRestaurantId").then((restaurantId) => {
      cy.get("@createdProductId").then((productId) => {
        ProductApi.deactivateProduct(token, restaurantId, productId).then(
          (response) => {
            cy.wrap(response).as("apiResponse");
          },
        );
      });
    });
  });
});

When("I make a PUT request to activate the product", () => {
  cy.get("@token").then((token) => {
    cy.get("@createdRestaurantId").then((restaurantId) => {
      cy.get("@createdProductId").then((productId) => {
        ProductApi.activateProduct(token, restaurantId, productId).then(
          (response) => {
            cy.wrap(response).as("apiResponse");
          },
        );
      });
    });
  });
});
// ================= PUT - relacionado às fotos =================
When(
  "I make a PUT request to endpoint products with a valid restaurantId and productId in endpoint photo",
  () => {
    cy.get("@token").then((token) => {
      cy.get("@createdRestaurantId").then((restaurantId) => {
        cy.get("@createdProductId").then((productId) => {
          ProductApi.addPhotoToProduct(
            token,
            restaurantId,
            productId,
            "exampleImage.jpg",
          ).then((response) => {
            cy.wrap(response).as("apiResponse");
          });

        });
      });
    });
  }
);
When(
  "I make a PUT request to endpoint products to get a photo with an invalid file type",
  () => {
    cy.get("@token").then((token) => {
          ProductApi.addInvalidPhotoToProduct(
            token,
            restaurantId,
            productId,
            "imagemFalsa.pdf",
          ).then((response) => {
            cy.wrap(response).as("apiResponse");
          });
      });
  }
);
// ================= DELETE =================
When(
  "I make a DELETE request to endpoint products with a valid restaurantId and productId in endpoint photo",
  () => {
    cy.get("@token").then((token) => {
      cy.get("@createdRestaurantId").then((restaurantId) => {
        cy.get("@createdProductId").then((productId) => {
          ProductApi.removeProductPhoto(token, restaurantId, productId).then(
            (response) => {
              cy.wrap(response).as("apiResponse");
            },
          );
        });
      });
    });
  },
);

// ================= RESTAURANT TEARDOWN =================
When(
  "I make a DELETE request to deactivate the restaurant in endpoint products",
  () => {
    cy.get("@token").then((token) => {
      cy.get("@createdRestaurantId").then((restaurantId) => {
        RestaurantApi.deactivateRestaurant(token, restaurantId).then(
          (response) => {
            cy.wrap(response).as("apiResponse");
          },
        );
      });
    });
  },
);

// ================= ASSERTIONS (THEN) =================
Then(
  "I should receive a response with status code from endpoint products {string}",
  (statusCode) => {
    cy.get("@apiResponse").then((response) => {
      expect(response.status).to.eq(parseInt(statusCode));
    });
  },
);

Then(
  "I should receive a response body with name from endpoint products {string}",
  (name) => {
    cy.get("@apiResponse").then((response) => {
      expect(response.body.name).to.include(name);
    });
  },
);

Then(
  "I should receive a response body with detail from endpoint products {string}",
  (detail) => {
    cy.get("@apiResponse").then((response) => {
      expect(response.body.detail).to.include(detail);
    });
  },
);

Then(
  "I should receive a response body with title from endpoint products {string}",
  (title) => {
    cy.get("@apiResponse").then((response) => {
      expect(response.body.title).to.include(title);
    });
  },
);

Then("I should receive an array in endpoint products", () => {
  cy.get("@apiResponse").then((response) => {
    expect(response.body._embedded.products).to.be.an("array");
  });
});

Then(
  "I should receive a response with statusText from endpoint products {string}",
  (statusText) => {
    cy.get("@apiResponse").then((response) => {
      expect(response.statusText).to.include(statusText);
    });
  },
);

Then(
  "I should receive a response body with objects name from endpoint products {string}",
  (objectName) => {
    cy.get("@apiResponse").then((response) => {
      expect(response.body.objects[0].name).to.include(objectName);
    });
  },
);

Then(
  "I should receive a response body with objects userMessage from endpoint products {string}",
  (objectUserMessage) => {
    cy.get("@apiResponse").then((response) => {
      expect(response.body.objects[0].userMessage).to.include(
        objectUserMessage,
      );
    });
  },
);

Then(
  "I should receive a response body with description from endpoint products {string}",
  (description) => {
    cy.get("@apiResponse").then((response) => {
      expect(response.body.description).to.include(
        description,
      );
    });
  },
);
