-- Geralmente  eu faria isso com o afterMigrate, porém como eu estou usando 2 instâncias da api e desta vez eu uso UUID não da pra fazer isso de forma eficiente sem usar uma migrate específica pra isso

-- 1. ESTADOS

INSERT INTO state (id, name, update_date) VALUES
(UUID_TO_BIN('ac3bb31f-4c4f-44ff-88e8-92646ba56240'), 'São Paulo', UTC_TIMESTAMP()),
(UUID_TO_BIN(UUID()), 'Rio de Janeiro', UTC_TIMESTAMP()),
(UUID_TO_BIN(UUID()), 'Minas Gerais', UTC_TIMESTAMP());


-- 2. CIDADES

INSERT INTO city (id, name, state_id, update_date) VALUES
(UUID_TO_BIN(UUID()), 'São Paulo',      (SELECT id FROM state WHERE name = 'São Paulo' LIMIT 1), UTC_TIMESTAMP()),
(UUID_TO_BIN(UUID()), 'Campinas',       (SELECT id FROM state WHERE name = 'São Paulo' LIMIT 1), UTC_TIMESTAMP()),
(UUID_TO_BIN(UUID()), 'Rio de Janeiro', (SELECT id FROM state WHERE name = 'Rio de Janeiro' LIMIT 1), UTC_TIMESTAMP()),
(UUID_TO_BIN(UUID()), 'Niterói',        (SELECT id FROM state WHERE name = 'Rio de Janeiro' LIMIT 1), UTC_TIMESTAMP()),
(UUID_TO_BIN(UUID()), 'Belo Horizonte', (SELECT id FROM state WHERE name = 'Minas Gerais' LIMIT 1), UTC_TIMESTAMP()),
(UUID_TO_BIN(UUID()), 'Uberlândia',     (SELECT id FROM state WHERE name = 'Minas Gerais' LIMIT 1), UTC_TIMESTAMP()),
(UUID_TO_BIN('0e0362cc-db84-4484-9909-d6977b96b619'), 'Monte Verde', (SELECT id FROM state WHERE name = 'Minas Gerais' LIMIT 1), UTC_TIMESTAMP());


-- 3. FORMAS DE PAGAMENTO

INSERT INTO payment_method (id, description, update_date) VALUES
(UUID_TO_BIN(UUID()), 'Cartão de Crédito', UTC_TIMESTAMP()),
(UUID_TO_BIN(UUID()), 'Cartão de Débito',  UTC_TIMESTAMP()),
(UUID_TO_BIN(UUID()), 'Pix',               UTC_TIMESTAMP()),
(UUID_TO_BIN("3ee42ee7-3d35-4680-afe0-e01a24e649dc"), 'Dinheiro',          UTC_TIMESTAMP());


-- 4. PERMISSÕES

insert into permission (id, name, description, update_date) values (UUID_TO_BIN(UUID()), 'EDITAR_COZINHAS', 'Permite editar cozinhas', UTC_TIMESTAMP());
insert into permission (id, name, description, update_date) values (UUID_TO_BIN(UUID()), 'EDITAR_FORMAS_PAGAMENTO', 'Permite criar ou editar formas de pagamento', UTC_TIMESTAMP());
insert into permission (id, name, description, update_date) values (UUID_TO_BIN(UUID()), 'EDITAR_CIDADES', 'Permite criar ou editar cidades', UTC_TIMESTAMP());
insert into permission (id, name, description, update_date) values (UUID_TO_BIN(UUID()), 'EDITAR_ESTADOS', 'Permite criar ou editar estados', UTC_TIMESTAMP());
insert into permission (id, name, description, update_date) values (UUID_TO_BIN(UUID()), 'CONSULTAR_USUARIOS_GRUPOS_PERMISSOES', 'Permite consultar usuários', UTC_TIMESTAMP());
insert into permission (id, name, description, update_date) values (UUID_TO_BIN(UUID()), 'EDITAR_USUARIOS_GRUPOS_PERMISSOES', 'Permite criar ou editar usuários', UTC_TIMESTAMP());
insert into permission (id, name, description, update_date) values (UUID_TO_BIN(UUID()), 'EDITAR_RESTAURANTES', 'Permite criar, editar ou gerenciar restaurantes', UTC_TIMESTAMP());
insert into permission (id, name, description, update_date) values (UUID_TO_BIN(UUID()), 'CONSULTAR_PEDIDOS', 'Permite consultar pedidos', UTC_TIMESTAMP());
insert into permission (id, name, description, update_date) values (UUID_TO_BIN(UUID()), 'GERENCIAR_PEDIDOS', 'Permite gerenciar pedidos', UTC_TIMESTAMP());
insert into permission (id, name, description, update_date) values (UUID_TO_BIN(UUID()), 'GERAR_RELATORIOS', 'Permite gerar relatórios', UTC_TIMESTAMP());
insert into permission (id, name, description, update_date) values (UUID_TO_BIN("1925eff2-a761-49ff-ab2a-fd471828cb9d"), 'GANHAR_AURA', 'Permite gerar aura', UTC_TIMESTAMP());

