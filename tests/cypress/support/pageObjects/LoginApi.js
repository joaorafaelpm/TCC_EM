const USERNAME_INPUT = '[name="username"]';
const PASSWORD_INPUT = '[name="password"]';
const SUBMIT_BUTTON = "#LoginButton";
const ERROR_MESSAGE_CLASS = "#erroMsg";
const ERROR_MESSAGE_TEXT = "Email ou senha inválidos.";

class LoginPage {
  static visitLoginPage() {
    cy.getAuthUrlEnv().then((env) => {
      cy.visit(env.authUrl);
    });
  }

  static fillUsername(name) {
    cy.get(USERNAME_INPUT).type(name);
  }

  static fillPassword(password) {
    cy.get(PASSWORD_INPUT).type(password);
  }

  static submit() {
    cy.get(".login-popup-condition > input").check();
    cy.get(SUBMIT_BUTTON).click();
  }

  static getErrorMessage() {
    cy.get(ERROR_MESSAGE_CLASS).contains(ERROR_MESSAGE_TEXT);
  }

  static makeAccessTokenRequest() {
    cy.getAccessTokenRequestEnv().then((env) => {
      cy.makeAccessTokenRequestAndWriteIt(env , "admin_access_token");
    });
  }
}

export default LoginPage;
