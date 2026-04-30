/// <reference types="cypress" />

import { Given, When, Then , Before } from "@badeball/cypress-cucumber-preprocessor";
import LoginPage from "../pageObjects/LoginApi";



When("I type a registered email and password", () => {
  cy.getLoginTestEnv().then((info) => {
    LoginPage.fillUsername(info.username);
    LoginPage.fillPassword(info.password);
    LoginPage.submit();
  });
});

When("I type a registered username: {string}", (TEST_USERNAME) => {
  LoginPage.fillUsername(TEST_USERNAME);
});

When("I type a registered password: {string}", (TEST_PASSWORD) => {
  LoginPage.fillPassword(TEST_PASSWORD);
});

