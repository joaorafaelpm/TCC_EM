-- LIMPEZA (ordem inversa das dependências)
SET FOREIGN_KEY_CHECKS = 0;
DELETE FROM oauth2_registered_client;
DELETE FROM order_item;
DELETE FROM `order`;
DELETE FROM restaurant_user_responsible;
DELETE FROM restaurant_payment_method;
DELETE FROM product;
DELETE FROM user_group;
DELETE FROM group_permission;
DELETE FROM restaurant;
DELETE FROM `user`;
DELETE FROM `group`;
DELETE FROM permission;
DELETE FROM payment_method;
DELETE FROM city;
DELETE FROM state;
SET FOREIGN_KEY_CHECKS = 1;

-- 1. ESTADOS

INSERT INTO state (id, name, update_date) VALUES
(UUID_TO_BIN('ac3bb31f-4c4f-44ff-88e8-92646ba56240'), 'São Paulo',       UTC_TIMESTAMP()),
(UUID_TO_BIN('b2c4d5e6-f7a8-4b9c-8d0e-1f2a3b4c5d6e'), 'Rio de Janeiro',  UTC_TIMESTAMP()),
(UUID_TO_BIN('c3d4e5f6-a7b8-4c9d-9e0f-2a3b4c5d6e7f'), 'Minas Gerais',    UTC_TIMESTAMP());


-- 2. CIDADES

INSERT INTO city (id, name, state_id, update_date) VALUES
(UUID_TO_BIN('d4e5f6a7-b8c9-4d0e-ae1f-3b4c5d6e7f8a'), 'São Paulo',      (SELECT id FROM state WHERE name = 'São Paulo'      LIMIT 1), UTC_TIMESTAMP()),
(UUID_TO_BIN('e5f6a7b8-c9d0-4e1f-bf2a-4c5d6e7f8a9b'), 'Campinas',       (SELECT id FROM state WHERE name = 'São Paulo'      LIMIT 1), UTC_TIMESTAMP()),
(UUID_TO_BIN('f6a7b8c9-d0e1-4f2a-cf3b-5d6e7f8a9b0c'), 'Rio de Janeiro', (SELECT id FROM state WHERE name = 'Rio de Janeiro' LIMIT 1), UTC_TIMESTAMP()),
(UUID_TO_BIN('a7b8c9d0-e1f2-4a3b-df4c-6e7f8a9b0c1d'), 'Niterói',        (SELECT id FROM state WHERE name = 'Rio de Janeiro' LIMIT 1), UTC_TIMESTAMP()),
(UUID_TO_BIN('b8c9d0e1-f2a3-4b4c-ef5d-7f8a9b0c1d2e'), 'Belo Horizonte', (SELECT id FROM state WHERE name = 'Minas Gerais'   LIMIT 1), UTC_TIMESTAMP()),
(UUID_TO_BIN('c9d0e1f2-a3b4-4c5d-fa6e-8a9b0c1d2e3f'), 'Uberlândia',     (SELECT id FROM state WHERE name = 'Minas Gerais'   LIMIT 1), UTC_TIMESTAMP()),
(UUID_TO_BIN('0e0362cc-db84-4484-9909-d6977b96b619'), 'Monte Verde',     (SELECT id FROM state WHERE name = 'Minas Gerais'   LIMIT 1), UTC_TIMESTAMP());


-- 3. FORMAS DE PAGAMENTO

INSERT INTO payment_method (id, description, update_date) VALUES
(UUID_TO_BIN('d0e1f2a3-b4c5-4d6e-ab7f-9b0c1d2e3f4a'), 'Cartão de Crédito', UTC_TIMESTAMP()),
(UUID_TO_BIN('e1f2a3b4-c5d6-4e7f-bc8a-0c1d2e3f4a5b'), 'Cartão de Débito',  UTC_TIMESTAMP()),
(UUID_TO_BIN('f2a3b4c5-d6e7-4f8a-cd9b-1d2e3f4a5b6c'), 'Pix',               UTC_TIMESTAMP()),
(UUID_TO_BIN('3ee42ee7-3d35-4680-afe0-e01a24e649dc'), 'Dinheiro',           UTC_TIMESTAMP());


-- 4. PERMISSÕES

