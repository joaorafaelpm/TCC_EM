Feature: GroupTest

  Scenario: LifeCycle of a Group
  # POST
    Given I make a POST request to endpoint groups with a valid body
    Then I should receive a response with status code "201"
    Then I should receive a response body with name "Cafetão"
  # GET
    When I make a GET request to endpoint "/v1/groups" with id "4a3fdd17-542f-4f6c-b450-871ff0f21092"
    Then I should receive a response with status code "200"
    Then I should receive a response body with name "Tester"
  # PUT
    When I make a PUT request to endpoint groups with a valid body and id
    Then I should receive a response with status code "200"
    Then I should receive a response body with name "Cafetão Atualizado"
  # DELETE
    When I make a DELETE request to endpoint groups with a valid id
    Then I should receive a response with status code "204"
    Then I should receive a response with statusText "No Content"
  # Checando se o delete funcionou 
    When I make a GET request to endpoint groups with a deleted id
    Then I should receive a response with status code "404"
    Then I should receive a response body with title "Recurso não encontrado."

  Scenario Outline: All Get Requests for Groups
    Given I make a GET request to endpoint groups with a <id>
    Then I should receive a response with status code <statusCode>
    Then I should receive a response body with detail <detail>

    Examples:
      | id                                     | statusCode | detail                                                                                                                                 |
      | "c026aaa1-0cd3-11f1-a36f-ea4dd8dd74c2" | "404"      | "Grupo de id 'c026aaa1-0cd3-11f1-a36f-ea4dd8dd74c2' não encontrado!"                                                                   |
      | "abc"                                  | "400"      | "O parâmetro da URL 'groupId' recebeu o valor de 'abc', que é um tipo inválido. Corrija e informe um valor compatível ao tipo 'UUID'." |

  Scenario: I make a group POST with an empty name and check the response
    Given I make a POST request to endpoint groups with empty name
    Then I should receive a response with status code "400"
    Then I should receive a response body with objects name "name"
    Then I should receive a response body with objects userMessage "Nome do grupo contém caracteres inválidos."

  Scenario: I make a group PUT with an empty name and check the response
    Given I make a PUT request to endpoint groups with id "0e0362cc-db84-4484-9909-d6977b96b619" and name " "
    Then I should receive a response with status code "400"
    Then I should receive a response body with objects name "name"
    Then I should receive a response body with objects userMessage "Nome do grupo contém caracteres inválidos."

  Scenario Outline: Check a lot of PUT responses for Groups
    Given I make a PUT request to endpoint groups with id <id> and name <name>
    Then I should receive a response with status code <statusCode>
    Then I should receive a response body with detail <detail>

    Examples:
      | id                                     | name        | statusCode | detail                                                                                                                                  |
      | "abcd"                                 | "São Paulo" | "400"      | "O parâmetro da URL 'groupId' recebeu o valor de 'abcd', que é um tipo inválido. Corrija e informe um valor compatível ao tipo 'UUID'." |
      | "c026aaa1-0cd3-11f1-a36f-ea4dd8dd74c2" | "São Paulo" | "404"      | "Grupo de id 'c026aaa1-0cd3-11f1-a36f-ea4dd8dd74c2' não encontrado!"                                                                    |

  Scenario: Error 406
    Given I make a GET request to endpoint groups with a valid id and accept header "application/pdf"
    Then I should receive a response with status code "406"
    Then I should receive a response with statusText "Not Acceptable"

  Scenario: Associate a Group to a Permission
    Given I make a PUT request to endpoint groups with a valid id and valid permissionId
    Then I should receive a response with status code "204"

  Scenario: Disassociate a Group to a Permission
    Given I make a DELETE request to endpoint groups with a valid id and valid permissionId
    Then I should receive a response with status code "204"

  Scenario Outline: Associate and Disassociate Group to a Permission badends
    Given I make a <request> request to endpoint groups with id <groupId> and permissionId <permissionId>
    Then I should receive a response with status code <statusCode>
    Then I should receive a response with detail containing the message <message>

    Examples:
      | request  | groupId                                | permissionId                           | statusCode | message                                                                                                                                      |
      | "PUT"    | "4a3fdd17-542f-4f6c-b450-871ff0f21092" | "4a3fdd17-542f-4f6c-b450-871ff0f21092" | "404"      | "Permissao de id '4a3fdd17-542f-4f6c-b450-871ff0f21092' não encontrado!"                                                                                                                                                   |
      | "PUT"    | "ddb25bff-fe9b-4337-b2a6-6e28a615a6d4" | "ddb25bff-fe9b-4337-b2a6-6e28a615a6d4" | "404"      | "Grupo de id 'ddb25bff-fe9b-4337-b2a6-6e28a615a6d4' não encontrado!"                                                                    |
      | "PUT"    | "abcd"                                 | "ddb25bff-fe9b-4337-b2a6-6e28a615a6d4" | "400"      | "O parâmetro da URL 'groupId' recebeu o valor de 'abcd', que é um tipo inválido. Corrija e informe um valor compatível ao tipo 'UUID'."                                                                                                                                             |
      | "PUT"    | "4a3fdd17-542f-4f6c-b450-871ff0f21092" | "abcd"                                 | "400"      | "O parâmetro da URL 'permissionId' recebeu o valor de 'abcd', que é um tipo inválido. Corrija e informe um valor compatível ao tipo 'UUID'." |
      | "DELETE" | "ddb25bff-fe9b-4337-b2a6-6e28a615a6d4" | "4a3fdd17-542f-4f6c-b450-871ff0f21092" | "404"      | "Grupo de id 'ddb25bff-fe9b-4337-b2a6-6e28a615a6d4' não encontrado!"                                                                                                                                             |
      | "DELETE" | "4a3fdd17-542f-4f6c-b450-871ff0f21092" | "ddb25bff-fe9b-4337-b2a6-6e28a615a6d4" | "404"      | "Permissao de id 'ddb25bff-fe9b-4337-b2a6-6e28a615a6d4' não encontrado!"                                                                        |
      | "DELETE" | "abcd"                                 | "ddb25bff-fe9b-4337-b2a6-6e28a615a6d4" | "400"      | "O parâmetro da URL 'groupId' recebeu o valor de 'abcd', que é um tipo inválido. Corrija e informe um valor compatível ao tipo 'UUID'."                                                                                                                                             |
      | "DELETE" | "4a3fdd17-542f-4f6c-b450-871ff0f21092" | "abcd"                                 | "400"      | "O parâmetro da URL 'permissionId' recebeu o valor de 'abcd', que é um tipo inválido. Corrija e informe um valor compatível ao tipo 'UUID'." |