-- 5. GRUPOS

insert into `group` (id, name, update_date) values
(UUID_TO_BIN(UUID()), 'Gerente', UTC_TIMESTAMP()),
(UUID_TO_BIN(UUID()), 'Vendedor', UTC_TIMESTAMP()),
(UUID_TO_BIN(UUID()), 'Secretária', UTC_TIMESTAMP()),
(UUID_TO_BIN(UUID()), 'Cadastrador', UTC_TIMESTAMP()),
(UUID_TO_BIN("4a3fdd17-542f-4f6c-b450-871ff0f21092"), 'Tester', UTC_TIMESTAMP());

-- 6. USUÁRIOS (com senha BCRYPT)

INSERT INTO `user` (id, name, email, password, update_date) VALUES
(UUID_TO_BIN(UUID()), 'João da Silva', 'joao.ger@pendezzapizza.com', '$2a$12$xM3T9jhJ/qTbQ8yKkFyapOJeD.xzlaOB.CIgaKUBBsSfxw2dAbzM6', UTC_TIMESTAMP()),
(UUID_TO_BIN(UUID()), 'Maria Joaquina', 'maria.vnd@pendezzapizza.com', '$2a$12$xM3T9jhJ/qTbQ8yKkFyapOJeD.xzlaOB.CIgaKUBBsSfxw2dAbzM6', UTC_TIMESTAMP()),
(UUID_TO_BIN(UUID()), 'Roberto fazbear', 'guinas.sec@pendezzapizza.com', '$2a$12$xM3T9jhJ/qTbQ8yKkFyapOJeD.xzlaOB.CIgaKUBBsSfxw2dAbzM6', UTC_TIMESTAMP()),
(UUID_TO_BIN(UUID()), 'La ele da silva', 'alele.cad@pendezzapizza.com', '$2a$12$xM3T9jhJ/qTbQ8yKkFyapOJeD.xzlaOB.CIgaKUBBsSfxw2dAbzM6', UTC_TIMESTAMP()),
(UUID_TO_BIN(UUID()), 'José Souza', 'email.teste.pendezzapizza.tcc+hubert@gmail.com', '$2a$12$xM3T9jhJ/qTbQ8yKkFyapOJeD.xzlaOB.CIgaKUBBsSfxw2dAbzM6', UTC_TIMESTAMP()),
(UUID_TO_BIN(UUID()), 'Sebastião Martins', 'email.teste.pendezzapizza.tcc+sebastiao@gmail.com', '$2a$12$xM3T9jhJ/qTbQ8yKkFyapOJeD.xzlaOB.CIgaKUBBsSfxw2dAbzM6', UTC_TIMESTAMP()),
(UUID_TO_BIN(UUID()), 'Ronaldo Pinto', 'cocoxixicocopinto@gmail.com', '$2a$12$xM3T9jhJ/qTbQ8yKkFyapOJeD.xzlaOB.CIgaKUBBsSfxw2dAbzM6', UTC_TIMESTAMP()),
(UUID_TO_BIN("a6162eb1-df44-471b-aef3-9feee0d9d267"), 'João Mohammed Pendezza', 'joaomohammed@gmail.com', '$2a$12$xM3T9jhJ/qTbQ8yKkFyapOJeD.xzlaOB.CIgaKUBBsSfxw2dAbzM6', UTC_TIMESTAMP());

