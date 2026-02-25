Feature: Login 

    Scenario: Valid Login as an Admin
        Given I'm on the login page
        When I type a registered email and password
        Then I have a successful login and see my access_token

    Scenario: Valid Login as an Admin
        Given I'm on the login page
        When I type a registered email and password
        Then I have a successful login and see my access_token

    Scenario: Valid Login as owner of restaurant
        Given I'm on the login page
        When I type a registered email and password
        Then I have a successful login and see my access_token

    Scenario: Valid Login as owner of restaurant
        Given I'm on the login page
        When I type a registered email and password
        Then I have a successful login and see my access_token