INSERT INTO permission (id, name, description, update_date) VALUES
(UUID_TO_BIN('a3b4c5d6-e7f8-4a9b-de0c-2e3f4a5b6c7d'), 'EDITAR_COZINHAS',                    'Permite editar cozinhas',                          UTC_TIMESTAMP()),
(UUID_TO_BIN('b4c5d6e7-f8a9-4b0c-ef1d-3f4a5b6c7d8e'), 'EDITAR_FORMAS_PAGAMENTO',             'Permite criar ou editar formas de pagamento',       UTC_TIMESTAMP()),
(UUID_TO_BIN('c5d6e7f8-a9b0-4c1d-fa2e-4a5b6c7d8e9f'), 'EDITAR_CIDADES',                      'Permite criar ou editar cidades',                   UTC_TIMESTAMP()),
(UUID_TO_BIN('d6e7f8a9-b0c1-4d2e-ab3f-5b6c7d8e9f0a'), 'EDITAR_ESTADOS',                      'Permite criar ou editar estados',                   UTC_TIMESTAMP()),
(UUID_TO_BIN('e7f8a9b0-c1d2-4e3f-bc4a-6c7d8e9f0a1b'), 'CONSULTAR_USUARIOS_GRUPOS_PERMISSOES','Permite consultar usuários',                        UTC_TIMESTAMP()),
(UUID_TO_BIN('f8a9b0c1-d2e3-4f4a-cd5b-7d8e9f0a1b2c'), 'EDITAR_USUARIOS_GRUPOS_PERMISSOES',   'Permite criar ou editar usuários',                  UTC_TIMESTAMP()),
(UUID_TO_BIN('a9b0c1d2-e3f4-4a5b-de6c-8e9f0a1b2c3d'), 'EDITAR_RESTAURANTES',                 'Permite criar, editar ou gerenciar restaurantes',   UTC_TIMESTAMP()),
(UUID_TO_BIN('b0c1d2e3-f4a5-4b6c-ef7d-9f0a1b2c3d4e'), 'CONSULTAR_PEDIDOS',                   'Permite consultar pedidos',                         UTC_TIMESTAMP()),
(UUID_TO_BIN('c1d2e3f4-a5b6-4c7d-fa8e-0a1b2c3d4e5f'), 'GERENCIAR_PEDIDOS',                   'Permite gerenciar pedidos',                         UTC_TIMESTAMP()),
(UUID_TO_BIN('d2e3f4a5-b6c7-4d8e-ab9f-1b2c3d4e5f6a'), 'GERAR_RELATORIOS',                    'Permite gerar relatórios',                          UTC_TIMESTAMP()),
(UUID_TO_BIN('1925eff2-a761-49ff-ab2a-fd471828cb9d'), 'GANHAR_AURA',                          'Permite gerar aura',                                UTC_TIMESTAMP());


-- 5. GRUPOS

INSERT INTO `group` (id, name, update_date) VALUES
(UUID_TO_BIN('e3f4a5b6-c7d8-4e9f-bc0a-2c3d4e5f6a7b'), 'Gerente',    UTC_TIMESTAMP()),
(UUID_TO_BIN('f4a5b6c7-d8e9-4f0a-cd1b-3d4e5f6a7b8c'), 'Vendedor',   UTC_TIMESTAMP()),
(UUID_TO_BIN('a5b6c7d8-e9f0-4a1b-de2c-4e5f6a7b8c9d'), 'Secretária', UTC_TIMESTAMP()),
(UUID_TO_BIN('b6c7d8e9-f0a1-4b2c-ef3d-5f6a7b8c9d0e'), 'Cadastrador',UTC_TIMESTAMP()),
(UUID_TO_BIN('4a3fdd17-542f-4f6c-b450-871ff0f21092'), 'Tester',      UTC_TIMESTAMP());


-- 6. USUÁRIOS (com senha BCRYPT)

