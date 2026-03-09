Feature: CityTest

  Scenario: LifeCycle of a City
  # POST
    Given I make a POST request to endpoint cities with a valid body
    Then I should receive a response with status code "201"
    Then I should receive a response body with name "São Carlos"
  # GET
    When I make a GET request to endpoint "/v1/cities" with id "0e0362cc-db84-4484-9909-d6977b96b619"
    Then I should receive a response with status code "200"
    Then I should receive a response body with name "Monte Verde"
  # PUT
    When I make a PUT request to endpoint cities with a valid body and id
    Then I should receive a response with status code "200"
    Then I should receive a response body with name "São Carlos Atualizada"
  # DeLETE
    When I make a DELETE request to endpoint cities with a valid id
    Then I should receive a response with status code "204"
    Then I should receive a response with statusText "No Content"
  # Checando se o delete funcionou 
    When I make a GET request to endpoint cities with a deleted id
    Then I should receive a response with status code "404"
    Then I should receive a response body with title "Recurso não encontrado."

  Scenario Outline: All Get Requests for Cities
    Given I make a GET request to endpoint cities with a <id>
    Then I should receive a response with status code <statusCode>
    Then I should receive a response body with detail <detail>

    Examples:
      | id                                     | statusCode | detail                                                                                                                                |
      | "c026aaa1-0cd3-11f1-a36f-ea4dd8dd74c2" | "404"      | "Cidade com id 'c026aaa1-0cd3-11f1-a36f-ea4dd8dd74c2' não encontrada!"                                                                |
      | "abc"                                  | "400"      | "O parâmetro da URL 'cityId' recebeu o valor de 'abc', que é um tipo inválido. Corrija e informe um valor compatível ao tipo 'UUID'." |

  Scenario: All Post Requests for Cities
    Given I make a POST request to endpoint cities with empty name and a valid stateId
    Then I should receive a response with status code "400"
    Then I should receive a response body with objects name "name"
    Then I should receive a response body with objects userMessage "Nome da cidade contém caracteres inválidos."
    Given I make a POST request to endpoint cities with valid name but an invalid stateId "8db9b408-0df6-11f1-ab75-82b16df15064"
    Then I should receive a response with status code "400"
    Then I should receive a response body with detail "Estado de id '8db9b408-0df6-11f1-ab75-82b16df15064' não encontrado!"

  Scenario: Check PUT request to return especific userMessage
    Given I make a PUT request to endpoint cities with id "0e0362cc-db84-4484-9909-d6977b96b619" and name " "
    Then I should receive a response with status code "400"
    Then I should receive a response body with objects name "name"
    Then I should receive a response body with objects userMessage "Nome da cidade contém caracteres inválidos."

  Scenario Outline: All Put Requests for Cities
    Given I make a PUT request to endpoint cities with id <id> and name <name>
    Then I should receive a response with status code <statusCode>
    Then I should receive a response body with detail <detail>

    Examples:
      | id                                     | name         | statusCode | detail                                                                                                                                 |
      | "abcd"                                 | "São Carlos" | "400"      | "O parâmetro da URL 'cityId' recebeu o valor de 'abcd', que é um tipo inválido. Corrija e informe um valor compatível ao tipo 'UUID'." |
      | "c026aaa1-0cd3-11f1-a36f-ea4dd8dd74c2" | "São Carlos" | "404"      | "Cidade com id 'c026aaa1-0cd3-11f1-a36f-ea4dd8dd74c2' não encontrada!"                                                                 |

  Scenario Outline: All Delete Requests for Cities
    Given I make a DELETE request to endpoint cities with an id <id>
    Then I should receive a response with status code <statusCode>
    Then I should receive a response body with detail <detail>

    Examples:
      | id                                     | statusCode | detail                                                                                                                                 |
      | "c026aaa1-0cd3-11f1-a36f-ea4dd8dd74c2" | "404"      | "Cidade com id 'c026aaa1-0cd3-11f1-a36f-ea4dd8dd74c2' não encontrada!"                                                                 |
      | "abcd"                                 | "400"      | "O parâmetro da URL 'cityId' recebeu o valor de 'abcd', que é um tipo inválido. Corrija e informe um valor compatível ao tipo 'UUID'." |

  Scenario: Error 406
    Given I make a GET request to endpoint cities with a valid id and accept header "application/xml"
    Then I should receive a response with status code "406"
    Then I should receive a response with statusText "Not Acceptable"

