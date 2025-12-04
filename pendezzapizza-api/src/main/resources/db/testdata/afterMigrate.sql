set foreign_key_checks = 0;

delete from city;
delete from state;
delete from payment_method;
delete from `group`;
delete from group_permission;
delete from permission;
delete from product;
delete from restaurant;
delete from restaurant_payment_method;
delete from `user`;
delete from user_group;
delete from restaurant_user_responsible;
delete from `order`;
delete from order_item;
delete from product_photo;

set foreign_key_checks = 1;

-- Variáveis para armazenar IDs de estados e cidades (para facilitar as inserções subsequentes)
-- Nota: UUIDs são gerados a cada execução, então a associação por nome é mais segura
-- ou o uso de variáveis temporárias, mas para fins de correção, usaremos o SELECT por nome.

-- 1. INSERT INTO state
INSERT INTO state (id, name) VALUES
(UUID_TO_BIN(UUID()), 'São Paulo'),
(UUID_TO_BIN(UUID()), 'Rio de Janeiro'),
(UUID_TO_BIN(UUID()), 'Minas Gerais');

-- 2. INSERT INTO city (Usando subconsultas SELECT corretamente)
INSERT INTO city (id, name, state_id) VALUES
(UUID_TO_BIN(UUID()), 'São Paulo', (SELECT id FROM state WHERE name = 'São Paulo')),
(UUID_TO_BIN(UUID()), 'Campinas', (SELECT id FROM state WHERE name = 'São Paulo')),
(UUID_TO_BIN(UUID()), 'Rio de Janeiro', (SELECT id FROM state WHERE name = 'Rio de Janeiro')),
(UUID_TO_BIN(UUID()), 'Niterói', (SELECT id FROM state WHERE name = 'Rio de Janeiro')),
(UUID_TO_BIN(UUID()), 'Belo Horizonte', (SELECT id FROM state WHERE name = 'Minas Gerais')),
(UUID_TO_BIN(UUID()), 'Uberlândia', (SELECT id FROM state WHERE name = 'Minas Gerais'));


-- 3. INSERT INTO payment_method
INSERT INTO payment_method (id, description, update_date) VALUES
(UUID_TO_BIN(UUID()), 'Cartão de Crédito', UTC_TIMESTAMP()),
(UUID_TO_BIN(UUID()), 'Cartão de Débito', UTC_TIMESTAMP()),
(UUID_TO_BIN(UUID()), 'Pix', UTC_TIMESTAMP()),
(UUID_TO_BIN(UUID()), 'Dinheiro', UTC_TIMESTAMP());


-- 4. INSERT INTO permission
INSERT INTO permission (id, name, description) VALUES
(UUID_TO_BIN(UUID()), 'CADASTRAR_RESTAURANTE', 'Permite cadastrar restaurantes'),
(UUID_TO_BIN(UUID()), 'GERENCIAR_PEDIDOS', 'Permite atualizar e gerenciar pedidos'),
(UUID_TO_BIN(UUID()), 'ADMIN', 'Acesso total ao sistema');


-- 5. INSERT INTO `group`
INSERT INTO `group` (id, name) VALUES
(UUID_TO_BIN(UUID()), 'ADMIN'),
(UUID_TO_BIN(UUID()), 'GERENTE'),
(UUID_TO_BIN(UUID()), 'CLIENTE');


-- 6. INSERT INTO `user`
INSERT INTO `user` (id, name, email, password) VALUES
(UUID_TO_BIN(UUID()), 'Administrador do Sistema', 'admin@sistema.com', 'admin123'),
(UUID_TO_BIN(UUID()), 'João Silva', 'joao@gmail.com', 'senha123'),
(UUID_TO_BIN(UUID()), 'Maria Oliveira', 'maria@gmail.com', 'senha123'),
(UUID_TO_BIN(UUID()), 'Responsável Restaurante', 'resp@restaurant.com', 'senha123');


-- 7. INSERT INTO group_permission
-- ADMIN
INSERT INTO group_permission (group_id, permission_id)
SELECT g.id, p.id FROM `group` g CROSS JOIN permission p WHERE g.name = 'ADMIN';

-- GERENTE
INSERT INTO group_permission (group_id, permission_id)
SELECT g.id, p.id
FROM `group` g
JOIN permission p
WHERE g.name = 'GERENTE'
AND p.name IN ('CADASTRAR_RESTAURANTE', 'GERENCIAR_PEDIDOS');


-- 8. INSERT INTO user_group
INSERT INTO user_group (user_id, group_id)
SELECT u.id, g.id FROM `user` u, `group` g WHERE u.email = 'admin@sistema.com' AND g.name = 'ADMIN';

INSERT INTO user_group (user_id, group_id)
SELECT u.id, g.id FROM `user` u, `group` g WHERE u.email = 'joao@gmail.com' AND g.name = 'CLIENTE';

INSERT INTO user_group (user_id, group_id)
SELECT u.id, g.id FROM `user` u, `group` g WHERE u.email = 'maria@gmail.com' AND g.name = 'CLIENTE';

INSERT INTO user_group (user_id, group_id)
SELECT u.id, g.id FROM `user` u, `group` g WHERE u.email = 'resp@restaurant.com' AND g.name = 'GERENTE';


