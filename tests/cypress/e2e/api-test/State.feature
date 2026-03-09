Feature: StateTest

  Scenario: LifeCycle of a State
  # POST
    Given I make a POST request to endpoint states with a valid body
    Then I should receive a response with status code "201"
    Then I should receive a response body with name "São Paulo"
  # GET
    When I make a GET request to endpoint states with a valid id
    Then I should receive a response with status code "200"
    Then I should receive a response body with name "São Paulo"
  # PUT
    When I make a PUT request to endpoint states with a valid body and id
    Then I should receive a response with status code "200"
    Then I should receive a response body with name "São Paulo Atualizada"
  # DELETE
    When I make a DELETE request to endpoint states with a valid id
    Then I should receive a response with status code "204"
    Then I should receive a response with statusText "No Content"
  # Checando se o delete funcionou 
    When I make a GET request to endpoint states with a deleted id
    Then I should receive a response with status code "404"
    Then I should receive a response body with title "Recurso não encontrado."

  Scenario Outline: All Get Requests for States
    Given I make a GET request to endpoint states with a <id>
    Then I should receive a response with status code <statusCode>
    Then I should receive a response body with detail <detail>

    Examples:
      | id                                     | statusCode | detail                                                                                                                                 |
      | "c026aaa1-0cd3-11f1-a36f-ea4dd8dd74c2" | "404"      | "Estado de id 'c026aaa1-0cd3-11f1-a36f-ea4dd8dd74c2' não encontrado!"                                                                  |
      | "abc"                                  | "400"      | "O parâmetro da URL 'stateId' recebeu o valor de 'abc', que é um tipo inválido. Corrija e informe um valor compatível ao tipo 'UUID'." |

  Scenario: All Post Requests for States
    Given I make a POST request to endpoint states with empty name
    Then I should receive a response with status code "400"
    Then I should receive a response body with objects name "name"
    Then I should receive a response body with objects userMessage "Nome do estado contém caracteres inválidos."

  Scenario: Check PUT request to return especific userMessage
    Given I make a PUT request to endpoint states with id "0e0362cc-db84-4484-9909-d6977b96b619" and name " "
    Then I should receive a response with status code "400"
    Then I should receive a response body with objects name "name"
    Then I should receive a response body with objects userMessage "Nome do estado contém caracteres inválidos."

  Scenario Outline: All Put Requests for States
    Given I make a PUT request to endpoint states with id <id> and name <name>
    Then I should receive a response with status code <statusCode>
    Then I should receive a response body with detail <detail>

    Examples:
      | id                                     | name        | statusCode | detail                                                                                                                                  |
      | "abcd"                                 | "São Paulo" | "400"      | "O parâmetro da URL 'stateId' recebeu o valor de 'abcd', que é um tipo inválido. Corrija e informe um valor compatível ao tipo 'UUID'." |
      | "c026aaa1-0cd3-11f1-a36f-ea4dd8dd74c2" | "São Paulo" | "404"      | "Estado de id 'c026aaa1-0cd3-11f1-a36f-ea4dd8dd74c2' não encontrado!"                                                                   |

  Scenario Outline: All Delete Requests for States
    Given I make a DELETE request to endpoint states with an id <id>
    Then I should receive a response with status code <statusCode>
    Then I should receive a response body with detail <detail>

    Examples:
      | id                                     | statusCode | detail                                                                                                                                  |
      | "c026aaa1-0cd3-11f1-a36f-ea4dd8dd74c2" | "404"      | "Estado de id 'c026aaa1-0cd3-11f1-a36f-ea4dd8dd74c2' não encontrado!"                                                                   |
      | "abcd"                                 | "400"      | "O parâmetro da URL 'stateId' recebeu o valor de 'abcd', que é um tipo inválido. Corrija e informe um valor compatível ao tipo 'UUID'." |

  Scenario: Error 409
    Given I make a DELETE request to endpoint states with a using state
    Then I should receive a response with status code "409"
    Then I should receive a response body with userMessage "Entidade com id 'ac3bb31f-4c4f-44ff-88e8-92646ba56240' já está sendo usada!"
    Then I should receive a response with statusText "Conflict"

  Scenario: Error 406
    Given I make a GET request to endpoint states with a valid id and accept header "application/pdf"
    Then I should receive a response with status code "406"
    Then I should receive a response with statusText "Not Acceptable"

