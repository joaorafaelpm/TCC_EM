Feature: StatisticsTest

  Scenario: I make a get in statistics and I receive a response
    Given I make a GET request to endpoint statistics
    Then I should receive a response with status code "200"
    Then I should receive a response body with an array

  Scenario: I make a get in statistics and with a specific restaurantId I receive a response of this restaurant
    Given I make a GET request to endpoint statistics with a valid id
    Then I should receive a response with status code "200"
    Then I should receive a response body with date "2026-02-22" 
    Then I should receive a response body with totalBilled "725.5"
    Then I should receive a response body with totalSales "1"

  
  Scenario: Error 406
    Given I make a GET request to endpoint statistics with accept header "application/ogg"
    Then I should receive a response with status code "406"
    Then I should receive a response with statusText ronaldo "Not Acceptable"