-- 7. RELAÇÃO GRUPO_PERMISSAO

-- Gerente (antigo ID 1) - Pode fazer tudo
INSERT INTO group_permission (group_id, permission_id)
SELECT (SELECT id FROM `group` WHERE name = 'Gerente'), id
FROM permission;

-- Vendedor (antigo ID 2) - Permissões de CONSULTA + GERENCIAR_PEDIDOS
INSERT INTO group_permission (group_id, permission_id)
SELECT (SELECT id FROM `group` WHERE name = 'Vendedor'), id
FROM permission WHERE name LIKE 'CONSULTAR_%';

-- Adicionando especificamente GERENCIAR_PEDIDOS para Vendedor
INSERT INTO group_permission (group_id, permission_id)
SELECT (SELECT id FROM `group` WHERE name = 'Vendedor'), id
FROM permission WHERE name = 'GERENCIAR_PEDIDOS';

-- Secretária (antigo ID 3) - Permissões de CONSULTA
INSERT INTO group_permission (group_id, permission_id)
SELECT (SELECT id FROM `group` WHERE name = 'Secretária'), id
FROM permission WHERE name LIKE 'CONSULTAR_%';

-- Cadastrador (antigo ID 4) - Permissões de RESTAURANTES
INSERT INTO group_permission (group_id, permission_id)
SELECT (SELECT id FROM `group` WHERE name = 'Cadastrador'), id
FROM permission WHERE name LIKE '%_RESTAURANTES';

INSERT INTO group_permission (group_id, permission_id)
SELECT (SELECT id FROM `group` WHERE name = 'Tester'), id
FROM permission WHERE name = 'GANHAR_AURA';

-- 8. RELAÇÃO USUÁRIO_GRUPO

-- João da Silva -> Gerente
INSERT INTO user_group (user_id, group_id)
VALUES (
    (SELECT id FROM `user` WHERE email = 'joao.ger@pendezzapizza.com'),
    (SELECT id FROM `group` WHERE name = 'Gerente')
);

-- Maria Joaquina -> Vendedor
INSERT INTO user_group (user_id, group_id)
VALUES (
    (SELECT id FROM `user` WHERE email = 'maria.vnd@pendezzapizza.com'),
    (SELECT id FROM `group` WHERE name = 'Vendedor')
);

-- Roberto fazbear -> Secretária
INSERT INTO user_group (user_id, group_id)
VALUES (
    (SELECT id FROM `user` WHERE email = 'guinas.sec@pendezzapizza.com'),
    (SELECT id FROM `group` WHERE name = 'Secretária')
);

-- La ele da silva -> Cadastrador
INSERT INTO user_group (user_id, group_id)
VALUES (
    (SELECT id FROM `user` WHERE email = 'alele.cad@pendezzapizza.com'),
    (SELECT id FROM `group` WHERE name = 'Cadastrador')
);

INSERT INTO user_group (user_id, group_id)
VALUES (
    (SELECT id FROM `user` WHERE email = 'joaomohammed@gmail.com'),
    (SELECT id FROM `group` WHERE name = 'Tester')
);

-- 9. RESTAURANTES

INSERT INTO restaurant (
    id, name, shipping_fee, address_zip_code, address_street,
    address_number, address_complement, address_neighborhood,
    address_city_id, registration_date, update_date, active, open
)
VALUES
(UUID_TO_BIN(UUID()), 'Trattoria da Mamma', 12.50,
'01311-200', 'Rua Avanhandava', '123', 'Perto da fonte', 'Bela Vista',
(SELECT id FROM city WHERE name = 'São Paulo' LIMIT 1),
NOW(), NOW(), 1, 1),

(UUID_TO_BIN(UUID()), 'Pasta & Vino', 9.00,
'04533-000', 'Rua Amauri', '45', 'Térreo', 'Itaim Bibi',
(SELECT id FROM city WHERE name = 'São Paulo' LIMIT 1),
NOW(), NOW(), 1, 1),

