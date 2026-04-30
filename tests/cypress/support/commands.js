import axios from "axios";
Cypress.Commands.add("getTestEnv", () => {
  return cy
  // Primeiramente a gente pega as variáveis de ambiente necessárias para a autenticação
    .env(["SCOPES", "CLIENT_ID", "CLIENT_SECRET"])
    // Então retorna elas em um objeto para facilitar o uso
    .then((env) => {
      return {
        scopes: env.SCOPES,
        clientId: env.CLIENT_ID,
        clientSecret: env.CLIENT_SECRET,
      };
    });
});


// Auth url
// ============================================================
Cypress.Commands.add("getAuthUrlEnv", () => {
  return (
    cy
      // Primeiramente a gente pega as variáveis de ambiente necessárias para a autenticação
      .env(["API_AUTH_URL"])
      // Então retorna elas em um objeto para facilitar o uso
      .then((env) => {
        return {
          authUrl: env.API_AUTH_URL,
        };
      })
  );
});


// Aqui a gente define um comando customizado para solicitar o token de acesso usando as credenciais e escopos fornecidos
Cypress.Commands.add(
  "requestAccessToken",
  // O comando recebe um objeto com as credenciais e escopos necessários para a autenticação, neste caso, é exatamente o objeto retornado pelo comando getTestEnv
  ({ scopes, clientId, clientSecret }) => {
    // Usamos o axios para fazer a requisição POST para o endpoint de token do OAuth2, passando as credenciais e escopos necessários (o cypress não é bom ao lidar com autenticação básica com domínios cruzados, por isso usamos o axios aqui)
    return axios({
      method: "POST",
      url: "http://localhost:80/oauth2/token",
      headers: {
        "Content-Type": "application/x-www-form-urlencoded",
        Authorization: "Basic " + btoa(`${clientId}:${clientSecret}`),
      },
      data: new URLSearchParams({
        grant_type: "client_credentials",
        scope: scopes,
      }),
    }).then((res) => res.data.access_token);
  },
);


// Login
// ============================================================
Cypress.Commands.add("getLoginTestEnv", () => {
  return cy.env(["CLIENT_USERNAME", "CLIENT_PASSWORD"]).then((env) => {
    return {
      username: env.CLIENT_USERNAME,
      password: env.CLIENT_PASSWORD,
    };
  });
});

// Restaurant Owner Informations
// ============================================================
Cypress.Commands.add("getRestaurantOwnerInfo", () => {
  return cy
    .env(["RESTAURANT_OWNER_USERNAME", "RESTAURANT_OWNER_PASSWORD"])
    .then((env) => {
      return {
        restaurantUsername: env.RESTAURANT_OWNER_USERNAME,
        restaurantPassword: env.RESTAURANT_OWNER_PASSWORD,
      };
    });
});



// Access token
// ============================================================
Cypress.Commands.add("getAccessTokenRequestEnv", () => {
  return (
    cy
      .env(["REDIRECT_URL", "API_URL", "CLIENT_ID" , "CLIENT_SECRET" , "CODE_VERIFIER"])
      .then((env) => {
        return {
          redirectUrl: env.REDIRECT_URL,
          apiUrl: env.API_URL,
          clientId: env.CLIENT_ID,
          clientSecret: env.CLIENT_SECRET,
          codeVerifier: env.CODE_VERIFIER,
        };
      })
  );
});
Cypress.Commands.add("makeAccessTokenRequestAndWriteIt", ({ redirectUrl, apiUrl, clientId, clientSecret, codeVerifier } , fileName) => {
   cy.location("search").then((search) => {
      const urlParams = new URLSearchParams(search);
      const capturedCode = urlParams.get("code");
      expect(capturedCode).to.not.be.null;

      cy.request({
        method: "POST",
        url: apiUrl + "/oauth2/token",
        form: true,
        auth: {
          username: clientId,
          password: clientSecret,
        },
        body: {
          grant_type: "authorization_code",
          code: capturedCode,
          redirect_uri: redirectUrl,
          code_verifier: codeVerifier,
        },
      });
    }).then((response) => {
      cy.task("serializeData", {
        token: response.body.access_token,
        fileName: fileName,
      });
    });
});


// Date
Cypress.Commands.add(
  "formatSpecificDate",
  () => {
    return new Date().toISOString().split("T")[0];
  },
);