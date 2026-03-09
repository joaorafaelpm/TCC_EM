

Feature: UserTest

  Scenario: LifeCycle of a User
  # POST
    Given I make a POST request to endpoint users with a valid body
    Then I should receive a response with status code "201"
    Then I should receive a response body with name "João Teste"
  # GET
    When I make a GET request to endpoint users with a valid id
    Then I should receive a response with status code "200"
    Then I should receive a response body with name "João Teste"
  # PUT
    When I make a PUT request to endpoint users with a valid body and id
    Then I should receive a response with status code "200"
    Then I should receive a response body with name "João Teste Atualizado"
  # DELETE
    When I make a DELETE request to endpoint users with a valid id
    Then I should receive a response with status code "204"
    Then I should receive a response with statusText "No Content"
  # Checando se o delete funcionou
    When I make a GET request to endpoint users with a deleted id
    Then I should receive a response with status code "404"
    Then I should receive a response body with title "Recurso não encontrado."

  Scenario Outline: All Get Requests for Users
    Given I make a GET request to endpoint users with a <id>
    Then I should receive a response with status code <statusCode>
    Then I should receive a response body with detail <detail>

    Examples:
      | id                                     | statusCode | detail                                                                                                                                |
      | "c026aaa1-0cd3-11f1-a36f-ea4dd8dd74c2" | "404"      | "User com ID c026aaa1-0cd3-11f1-a36f-ea4dd8dd74c2 não foi encontrado."                                                                |
      | "abc"                                  | "400"      | "O parâmetro da URL 'userId' recebeu o valor de 'abc', que é um tipo inválido. Corrija e informe um valor compatível ao tipo 'UUID'." |

  Scenario Outline: I make a POST in endpoint user with an invalid name and should see a bad request
    Given I make a POST request to endpoint users with a <name> , <email> and <password>
    Then I should receive a response with status code "400"
    Then I should receive a response body with objects name <objectName>
    Then I should receive a response body with objects userMessage <userMessage>

    Examples:
      | name                   | email                           | password              | objectName | userMessage                                    |
      | " "                    | "emailgenericovalido@gmail.com" | "senhagenericavalida" | "name"     | "Nome do usuário contém caracteres inválidos." |
      | "Nomecomnumero67"      | "emailgenericovalido@gmail.com" | "senhagenericavalida" | "name"     | "Nome do usuário contém caracteres inválidos." |
      | "Nome generico valido" | "emailsemarroba"                | "senhagenericavalida" | "email"    | "O email deve ser um email válido"             |
      | "Nome generico valido" | " "                             | "senhagenericavalida" | "email"    | "O email deve ser um email válido"            |
      | "Nome generico valido" | "emailgenericovalido@gmail.com" | " "                   | "password" | "Senha do usuário é obrigatório."              |

  Scenario Outline: I make a PUT in endpoint user with an invalid name and should see a bad request
    Given I make a PUT request to endpoint users with a valid id, <name> and <email>
    Then I should receive a response with status code "400"
    Then I should receive a response body with objects name <objectName>
    Then I should receive a response body with objects userMessage <userMessage>

    Examples:
      | name                   | email                           | objectName | userMessage                                    |
      | " "                    | "emailgenericovalido@gmail.com" | "name"     | "Nome do usuário contém caracteres inválidos." |
      | "Nomecomnumero67"      | "emailgenericovalido@gmail.com" | "name"     | "Nome do usuário contém caracteres inválidos." |
      | "Nome generico valido" | "emailsemarroba"                | "email"    | "O email deve ser um email válido"             |
      | "Nome generico valido" | " "                             | "email"    | "O email deve ser um email válido"             |

  Scenario Outline: Check a lot of PUT responses for Users
    Given I make a PUT request to endpoint users with an id <id> and name <name>
    Then I should receive a response with status code <statusCode>
    Then I should receive a response body with detail <detail>

    Examples:
      | id                                     | name               | statusCode | detail                                                                                                                                 |
      | "abcd"                                 | "Ronaldo da silva" | "400"      | "O parâmetro da URL 'userId' recebeu o valor de 'abcd', que é um tipo inválido. Corrija e informe um valor compatível ao tipo 'UUID'." |
      | "c026aaa1-0cd3-11f1-a36f-ea4dd8dd74c2" | "Ronaldo da silva" | "404"      | "User com ID c026aaa1-0cd3-11f1-a36f-ea4dd8dd74c2 não foi encontrado."                                                                 |

  Scenario: Error 406
    Given I make a GET request to endpoint users with a valid id and accept header "application/pdf"
    Then I should receive a response with status code "406"
    Then I should receive a response with statusText "Not Acceptable"

  Scenario: Associate a User to a Group
    Given I make a PUT request to endpoint users with a valid id and valid groupId
    Then I should receive a response with status code "204"

  Scenario: Disassociate a User to a Group
    Given I make a DELETE request to endpoint users with a valid id and valid groupId
    Then I should receive a response with status code "204"

  Scenario Outline: Associate and Disassociate User to a Group badends
    Given I make a <request> request to endpoint users with id <userId> and groupId <groupId>
    Then I should receive a response with status code <statusCode>
    Then I should receive a response with detail containing the message from users <message>

    Examples:
      | request  | userId                                 | groupId                                | statusCode | message                                                                                                                                 |
      | "PUT"    | "4a3fdd17-542f-4f6c-b450-871ff0f21092" | "4a3fdd17-542f-4f6c-b450-871ff0f21092" | "404"      | "User com ID 4a3fdd17-542f-4f6c-b450-871ff0f21092 não foi encontrado."                                                                  |
      | "PUT"    | "a6162eb1-df44-471b-aef3-9feee0d9d267" | "a6162eb1-df44-471b-aef3-9feee0d9d267" | "404"      | "Grupo de id 'a6162eb1-df44-471b-aef3-9feee0d9d267' não encontrado!"                                                                    |
      | "PUT"    | "abcd"                                 | "a6162eb1-df44-471b-aef3-9feee0d9d267" | "400"      | "O parâmetro da URL 'userId' recebeu o valor de 'abcd', que é um tipo inválido. Corrija e informe um valor compatível ao tipo 'UUID'."  |
      | "PUT"    | "a6162eb1-df44-471b-aef3-9feee0d9d267" | "abcd"                                 | "400"      | "O parâmetro da URL 'groupId' recebeu o valor de 'abcd', que é um tipo inválido. Corrija e informe um valor compatível ao tipo 'UUID'." |
      | "DELETE" | "4a3fdd17-542f-4f6c-b450-871ff0f21092" | "4a3fdd17-542f-4f6c-b450-871ff0f21092" | "404"      | "User com ID 4a3fdd17-542f-4f6c-b450-871ff0f21092 não foi encontrado."                                                                  |
      | "DELETE" | "a6162eb1-df44-471b-aef3-9feee0d9d267" | "a6162eb1-df44-471b-aef3-9feee0d9d267" | "404"      | "Grupo de id 'a6162eb1-df44-471b-aef3-9feee0d9d267' não encontrado!"                                                                    |
      | "DELETE" | "abcd"                                 | "a6162eb1-df44-471b-aef3-9feee0d9d267" | "400"      | "O parâmetro da URL 'userId' recebeu o valor de 'abcd', que é um tipo inválido. Corrija e informe um valor compatível ao tipo 'UUID'."  |
      | "DELETE" | "4a3fdd17-542f-4f6c-b450-871ff0f21092" | "abcd"                                 | "400"      | "O parâmetro da URL 'groupId' recebeu o valor de 'abcd', que é um tipo inválido. Corrija e informe um valor compatível ao tipo 'UUID'." |