-- 9. INSERT INTO restaurant
INSERT INTO restaurant (
    id, name, shipping_fee,
    address_zip_code, address_street, address_number, address_complement, address_neighborhood,
    address_city_id, registration_date, update_date, active, open
)
VALUES
(UUID_TO_BIN(UUID()), 'Cantina Bella Itália', 8.90,
 '01010-000', 'Av. Paulista', '1000', 'Perto do MASP', 'Bela Vista',
 (SELECT id FROM city WHERE name = 'São Paulo' AND state_id = (SELECT id FROM state WHERE name = 'São Paulo')), NOW(), NOW(), 1, 1),

(UUID_TO_BIN(UUID()), 'Sushi House Premium', 6.50,
 '22220-000', 'Rua das Laranjeiras', '200', NULL, 'Laranjeiras',
 (SELECT id FROM city WHERE name = 'Rio de Janeiro' AND state_id = (SELECT id FROM state WHERE name = 'Rio de Janeiro')), NOW(), NOW(), 1, 1),

(UUID_TO_BIN(UUID()), 'Burger Master Grill', 5.00,
 '30110-000', 'Av. Afonso Pena', '500', 'Loja 2', 'Centro',
 (SELECT id FROM city WHERE name = 'Belo Horizonte' AND state_id = (SELECT id FROM state WHERE name = 'Minas Gerais')), NOW(), NOW(), 1, 1);


-- 10. INSERT INTO restaurant_payment_method
INSERT INTO restaurant_payment_method (restaurant_id, payment_method_id)
SELECT r.id, pm.id FROM restaurant r, payment_method pm
WHERE r.name = 'Cantina Bella Itália'
AND pm.description IN ('Cartão de Crédito', 'Pix');

INSERT INTO restaurant_payment_method (restaurant_id, payment_method_id)
SELECT r.id, pm.id FROM restaurant r, payment_method pm
WHERE r.name = 'Sushi House Premium'
AND pm.description IN ('Cartão de Crédito', 'Cartão de Débito', 'Pix');

INSERT INTO restaurant_payment_method (restaurant_id, payment_method_id)
SELECT r.id, pm.id FROM restaurant r, payment_method pm
WHERE r.name = 'Burger Master Grill'
AND pm.description IN ('Dinheiro', 'Pix');


-- 11. INSERT INTO product
INSERT INTO product (id, restaurant_id, name, description, price, active)
SELECT UUID_TO_BIN(UUID()), r.id, 'Pizza Margherita', 'Pizza clássica com tomate e manjericão.', 32.90, 1
FROM restaurant r WHERE r.name = 'Cantina Bella Itália';

INSERT INTO product (id, restaurant_id, name, description, price, active)
SELECT UUID_TO_BIN(UUID()), r.id, 'Lasanha Bolonhesa', 'Lasanha tradicional com molho bolonhesa.', 38.50, 1
FROM restaurant r WHERE r.name = 'Cantina Bella Itália';

INSERT INTO product (id, restaurant_id, name, description, price, active)
SELECT UUID_TO_BIN(UUID()), r.id, 'Sushi Combo 20 peças', 'Combo variado com 20 peças.', 49.90, 1
FROM restaurant r WHERE r.name = 'Sushi House Premium';

INSERT INTO product (id, restaurant_id, name, description, price, active)
SELECT UUID_TO_BIN(UUID()), r.id, 'Burger Clássico', 'Hambúrguer artesanal.', 22.90, 1
FROM restaurant r WHERE r.name = 'Burger Master Grill';


-- 12. INSERT INTO product_photo
INSERT INTO product_photo (product_id, archive_name, description, content_type, size)
SELECT p.id, 'margherita.jpg', 'Foto da pizza margherita', 'image/jpeg', 350000
FROM product p WHERE p.name = 'Pizza Margherita';


-- 13. INSERT INTO restaurant_user_responsible
INSERT INTO restaurant_user_responsible (user_id, restaurant_id)
SELECT u.id, r.id FROM `user` u, restaurant r
WHERE u.email = 'resp@restaurant.com'
  AND r.name = 'Cantina Bella Itália';


-- 14. INSERT INTO `order`
INSERT INTO `order` (
 id, subtotal, shipping_fee, total_cost,
 creation_date, status_order,
 payment_method_id, restaurant_id, client_user_id,
 address_city_id, address_street, address_number,
 address_complement, address_neighborhood, address_zip_code
)
SELECT
 UUID_TO_BIN(UUID()), 49.90, 8.90, 58.80,
 NOW(), 'CREATE',
 pm.id, r.id, u.id,
 c.id, 'Av. Paulista', '1500', NULL, 'Bela Vista', '01010-000'
FROM `user` u, restaurant r, payment_method pm, city c
WHERE u.email = 'joao@gmail.com'
AND r.name = 'Cantina Bella Itália'
AND pm.description = 'Pix'
AND c.name = 'São Paulo' AND c.state_id = (SELECT id FROM state WHERE name = 'São Paulo');


-- 15. INSERT INTO order_item
INSERT INTO order_item (id, quantity, unity_price, total_price, observation, order_id, product_id)
SELECT
 UUID_TO_BIN(UUID()), 1, 49.90, 49.90, 'Sem cebola',
 o.id, p.id
FROM `order` o, product p
WHERE p.name = 'Pizza Margherita'
AND o.client_user_id = (
    SELECT id FROM `user` WHERE email = 'joao@gmail.com'
);