INSERT INTO `user` (id, name, email, password, update_date) VALUES
(UUID_TO_BIN('c7d8e9f0-a1b2-4c3d-fa4e-6a7b8c9d0e1f'), 'João da Silva',        'joao.ger@pendezzapizza.com',                          '$2a$12$xM3T9jhJ/qTbQ8yKkFyapOJeD.xzlaOB.CIgaKUBBsSfxw2dAbzM6', UTC_TIMESTAMP()),
(UUID_TO_BIN('d8e9f0a1-b2c3-4d4e-ab5f-7b8c9d0e1f2a'), 'Maria Joaquina',       'maria.vnd@pendezzapizza.com',                         '$2a$12$xM3T9jhJ/qTbQ8yKkFyapOJeD.xzlaOB.CIgaKUBBsSfxw2dAbzM6', UTC_TIMESTAMP()),
(UUID_TO_BIN('e9f0a1b2-c3d4-4e5f-bc6a-8c9d0e1f2a3b'), 'Roberto Fazbear',      'guinas.sec@pendezzapizza.com',                        '$2a$12$xM3T9jhJ/qTbQ8yKkFyapOJeD.xzlaOB.CIgaKUBBsSfxw2dAbzM6', UTC_TIMESTAMP()),
(UUID_TO_BIN('f0a1b2c3-d4e5-4f6a-cd7b-9d0e1f2a3b4c'), 'La Ele da Silva',      'alele.cad@pendezzapizza.com',                         '$2a$12$xM3T9jhJ/qTbQ8yKkFyapOJeD.xzlaOB.CIgaKUBBsSfxw2dAbzM6', UTC_TIMESTAMP()),
(UUID_TO_BIN('a1b2c3d4-e5f6-4a7b-de8c-0e1f2a3b4c5d'), 'José Souza',           'email.teste.pendezzapizza.tcc+hubert@gmail.com',      '$2a$12$xM3T9jhJ/qTbQ8yKkFyapOJeD.xzlaOB.CIgaKUBBsSfxw2dAbzM6', UTC_TIMESTAMP()),
(UUID_TO_BIN('b2c3d4e5-f6a7-4b8c-ef9d-1f2a3b4c5d6e'), 'Sebastião Martins',    'email.teste.pendezzapizza.tcc+sebastiao@gmail.com',   '$2a$12$xM3T9jhJ/qTbQ8yKkFyapOJeD.xzlaOB.CIgaKUBBsSfxw2dAbzM6', UTC_TIMESTAMP()),
(UUID_TO_BIN('c3d4e5f6-a7b8-4c9d-fa0e-2a3b4c5d6e7f'), 'Ronaldo Pinto',        'cocoxixicocopinto@gmail.com',                         '$2a$12$xM3T9jhJ/qTbQ8yKkFyapOJeD.xzlaOB.CIgaKUBBsSfxw2dAbzM6', UTC_TIMESTAMP()),
(UUID_TO_BIN('a6162eb1-df44-471b-aef3-9feee0d9d267'), 'João Mohammed Pendezza','joaomohammed@gmail.com',                              '$2a$12$xM3T9jhJ/qTbQ8yKkFyapOJeD.xzlaOB.CIgaKUBBsSfxw2dAbzM6', UTC_TIMESTAMP());


-- 7. RELAÇÃO GRUPO_PERMISSAO

-- Gerente - Pode fazer tudo
INSERT INTO group_permission (group_id, permission_id)
SELECT (SELECT id FROM `group` WHERE name = 'Gerente'), id
FROM permission;

-- Vendedor - Permissões de CONSULTA + GERENCIAR_PEDIDOS
INSERT INTO group_permission (group_id, permission_id)
SELECT (SELECT id FROM `group` WHERE name = 'Vendedor'), id
FROM permission WHERE name LIKE 'CONSULTAR_%';

INSERT INTO group_permission (group_id, permission_id)
SELECT (SELECT id FROM `group` WHERE name = 'Vendedor'), id
FROM permission WHERE name = 'GERENCIAR_PEDIDOS';

-- Secretária - Permissões de CONSULTA
INSERT INTO group_permission (group_id, permission_id)
SELECT (SELECT id FROM `group` WHERE name = 'Secretária'), id
FROM permission WHERE name LIKE 'CONSULTAR_%';

-- Cadastrador - Permissões de RESTAURANTES
INSERT INTO group_permission (group_id, permission_id)
SELECT (SELECT id FROM `group` WHERE name = 'Cadastrador'), id
FROM permission WHERE name LIKE '%_RESTAURANTES';

-- Tester
INSERT INTO group_permission (group_id, permission_id)
SELECT (SELECT id FROM `group` WHERE name = 'Tester'), id
FROM permission WHERE name = 'GANHAR_AURA';


-- 8. RELAÇÃO USUÁRIO_GRUPO

INSERT INTO user_group (user_id, group_id) VALUES
((SELECT id FROM `user` WHERE email = 'joao.ger@pendezzapizza.com'),                        (SELECT id FROM `group` WHERE name = 'Gerente'));

