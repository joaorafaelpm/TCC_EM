@bussinessFeatures
Feature: BussinessLogin
# Regras de negócio genéricas para auxiliar os testes mais específicos e complexos, como a alteração de senha ou de dados de um objeto de outro usuário

  Scenario: Valid Login as owner of restaurant
    Given I'm on the login page
    When I type a registered email and password of a restaurant owner
    Then I have a successful login and see my access_token as an admin

  Scenario: I try to change the information of a restaurant that isn't mine
    Given I hit "PUT" in endpoint "/v1/restaurants" with valid body that isn't mine
    Then I should receive a response with status code "403"
    Then I should receive a response body with title "Acesso negado."
    Then I should receive a response body with detail "Access Denied"
    Then I should receive a response body with userMessage "Você não tem permissão necessária para executar a ação."

  Scenario Outline: I try to open and close a restaurant that isn't mine
    Given I hit <method> in endpoint <endpoint> with valid id that isn't mine to open or close it
    Then I should receive a response with status code "403"
    Then I should receive a response body with title "Acesso negado."
    Then I should receive a response body with detail "Access Denied"
    Then I should receive a response body with userMessage "Você não tem permissão necessária para executar a ação."

    Examples:
      | method | endpoint                     |
      | "PUT"  | "/v1/restaurants/id/opening" |
      | "PUT"  | "/v1/restaurants/id/closing" |

  Scenario Outline: I try to add and remove a payment method in a restaurant that isn't mine
    Given I hit <method> in endpoint <endpoint> with valid id that isn't mine to manage the payment methods
    Then I should receive a response with status code "403"
    Then I should receive a response body with title "Acesso negado."
    Then I should receive a response body with detail "Access Denied"
    Then I should receive a response body with userMessage "Você não tem permissão necessária para executar a ação."

    Examples:
      | method   | endpoint                                                       |
      | "DELETE" | "/v1/restaurants/restaurantId/payment-methods/paymentMethodId" |
      | "PUT"    | "/v1/restaurants/restaurantId/payment-methods/paymentMethodId" |

  Scenario Outline: I try to add and remove a product in a restaurant that isn't mine
    Given I hit <method> in endpoint <endpoint> with valid body that isn't mine to manage the products
    Then I should receive a response with status code "403"
    Then I should receive a response body with title "Acesso negado."
    Then I should receive a response body with detail "Access Denied"
    Then I should receive a response body with userMessage "Você não tem permissão necessária para executar a ação."

    Examples:
      | method | endpoint                                          |
      | "POST" | "/v1/restaurants/restaurantId/products"           |
      | "PUT"  | "/v1/restaurants/restaurantId/products/productId" |

  Scenario Outline: I try to activate or deactivate a product in a restaurant that isn't mine
    Given I hit <method> in endpoint <endpoint> with valid id that isn't mine to activate or deactivate the products
    Then I should receive a response with status code "403"
    Then I should receive a response body with title "Acesso negado."
    Then I should receive a response body with detail "Access Denied"
    Then I should receive a response body with userMessage "Você não tem permissão necessária para executar a ação."

    Examples:
      | method   | endpoint                                                 |
      | "PUT"    | "/v1/restaurants/restaurantId/products/productId/active" |
      | "DELETE" | "/v1/restaurants/restaurantId/products/productId/active" |

  Scenario Outline: I try to add or remove a product photo in a restaurant that isn't mine
    Given I hit <method> in endpoint <endpoint> with valid body that isn't mine to add or remove product photos
    Then I should receive a response with status code "403"
    Then I should receive a response body with title "Acesso negado."
    Then I should receive a response body with detail "Access Denied"
    Then I should receive a response body with userMessage "Você não tem permissão necessária para executar a ação."

    Examples:
      | method   | endpoint                                                |
      | "PUT"    | "/v1/restaurants/restaurantId/products/productId/photo" |
      | "DELETE" | "/v1/restaurants/restaurantId/products/productId/photo" |

  Scenario Outline: I try to see a order that isn't mine
    Given I hit <method> in endpoint <endpoint> with valid id that isn't mine
    Then I should receive a response with status code "403"
    Then I should receive a response body with title "Acesso negado."
    Then I should receive a response body with detail "Access Denied"
    Then I should receive a response body with userMessage "Você não tem permissão necessária para executar a ação."

    Examples:
      | method | endpoint            |
      | "GET"  | "v1/orders"         |
      | "GET"  | "v1/orders/orderId" |

 Scenario: I try to change the password of a user that isn't mine
    Given I hit "PUT" in endpoint "v1/users/userId/password" with valid id that isn't mine to change his password
    Then I should receive a response with status code "403"
    Then I should receive a response body with title "Acesso negado."
    Then I should receive a response body with detail "Access Denied"
    Then I should receive a response body with userMessage "Você não tem permissão necessária para executar a ação."
