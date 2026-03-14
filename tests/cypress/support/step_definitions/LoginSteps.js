/// <reference types="cypress" />

import { Given, When, Then , Before } from "@badeball/cypress-cucumber-preprocessor";
import LoginPage from "../pageObjects/LoginApi";

const CLIENT_USERNAME = Cypress.env("CLIENT_USERNAME");
const CLIENT_PASSWORD = Cypress.env("CLIENT_PASSWORD");

Before(() => {
  cy.task("unSerializeData", "admin_access_token").then((token) => {
    cy.wrap(token).as("token");
  });
})

Given("I'm on the login page", () => {
  LoginPage.visitLoginPage();
});

When("I type a registered email and password", () => {
    LoginPage.fillUsername(CLIENT_USERNAME);
    LoginPage.fillPassword(CLIENT_PASSWORD);
    LoginPage.submit();
});

When("I type a registered username: {string}", (TEST_USERNAME) => {
  LoginPage.fillUsername(TEST_USERNAME);
});

When("I type a registered password: {string}", (TEST_PASSWORD) => {
  LoginPage.fillPassword(TEST_PASSWORD);
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