INSERT INTO user_group (user_id, group_id) VALUES
((SELECT id FROM `user` WHERE email = 'maria.vnd@pendezzapizza.com'),                       (SELECT id FROM `group` WHERE name = 'Vendedor'));

INSERT INTO user_group (user_id, group_id) VALUES
((SELECT id FROM `user` WHERE email = 'guinas.sec@pendezzapizza.com'),                      (SELECT id FROM `group` WHERE name = 'Secretária'));

INSERT INTO user_group (user_id, group_id) VALUES
((SELECT id FROM `user` WHERE email = 'alele.cad@pendezzapizza.com'),                       (SELECT id FROM `group` WHERE name = 'Cadastrador'));

INSERT INTO user_group (user_id, group_id) VALUES
((SELECT id FROM `user` WHERE email = 'joaomohammed@gmail.com'),                            (SELECT id FROM `group` WHERE name = 'Tester'));


-- 9. RESTAURANTES

INSERT INTO restaurant (
    id, name, shipping_fee, address_zip_code, address_street,
    address_number, address_complement, address_neighborhood,
    address_city_id, registration_date, update_date, active, open
) VALUES
(UUID_TO_BIN('d4e5f6a7-b8c9-4d0e-ab1f-3b4c5d6e7f8a'), 'Trattoria da Mamma',      12.50, '01311-200', 'Rua Avanhandava',      '123', 'Perto da fonte',       'Bela Vista',  (SELECT id FROM city WHERE name = 'São Paulo'      LIMIT 1), NOW(), NOW(), 1, 1),
(UUID_TO_BIN('e5f6a7b8-c9d0-4e1f-bc2a-4c5d6e7f8a9b'), 'Pasta & Vino',             9.00, '04533-000', 'Rua Amauri',           '45',  'Térreo',               'Itaim Bibi',  (SELECT id FROM city WHERE name = 'São Paulo'      LIMIT 1), NOW(), NOW(), 1, 1),
(UUID_TO_BIN('52ec094f-3e34-42d4-845a-bc1c178259c1'), 'Pizzeria Napoli Centrale',  7.50, '22410-003', 'Rua Garcia d\'Avila',  '88',  NULL,                   'Ipanema',     (SELECT id FROM city WHERE name = 'Rio de Janeiro' LIMIT 1), NOW(), NOW(), 1, 1),
(UUID_TO_BIN('f6a7b8c9-d0e1-4f2a-cd3b-5d6e7f8a9b0c'), 'Osteria del Porto',       15.00, '30130-140', 'Rua Sergipe',          '1050','Próximo à Savassi',    'Funcionários',(SELECT id FROM city WHERE name = 'Belo Horizonte' LIMIT 1), NOW(), NOW(), 1, 1);


-- 10. RESTAURANTE_FORMA_PAGAMENTO

INSERT INTO restaurant_payment_method (restaurant_id, payment_method_id)
SELECT (SELECT id FROM restaurant WHERE name = 'Trattoria da Mamma'), id
FROM payment_method WHERE description IN ('Cartão de Crédito', 'Cartão de Débito', 'Pix', 'Dinheiro');

INSERT INTO restaurant_payment_method (restaurant_id, payment_method_id)
SELECT (SELECT id FROM restaurant WHERE name = 'Pasta & Vino'), id
FROM payment_method WHERE description = 'Pix';

INSERT INTO restaurant_payment_method (restaurant_id, payment_method_id)
SELECT (SELECT id FROM restaurant WHERE name = 'Pizzeria Napoli Centrale'), id
FROM payment_method WHERE description IN ('Cartão de Débito', 'Pix', 'Dinheiro');

INSERT INTO restaurant_payment_method (restaurant_id, payment_method_id)
SELECT (SELECT id FROM restaurant WHERE name = 'Osteria del Porto'), id
FROM payment_method WHERE description IN ('Cartão de Crédito', 'Cartão de Débito');


-- 11. PRODUTOS

INSERT INTO product (id, restaurant_id, name, description, price, active, update_date)
SELECT UUID_TO_BIN('a7b8c9d0-e1f2-4a3b-de4c-6e7f8a9b0c1d'), r.id, 'Lasagna alla Bolognese',
'Massa fresca, ragu de carne bovina, molho bechamel e muito queijo gratinado.', 58.00, 1, UTC_TIMESTAMP()
FROM restaurant r WHERE r.name = 'Trattoria da Mamma' LIMIT 1;

