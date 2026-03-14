Feature: PaymentMethodTest

  Scenario: LifeCycle of a Payment Method
  # POST
    Given I make a POST request to endpoint payment-methods with a valid body
    Then I should receive a response with status code "201"
    Then I should receive a response body with description "Boleto"
  # GET
    When I make a GET request to endpoint payment-methods with a valid id
    Then I should receive a response with status code "200"
    Then I should receive a response body with description "Boleto"
  # PUT
    When I make a PUT request to endpoint payment-methods with a valid body and id
    Then I should receive a response with status code "200"
    Then I should receive a response body with description "Boleto Atualizado"
  # DELETE
    When I make a DELETE request to endpoint payment-methods with a valid id
    Then I should receive a response with status code "204"
    Then I should receive a response with statusText "No Content"
  # Checando se o delete funcionou 
    When I make a GET request to endpoint payment-methods with a deleted id
    Then I should receive a response with status code "404"
    Then I should receive a response body with title "Recurso não encontrado."

  Scenario Outline: All Get Requests for Payment Methods
    Given I make a GET request to endpoint payment-methods with a <id>
    Then I should receive a response with status code <statusCode>
    Then I should receive a response body with detail <detail>

    Examples:
      | id                                     | statusCode | detail                                                                                                                                         |
      | "c026aaa1-0cd3-11f1-a36f-ea4dd8dd74c2" | "404"      | "Forma de pagamento de id 'c026aaa1-0cd3-11f1-a36f-ea4dd8dd74c2' não encontrada!"                                                              |
      | "abc"                                  | "400"      | "O parâmetro da URL 'paymentMethodId' recebeu o valor de 'abc', que é um tipo inválido. Corrija e informe um valor compatível ao tipo 'UUID'." |

  Scenario: All Post Requests for Payment Methods
    Given I make a POST request to endpoint payment-methods with empty description
    Then I should receive a response with status code "400"
    Then I should receive a response body with objects name "description"
    Then I should receive a response body with objects userMessage "Descrição da forma de pagamento contém caracteres inválidos."

  Scenario: Check PUT request to return especific userMessage
    Given I make a PUT request to endpoint payment-methods with id "0e0362cc-db84-4484-9909-d6977b96b619" and empty description
    Then I should receive a response with status code "400"
    Then I should receive a response body with objects name "description"
    Then I should receive a response body with objects userMessage "Descrição da forma de pagamento contém caracteres inválidos."

  Scenario Outline: All Put Requests for Payment Methods
    Given I make a PUT request to endpoint payment-methods with id <id> and description <description>
    Then I should receive a response with status code <statusCode>
    Then I should receive a response body with detail <detail>

    Examples:
      | id                                     | description         | statusCode | detail                                                                                                                                          |
      | "abcd"                                 | "Cartão de Crédito" | "400"      | "O parâmetro da URL 'paymentMethodId' recebeu o valor de 'abcd', que é um tipo inválido. Corrija e informe um valor compatível ao tipo 'UUID'." |
      | "c026aaa1-0cd3-11f1-a36f-ea4dd8dd74c2" | "Cartão de Crédito" | "404"      | "Forma de pagamento de id 'c026aaa1-0cd3-11f1-a36f-ea4dd8dd74c2' não encontrada!"                                                               |

  Scenario Outline: All Delete Requests for Payment Methods
    Given I make a DELETE request to endpoint payment-methods with an id <id>
    Then I should receive a response with status code <statusCode>
    Then I should receive a response body with detail <detail>

    Examples:
      | id                                     | statusCode | detail                                                                                                                                          |
      | "c026aaa1-0cd3-11f1-a36f-ea4dd8dd74c2" | "404"      | "Forma de pagamento de id 'c026aaa1-0cd3-11f1-a36f-ea4dd8dd74c2' não encontrada!"                                                               |
      | "abcd"                                 | "400"      | "O parâmetro da URL 'paymentMethodId' recebeu o valor de 'abcd', que é um tipo inválido. Corrija e informe um valor compatível ao tipo 'UUID'." |

  Scenario: Error 409
    Given I make a DELETE request to endpoint payment-methods with a using payment method
    Then I should receive a response with status code "409"
    Then I should receive a response body with userMessage "Entidade com id '3ee42ee7-3d35-4680-afe0-e01a24e649dc' já está sendo usada!"

  Scenario: Error 406
    Given I make a GET request to endpoint payment-methods with a valid id and accept header "application/pdf"
    Then I should receive a response with status code "406"
    Then I should receive a response with statusText "Not Acceptable"



