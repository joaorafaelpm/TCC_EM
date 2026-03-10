Feature: RestaurantTest

  Scenario: LifeCycle of a Restaurant
  # POST
    Given I make a POST request to endpoint restaurants with a valid body
    Then I should receive a response with status code "201"
    Then I should receive a response body with name "Pizzaria Fredbear"
  # GET by id
    When I make a GET request to endpoint restaurants with a valid id
    Then I should receive a response with status code "200"
    Then I should receive a response body with name "Pizzaria Fredbear"
  # GET list
    When I make a GET request to list all restaurants
    Then I should receive a response with status code "200"
    Then I should receive an array
  # PUT
    When I make a PUT request to endpoint restaurants with a valid body and id
    Then I should receive a response with status code "200"
    Then I should receive a response body with name "Pizzaria Fredbear Atualizada"
  # Activate / Deactivate
    When I make a PUT request to activate the restaurant
    Then I should receive a response with status code "204"
    When I make a DELETE request to deactivate the restaurant
    Then I should receive a response with status code "204"
  # Open / Close
    When I make a PUT request to activate the restaurant
    When I make a PUT request to open the restaurant
    Then I should receive a response with status code "204"
    When I make a PUT request to close the restaurant
    Then I should receive a response with status code "204"

  # PUT Associate users and payment-methods 
    Given I make a PUT request to endpoint restaurants with a valid id and valid userId
    Then I should receive a response with status code "204"

    Given I make a PUT request to endpoint restaurants with a valid id and valid paymentMethodId
    Then I should receive a response with status code "204"

  # GET responsible-users e payment-methods
    When I make a GET request to list responsible users of the restaurant
    Then I should receive a response with status code "200"
    Then I should receive an array 
    
    When I make a GET request to list payment methods of the restaurant
    Then I should receive a response with status code "200"
    Then I should receive an array

  Scenario Outline: All Get Requests for Restaurants - invalid ids
    Given I make a GET request to endpoint restaurants with id <id>
    Then I should receive a response with status code <statusCode>
    Then I should receive a response body with detail <detail>

    Examples:
      | id                                     | statusCode | detail                                                                                                                                      |
      | "c026aaa1-0cd3-11f1-a36f-ea4dd8dd74c2" | "404"      | "Restaurante de id 'c026aaa1-0cd3-11f1-a36f-ea4dd8dd74c2' não encontrado!"                                                                  |
      | "abc"                                  | "400"      | "O parâmetro da URL 'restaurantId' recebeu o valor de 'abc', que é um tipo inválido. Corrija e informe um valor compatível ao tipo 'UUID'." |

  Scenario: Error 406 for Restaurants
    Given I make a POST request to endpoint restaurants with a valid body
    Given I make a GET request to endpoint restaurants with a valid id and accept header "application/pdf"
    Then I should receive a response with status code "406"
    Then I should receive a response with statusText "Not Acceptable"

  Scenario: Associate a responsible user to a Restaurant
    Given I make a POST request to endpoint restaurants with a valid body
    Given I make a PUT request to endpoint restaurants with a valid id and valid userId
    Then I should receive a response with status code "204"

  Scenario: Disassociate a responsible user from a Restaurant
    Given I make a POST request to endpoint restaurants with a valid body
    Given I make a DELETE request to endpoint restaurants with a valid id and valid userId
    Then I should receive a response with status code "204"

  Scenario Outline: Associate and Disassociate responsible users - badends
    Given I make a <request> request to endpoint restaurants responsible-users with restaurantId <restaurantId> and userId <userId>
    Then I should receive a response with status code <statusCode>
    Then I should receive a response body with detail <message>

    Examples:
      | request  | restaurantId                           | userId                                 | statusCode | message                                                                                                                                      |
      | "PUT"    | "52ec094f-3e34-42d4-845a-bc1c178259c1" | "52ec094f-3e34-42d4-845a-bc1c178259c1" | "404"      | "User com ID 52ec094f-3e34-42d4-845a-bc1c178259c1 não foi encontrado."                                                                       |
      | "PUT"    | "c026aaa1-0cd3-11f1-a36f-ea4dd8dd74c2" | "c026aaa1-0cd3-11f1-a36f-ea4dd8dd74c2" | "404"      | "Restaurante de id 'c026aaa1-0cd3-11f1-a36f-ea4dd8dd74c2' não encontrado!"                                                                   |
      | "PUT"    | "abcd"                                 | "c026aaa1-0cd3-11f1-a36f-ea4dd8dd74c2" | "400"      | "O parâmetro da URL 'restaurantId' recebeu o valor de 'abcd', que é um tipo inválido. Corrija e informe um valor compatível ao tipo 'UUID'." |
      | "PUT"    | "52ec094f-3e34-42d4-845a-bc1c178259c1" | "abcd"                                 | "400"      | "O parâmetro da URL 'userId' recebeu o valor de 'abcd', que é um tipo inválido. Corrija e informe um valor compatível ao tipo 'UUID'."       |
      | "DELETE" | "c026aaa1-0cd3-11f1-a36f-ea4dd8dd74c2" | "52ec094f-3e34-42d4-845a-bc1c178259c1" | "404"      | "Restaurante de id 'c026aaa1-0cd3-11f1-a36f-ea4dd8dd74c2' não encontrado!"                                                                   |
      | "DELETE" | "52ec094f-3e34-42d4-845a-bc1c178259c1" | "c026aaa1-0cd3-11f1-a36f-ea4dd8dd74c2" | "404"      | "User com ID c026aaa1-0cd3-11f1-a36f-ea4dd8dd74c2 não foi encontrado."                                                                       |
      | "DELETE" | "abcd"                                 | "c026aaa1-0cd3-11f1-a36f-ea4dd8dd74c2" | "400"      | "O parâmetro da URL 'restaurantId' recebeu o valor de 'abcd', que é um tipo inválido. Corrija e informe um valor compatível ao tipo 'UUID'." |
      | "DELETE" | "52ec094f-3e34-42d4-845a-bc1c178259c1" | "abcd"                                 | "400"      | "O parâmetro da URL 'userId' recebeu o valor de 'abcd', que é um tipo inválido. Corrija e informe um valor compatível ao tipo 'UUID'."       |

  Scenario: Associate a payment method to a Restaurant
    Given I make a POST request to endpoint restaurants with a valid body
    Given I make a PUT request to endpoint restaurants with a valid id and valid paymentMethodId
    Then I should receive a response with status code "204"

  Scenario: Disassociate a payment method from a Restaurant
    Given I make a POST request to endpoint restaurants with a valid body
    Given I make a DELETE request to endpoint restaurants with a valid id and valid paymentMethodId
    Then I should receive a response with status code "204"

  Scenario Outline: Associate and Disassociate payment methods - badends
    Given I make a <request> request to endpoint restaurants payment-methods with restaurantId <restaurantId> and paymentMethodId <paymentMethodId>
    Then I should receive a response with status code <statusCode>
    Then I should receive a response body with detail <message>

    Examples:
      | request  | restaurantId                           | paymentMethodId                        | statusCode | message                                                                                                                                         |
      | "PUT"    | "52ec094f-3e34-42d4-845a-bc1c178259c1" | "52ec094f-3e34-42d4-845a-bc1c178259c1" | "404"      | "Forma de pagamento de id '52ec094f-3e34-42d4-845a-bc1c178259c1' não encontrada!"                                                               |
      | "PUT"    | "c026aaa1-0cd3-11f1-a36f-ea4dd8dd74c2" | "c026aaa1-0cd3-11f1-a36f-ea4dd8dd74c2" | "404"      | "Restaurante de id 'c026aaa1-0cd3-11f1-a36f-ea4dd8dd74c2' não encontrado!"                                                                      |
      | "PUT"    | "abcd"                                 | "c026aaa1-0cd3-11f1-a36f-ea4dd8dd74c2" | "400"      | "O parâmetro da URL 'restaurantId' recebeu o valor de 'abcd', que é um tipo inválido. Corrija e informe um valor compatível ao tipo 'UUID'."    |
      | "PUT"    | "52ec094f-3e34-42d4-845a-bc1c178259c1" | "abcd"                                 | "400"      | "O parâmetro da URL 'paymentMethodId' recebeu o valor de 'abcd', que é um tipo inválido. Corrija e informe um valor compatível ao tipo 'UUID'." |
      | "DELETE" | "c026aaa1-0cd3-11f1-a36f-ea4dd8dd74c2" | "52ec094f-3e34-42d4-845a-bc1c178259c1" | "404"      | "Restaurante de id 'c026aaa1-0cd3-11f1-a36f-ea4dd8dd74c2' não encontrado!"                                                                      |
      | "DELETE" | "52ec094f-3e34-42d4-845a-bc1c178259c1" | "c026aaa1-0cd3-11f1-a36f-ea4dd8dd74c2" | "404"      | "Forma de pagamento de id 'c026aaa1-0cd3-11f1-a36f-ea4dd8dd74c2' não encontrada!"                                                               |
      | "DELETE" | "abcd"                                 | "c026aaa1-0cd3-11f1-a36f-ea4dd8dd74c2" | "400"      | "O parâmetro da URL 'restaurantId' recebeu o valor de 'abcd', que é um tipo inválido. Corrija e informe um valor compatível ao tipo 'UUID'."    |
      | "DELETE" | "52ec094f-3e34-42d4-845a-bc1c178259c1" | "abcd"                                 | "400"      | "O parâmetro da URL 'paymentMethodId' recebeu o valor de 'abcd', que é um tipo inválido. Corrija e informe um valor compatível ao tipo 'UUID'." |
