
Feature: OrderTest

  Scenario: LifeCycle of an Order - confirmation then delivery
  # POST
    Given I make a POST request to endpoint orders with a valid body
    Then I should receive a response with status code "201"
    Then I should receive a response body with status "CREATED"
  # GET by id
    When I make a GET request to endpoint "/v1/orders" with id "c5d6e7f8-a9b0-4c1d-fa2e-4a5b6c7d8e9f"
    Then I should receive a response with status code "200"
    Then I should receive a response body with status "CREATED"
  # GET list
    When I make a GET request to list all orders
    Then I should receive a response with status code "200"
    Then I should receive an array in endpoint orders
  # confirmation
    When I make a PUT request to confirm the order
    Then I should receive a response with status code "204"
  # Checar status após confirmação
    When I make a GET request to endpoint orders with a valid id
    Then I should receive a response with status code "200"
    Then I should receive a response body with status "CONFIRMED"
  # delivery
    When I make a PUT request to deliver the order
    Then I should receive a response with status code "204"
  # Checar status após entrega
    When I make a GET request to endpoint orders with a valid id
    Then I should receive a response with status code "200"
    Then I should receive a response body with status "DELIVERED"

  Scenario: LifeCycle of an Order - creation then cancellation
    Given I make a POST request to endpoint orders with a valid body
    Then I should receive a response with status code "201"
  # cancellation
    When I make a PUT request to cancel the order
    Then I should receive a response with status code "204"
  # Checar status após cancelamento
    When I make a GET request to endpoint orders with a valid id
    Then I should receive a response with status code "200"
    Then I should receive a response body with status "CANCELED"

  Scenario: Cancel an Order without confirming first
    Given I make a POST request to endpoint orders with a valid body
    Then I should receive a response with status code "201"
    When I make a PUT request to cancel the order
    Then I should receive a response with status code "204"
    When I make a GET request to endpoint orders with a valid id
    Then I should receive a response body with status "CANCELED"

  Scenario: Try to deliver an Order without confirming first
    Given I make a POST request to endpoint orders with a valid body
    Then I should receive a response with status code "201"
    When I make a PUT request to deliver the order
    Then I should receive a response with status code "400"
    Then I should receive a response body with title "Houve uma violação da regra de negócio."

  Scenario: Try to deliver a cancelled Order
    Given I make a POST request to endpoint orders with a valid body
    When I make a PUT request to cancel the order
    When I make a PUT request to deliver the order
    Then I should receive a response with status code "400"
    Then I should receive a response body with title "Houve uma violação da regra de negócio."

  Scenario Outline: All Get Requests for Orders - invalid ids
    Given I make a GET request to endpoint orders with id <id>
    Then I should receive a response with status code <statusCode>
    Then I should receive a response body with detail <detail>

    Examples:
      | id                                     | statusCode | detail                                                                                                                                 |
      | "c026aaa1-0cd3-11f1-a36f-ea4dd8dd74c2" | "404"      | "Pedido de id 'c026aaa1-0cd3-11f1-a36f-ea4dd8dd74c2' não encontrado!"                                                                  |
      | "abc"                                  | "400"      | "O parâmetro da URL 'orderId' recebeu o valor de 'abc', que é um tipo inválido. Corrija e informe um valor compatível ao tipo 'UUID'." |

  Scenario: Error 406 for Orders
    Given I make a POST request to endpoint orders with a valid body
    Given I make a GET request to endpoint orders with a valid id and accept header "application/pdf"
    Then I should receive a response with status code "406"
    Then I should receive a response with statusText "Not Acceptable"

  Scenario Outline: I make a POST in endpoint orders with invalid fields and should see a bad request
    Given I make a POST request to endpoint orders with a null <field>
    Then I should receive a response with status code "400"
    Then I should receive a response body with objects name <objectName>
    Then I should receive a response body with objects userMessage <userMessage>

    Examples:
      | field             | objectName        | userMessage                                    |
      | "restaurantId"    | "restaurantId"    | "Id do restaurante não pode ser vazio."        |
      | "paymentMethodId" | "paymentMethodId" | "Id da forma de pagamento não pode ser vazio." |
      | "deliveryAddress" | "deliveryAddress" | "Endereço de entrega não pode ser vazio."      |
      | "items"           | "items"           | "A lista de itens não pode ser vazio."         |