(UUID_TO_BIN("52ec094f-3e34-42d4-845a-bc1c178259c1"), 'Pizzeria Napoli Centrale', 7.50,
'22410-003', 'Rua Garcia d\'Avila', '88', NULL, 'Ipanema',
(SELECT id FROM city WHERE name = 'Rio de Janeiro' LIMIT 1),
NOW(), NOW(), 1, 1),

(UUID_TO_BIN(UUID()), 'Osteria del Porto', 15.00,
'30130-140', 'Rua Sergipe', '1050', 'Próximo à Savassi', 'Funcionários',
(SELECT id FROM city WHERE name = 'Belo Horizonte' LIMIT 1),
NOW(), NOW(), 1, 1);

-- 10. RESTAURANTE_FORMA_PAGAMENTO

-- Trattoria da Mamma (Restaurante 1) aceita Crédito, Dinheiro, Débito e Pix
INSERT INTO restaurant_payment_method (restaurant_id, payment_method_id)
SELECT (SELECT id FROM restaurant WHERE name = 'Trattoria da Mamma'), id
FROM payment_method WHERE description IN ('Cartão de Crédito', 'Cartão de Débito', 'Pix' , 'Dinheiro');

-- Pasta & Vino (Restaurante 2) aceita apenas Pix
INSERT INTO restaurant_payment_method (restaurant_id, payment_method_id)
SELECT (SELECT id FROM restaurant WHERE name = 'Pasta & Vino'), id
FROM payment_method WHERE description = 'Pix';

-- Pizzeria Napoli Centrale (Restaurante 3) aceita Débito, Dinheiro e Pix
INSERT INTO restaurant_payment_method (restaurant_id, payment_method_id)
SELECT (SELECT id FROM restaurant WHERE name = 'Pizzeria Napoli Centrale'), id
FROM payment_method WHERE description IN ('Cartão de Débito', 'Pix', "Dinheiro");

-- Osteria del Porto (Restaurante 4) aceita Crédito e Débito
INSERT INTO restaurant_payment_method (restaurant_id, payment_method_id)
SELECT (SELECT id FROM restaurant WHERE name = 'Osteria del Porto'), id
FROM payment_method WHERE description IN ('Cartão de Crédito', 'Cartão de Débito');

-- 11. PRODUTOS

-- 1. Trattoria da Mamma
INSERT INTO product (id, restaurant_id, name, description, price, active, update_date)
SELECT UUID_TO_BIN(UUID()), r.id, 'Lasagna alla Bolognese',
'Massa fresca, ragu de carne bovina, molho bechamel e muito queijo gratinado.', 58.00, 1, UTC_TIMESTAMP()
FROM restaurant r WHERE r.name = 'Trattoria da Mamma' LIMIT 1;

INSERT INTO product (id, restaurant_id, name, description, price, active, update_date)
SELECT UUID_TO_BIN(UUID()), r.id, 'Fettuccine Alfredo',
'Fettuccine artesanal com molho cremoso de manteiga e parmesão de 24 meses.', 45.50, 1, UTC_TIMESTAMP()
FROM restaurant r WHERE r.name = 'Trattoria da Mamma' LIMIT 1;

-- 2. Pasta & Vino
INSERT INTO product (id, restaurant_id, name, description, price, active, update_date)
SELECT UUID_TO_BIN(UUID()), r.id, 'Spaghetti alla Carbonara',
'A autêntica receita romana com guanciale, gemas de ovos e queijo pecorino.', 52.00, 1, UTC_TIMESTAMP()
FROM restaurant r WHERE r.name = 'Pasta & Vino' LIMIT 1;

INSERT INTO product (id, restaurant_id, name, description, price, active, update_date)
SELECT UUID_TO_BIN(UUID()), r.id, 'Risotto ai Funghi',
'Arroz arbóreo, mix de cogumelos frescos e finalizado com azeite de trufas.', 64.90, 1, UTC_TIMESTAMP()
FROM restaurant r WHERE r.name = 'Pasta & Vino' LIMIT 1;

