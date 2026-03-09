Feature: PermissionsTest

  Scenario: LifeCycle of a Permission
    When I make a GET request to endpoint permissions
    Then I should receive a response with status code "200"
    Then I should receive a response body as an array

  Scenario: Error 406
    Given I make a GET request to endpoint permissions with accept header "application/pdf"
    Then I should receive a response with status code "406"
    Then I should receive a response with statusText "Not Acceptable"