INSERT INTO product (id, restaurant_id, name, description, price, active, update_date)
SELECT UUID_TO_BIN('b8c9d0e1-f2a3-4b4c-ef5d-7f8a9b0c1d2e'), r.id, 'Fettuccine Alfredo',
'Fettuccine artesanal com molho cremoso de manteiga e parmesão de 24 meses.', 45.50, 1, UTC_TIMESTAMP()
FROM restaurant r WHERE r.name = 'Trattoria da Mamma' LIMIT 1;

INSERT INTO product (id, restaurant_id, name, description, price, active, update_date)
SELECT UUID_TO_BIN('c9d0e1f2-a3b4-4c5d-fa6e-8a9b0c1d2e3f'), r.id, 'Spaghetti alla Carbonara',
'A autêntica receita romana com guanciale, gemas de ovos e queijo pecorino.', 52.00, 1, UTC_TIMESTAMP()
FROM restaurant r WHERE r.name = 'Pasta & Vino' LIMIT 1;

INSERT INTO product (id, restaurant_id, name, description, price, active, update_date)
SELECT UUID_TO_BIN('d0e1f2a3-b4c5-4d6e-ab7f-9b0c1d2e3f4a'), r.id, 'Risotto ai Funghi',
'Arroz arbóreo, mix de cogumelos frescos e finalizado com azeite de trufas.', 64.90, 1, UTC_TIMESTAMP()
FROM restaurant r WHERE r.name = 'Pasta & Vino' LIMIT 1;

INSERT INTO product (id, restaurant_id, name, description, price, active, update_date)
SELECT UUID_TO_BIN('72e58c00-e73f-41ee-bdd7-acf75341a7a7'), r.id, 'Pizza Margherita Verace',
'Tomate San Marzano, mozzarella de búfala, manjericão fresco e azeite extra virgem.', 48.00, 1, UTC_TIMESTAMP()
FROM restaurant r WHERE r.name = 'Pizzeria Napoli Centrale' LIMIT 1;

INSERT INTO product (id, restaurant_id, name, description, price, active, update_date)
SELECT UUID_TO_BIN('e1f2a3b4-c5d6-4e7f-bc8a-0c1d2e3f4a5b'), r.id, 'Pizza Diavola',
'Molho de tomate, mozzarella, salame picante italiano e cebola roxa.', 54.00, 1, UTC_TIMESTAMP()
FROM restaurant r WHERE r.name = 'Pizzeria Napoli Centrale' LIMIT 1;

INSERT INTO product (id, restaurant_id, name, description, price, active, update_date)
SELECT UUID_TO_BIN('f2a3b4c5-d6e7-4f8a-cd9b-1d2e3f4a5b6c'), r.id, 'Spaghetti allo Scoglio',
'Massa com frutos do mar (camarão, lula e mariscos) ao molho de vinho branco.', 78.00, 1, UTC_TIMESTAMP()
FROM restaurant r WHERE r.name = 'Osteria del Porto' LIMIT 1;

INSERT INTO product (id, restaurant_id, name, description, price, active, update_date)
SELECT UUID_TO_BIN('a3b4c5d6-e7f8-4a9b-de0c-2e3f4a5b6c7d'), r.id, 'Tiramisù Clássico',
'Sobremesa de biscoito champagne embebido em café com creme de mascarpone.', 28.00, 1, UTC_TIMESTAMP()
FROM restaurant r WHERE r.name = 'Osteria del Porto' LIMIT 1;


-- 13. RESPONSÁVEL RESTAURANTE

INSERT INTO restaurant_user_responsible (user_id, restaurant_id) VALUES
((SELECT id FROM `user` WHERE email = 'email.teste.pendezzapizza.tcc+hubert@gmail.com'),    (SELECT id FROM restaurant WHERE name = 'Trattoria da Mamma'));

INSERT INTO restaurant_user_responsible (user_id, restaurant_id) VALUES
((SELECT id FROM `user` WHERE email = 'email.teste.pendezzapizza.tcc+hubert@gmail.com'),    (SELECT id FROM restaurant WHERE name = 'Pizzeria Napoli Centrale'));

