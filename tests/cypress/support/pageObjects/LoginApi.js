const USERNAME_INPUT = "#Email";
const PASSWORD_INPUT = "#Password";
const SUBMIT_BUTTON = "#LoginButton";
const ERROR_MESSAGE_CLASS = ".alert-danger";
const ERROR_MESSAGE_TEXT = "O e-mail ou senha não confere";

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
    cy.get(SUBMIT_BUTTON).click();
  }

  static getErrorMessage() {
    cy.get(ERROR_MESSAGE_CLASS).contains(ERROR_MESSAGE_TEXT);
  }

  static makeAccessTokenRequest() {
    cy.getAccessTokenRequestEnv().then((env) => {
      cy.makeAccessTokenRequestAndWriteIt(env);
    });
  }
}

export default LoginPage;