-- 3. Pizzeria Napoli Centrale
INSERT INTO product (id, restaurant_id, name, description, price, active, update_date)
SELECT UUID_TO_BIN("72e58c00-e73f-41ee-bdd7-acf75341a7a7"), r.id, 'Pizza Margherita Verace',
'Tomate San Marzano, mozzarella de búfala, manjericão fresco e azeite extra virgem.', 48.00, 1, UTC_TIMESTAMP()
FROM restaurant r WHERE r.name = 'Pizzeria Napoli Centrale' LIMIT 1;

INSERT INTO product (id, restaurant_id, name, description, price, active, update_date)
SELECT UUID_TO_BIN(UUID()), r.id, 'Pizza Diavola',
'Molho de tomate, mozzarella, salame picante italiano e cebola roxa.', 54.00, 1, UTC_TIMESTAMP()
FROM restaurant r WHERE r.name = 'Pizzeria Napoli Centrale' LIMIT 1;

-- 4. Osteria del Porto
INSERT INTO product (id, restaurant_id, name, description, price, active, update_date)
SELECT UUID_TO_BIN(UUID()), r.id, 'Spaghetti allo Scoglio',
'Massa com frutos do mar (camarão, lula e mariscos) ao molho de vinho branco.', 78.00, 1, UTC_TIMESTAMP()
FROM restaurant r WHERE r.name = 'Osteria del Porto' LIMIT 1;

INSERT INTO product (id, restaurant_id, name, description, price, active, update_date)
SELECT UUID_TO_BIN(UUID()), r.id, 'Tiramisù Clássico',
'Sobremesa de biscoito champagne embebido em café com creme de mascarpone.', 28.00, 1, UTC_TIMESTAMP()
FROM restaurant r WHERE r.name = 'Osteria del Porto' LIMIT 1;

-- 13. RESPONSÁVEL RESTAURANTE

-- José Souza (ID 5) -> Trattoria da Mamma (Rest 1)
INSERT INTO restaurant_user_responsible (user_id, restaurant_id)
VALUES (
    (SELECT id FROM `user` WHERE email = 'email.teste.pendezzapizza.tcc+hubert@gmail.com'),
    (SELECT id FROM restaurant WHERE name = 'Trattoria da Mamma')
);

-- José Souza (ID 5) -> Pizzeria Napoli Centrale (Rest 3)
INSERT INTO restaurant_user_responsible (user_id, restaurant_id)
VALUES (
    (SELECT id FROM `user` WHERE email = 'email.teste.pendezzapizza.tcc+hubert@gmail.com'),
    (SELECT id FROM restaurant WHERE name = 'Pizzeria Napoli Centrale')
);

-- Sebastião Martins (ID 6) -> Pasta & Vino (Rest 2)
INSERT INTO restaurant_user_responsible (user_id, restaurant_id)
VALUES (
    (SELECT id FROM `user` WHERE email = 'email.teste.pendezzapizza.tcc+sebastiao@gmail.com'),
    (SELECT id FROM restaurant WHERE name = 'Pasta & Vino')
);

-- Sebastião Martins (ID 6) -> Osteria del Porto (Rest 4)
INSERT INTO restaurant_user_responsible (user_id, restaurant_id)
VALUES (
    (SELECT id FROM `user` WHERE email = 'email.teste.pendezzapizza.tcc+sebastiao@gmail.com'),
    (SELECT id FROM restaurant WHERE name = 'Osteria del Porto')
);


-- 14. PEDIDO DE EXEMPLO

