Feature: Login 

    Scenario: Invalid Login
    Given I'm on the login page
    When I type a registered username: "wrong@email"
    And I type a registered password: "wrong_password"
    And I click submit button
    Then I should see a error page

    Scenario: Valid Login as an Admin
    Given I'm on the login page
    When I type a registered email and password
    Then I have a successful login and see my access_token
