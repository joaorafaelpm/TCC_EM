Feature: ProductTest

  Scenario: LifeCycle of a Product
  # Setup - Criar restaurante para ser dono
    Given I make a POST request to create a restaurant for product tests
    Then I should receive a response with status code from endpoint products "201"

  # POST - Criar produto
    Given I make a POST request to endpoint products with a valid body
    Then I should receive a response with status code from endpoint products "201"
    Then I should receive a response body with name from endpoint products "Pizza Margherita"

  # GET - Buscar produto criado
    When I make a GET request to endpoint products with a valid id
    Then I should receive a response with status code from endpoint products "200"
    Then I should receive a response body with name from endpoint products "Pizza Margherita"

  # Ativar produto
    When I make a PUT request to activate the product
    Then I should receive a response with status code from endpoint products "204"

  # GET ALL - Listar produtos do restaurante
    When I make a GET request to list all products from the restaurant
    Then I should receive a response with status code from endpoint products "200"
    Then I should receive an array in endpoint products

  # PUT - Atualizar produto
    When I make a PUT request to endpoint products with a valid body and id
    Then I should receive a response with status code from endpoint products "200"
    Then I should receive a response body with name from endpoint products "Pizza Margherita Atualizada"

    When I make a PUT request to endpoint products with a valid restaurantId and productId in endpoint photo
    Then I should receive a response with status code from endpoint products "200"
    Then I should receive a response body with description from endpoint products "Descrição"

    When I make a GET request to endpoint products with a valid restaurantId and productId in endpoint photo
    Then I should receive a response body with description from endpoint products "Descrição"

    When I make a DELETE request to endpoint products with a valid restaurantId and productId in endpoint photo 
    Then I should receive a response with status code from endpoint products "204"

  # Desativar produto
    When I make a DELETE request to deactivate the product
    Then I should receive a response with status code from endpoint products "204"

  # Teardown - Desativar restaurante ao final
    When I make a DELETE request to deactivate the restaurant in endpoint products
    Then I should receive a response with status code from endpoint products "204"

  Scenario Outline: I make a GET in endpoint product with restaurantId and productId and should recieve bad request
    Given I make a GET request to endpoint products to get a photo with <restaurantId> and <productId>
    Then I should receive a response with status code from endpoint products <statusCode>
    Then I should receive a response body with detail from endpoint products <detail>
    
    Examples:
      | restaurantId                           | productId                              | statusCode | detail                                                                                                                                        |
      | "52ec094f-3e34-42d4-845a-bc1c178259c1" | "52ec094f-3e34-42d4-845a-bc1c178259c1" | "404"      | "Foto do produto de id '52ec094f-3e34-42d4-845a-bc1c178259c1' não encontrado no restaurante de id '52ec094f-3e34-42d4-845a-bc1c178259c1'"                                                                    |
      | "abc"                                  | "52ec094f-3e34-42d4-845a-bc1c178259c1" | "400"      | "O parâmetro da URL 'restaurantId' recebeu o valor de 'abc', que é um tipo inválido. Corrija e informe um valor compatível ao tipo 'UUID'."    |
      | "72e58c00-e73f-41ee-bdd7-acf75341a7a7" | "72e58c00-e73f-41ee-bdd7-acf75341a7a7" | "404"      | "Foto do produto de id '72e58c00-e73f-41ee-bdd7-acf75341a7a7' não encontrado no restaurante de id '72e58c00-e73f-41ee-bdd7-acf75341a7a7'"       |
      | "52ec094f-3e34-42d4-845a-bc1c178259c1" | "abc"                                  | "400"      | "O parâmetro da URL 'productId' recebeu o valor de 'abc', que é um tipo inválido. Corrija e informe um valor compatível ao tipo 'UUID'."       |

  Scenario: I make a PUT in endpoint product with valid restaurantId and productId with a not PNG or JPG file and should recieve bad request
    Given I make a PUT request to endpoint products to get a photo with an invalid file type
    Then I should receive a response with status code from endpoint products "400"
    Then I should receive a response body with objects userMessage from endpoint products "Formato da imagem inválido, deve corresponder aos tipos jpeg ou png"
    
  Scenario Outline: All Get Requests for Products - invalid ids
    Given I make a GET request to endpoint products with restaurantId <restaurantId> and productId <productId>
    Then I should receive a response with status code from endpoint products <statusCode>
    Then I should receive a response body with detail from endpoint products <detail>

    Examples:
      | restaurantId                           | productId                              | statusCode | detail                                                                                                                                        |
      | "c026aaa1-0cd3-11f1-a36f-ea4dd8dd74c2" | "c026aaa1-0cd3-11f1-a36f-ea4dd8dd74c2" | "404"      | "Restaurante de id 'c026aaa1-0cd3-11f1-a36f-ea4dd8dd74c2' não encontrado!"                                                                    |
      | "abc"                                  | "c026aaa1-0cd3-11f1-a36f-ea4dd8dd74c2" | "400"      | "O parâmetro da URL 'restaurantId' recebeu o valor de 'abc', que é um tipo inválido. Corrija e informe um valor compatível ao tipo 'UUID'."    |
      | "c026aaa1-0cd3-11f1-a36f-ea4dd8dd74c2" | "abc"                                  | "400"      | "O parâmetro da URL 'productId' recebeu o valor de 'abc', que é um tipo inválido. Corrija e informe um valor compatível ao tipo 'UUID'."       |

  Scenario Outline: Check PUT responses for Products with invalid ids
    Given I make a PUT request to endpoint products with restaurantId <restaurantId> and productId <productId> and name <name>
    Then I should receive a response with status code from endpoint products <statusCode>
    Then I should receive a response body with detail from endpoint products <detail>

    Examples:
      | restaurantId                           | productId                              | name        | statusCode | detail                                                                                                                                        |
      | "abc"                                  | "c026aaa1-0cd3-11f1-a36f-ea4dd8dd74c2" | "Calzone"   | "400"      | "O parâmetro da URL 'restaurantId' recebeu o valor de 'abc', que é um tipo inválido. Corrija e informe um valor compatível ao tipo 'UUID'."    |
      | "c026aaa1-0cd3-11f1-a36f-ea4dd8dd74c2" | "abc"                                  | "Calzone"   | "400"      | "O parâmetro da URL 'productId' recebeu o valor de 'abc', que é um tipo inválido. Corrija e informe um valor compatível ao tipo 'UUID'."       |
      | "c026aaa1-0cd3-11f1-a36f-ea4dd8dd74c2" | "c026aaa1-0cd3-11f1-a36f-ea4dd8dd74c2" | "Calzone"   | "404"      | "Restaurante de id 'c026aaa1-0cd3-11f1-a36f-ea4dd8dd74c2' não encontrado!"                                                                    |

  Scenario Outline: I make a POST in endpoint product with invalid fields and should see a bad request
    Given I make a POST request to create a restaurant for product tests
    Given I make a POST request to endpoint products with a <name>, <description> and <price>
    Then I should receive a response with status code from endpoint products "400"
    Then I should receive a response body with objects name from endpoint products <objectName>
    Then I should receive a response body with objects userMessage from endpoint products <userMessage>
    When I make a DELETE request to deactivate the restaurant in endpoint products

    Examples:
      | name              | description          | price   | objectName    | userMessage                                      |
      | " "               | "Descrição válida"   | 49.90   | "name"        | "Nome do produto é obrigatório."   |
      | "Nome Valido"     | "Descrição válida"   | -1.00   | "price"       | "Preço do produto deve ser positivo ou zero."    |
      | "Nome Valido"     | " "                  | 49.90   | "description" | "Descrição do produto é obrigatório."|

  Scenario Outline: I make a POST in endpoint product with null fields and should see a bad request
    Given I make a POST request to create a restaurant for product tests
    Given I make a POST request to endpoint products with a null <field>
    Then I should receive a response with status code from endpoint products "400"
    Then I should receive a response body with objects name from endpoint products <objectName>
    Then I should receive a response body with objects userMessage from endpoint products <userMessage>
    When I make a DELETE request to deactivate the restaurant in endpoint products

    Examples:
      | field         | objectName    | userMessage                                         |
      | "name"        | "name"        | "Nome do produto é obrigatório."                    |
      | "price"       | "price"       | "Preço do produto não pode ser vazio."                 |
      | "description" | "description" | "Descrição do produto é obrigatório."             |

  Scenario Outline: I make a PUT in endpoint product with invalid fields and should see a bad request
    Given I make a POST request to create a restaurant for product tests
    Given I make a POST request to endpoint products with a valid body
    Given I make a PUT request to endpoint products with a <name>, <description> and <price>
    Then I should receive a response with status code from endpoint products "400"
    Then I should receive a response body with objects name from endpoint products <objectName>
    Then I should receive a response body with objects userMessage from endpoint products <userMessage>
    When I make a DELETE request to deactivate the restaurant in endpoint products

    Examples:
      | name            | description         | price  | objectName    | userMessage                                       |
      | " "             | "Descrição válida"  | 49.90  | "name"        | "Nome do produto é obrigatório."    |
      | "Nome Valido"   | "Descrição válida"  | -1.00  | "price"       | "Preço do produto deve ser positivo ou zero."     |
      | "Nome Valido"   | " "                 | 49.90  | "description" | "Descrição do produto é obrigatório."|