-- Pedido 1: Maria Joaquina pedindo uma Lasagna na Trattoria da Mamma
INSERT INTO `order` (
    id, subtotal, shipping_fee, total_cost, creation_date, order_status,
    payment_method_id, restaurant_id, customer_user_id,
    address_city_id, address_street, address_number,
    address_neighborhood, address_zip_code, update_date
)
SELECT
    UUID_TO_BIN(UUID()),
    58.00, -- Subtotal (Lasagna alla Bolognese)
    12.50, -- Shipping Fee (Trattoria da Mamma)
    70.50, -- Total Cost (58.00 + 12.50)
    NOW(), 'CONFIRMED',
    (SELECT id FROM payment_method WHERE description = 'Cartão de Crédito' LIMIT 1),
    (SELECT id FROM restaurant WHERE name = 'Trattoria da Mamma' LIMIT 1),
    (SELECT id FROM `user` WHERE email = 'maria.vnd@pendezzapizza.com' LIMIT 1),
    (SELECT id FROM city WHERE name = 'São Paulo' LIMIT 1),
    'Rua das Laranjeiras', '500', 'Aclimação', '01537-000', UTC_TIMESTAMP();

-- Pedido 2: Roberto Fazbear pedindo uma Pizza Diavola na Napoli Centrale
INSERT INTO `order` (
    id, subtotal, shipping_fee, total_cost, creation_date, order_status,
    payment_method_id, restaurant_id, customer_user_id,
    address_city_id, address_street, address_number,
    address_neighborhood, address_zip_code, update_date
)
SELECT
    UUID_TO_BIN(UUID()),
    54.00, -- Subtotal (Pizza Diavola)
    7.50,  -- Shipping Fee (Napoli Centrale)
    61.50, -- Total Cost (54.00 + 7.50)
    NOW(), 'DELIVERED',
    (SELECT id FROM payment_method WHERE description = 'Pix' LIMIT 1),
    (SELECT id FROM restaurant WHERE name = 'Pizzeria Napoli Centrale' LIMIT 1),
    (SELECT id FROM `user` WHERE email = 'guinas.sec@pendezzapizza.com' LIMIT 1),
    (SELECT id FROM city WHERE name = 'Rio de Janeiro' LIMIT 1),
    'Rua Visconde de Pirajá', '120', 'Ipanema', '22410-003', UTC_TIMESTAMP();


-- clients:

insert into oauth2_registered_client
    (id, client_id, client_id_issued_at, client_secret, client_secret_expires_at, client_name, client_authentication_methods, authorization_grant_types, redirect_uris, scopes, client_settings, token_settings, post_logout_redirect_uris)