INSERT INTO restaurant_user_responsible (user_id, restaurant_id) VALUES
((SELECT id FROM `user` WHERE email = 'email.teste.pendezzapizza.tcc+sebastiao@gmail.com'), (SELECT id FROM restaurant WHERE name = 'Pasta & Vino'));

INSERT INTO restaurant_user_responsible (user_id, restaurant_id) VALUES
((SELECT id FROM `user` WHERE email = 'email.teste.pendezzapizza.tcc+sebastiao@gmail.com'), (SELECT id FROM restaurant WHERE name = 'Osteria del Porto'));


-- 14. PEDIDOS DE EXEMPLO

INSERT INTO `order` (
    id, subtotal, shipping_fee, total_cost, creation_date, order_status,
    payment_method_id, restaurant_id, customer_user_id,
    address_city_id, address_street, address_number,
    address_neighborhood, address_zip_code, update_date
) VALUES (
    UUID_TO_BIN('b4c5d6e7-f8a9-4b0c-ef1d-3f4a5b6c7d8e'),
    58.00, 12.50, 70.50, NOW(), 'CONFIRMED',
    (SELECT id FROM payment_method WHERE description = 'Cartão de Crédito' LIMIT 1),
    (SELECT id FROM restaurant WHERE name = 'Trattoria da Mamma' LIMIT 1),
    (SELECT id FROM `user` WHERE email = 'maria.vnd@pendezzapizza.com' LIMIT 1),
    (SELECT id FROM city WHERE name = 'São Paulo' LIMIT 1),
    'Rua das Laranjeiras', '500', 'Aclimação', '01537-000', UTC_TIMESTAMP()
);

INSERT INTO `order` (
    id, subtotal, shipping_fee, total_cost, creation_date, order_status,
    payment_method_id, restaurant_id, customer_user_id,
    address_city_id, address_street, address_number,
    address_neighborhood, address_zip_code, update_date
) VALUES (
    UUID_TO_BIN('c5d6e7f8-a9b0-4c1d-fa2e-4a5b6c7d8e9f'),
    54.00, 7.50, 61.50, NOW(), 'DELIVERED',
    (SELECT id FROM payment_method WHERE description = 'Pix' LIMIT 1),
    (SELECT id FROM restaurant WHERE name = 'Pizzeria Napoli Centrale' LIMIT 1),
    (SELECT id FROM `user` WHERE email = 'guinas.sec@pendezzapizza.com' LIMIT 1),
    (SELECT id FROM city WHERE name = 'Rio de Janeiro' LIMIT 1),
    'Rua Visconde de Pirajá', '120', 'Ipanema', '22410-003', UTC_TIMESTAMP()
);


-- clients

INSERT INTO oauth2_registered_client
    (id, client_id, client_id_issued_at, client_secret, client_secret_expires_at, client_name, client_authentication_methods, authorization_grant_types, redirect_uris, scopes, client_settings, token_settings, post_logout_redirect_uris)
VALUES
('a97d2554-0128-4307-9f41-f6659cf1091b', 'pendezzapizza-web', '2026-01-25 14:44:40', '$2a$12$pI6u.WC.Lh/d3oSTmjQewufL2majI8t43Iwv59hZQjz5UogcYHzUC', NULL, 'a97d2554-0128-4307-9f41-f6659cf1091b', 'client_secret_basic', 'refresh_token,authorization_code', 'http://localhost:8080/redirect', 'READ,WRITE', '{\"@class\":\"java.util.Collections$UnmodifiableMap\",\"settings.client.require-proof-key\":true,\"settings.client.require-authorization-consent\":false}', '{\"@class\":\"java.util.Collections$UnmodifiableMap\",\"settings.token.reuse-refresh-tokens\":false,\"settings.token.x509-certificate-bound-access-tokens\":false,\"settings.token.id-token-signature-algorithm\":[\"org.springframework.security.oauth2.jose.jws.SignatureAlgorithm\",\"RS256\"],\"settings.token.access-token-time-to-live\":[\"java.time.Duration\",1800.000000000],\"settings.token.access-token-format\":{\"@class\":\"org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat\",\"value\":\"self-contained\"},\"settings.token.refresh-token-time-to-live\":[\"java.time.Duration\",2592000.000000000],\"settings.token.authorization-code-time-to-live\":[\"java.time.Duration\",300.000000000],\"settings.token.device-code-time-to-live\":[\"java.time.Duration\",300.000000000]}', '');
