@statistics
Feature: StatisticsTest

  Scenario: I make a get in statistics and I receive a response
    Given I make a GET request to endpoint statistics
    Then I should receive a response with status code "200"
    Then I should receive an array but not as a page

  Scenario: I make a get in statistics and with a specific restaurantId I receive a response of this restaurant
    Given I make a GET request to endpoint statistics with a valid id
    Then I should receive a response with status code "200"
    Then I should receive a response body with specific date
    Then I should receive a response body with totalBilled "61.5"
    Then I should receive a response body with totalSales "1"

  Scenario: Error 406
    Given I make a GET request to endpoint statistics with accept header "application/ogg"
    Then I should receive a response with status code "406"
    Then I should receive a response with statusText "Not Acceptable"