values ('af5aa5c3-e3de-430d-9251-1682d9329280', 'pendezzapizza-commom-client', '2026-01-25 14:44:40', '$2a$12$48T2NXd/.auET2UoPNYT8enHWRKuzlt1bhwdbZHgxpElMn4VTGaUu', NULL, 'af5aa5c3-e3de-430d-9251-1682d9329280', 'client_secret_basic', 'refresh_token,authorization_code', 'http://localhost:5173/redirect,http://localhost:80/redirect', 'READ,WRITE', '{\"@class\":\"java.util.Collections$UnmodifiableMap\",\"settings.client.require-proof-key\":true,\"settings.client.require-authorization-consent\":false}', '{\"@class\":\"java.util.Collections$UnmodifiableMap\",\"settings.token.reuse-refresh-tokens\":false,\"settings.token.x509-certificate-bound-access-tokens\":false,\"settings.token.id-token-signature-algorithm\":[\"org.springframework.security.oauth2.jose.jws.SignatureAlgorithm\",\"RS256\"],\"settings.token.access-token-time-to-live\":[\"java.time.Duration\",1800.000000000],\"settings.token.access-token-format\":{\"@class\":\"org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat\",\"value\":\"self-contained\"},\"settings.token.refresh-token-time-to-live\":[\"java.time.Duration\",2592000.000000000],\"settings.token.authorization-code-time-to-live\":[\"java.time.Duration\",300.000000000],\"settings.token.device-code-time-to-live\":[\"java.time.Duration\",300.000000000]}', ''),
('a97d2554-0128-4307-9f41-f6659cf1091b', 'pendezzapizza-web', '2026-01-25 14:44:40', '$2a$12$pI6u.WC.Lh/d3oSTmjQewufL2majI8t43Iwv59hZQjz5UogcYHzUC', NULL, 'a97d2554-0128-4307-9f41-f6659cf1091b', 'client_secret_basic', 'refresh_token,authorization_code', 'http://localhost:5173/redirect,http://localhost:80/redirect', 'READ,WRITE', '{\"@class\":\"java.util.Collections$UnmodifiableMap\",\"settings.client.require-proof-key\":true,\"settings.client.require-authorization-consent\":false}', '{\"@class\":\"java.util.Collections$UnmodifiableMap\",\"settings.token.reuse-refresh-tokens\":false,\"settings.token.x509-certificate-bound-access-tokens\":false,\"settings.token.id-token-signature-algorithm\":[\"org.springframework.security.oauth2.jose.jws.SignatureAlgorithm\",\"RS256\"],\"settings.token.access-token-time-to-live\":[\"java.time.Duration\",1800.000000000],\"settings.token.access-token-format\":{\"@class\":\"org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat\",\"value\":\"self-contained\"},\"settings.token.refresh-token-time-to-live\":[\"java.time.Duration\",2592000.000000000],\"settings.token.authorization-code-time-to-live\":[\"java.time.Duration\",300.000000000],\"settings.token.device-code-time-to-live\":[\"java.time.Duration\",300.000000000]}', ''),
('5ad789dc-d6c6-45c6-81e6-248892cffa7d', 'pendezzapizza-doc', '2026-01-25 14:44:40', '$2a$12$3K81IleIYCdK7sk7tmXSWOqF0nng.HFeI.qPiPa/rpInY/XhGnnp6', NULL, '5ad789dc-d6c6-45c6-81e6-248892cffa7d', 'client_secret_basic', 'refresh_token,authorization_code', 'http://localhost/swagger-ui/index.html', 'READ,WRITE', '{\"@class\":\"java.util.Collections$UnmodifiableMap\",\"settings.client.require-proof-key\":true,\"settings.client.require-authorization-consent\":false}', '{\"@class\":\"java.util.Collections$UnmodifiableMap\",\"settings.token.reuse-refresh-tokens\":false,\"settings.token.x509-certificate-bound-access-tokens\":false,\"settings.token.id-token-signature-algorithm\":[\"org.springframework.security.oauth2.jose.jws.SignatureAlgorithm\",\"RS256\"],\"settings.token.access-token-time-to-live\":[\"java.time.Duration\",1800.000000000],\"settings.token.access-token-format\":{\"@class\":\"org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat\",\"value\":\"self-contained\"},\"settings.token.refresh-token-time-to-live\":[\"java.time.Duration\",2592000.000000000],\"settings.token.authorization-code-time-to-live\":[\"java.time.Duration\",300.000000000],\"settings.token.device-code-time-to-live\":[\"java.time.Duration\",300.000000000]}', ''),
('5281d8fd-856c-417b-94c3-ac8fc13a4758', 'pendezzapizza-end', '2026-01-25 14:44:40', '$2a$12$eEF1poMf6bOVVd/P6Q9JI.116IjvRAFqgVD5dYubKr3Z00RiMaCLS', NULL, '5281d8fd-856c-417b-94c3-ac8fc13a4758', 'client_secret_basic', 'refresh_token,authorization_code', 'http://localhost:5173/redirect,http://localhost:80/redirect', 'READ,WRITE', '{\"@class\":\"java.util.Collections$UnmodifiableMap\",\"settings.client.require-proof-key\":true,\"settings.client.require-authorization-consent\":false}', '{\"@class\":\"java.util.Collections$UnmodifiableMap\",\"settings.token.reuse-refresh-tokens\":false,\"settings.token.x509-certificate-bound-access-tokens\":false,\"settings.token.id-token-signature-algorithm\":[\"org.springframework.security.oauth2.jose.jws.SignatureAlgorithm\",\"RS256\"],\"settings.token.access-token-time-to-live\":[\"java.time.Duration\",1800.000000000],\"settings.token.access-token-format\":{\"@class\":\"org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat\",\"value\":\"self-contained\"},\"settings.token.refresh-token-time-to-live\":[\"java.time.Duration\",2592000.000000000],\"settings.token.authorization-code-time-to-live\":[\"java.time.Duration\",300.000000000],\"settings.token.device-code-time-to-live\":[\"java.time.Duration\",300.000000000]}', '');
