-- =====================================================================
-- SEED: Pizzeria Napoli Centrale (estoque + pedidos para o relatório)
-- Cole este bloco no seu script de seed, DEPOIS da seção 15 (itens dos pedidos)
-- e ANTES de '-- clients:'.
--
-- IMPORTANTE: no INÍCIO do script de reset (antes de 'DELETE FROM order_item;') inclua:
--   DELETE FROM ingredient_movement;
--   DELETE FROM recipe_item;
--   DELETE FROM ingredient;
-- Sem isso, os DELETEs de `order` e product falham por causa das chaves estrangeiras.
-- =====================================================================

-- Os horários abaixo estão em horário de Brasília (UTC-3). O banco guarda em UTC,
-- então somamos o deslocamento. Se o seu app lê o horário cru do banco, use 0.
SET @tz_offset = 3;

SET @rest      = (SELECT id FROM restaurant WHERE name = 'Pizzeria Napoli Centrale' LIMIT 1);
SET @city_rio  = (SELECT id FROM city WHERE name = 'Rio de Janeiro' LIMIT 1);
SET @pm_pix = (SELECT id FROM payment_method WHERE description = 'Pix' LIMIT 1);
SET @pm_debito = (SELECT id FROM payment_method WHERE description = 'Cartão de Débito' LIMIT 1);
SET @pm_dinheiro = (SELECT id FROM payment_method WHERE description = 'Dinheiro' LIMIT 1);
SET @u_jm = (SELECT id FROM `user` WHERE email = 'joaorafael@gmail.com' LIMIT 1);   -- João Mohammed Pendezza
SET @u_rp = (SELECT id FROM `user` WHERE email = 'cocoxixicocopinto@gmail.com' LIMIT 1);   -- Ronaldo Pinto
SET @u_sm = (SELECT id FROM `user` WHERE email = 'email.teste.pendezzapizza.tcc+sebastiao@gmail.com' LIMIT 1);   -- Sebastião Martins
SET @u_js = (SELECT id FROM `user` WHERE email = 'cliente@pendezzapizza.com' LIMIT 1);   -- José Souza
SET @u_jd = (SELECT id FROM `user` WHERE email = 'gerente@pendezzapizza.com' LIMIT 1);   -- João da Silva
SET @u_mj = (SELECT id FROM `user` WHERE email = 'maria.vnd@pendezzapizza.com' LIMIT 1);   -- Maria Joaquina

-- ---------------------------------------------------------------------
-- 16. PRODUTOS NOVOS
-- ---------------------------------------------------------------------
SET @p_C = UUID_TO_BIN(UUID());
SET @p_Q = UUID_TO_BIN(UUID());
INSERT INTO product (id, restaurant_id, name, description, price, active, update_date) VALUES
(@p_C, @rest, 'Pizza Calabresa', 'Molho de tomate, mozzarella, calabresa artesanal fatiada, cebola e orégano.', 52.00, 1, UTC_TIMESTAMP()),
(@p_Q, @rest, 'Pizza Quatro Queijos', 'Mozzarella, provolone, gorgonzola e parmesão sobre molho de tomate e azeite.', 56.00, 1, UTC_TIMESTAMP());

-- ---------------------------------------------------------------------
-- 17. INGREDIENTES (saldo é recalculado no fim, a partir do histórico)
-- ---------------------------------------------------------------------
SET @i_farinha = UUID_TO_BIN(UUID());
SET @i_molho = UUID_TO_BIN(UUID());
SET @i_mussarela = UUID_TO_BIN(UUID());
SET @i_calabresa = UUID_TO_BIN(UUID());
SET @i_cebola = UUID_TO_BIN(UUID());
SET @i_provolone = UUID_TO_BIN(UUID());
SET @i_gorgonzola = UUID_TO_BIN(UUID());
SET @i_parmesao = UUID_TO_BIN(UUID());
SET @i_azeite = UUID_TO_BIN(UUID());
SET @i_oregano = UUID_TO_BIN(UUID());
SET @i_caixa = UUID_TO_BIN(UUID());
INSERT INTO ingredient (id, restaurant_id, name, unit, stock_quantity, low_stock_threshold) VALUES
(@i_farinha, @rest, 'Farinha de trigo', 'G', 0, 2000),
(@i_molho, @rest, 'Molho de tomate', 'ML', 0, 1800),
(@i_mussarela, @rest, 'Mussarela', 'G', 0, 1200),
(@i_calabresa, @rest, 'Calabresa fatiada', 'G', 0, 1600),
(@i_cebola, @rest, 'Cebola', 'G', 0, 200),
(@i_provolone, @rest, 'Provolone', 'G', 0, 700),
(@i_gorgonzola, @rest, 'Gorgonzola', 'G', 0, 450),
(@i_parmesao, @rest, 'Parmesão ralado', 'G', 0, 250),
(@i_azeite, @rest, 'Azeite de oliva', 'ML', 0, 150),
(@i_oregano, @rest, 'Orégano', 'G', 0, 30),
(@i_caixa, @rest, 'Caixa de pizza', 'UN', 0, 50);

-- ---------------------------------------------------------------------
-- 18. FICHA TÉCNICA (consumo por 1 unidade; g, ml ou un)
-- ---------------------------------------------------------------------
INSERT INTO recipe_item (id, product_id, ingredient_id, quantity_per_unit) VALUES
(UUID_TO_BIN(UUID()), @p_C, @i_farinha, 300),
(UUID_TO_BIN(UUID()), @p_C, @i_molho, 120),
(UUID_TO_BIN(UUID()), @p_C, @i_mussarela, 200),
(UUID_TO_BIN(UUID()), @p_C, @i_calabresa, 150),
(UUID_TO_BIN(UUID()), @p_C, @i_cebola, 40),
(UUID_TO_BIN(UUID()), @p_C, @i_azeite, 10),
(UUID_TO_BIN(UUID()), @p_C, @i_oregano, 2),
(UUID_TO_BIN(UUID()), @p_C, @i_caixa, 1),
(UUID_TO_BIN(UUID()), @p_Q, @i_farinha, 300),
(UUID_TO_BIN(UUID()), @p_Q, @i_molho, 120),
(UUID_TO_BIN(UUID()), @p_Q, @i_mussarela, 120),
(UUID_TO_BIN(UUID()), @p_Q, @i_provolone, 70),
(UUID_TO_BIN(UUID()), @p_Q, @i_gorgonzola, 60),
(UUID_TO_BIN(UUID()), @p_Q, @i_parmesao, 40),
(UUID_TO_BIN(UUID()), @p_Q, @i_azeite, 10),
(UUID_TO_BIN(UUID()), @p_Q, @i_oregano, 2),
(UUID_TO_BIN(UUID()), @p_Q, @i_caixa, 1);

-- ---------------------------------------------------------------------
-- 19. COMPRAS (ENTRADA): estoque inicial em 09/10 e reposição semanal às sextas
-- ---------------------------------------------------------------------
INSERT INTO ingredient_movement (id, ingredient_id, type, quantity, order_id, created_at) VALUES
(UUID_TO_BIN(UUID()), @i_farinha, 'ENTRADA', 10000, NULL, DATE_ADD('2026-10-09 09:10:00', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_farinha, 'ENTRADA', 5000, NULL, DATE_ADD('2026-10-23 09:10:00', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_farinha, 'ENTRADA', 10000, NULL, DATE_ADD('2026-10-30 09:10:00', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_molho, 'ENTRADA', 3000, NULL, DATE_ADD('2026-10-09 09:10:00', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_molho, 'ENTRADA', 3000, NULL, DATE_ADD('2026-10-16 09:10:00', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_molho, 'ENTRADA', 3000, NULL, DATE_ADD('2026-10-30 09:10:00', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_molho, 'ENTRADA', 3000, NULL, DATE_ADD('2026-11-06 09:10:00', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_mussarela, 'ENTRADA', 4000, NULL, DATE_ADD('2026-10-09 09:10:00', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_mussarela, 'ENTRADA', 2000, NULL, DATE_ADD('2026-10-16 09:10:00', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_mussarela, 'ENTRADA', 2000, NULL, DATE_ADD('2026-10-23 09:10:00', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_mussarela, 'ENTRADA', 4000, NULL, DATE_ADD('2026-10-30 09:10:00', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_mussarela, 'ENTRADA', 2000, NULL, DATE_ADD('2026-11-06 09:10:00', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_calabresa, 'ENTRADA', 2000, NULL, DATE_ADD('2026-10-09 09:10:00', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_calabresa, 'ENTRADA', 1000, NULL, DATE_ADD('2026-10-23 09:10:00', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_calabresa, 'ENTRADA', 2000, NULL, DATE_ADD('2026-10-30 09:10:00', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_calabresa, 'ENTRADA', 1000, NULL, DATE_ADD('2026-11-06 09:10:00', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_cebola, 'ENTRADA', 1000, NULL, DATE_ADD('2026-10-09 09:10:00', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_cebola, 'ENTRADA', 1000, NULL, DATE_ADD('2026-10-30 09:10:00', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_provolone, 'ENTRADA', 1000, NULL, DATE_ADD('2026-10-09 09:10:00', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_provolone, 'ENTRADA', 1000, NULL, DATE_ADD('2026-10-23 09:10:00', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_provolone, 'ENTRADA', 1000, NULL, DATE_ADD('2026-10-30 09:10:00', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_provolone, 'ENTRADA', 1000, NULL, DATE_ADD('2026-11-06 09:10:00', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_gorgonzola, 'ENTRADA', 500, NULL, DATE_ADD('2026-10-09 09:10:00', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_gorgonzola, 'ENTRADA', 500, NULL, DATE_ADD('2026-10-16 09:10:00', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_gorgonzola, 'ENTRADA', 500, NULL, DATE_ADD('2026-10-23 09:10:00', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_gorgonzola, 'ENTRADA', 1000, NULL, DATE_ADD('2026-10-30 09:10:00', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_parmesao, 'ENTRADA', 500, NULL, DATE_ADD('2026-10-09 09:10:00', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_parmesao, 'ENTRADA', 500, NULL, DATE_ADD('2026-10-16 09:10:00', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_parmesao, 'ENTRADA', 500, NULL, DATE_ADD('2026-10-23 09:10:00', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_parmesao, 'ENTRADA', 500, NULL, DATE_ADD('2026-10-30 09:10:00', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_azeite, 'ENTRADA', 500, NULL, DATE_ADD('2026-10-09 09:10:00', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_azeite, 'ENTRADA', 500, NULL, DATE_ADD('2026-10-30 09:10:00', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_oregano, 'ENTRADA', 100, NULL, DATE_ADD('2026-10-09 09:10:00', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_oregano, 'ENTRADA', 100, NULL, DATE_ADD('2026-10-30 09:10:00', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_caixa, 'ENTRADA', 50, NULL, DATE_ADD('2026-10-09 09:10:00', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_caixa, 'ENTRADA', 50, NULL, DATE_ADD('2026-10-23 09:10:00', INTERVAL @tz_offset HOUR));

-- Perdas por validade/descarte (AJUSTE negativo)
INSERT INTO ingredient_movement (id, ingredient_id, type, quantity, order_id, created_at) VALUES
(UUID_TO_BIN(UUID()), @i_mussarela, 'AJUSTE', -400, NULL, DATE_ADD('2026-10-28 15:00:00', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_cebola, 'AJUSTE', -250, NULL, DATE_ADD('2026-11-02 16:00:00', INTERVAL @tz_offset HOUR));

-- ---------------------------------------------------------------------
-- 20. PEDIDOS (25 de Pizza Calabresa + 25 de Pizza Quatro Queijos)
--     pagamentos: só Pix, Débito e Dinheiro (o que a Napoli aceita)
--     entregas: só Rio de Janeiro / Ipanema, CEP conferido por rua
-- ---------------------------------------------------------------------

-- #01 | 11/10 dom 21:59 | João Mohammed Pendezza | 3x Pizza Calabresa | Pix
SET @o = UUID_TO_BIN(UUID());
INSERT INTO `order` (id, subtotal, shipping_fee, total_cost, creation_date, order_status, payment_method_id, restaurant_id, customer_user_id, address_city_id, address_street, address_number, address_neighborhood, address_zip_code, update_date)
VALUES (@o, 156.00, 7.50, 163.50, DATE_ADD('2026-10-11 21:59:49', INTERVAL @tz_offset HOUR), 'DELIVERED', @pm_pix, @rest, @u_jm, @city_rio, 'Rua Maria Quitéria', '85', 'Ipanema', '22410-040', UTC_TIMESTAMP());
INSERT INTO order_item (id, order_id, product_id, quantity, unit_price, total_price, note)
VALUES (UUID_TO_BIN(UUID()), @o, @p_C, 3, 52.00, 156.00, NULL);
INSERT INTO ingredient_movement (id, ingredient_id, type, quantity, order_id, created_at) VALUES
(UUID_TO_BIN(UUID()), @i_farinha, 'SAIDA_PEDIDO', 900, @o, DATE_ADD('2026-10-11 21:59:49', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_molho, 'SAIDA_PEDIDO', 360, @o, DATE_ADD('2026-10-11 21:59:49', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_mussarela, 'SAIDA_PEDIDO', 600, @o, DATE_ADD('2026-10-11 21:59:49', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_calabresa, 'SAIDA_PEDIDO', 450, @o, DATE_ADD('2026-10-11 21:59:49', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_cebola, 'SAIDA_PEDIDO', 120, @o, DATE_ADD('2026-10-11 21:59:49', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_azeite, 'SAIDA_PEDIDO', 30, @o, DATE_ADD('2026-10-11 21:59:49', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_oregano, 'SAIDA_PEDIDO', 6, @o, DATE_ADD('2026-10-11 21:59:49', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_caixa, 'SAIDA_PEDIDO', 3, @o, DATE_ADD('2026-10-11 21:59:49', INTERVAL @tz_offset HOUR));

-- #02 | 12/10 seg 20:55 | Ronaldo Pinto | 1x Pizza Quatro Queijos | Pix
SET @o = UUID_TO_BIN(UUID());
INSERT INTO `order` (id, subtotal, shipping_fee, total_cost, creation_date, order_status, payment_method_id, restaurant_id, customer_user_id, address_city_id, address_street, address_number, address_neighborhood, address_zip_code, update_date)
VALUES (@o, 56.00, 7.50, 63.50, DATE_ADD('2026-10-12 20:55:06', INTERVAL @tz_offset HOUR), 'DELIVERED', @pm_pix, @rest, @u_rp, @city_rio, 'Rua Teixeira de Melo', '31', 'Ipanema', '22410-010', UTC_TIMESTAMP());
INSERT INTO order_item (id, order_id, product_id, quantity, unit_price, total_price, note)
VALUES (UUID_TO_BIN(UUID()), @o, @p_Q, 1, 56.00, 56.00, 'Cortar em 8 pedaços');
INSERT INTO ingredient_movement (id, ingredient_id, type, quantity, order_id, created_at) VALUES
(UUID_TO_BIN(UUID()), @i_farinha, 'SAIDA_PEDIDO', 300, @o, DATE_ADD('2026-10-12 20:55:06', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_molho, 'SAIDA_PEDIDO', 120, @o, DATE_ADD('2026-10-12 20:55:06', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_mussarela, 'SAIDA_PEDIDO', 120, @o, DATE_ADD('2026-10-12 20:55:06', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_provolone, 'SAIDA_PEDIDO', 70, @o, DATE_ADD('2026-10-12 20:55:06', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_gorgonzola, 'SAIDA_PEDIDO', 60, @o, DATE_ADD('2026-10-12 20:55:06', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_parmesao, 'SAIDA_PEDIDO', 40, @o, DATE_ADD('2026-10-12 20:55:06', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_azeite, 'SAIDA_PEDIDO', 10, @o, DATE_ADD('2026-10-12 20:55:06', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_oregano, 'SAIDA_PEDIDO', 2, @o, DATE_ADD('2026-10-12 20:55:06', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_caixa, 'SAIDA_PEDIDO', 1, @o, DATE_ADD('2026-10-12 20:55:06', INTERVAL @tz_offset HOUR));

-- #03 | 13/10 ter 21:18 | Sebastião Martins | 2x Pizza Calabresa | Pix
SET @o = UUID_TO_BIN(UUID());
INSERT INTO `order` (id, subtotal, shipping_fee, total_cost, creation_date, order_status, payment_method_id, restaurant_id, customer_user_id, address_city_id, address_street, address_number, address_neighborhood, address_zip_code, update_date)
VALUES (@o, 104.00, 7.50, 111.50, DATE_ADD('2026-10-13 21:18:05', INTERVAL @tz_offset HOUR), 'DELIVERED', @pm_pix, @rest, @u_sm, @city_rio, 'Rua Alberto de Campos', '140', 'Ipanema', '22411-030', UTC_TIMESTAMP());
INSERT INTO order_item (id, order_id, product_id, quantity, unit_price, total_price, note)
VALUES (UUID_TO_BIN(UUID()), @o, @p_C, 2, 52.00, 104.00, NULL);
INSERT INTO ingredient_movement (id, ingredient_id, type, quantity, order_id, created_at) VALUES
(UUID_TO_BIN(UUID()), @i_farinha, 'SAIDA_PEDIDO', 600, @o, DATE_ADD('2026-10-13 21:18:05', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_molho, 'SAIDA_PEDIDO', 240, @o, DATE_ADD('2026-10-13 21:18:05', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_mussarela, 'SAIDA_PEDIDO', 400, @o, DATE_ADD('2026-10-13 21:18:05', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_calabresa, 'SAIDA_PEDIDO', 300, @o, DATE_ADD('2026-10-13 21:18:05', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_cebola, 'SAIDA_PEDIDO', 80, @o, DATE_ADD('2026-10-13 21:18:05', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_azeite, 'SAIDA_PEDIDO', 20, @o, DATE_ADD('2026-10-13 21:18:05', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_oregano, 'SAIDA_PEDIDO', 4, @o, DATE_ADD('2026-10-13 21:18:05', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_caixa, 'SAIDA_PEDIDO', 2, @o, DATE_ADD('2026-10-13 21:18:05', INTERVAL @tz_offset HOUR));

-- #04 | 16/10 sex 18:34 | José Souza | 1x Pizza Calabresa | Pix
SET @o = UUID_TO_BIN(UUID());
INSERT INTO `order` (id, subtotal, shipping_fee, total_cost, creation_date, order_status, payment_method_id, restaurant_id, customer_user_id, address_city_id, address_street, address_number, address_neighborhood, address_zip_code, update_date)
VALUES (@o, 52.00, 7.50, 59.50, DATE_ADD('2026-10-16 18:34:17', INTERVAL @tz_offset HOUR), 'DELIVERED', @pm_pix, @rest, @u_js, @city_rio, 'Rua Joana Angélica', '83', 'Ipanema', '22420-030', UTC_TIMESTAMP());
INSERT INTO order_item (id, order_id, product_id, quantity, unit_price, total_price, note)
VALUES (UUID_TO_BIN(UUID()), @o, @p_C, 1, 52.00, 52.00, NULL);
INSERT INTO ingredient_movement (id, ingredient_id, type, quantity, order_id, created_at) VALUES
(UUID_TO_BIN(UUID()), @i_farinha, 'SAIDA_PEDIDO', 300, @o, DATE_ADD('2026-10-16 18:34:17', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_molho, 'SAIDA_PEDIDO', 120, @o, DATE_ADD('2026-10-16 18:34:17', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_mussarela, 'SAIDA_PEDIDO', 200, @o, DATE_ADD('2026-10-16 18:34:17', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_calabresa, 'SAIDA_PEDIDO', 150, @o, DATE_ADD('2026-10-16 18:34:17', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_cebola, 'SAIDA_PEDIDO', 40, @o, DATE_ADD('2026-10-16 18:34:17', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_azeite, 'SAIDA_PEDIDO', 10, @o, DATE_ADD('2026-10-16 18:34:17', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_oregano, 'SAIDA_PEDIDO', 2, @o, DATE_ADD('2026-10-16 18:34:17', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_caixa, 'SAIDA_PEDIDO', 1, @o, DATE_ADD('2026-10-16 18:34:17', INTERVAL @tz_offset HOUR));

-- #05 | 18/10 dom 12:52 | João da Silva | 2x Pizza Calabresa | Cartão de Débito
SET @o = UUID_TO_BIN(UUID());
INSERT INTO `order` (id, subtotal, shipping_fee, total_cost, creation_date, order_status, payment_method_id, restaurant_id, customer_user_id, address_city_id, address_street, address_number, address_neighborhood, address_zip_code, update_date)
VALUES (@o, 104.00, 7.50, 111.50, DATE_ADD('2026-10-18 12:52:57', INTERVAL @tz_offset HOUR), 'DELIVERED', @pm_debito, @rest, @u_jd, @city_rio, 'Rua Barão de Jaguaripe', '280', 'Ipanema', '22421-000', UTC_TIMESTAMP());
INSERT INTO order_item (id, order_id, product_id, quantity, unit_price, total_price, note)
VALUES (UUID_TO_BIN(UUID()), @o, @p_C, 2, 52.00, 104.00, NULL);
INSERT INTO ingredient_movement (id, ingredient_id, type, quantity, order_id, created_at) VALUES
(UUID_TO_BIN(UUID()), @i_farinha, 'SAIDA_PEDIDO', 600, @o, DATE_ADD('2026-10-18 12:52:57', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_molho, 'SAIDA_PEDIDO', 240, @o, DATE_ADD('2026-10-18 12:52:57', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_mussarela, 'SAIDA_PEDIDO', 400, @o, DATE_ADD('2026-10-18 12:52:57', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_calabresa, 'SAIDA_PEDIDO', 300, @o, DATE_ADD('2026-10-18 12:52:57', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_cebola, 'SAIDA_PEDIDO', 80, @o, DATE_ADD('2026-10-18 12:52:57', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_azeite, 'SAIDA_PEDIDO', 20, @o, DATE_ADD('2026-10-18 12:52:57', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_oregano, 'SAIDA_PEDIDO', 4, @o, DATE_ADD('2026-10-18 12:52:57', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_caixa, 'SAIDA_PEDIDO', 2, @o, DATE_ADD('2026-10-18 12:52:57', INTERVAL @tz_offset HOUR));

-- #06 | 18/10 dom 14:19 | Maria Joaquina | 2x Pizza Calabresa | Dinheiro
SET @o = UUID_TO_BIN(UUID());
INSERT INTO `order` (id, subtotal, shipping_fee, total_cost, creation_date, order_status, payment_method_id, restaurant_id, customer_user_id, address_city_id, address_street, address_number, address_neighborhood, address_zip_code, update_date)
VALUES (@o, 104.00, 7.50, 111.50, DATE_ADD('2026-10-18 14:19:01', INTERVAL @tz_offset HOUR), 'DELIVERED', @pm_dinheiro, @rest, @u_mj, @city_rio, 'Rua Farme de Amoedo', '34', 'Ipanema', '22420-020', UTC_TIMESTAMP());
INSERT INTO order_item (id, order_id, product_id, quantity, unit_price, total_price, note)
VALUES (UUID_TO_BIN(UUID()), @o, @p_C, 2, 52.00, 104.00, NULL);
INSERT INTO ingredient_movement (id, ingredient_id, type, quantity, order_id, created_at) VALUES
(UUID_TO_BIN(UUID()), @i_farinha, 'SAIDA_PEDIDO', 600, @o, DATE_ADD('2026-10-18 14:19:01', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_molho, 'SAIDA_PEDIDO', 240, @o, DATE_ADD('2026-10-18 14:19:01', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_mussarela, 'SAIDA_PEDIDO', 400, @o, DATE_ADD('2026-10-18 14:19:01', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_calabresa, 'SAIDA_PEDIDO', 300, @o, DATE_ADD('2026-10-18 14:19:01', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_cebola, 'SAIDA_PEDIDO', 80, @o, DATE_ADD('2026-10-18 14:19:01', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_azeite, 'SAIDA_PEDIDO', 20, @o, DATE_ADD('2026-10-18 14:19:01', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_oregano, 'SAIDA_PEDIDO', 4, @o, DATE_ADD('2026-10-18 14:19:01', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_caixa, 'SAIDA_PEDIDO', 2, @o, DATE_ADD('2026-10-18 14:19:01', INTERVAL @tz_offset HOUR));

-- #07 | 22/10 qui 20:41 | João Mohammed Pendezza | 1x Pizza Quatro Queijos | Pix
SET @o = UUID_TO_BIN(UUID());
INSERT INTO `order` (id, subtotal, shipping_fee, total_cost, creation_date, order_status, payment_method_id, restaurant_id, customer_user_id, address_city_id, address_street, address_number, address_neighborhood, address_zip_code, update_date)
VALUES (@o, 56.00, 7.50, 63.50, DATE_ADD('2026-10-22 20:41:32', INTERVAL @tz_offset HOUR), 'DELIVERED', @pm_pix, @rest, @u_jm, @city_rio, 'Rua Maria Quitéria', '85', 'Ipanema', '22410-040', UTC_TIMESTAMP());
INSERT INTO order_item (id, order_id, product_id, quantity, unit_price, total_price, note)
VALUES (UUID_TO_BIN(UUID()), @o, @p_Q, 1, 56.00, 56.00, NULL);
INSERT INTO ingredient_movement (id, ingredient_id, type, quantity, order_id, created_at) VALUES
(UUID_TO_BIN(UUID()), @i_farinha, 'SAIDA_PEDIDO', 300, @o, DATE_ADD('2026-10-22 20:41:32', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_molho, 'SAIDA_PEDIDO', 120, @o, DATE_ADD('2026-10-22 20:41:32', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_mussarela, 'SAIDA_PEDIDO', 120, @o, DATE_ADD('2026-10-22 20:41:32', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_provolone, 'SAIDA_PEDIDO', 70, @o, DATE_ADD('2026-10-22 20:41:32', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_gorgonzola, 'SAIDA_PEDIDO', 60, @o, DATE_ADD('2026-10-22 20:41:32', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_parmesao, 'SAIDA_PEDIDO', 40, @o, DATE_ADD('2026-10-22 20:41:32', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_azeite, 'SAIDA_PEDIDO', 10, @o, DATE_ADD('2026-10-22 20:41:32', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_oregano, 'SAIDA_PEDIDO', 2, @o, DATE_ADD('2026-10-22 20:41:32', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_caixa, 'SAIDA_PEDIDO', 1, @o, DATE_ADD('2026-10-22 20:41:32', INTERVAL @tz_offset HOUR));

-- #08 | 23/10 sex 19:50 | Ronaldo Pinto | 1x Pizza Quatro Queijos | Cartão de Débito
SET @o = UUID_TO_BIN(UUID());
INSERT INTO `order` (id, subtotal, shipping_fee, total_cost, creation_date, order_status, payment_method_id, restaurant_id, customer_user_id, address_city_id, address_street, address_number, address_neighborhood, address_zip_code, update_date)
VALUES (@o, 56.00, 7.50, 63.50, DATE_ADD('2026-10-23 19:50:58', INTERVAL @tz_offset HOUR), 'DELIVERED', @pm_debito, @rest, @u_rp, @city_rio, 'Rua Teixeira de Melo', '31', 'Ipanema', '22410-010', UTC_TIMESTAMP());
INSERT INTO order_item (id, order_id, product_id, quantity, unit_price, total_price, note)
VALUES (UUID_TO_BIN(UUID()), @o, @p_Q, 1, 56.00, 56.00, NULL);
INSERT INTO ingredient_movement (id, ingredient_id, type, quantity, order_id, created_at) VALUES
(UUID_TO_BIN(UUID()), @i_farinha, 'SAIDA_PEDIDO', 300, @o, DATE_ADD('2026-10-23 19:50:58', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_molho, 'SAIDA_PEDIDO', 120, @o, DATE_ADD('2026-10-23 19:50:58', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_mussarela, 'SAIDA_PEDIDO', 120, @o, DATE_ADD('2026-10-23 19:50:58', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_provolone, 'SAIDA_PEDIDO', 70, @o, DATE_ADD('2026-10-23 19:50:58', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_gorgonzola, 'SAIDA_PEDIDO', 60, @o, DATE_ADD('2026-10-23 19:50:58', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_parmesao, 'SAIDA_PEDIDO', 40, @o, DATE_ADD('2026-10-23 19:50:58', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_azeite, 'SAIDA_PEDIDO', 10, @o, DATE_ADD('2026-10-23 19:50:58', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_oregano, 'SAIDA_PEDIDO', 2, @o, DATE_ADD('2026-10-23 19:50:58', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_caixa, 'SAIDA_PEDIDO', 1, @o, DATE_ADD('2026-10-23 19:50:58', INTERVAL @tz_offset HOUR));

-- #09 | 24/10 sáb 21:39 | Sebastião Martins | 1x Pizza Calabresa | Pix
SET @o = UUID_TO_BIN(UUID());
INSERT INTO `order` (id, subtotal, shipping_fee, total_cost, creation_date, order_status, payment_method_id, restaurant_id, customer_user_id, address_city_id, address_street, address_number, address_neighborhood, address_zip_code, update_date)
VALUES (@o, 52.00, 7.50, 59.50, DATE_ADD('2026-10-24 21:39:50', INTERVAL @tz_offset HOUR), 'DELIVERED', @pm_pix, @rest, @u_sm, @city_rio, 'Rua Alberto de Campos', '140', 'Ipanema', '22411-030', UTC_TIMESTAMP());
INSERT INTO order_item (id, order_id, product_id, quantity, unit_price, total_price, note)
VALUES (UUID_TO_BIN(UUID()), @o, @p_C, 1, 52.00, 52.00, 'Bem assada');
INSERT INTO ingredient_movement (id, ingredient_id, type, quantity, order_id, created_at) VALUES
(UUID_TO_BIN(UUID()), @i_farinha, 'SAIDA_PEDIDO', 300, @o, DATE_ADD('2026-10-24 21:39:50', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_molho, 'SAIDA_PEDIDO', 120, @o, DATE_ADD('2026-10-24 21:39:50', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_mussarela, 'SAIDA_PEDIDO', 200, @o, DATE_ADD('2026-10-24 21:39:50', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_calabresa, 'SAIDA_PEDIDO', 150, @o, DATE_ADD('2026-10-24 21:39:50', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_cebola, 'SAIDA_PEDIDO', 40, @o, DATE_ADD('2026-10-24 21:39:50', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_azeite, 'SAIDA_PEDIDO', 10, @o, DATE_ADD('2026-10-24 21:39:50', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_oregano, 'SAIDA_PEDIDO', 2, @o, DATE_ADD('2026-10-24 21:39:50', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_caixa, 'SAIDA_PEDIDO', 1, @o, DATE_ADD('2026-10-24 21:39:50', INTERVAL @tz_offset HOUR));

-- #10 | 25/10 dom 12:49 | José Souza | 2x Pizza Quatro Queijos | Cartão de Débito
SET @o = UUID_TO_BIN(UUID());
INSERT INTO `order` (id, subtotal, shipping_fee, total_cost, creation_date, order_status, payment_method_id, restaurant_id, customer_user_id, address_city_id, address_street, address_number, address_neighborhood, address_zip_code, update_date)
VALUES (@o, 112.00, 7.50, 119.50, DATE_ADD('2026-10-25 12:49:08', INTERVAL @tz_offset HOUR), 'DELIVERED', @pm_debito, @rest, @u_js, @city_rio, 'Rua Joana Angélica', '83', 'Ipanema', '22420-030', UTC_TIMESTAMP());
INSERT INTO order_item (id, order_id, product_id, quantity, unit_price, total_price, note)
VALUES (UUID_TO_BIN(UUID()), @o, @p_Q, 2, 56.00, 112.00, NULL);
INSERT INTO ingredient_movement (id, ingredient_id, type, quantity, order_id, created_at) VALUES
(UUID_TO_BIN(UUID()), @i_farinha, 'SAIDA_PEDIDO', 600, @o, DATE_ADD('2026-10-25 12:49:08', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_molho, 'SAIDA_PEDIDO', 240, @o, DATE_ADD('2026-10-25 12:49:08', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_mussarela, 'SAIDA_PEDIDO', 240, @o, DATE_ADD('2026-10-25 12:49:08', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_provolone, 'SAIDA_PEDIDO', 140, @o, DATE_ADD('2026-10-25 12:49:08', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_gorgonzola, 'SAIDA_PEDIDO', 120, @o, DATE_ADD('2026-10-25 12:49:08', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_parmesao, 'SAIDA_PEDIDO', 80, @o, DATE_ADD('2026-10-25 12:49:08', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_azeite, 'SAIDA_PEDIDO', 20, @o, DATE_ADD('2026-10-25 12:49:08', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_oregano, 'SAIDA_PEDIDO', 4, @o, DATE_ADD('2026-10-25 12:49:08', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_caixa, 'SAIDA_PEDIDO', 2, @o, DATE_ADD('2026-10-25 12:49:08', INTERVAL @tz_offset HOUR));

-- #11 | 25/10 dom 20:30 | João da Silva | 1x Pizza Calabresa | Cartão de Débito
SET @o = UUID_TO_BIN(UUID());
INSERT INTO `order` (id, subtotal, shipping_fee, total_cost, creation_date, order_status, payment_method_id, restaurant_id, customer_user_id, address_city_id, address_street, address_number, address_neighborhood, address_zip_code, update_date)
VALUES (@o, 52.00, 7.50, 59.50, DATE_ADD('2026-10-25 20:30:32', INTERVAL @tz_offset HOUR), 'DELIVERED', @pm_debito, @rest, @u_jd, @city_rio, 'Rua Barão de Jaguaripe', '280', 'Ipanema', '22421-000', UTC_TIMESTAMP());
INSERT INTO order_item (id, order_id, product_id, quantity, unit_price, total_price, note)
VALUES (UUID_TO_BIN(UUID()), @o, @p_C, 1, 52.00, 52.00, 'Bem assada');
INSERT INTO ingredient_movement (id, ingredient_id, type, quantity, order_id, created_at) VALUES
(UUID_TO_BIN(UUID()), @i_farinha, 'SAIDA_PEDIDO', 300, @o, DATE_ADD('2026-10-25 20:30:32', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_molho, 'SAIDA_PEDIDO', 120, @o, DATE_ADD('2026-10-25 20:30:32', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_mussarela, 'SAIDA_PEDIDO', 200, @o, DATE_ADD('2026-10-25 20:30:32', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_calabresa, 'SAIDA_PEDIDO', 150, @o, DATE_ADD('2026-10-25 20:30:32', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_cebola, 'SAIDA_PEDIDO', 40, @o, DATE_ADD('2026-10-25 20:30:32', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_azeite, 'SAIDA_PEDIDO', 10, @o, DATE_ADD('2026-10-25 20:30:32', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_oregano, 'SAIDA_PEDIDO', 2, @o, DATE_ADD('2026-10-25 20:30:32', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_caixa, 'SAIDA_PEDIDO', 1, @o, DATE_ADD('2026-10-25 20:30:32', INTERVAL @tz_offset HOUR));

-- #12 | 27/10 ter 21:31 | Maria Joaquina | 2x Pizza Quatro Queijos | Cartão de Débito
SET @o = UUID_TO_BIN(UUID());
INSERT INTO `order` (id, subtotal, shipping_fee, total_cost, creation_date, order_status, payment_method_id, restaurant_id, customer_user_id, address_city_id, address_street, address_number, address_neighborhood, address_zip_code, update_date)
VALUES (@o, 112.00, 7.50, 119.50, DATE_ADD('2026-10-27 21:31:48', INTERVAL @tz_offset HOUR), 'DELIVERED', @pm_debito, @rest, @u_mj, @city_rio, 'Rua Farme de Amoedo', '34', 'Ipanema', '22420-020', UTC_TIMESTAMP());
INSERT INTO order_item (id, order_id, product_id, quantity, unit_price, total_price, note)
VALUES (UUID_TO_BIN(UUID()), @o, @p_Q, 2, 56.00, 112.00, 'Cortar em 12 pedaços');
INSERT INTO ingredient_movement (id, ingredient_id, type, quantity, order_id, created_at) VALUES
(UUID_TO_BIN(UUID()), @i_farinha, 'SAIDA_PEDIDO', 600, @o, DATE_ADD('2026-10-27 21:31:48', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_molho, 'SAIDA_PEDIDO', 240, @o, DATE_ADD('2026-10-27 21:31:48', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_mussarela, 'SAIDA_PEDIDO', 240, @o, DATE_ADD('2026-10-27 21:31:48', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_provolone, 'SAIDA_PEDIDO', 140, @o, DATE_ADD('2026-10-27 21:31:48', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_gorgonzola, 'SAIDA_PEDIDO', 120, @o, DATE_ADD('2026-10-27 21:31:48', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_parmesao, 'SAIDA_PEDIDO', 80, @o, DATE_ADD('2026-10-27 21:31:48', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_azeite, 'SAIDA_PEDIDO', 20, @o, DATE_ADD('2026-10-27 21:31:48', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_oregano, 'SAIDA_PEDIDO', 4, @o, DATE_ADD('2026-10-27 21:31:48', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_caixa, 'SAIDA_PEDIDO', 2, @o, DATE_ADD('2026-10-27 21:31:48', INTERVAL @tz_offset HOUR));

-- #13 | 27/10 ter 22:31 | João Mohammed Pendezza | 1x Pizza Quatro Queijos | Cartão de Débito
SET @o = UUID_TO_BIN(UUID());
INSERT INTO `order` (id, subtotal, shipping_fee, total_cost, creation_date, order_status, payment_method_id, restaurant_id, customer_user_id, address_city_id, address_street, address_number, address_neighborhood, address_zip_code, update_date)
VALUES (@o, 56.00, 7.50, 63.50, DATE_ADD('2026-10-27 22:31:44', INTERVAL @tz_offset HOUR), 'DELIVERED', @pm_debito, @rest, @u_jm, @city_rio, 'Rua Maria Quitéria', '85', 'Ipanema', '22410-040', UTC_TIMESTAMP());
INSERT INTO order_item (id, order_id, product_id, quantity, unit_price, total_price, note)
VALUES (UUID_TO_BIN(UUID()), @o, @p_Q, 1, 56.00, 56.00, NULL);
INSERT INTO ingredient_movement (id, ingredient_id, type, quantity, order_id, created_at) VALUES
(UUID_TO_BIN(UUID()), @i_farinha, 'SAIDA_PEDIDO', 300, @o, DATE_ADD('2026-10-27 22:31:44', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_molho, 'SAIDA_PEDIDO', 120, @o, DATE_ADD('2026-10-27 22:31:44', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_mussarela, 'SAIDA_PEDIDO', 120, @o, DATE_ADD('2026-10-27 22:31:44', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_provolone, 'SAIDA_PEDIDO', 70, @o, DATE_ADD('2026-10-27 22:31:44', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_gorgonzola, 'SAIDA_PEDIDO', 60, @o, DATE_ADD('2026-10-27 22:31:44', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_parmesao, 'SAIDA_PEDIDO', 40, @o, DATE_ADD('2026-10-27 22:31:44', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_azeite, 'SAIDA_PEDIDO', 10, @o, DATE_ADD('2026-10-27 22:31:44', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_oregano, 'SAIDA_PEDIDO', 2, @o, DATE_ADD('2026-10-27 22:31:44', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_caixa, 'SAIDA_PEDIDO', 1, @o, DATE_ADD('2026-10-27 22:31:44', INTERVAL @tz_offset HOUR));

-- #14 | 28/10 qua 20:09 | Ronaldo Pinto | 2x Pizza Quatro Queijos | Pix
SET @o = UUID_TO_BIN(UUID());
INSERT INTO `order` (id, subtotal, shipping_fee, total_cost, creation_date, order_status, payment_method_id, restaurant_id, customer_user_id, address_city_id, address_street, address_number, address_neighborhood, address_zip_code, update_date)
VALUES (@o, 112.00, 7.50, 119.50, DATE_ADD('2026-10-28 20:09:33', INTERVAL @tz_offset HOUR), 'DELIVERED', @pm_pix, @rest, @u_rp, @city_rio, 'Rua Teixeira de Melo', '31', 'Ipanema', '22410-010', UTC_TIMESTAMP());
INSERT INTO order_item (id, order_id, product_id, quantity, unit_price, total_price, note)
VALUES (UUID_TO_BIN(UUID()), @o, @p_Q, 2, 56.00, 112.00, 'Bem assada');
INSERT INTO ingredient_movement (id, ingredient_id, type, quantity, order_id, created_at) VALUES
(UUID_TO_BIN(UUID()), @i_farinha, 'SAIDA_PEDIDO', 600, @o, DATE_ADD('2026-10-28 20:09:33', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_molho, 'SAIDA_PEDIDO', 240, @o, DATE_ADD('2026-10-28 20:09:33', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_mussarela, 'SAIDA_PEDIDO', 240, @o, DATE_ADD('2026-10-28 20:09:33', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_provolone, 'SAIDA_PEDIDO', 140, @o, DATE_ADD('2026-10-28 20:09:33', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_gorgonzola, 'SAIDA_PEDIDO', 120, @o, DATE_ADD('2026-10-28 20:09:33', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_parmesao, 'SAIDA_PEDIDO', 80, @o, DATE_ADD('2026-10-28 20:09:33', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_azeite, 'SAIDA_PEDIDO', 20, @o, DATE_ADD('2026-10-28 20:09:33', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_oregano, 'SAIDA_PEDIDO', 4, @o, DATE_ADD('2026-10-28 20:09:33', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_caixa, 'SAIDA_PEDIDO', 2, @o, DATE_ADD('2026-10-28 20:09:33', INTERVAL @tz_offset HOUR));

-- #15 | 28/10 qua 20:31 | Sebastião Martins | 2x Pizza Quatro Queijos | Cartão de Débito
SET @o = UUID_TO_BIN(UUID());
INSERT INTO `order` (id, subtotal, shipping_fee, total_cost, creation_date, order_status, payment_method_id, restaurant_id, customer_user_id, address_city_id, address_street, address_number, address_neighborhood, address_zip_code, update_date)
VALUES (@o, 112.00, 7.50, 119.50, DATE_ADD('2026-10-28 20:31:23', INTERVAL @tz_offset HOUR), 'DELIVERED', @pm_debito, @rest, @u_sm, @city_rio, 'Rua Vinícius de Moraes', '129', 'Ipanema', '22411-010', UTC_TIMESTAMP());
INSERT INTO order_item (id, order_id, product_id, quantity, unit_price, total_price, note)
VALUES (UUID_TO_BIN(UUID()), @o, @p_Q, 2, 56.00, 112.00, NULL);
INSERT INTO ingredient_movement (id, ingredient_id, type, quantity, order_id, created_at) VALUES
(UUID_TO_BIN(UUID()), @i_farinha, 'SAIDA_PEDIDO', 600, @o, DATE_ADD('2026-10-28 20:31:23', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_molho, 'SAIDA_PEDIDO', 240, @o, DATE_ADD('2026-10-28 20:31:23', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_mussarela, 'SAIDA_PEDIDO', 240, @o, DATE_ADD('2026-10-28 20:31:23', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_provolone, 'SAIDA_PEDIDO', 140, @o, DATE_ADD('2026-10-28 20:31:23', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_gorgonzola, 'SAIDA_PEDIDO', 120, @o, DATE_ADD('2026-10-28 20:31:23', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_parmesao, 'SAIDA_PEDIDO', 80, @o, DATE_ADD('2026-10-28 20:31:23', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_azeite, 'SAIDA_PEDIDO', 20, @o, DATE_ADD('2026-10-28 20:31:23', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_oregano, 'SAIDA_PEDIDO', 4, @o, DATE_ADD('2026-10-28 20:31:23', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_caixa, 'SAIDA_PEDIDO', 2, @o, DATE_ADD('2026-10-28 20:31:23', INTERVAL @tz_offset HOUR));

-- #16 | 30/10 sex 19:20 | José Souza | 2x Pizza Calabresa | Pix
SET @o = UUID_TO_BIN(UUID());
INSERT INTO `order` (id, subtotal, shipping_fee, total_cost, creation_date, order_status, payment_method_id, restaurant_id, customer_user_id, address_city_id, address_street, address_number, address_neighborhood, address_zip_code, update_date)
VALUES (@o, 104.00, 7.50, 111.50, DATE_ADD('2026-10-30 19:20:42', INTERVAL @tz_offset HOUR), 'DELIVERED', @pm_pix, @rest, @u_js, @city_rio, 'Rua Joana Angélica', '83', 'Ipanema', '22420-030', UTC_TIMESTAMP());
INSERT INTO order_item (id, order_id, product_id, quantity, unit_price, total_price, note)
VALUES (UUID_TO_BIN(UUID()), @o, @p_C, 2, 52.00, 104.00, NULL);
INSERT INTO ingredient_movement (id, ingredient_id, type, quantity, order_id, created_at) VALUES
(UUID_TO_BIN(UUID()), @i_farinha, 'SAIDA_PEDIDO', 600, @o, DATE_ADD('2026-10-30 19:20:42', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_molho, 'SAIDA_PEDIDO', 240, @o, DATE_ADD('2026-10-30 19:20:42', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_mussarela, 'SAIDA_PEDIDO', 400, @o, DATE_ADD('2026-10-30 19:20:42', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_calabresa, 'SAIDA_PEDIDO', 300, @o, DATE_ADD('2026-10-30 19:20:42', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_cebola, 'SAIDA_PEDIDO', 80, @o, DATE_ADD('2026-10-30 19:20:42', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_azeite, 'SAIDA_PEDIDO', 20, @o, DATE_ADD('2026-10-30 19:20:42', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_oregano, 'SAIDA_PEDIDO', 4, @o, DATE_ADD('2026-10-30 19:20:42', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_caixa, 'SAIDA_PEDIDO', 2, @o, DATE_ADD('2026-10-30 19:20:42', INTERVAL @tz_offset HOUR));

-- #17 | 30/10 sex 19:34 | João da Silva | 1x Pizza Quatro Queijos | Dinheiro
SET @o = UUID_TO_BIN(UUID());
INSERT INTO `order` (id, subtotal, shipping_fee, total_cost, creation_date, order_status, payment_method_id, restaurant_id, customer_user_id, address_city_id, address_street, address_number, address_neighborhood, address_zip_code, update_date)
VALUES (@o, 56.00, 7.50, 63.50, DATE_ADD('2026-10-30 19:34:18', INTERVAL @tz_offset HOUR), 'DELIVERED', @pm_dinheiro, @rest, @u_jd, @city_rio, 'Rua Barão de Jaguaripe', '280', 'Ipanema', '22421-000', UTC_TIMESTAMP());
INSERT INTO order_item (id, order_id, product_id, quantity, unit_price, total_price, note)
VALUES (UUID_TO_BIN(UUID()), @o, @p_Q, 1, 56.00, 56.00, NULL);
INSERT INTO ingredient_movement (id, ingredient_id, type, quantity, order_id, created_at) VALUES
(UUID_TO_BIN(UUID()), @i_farinha, 'SAIDA_PEDIDO', 300, @o, DATE_ADD('2026-10-30 19:34:18', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_molho, 'SAIDA_PEDIDO', 120, @o, DATE_ADD('2026-10-30 19:34:18', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_mussarela, 'SAIDA_PEDIDO', 120, @o, DATE_ADD('2026-10-30 19:34:18', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_provolone, 'SAIDA_PEDIDO', 70, @o, DATE_ADD('2026-10-30 19:34:18', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_gorgonzola, 'SAIDA_PEDIDO', 60, @o, DATE_ADD('2026-10-30 19:34:18', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_parmesao, 'SAIDA_PEDIDO', 40, @o, DATE_ADD('2026-10-30 19:34:18', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_azeite, 'SAIDA_PEDIDO', 10, @o, DATE_ADD('2026-10-30 19:34:18', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_oregano, 'SAIDA_PEDIDO', 2, @o, DATE_ADD('2026-10-30 19:34:18', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_caixa, 'SAIDA_PEDIDO', 1, @o, DATE_ADD('2026-10-30 19:34:18', INTERVAL @tz_offset HOUR));

-- #18 | 30/10 sex 20:12 | Maria Joaquina | 3x Pizza Calabresa | Pix | CANCELADO (estorno)
SET @o = UUID_TO_BIN(UUID());
INSERT INTO `order` (id, subtotal, shipping_fee, total_cost, creation_date, order_status, payment_method_id, restaurant_id, customer_user_id, address_city_id, address_street, address_number, address_neighborhood, address_zip_code, update_date)
VALUES (@o, 156.00, 7.50, 163.50, DATE_ADD('2026-10-30 20:12:47', INTERVAL @tz_offset HOUR), 'CANCELED', @pm_pix, @rest, @u_mj, @city_rio, 'Rua Farme de Amoedo', '34', 'Ipanema', '22420-020', UTC_TIMESTAMP());
INSERT INTO order_item (id, order_id, product_id, quantity, unit_price, total_price, note)
VALUES (UUID_TO_BIN(UUID()), @o, @p_C, 3, 52.00, 156.00, NULL);
INSERT INTO ingredient_movement (id, ingredient_id, type, quantity, order_id, created_at) VALUES
(UUID_TO_BIN(UUID()), @i_farinha, 'SAIDA_PEDIDO', 900, @o, DATE_ADD('2026-10-30 20:12:47', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_molho, 'SAIDA_PEDIDO', 360, @o, DATE_ADD('2026-10-30 20:12:47', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_mussarela, 'SAIDA_PEDIDO', 600, @o, DATE_ADD('2026-10-30 20:12:47', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_calabresa, 'SAIDA_PEDIDO', 450, @o, DATE_ADD('2026-10-30 20:12:47', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_cebola, 'SAIDA_PEDIDO', 120, @o, DATE_ADD('2026-10-30 20:12:47', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_azeite, 'SAIDA_PEDIDO', 30, @o, DATE_ADD('2026-10-30 20:12:47', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_oregano, 'SAIDA_PEDIDO', 6, @o, DATE_ADD('2026-10-30 20:12:47', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_caixa, 'SAIDA_PEDIDO', 3, @o, DATE_ADD('2026-10-30 20:12:47', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_farinha, 'ESTORNO', 900, @o, DATE_ADD('2026-10-30 20:24:47', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_molho, 'ESTORNO', 360, @o, DATE_ADD('2026-10-30 20:24:47', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_mussarela, 'ESTORNO', 600, @o, DATE_ADD('2026-10-30 20:24:47', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_calabresa, 'ESTORNO', 450, @o, DATE_ADD('2026-10-30 20:24:47', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_cebola, 'ESTORNO', 120, @o, DATE_ADD('2026-10-30 20:24:47', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_azeite, 'ESTORNO', 30, @o, DATE_ADD('2026-10-30 20:24:47', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_oregano, 'ESTORNO', 6, @o, DATE_ADD('2026-10-30 20:24:47', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_caixa, 'ESTORNO', 3, @o, DATE_ADD('2026-10-30 20:24:47', INTERVAL @tz_offset HOUR));

-- #19 | 31/10 sáb 12:30 | João Mohammed Pendezza | 1x Pizza Calabresa | Dinheiro
SET @o = UUID_TO_BIN(UUID());
INSERT INTO `order` (id, subtotal, shipping_fee, total_cost, creation_date, order_status, payment_method_id, restaurant_id, customer_user_id, address_city_id, address_street, address_number, address_neighborhood, address_zip_code, update_date)
VALUES (@o, 52.00, 7.50, 59.50, DATE_ADD('2026-10-31 12:30:37', INTERVAL @tz_offset HOUR), 'DELIVERED', @pm_dinheiro, @rest, @u_jm, @city_rio, 'Rua Maria Quitéria', '85', 'Ipanema', '22410-040', UTC_TIMESTAMP());
INSERT INTO order_item (id, order_id, product_id, quantity, unit_price, total_price, note)
VALUES (UUID_TO_BIN(UUID()), @o, @p_C, 1, 52.00, 52.00, NULL);
INSERT INTO ingredient_movement (id, ingredient_id, type, quantity, order_id, created_at) VALUES
(UUID_TO_BIN(UUID()), @i_farinha, 'SAIDA_PEDIDO', 300, @o, DATE_ADD('2026-10-31 12:30:37', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_molho, 'SAIDA_PEDIDO', 120, @o, DATE_ADD('2026-10-31 12:30:37', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_mussarela, 'SAIDA_PEDIDO', 200, @o, DATE_ADD('2026-10-31 12:30:37', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_calabresa, 'SAIDA_PEDIDO', 150, @o, DATE_ADD('2026-10-31 12:30:37', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_cebola, 'SAIDA_PEDIDO', 40, @o, DATE_ADD('2026-10-31 12:30:37', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_azeite, 'SAIDA_PEDIDO', 10, @o, DATE_ADD('2026-10-31 12:30:37', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_oregano, 'SAIDA_PEDIDO', 2, @o, DATE_ADD('2026-10-31 12:30:37', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_caixa, 'SAIDA_PEDIDO', 1, @o, DATE_ADD('2026-10-31 12:30:37', INTERVAL @tz_offset HOUR));

-- #20 | 31/10 sáb 19:16 | Ronaldo Pinto | 1x Pizza Calabresa | Pix
SET @o = UUID_TO_BIN(UUID());
INSERT INTO `order` (id, subtotal, shipping_fee, total_cost, creation_date, order_status, payment_method_id, restaurant_id, customer_user_id, address_city_id, address_street, address_number, address_neighborhood, address_zip_code, update_date)
VALUES (@o, 52.00, 7.50, 59.50, DATE_ADD('2026-10-31 19:16:22', INTERVAL @tz_offset HOUR), 'DELIVERED', @pm_pix, @rest, @u_rp, @city_rio, 'Rua Teixeira de Melo', '31', 'Ipanema', '22410-010', UTC_TIMESTAMP());
INSERT INTO order_item (id, order_id, product_id, quantity, unit_price, total_price, note)
VALUES (UUID_TO_BIN(UUID()), @o, @p_C, 1, 52.00, 52.00, NULL);
INSERT INTO ingredient_movement (id, ingredient_id, type, quantity, order_id, created_at) VALUES
(UUID_TO_BIN(UUID()), @i_farinha, 'SAIDA_PEDIDO', 300, @o, DATE_ADD('2026-10-31 19:16:22', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_molho, 'SAIDA_PEDIDO', 120, @o, DATE_ADD('2026-10-31 19:16:22', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_mussarela, 'SAIDA_PEDIDO', 200, @o, DATE_ADD('2026-10-31 19:16:22', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_calabresa, 'SAIDA_PEDIDO', 150, @o, DATE_ADD('2026-10-31 19:16:22', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_cebola, 'SAIDA_PEDIDO', 40, @o, DATE_ADD('2026-10-31 19:16:22', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_azeite, 'SAIDA_PEDIDO', 10, @o, DATE_ADD('2026-10-31 19:16:22', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_oregano, 'SAIDA_PEDIDO', 2, @o, DATE_ADD('2026-10-31 19:16:22', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_caixa, 'SAIDA_PEDIDO', 1, @o, DATE_ADD('2026-10-31 19:16:22', INTERVAL @tz_offset HOUR));

-- #21 | 31/10 sáb 19:56 | Sebastião Martins | 1x Pizza Calabresa | Pix
SET @o = UUID_TO_BIN(UUID());
INSERT INTO `order` (id, subtotal, shipping_fee, total_cost, creation_date, order_status, payment_method_id, restaurant_id, customer_user_id, address_city_id, address_street, address_number, address_neighborhood, address_zip_code, update_date)
VALUES (@o, 52.00, 7.50, 59.50, DATE_ADD('2026-10-31 19:56:14', INTERVAL @tz_offset HOUR), 'DELIVERED', @pm_pix, @rest, @u_sm, @city_rio, 'Rua Vinícius de Moraes', '129', 'Ipanema', '22411-010', UTC_TIMESTAMP());
INSERT INTO order_item (id, order_id, product_id, quantity, unit_price, total_price, note)
VALUES (UUID_TO_BIN(UUID()), @o, @p_C, 1, 52.00, 52.00, 'Bem assada');
INSERT INTO ingredient_movement (id, ingredient_id, type, quantity, order_id, created_at) VALUES
(UUID_TO_BIN(UUID()), @i_farinha, 'SAIDA_PEDIDO', 300, @o, DATE_ADD('2026-10-31 19:56:14', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_molho, 'SAIDA_PEDIDO', 120, @o, DATE_ADD('2026-10-31 19:56:14', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_mussarela, 'SAIDA_PEDIDO', 200, @o, DATE_ADD('2026-10-31 19:56:14', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_calabresa, 'SAIDA_PEDIDO', 150, @o, DATE_ADD('2026-10-31 19:56:14', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_cebola, 'SAIDA_PEDIDO', 40, @o, DATE_ADD('2026-10-31 19:56:14', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_azeite, 'SAIDA_PEDIDO', 10, @o, DATE_ADD('2026-10-31 19:56:14', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_oregano, 'SAIDA_PEDIDO', 2, @o, DATE_ADD('2026-10-31 19:56:14', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_caixa, 'SAIDA_PEDIDO', 1, @o, DATE_ADD('2026-10-31 19:56:14', INTERVAL @tz_offset HOUR));

-- #22 | 31/10 sáb 20:12 | José Souza | 2x Pizza Calabresa | Dinheiro
SET @o = UUID_TO_BIN(UUID());
INSERT INTO `order` (id, subtotal, shipping_fee, total_cost, creation_date, order_status, payment_method_id, restaurant_id, customer_user_id, address_city_id, address_street, address_number, address_neighborhood, address_zip_code, update_date)
VALUES (@o, 104.00, 7.50, 111.50, DATE_ADD('2026-10-31 20:12:18', INTERVAL @tz_offset HOUR), 'DELIVERED', @pm_dinheiro, @rest, @u_js, @city_rio, 'Rua Joana Angélica', '83', 'Ipanema', '22420-030', UTC_TIMESTAMP());
INSERT INTO order_item (id, order_id, product_id, quantity, unit_price, total_price, note)
VALUES (UUID_TO_BIN(UUID()), @o, @p_C, 2, 52.00, 104.00, NULL);
INSERT INTO ingredient_movement (id, ingredient_id, type, quantity, order_id, created_at) VALUES
(UUID_TO_BIN(UUID()), @i_farinha, 'SAIDA_PEDIDO', 600, @o, DATE_ADD('2026-10-31 20:12:18', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_molho, 'SAIDA_PEDIDO', 240, @o, DATE_ADD('2026-10-31 20:12:18', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_mussarela, 'SAIDA_PEDIDO', 400, @o, DATE_ADD('2026-10-31 20:12:18', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_calabresa, 'SAIDA_PEDIDO', 300, @o, DATE_ADD('2026-10-31 20:12:18', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_cebola, 'SAIDA_PEDIDO', 80, @o, DATE_ADD('2026-10-31 20:12:18', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_azeite, 'SAIDA_PEDIDO', 20, @o, DATE_ADD('2026-10-31 20:12:18', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_oregano, 'SAIDA_PEDIDO', 4, @o, DATE_ADD('2026-10-31 20:12:18', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_caixa, 'SAIDA_PEDIDO', 2, @o, DATE_ADD('2026-10-31 20:12:18', INTERVAL @tz_offset HOUR));

-- #23 | 31/10 sáb 20:28 | João da Silva | 1x Pizza Calabresa | Pix
SET @o = UUID_TO_BIN(UUID());
INSERT INTO `order` (id, subtotal, shipping_fee, total_cost, creation_date, order_status, payment_method_id, restaurant_id, customer_user_id, address_city_id, address_street, address_number, address_neighborhood, address_zip_code, update_date)
VALUES (@o, 52.00, 7.50, 59.50, DATE_ADD('2026-10-31 20:28:59', INTERVAL @tz_offset HOUR), 'DELIVERED', @pm_pix, @rest, @u_jd, @city_rio, 'Rua Barão de Jaguaripe', '280', 'Ipanema', '22421-000', UTC_TIMESTAMP());
INSERT INTO order_item (id, order_id, product_id, quantity, unit_price, total_price, note)
VALUES (UUID_TO_BIN(UUID()), @o, @p_C, 1, 52.00, 52.00, 'Cortar em 12 pedaços');
INSERT INTO ingredient_movement (id, ingredient_id, type, quantity, order_id, created_at) VALUES
(UUID_TO_BIN(UUID()), @i_farinha, 'SAIDA_PEDIDO', 300, @o, DATE_ADD('2026-10-31 20:28:59', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_molho, 'SAIDA_PEDIDO', 120, @o, DATE_ADD('2026-10-31 20:28:59', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_mussarela, 'SAIDA_PEDIDO', 200, @o, DATE_ADD('2026-10-31 20:28:59', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_calabresa, 'SAIDA_PEDIDO', 150, @o, DATE_ADD('2026-10-31 20:28:59', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_cebola, 'SAIDA_PEDIDO', 40, @o, DATE_ADD('2026-10-31 20:28:59', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_azeite, 'SAIDA_PEDIDO', 10, @o, DATE_ADD('2026-10-31 20:28:59', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_oregano, 'SAIDA_PEDIDO', 2, @o, DATE_ADD('2026-10-31 20:28:59', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_caixa, 'SAIDA_PEDIDO', 1, @o, DATE_ADD('2026-10-31 20:28:59', INTERVAL @tz_offset HOUR));

-- #24 | 31/10 sáb 22:16 | Maria Joaquina | 1x Pizza Quatro Queijos | Pix
SET @o = UUID_TO_BIN(UUID());
INSERT INTO `order` (id, subtotal, shipping_fee, total_cost, creation_date, order_status, payment_method_id, restaurant_id, customer_user_id, address_city_id, address_street, address_number, address_neighborhood, address_zip_code, update_date)
VALUES (@o, 56.00, 7.50, 63.50, DATE_ADD('2026-10-31 22:16:35', INTERVAL @tz_offset HOUR), 'DELIVERED', @pm_pix, @rest, @u_mj, @city_rio, 'Rua Farme de Amoedo', '34', 'Ipanema', '22420-020', UTC_TIMESTAMP());
INSERT INTO order_item (id, order_id, product_id, quantity, unit_price, total_price, note)
VALUES (UUID_TO_BIN(UUID()), @o, @p_Q, 1, 56.00, 56.00, 'Cortar em 12 pedaços');
INSERT INTO ingredient_movement (id, ingredient_id, type, quantity, order_id, created_at) VALUES
(UUID_TO_BIN(UUID()), @i_farinha, 'SAIDA_PEDIDO', 300, @o, DATE_ADD('2026-10-31 22:16:35', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_molho, 'SAIDA_PEDIDO', 120, @o, DATE_ADD('2026-10-31 22:16:35', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_mussarela, 'SAIDA_PEDIDO', 120, @o, DATE_ADD('2026-10-31 22:16:35', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_provolone, 'SAIDA_PEDIDO', 70, @o, DATE_ADD('2026-10-31 22:16:35', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_gorgonzola, 'SAIDA_PEDIDO', 60, @o, DATE_ADD('2026-10-31 22:16:35', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_parmesao, 'SAIDA_PEDIDO', 40, @o, DATE_ADD('2026-10-31 22:16:35', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_azeite, 'SAIDA_PEDIDO', 10, @o, DATE_ADD('2026-10-31 22:16:35', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_oregano, 'SAIDA_PEDIDO', 2, @o, DATE_ADD('2026-10-31 22:16:35', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_caixa, 'SAIDA_PEDIDO', 1, @o, DATE_ADD('2026-10-31 22:16:35', INTERVAL @tz_offset HOUR));

-- #25 | 01/11 dom 18:05 | João Mohammed Pendezza | 2x Pizza Quatro Queijos | Dinheiro | CANCELADO (estorno)
SET @o = UUID_TO_BIN(UUID());
INSERT INTO `order` (id, subtotal, shipping_fee, total_cost, creation_date, order_status, payment_method_id, restaurant_id, customer_user_id, address_city_id, address_street, address_number, address_neighborhood, address_zip_code, update_date)
VALUES (@o, 112.00, 7.50, 119.50, DATE_ADD('2026-11-01 18:05:07', INTERVAL @tz_offset HOUR), 'CANCELED', @pm_dinheiro, @rest, @u_jm, @city_rio, 'Rua Maria Quitéria', '85', 'Ipanema', '22410-040', UTC_TIMESTAMP());
INSERT INTO order_item (id, order_id, product_id, quantity, unit_price, total_price, note)
VALUES (UUID_TO_BIN(UUID()), @o, @p_Q, 2, 56.00, 112.00, NULL);
INSERT INTO ingredient_movement (id, ingredient_id, type, quantity, order_id, created_at) VALUES
(UUID_TO_BIN(UUID()), @i_farinha, 'SAIDA_PEDIDO', 600, @o, DATE_ADD('2026-11-01 18:05:07', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_molho, 'SAIDA_PEDIDO', 240, @o, DATE_ADD('2026-11-01 18:05:07', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_mussarela, 'SAIDA_PEDIDO', 240, @o, DATE_ADD('2026-11-01 18:05:07', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_provolone, 'SAIDA_PEDIDO', 140, @o, DATE_ADD('2026-11-01 18:05:07', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_gorgonzola, 'SAIDA_PEDIDO', 120, @o, DATE_ADD('2026-11-01 18:05:07', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_parmesao, 'SAIDA_PEDIDO', 80, @o, DATE_ADD('2026-11-01 18:05:07', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_azeite, 'SAIDA_PEDIDO', 20, @o, DATE_ADD('2026-11-01 18:05:07', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_oregano, 'SAIDA_PEDIDO', 4, @o, DATE_ADD('2026-11-01 18:05:07', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_caixa, 'SAIDA_PEDIDO', 2, @o, DATE_ADD('2026-11-01 18:05:07', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_farinha, 'ESTORNO', 600, @o, DATE_ADD('2026-11-01 18:17:07', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_molho, 'ESTORNO', 240, @o, DATE_ADD('2026-11-01 18:17:07', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_mussarela, 'ESTORNO', 240, @o, DATE_ADD('2026-11-01 18:17:07', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_provolone, 'ESTORNO', 140, @o, DATE_ADD('2026-11-01 18:17:07', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_gorgonzola, 'ESTORNO', 120, @o, DATE_ADD('2026-11-01 18:17:07', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_parmesao, 'ESTORNO', 80, @o, DATE_ADD('2026-11-01 18:17:07', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_azeite, 'ESTORNO', 20, @o, DATE_ADD('2026-11-01 18:17:07', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_oregano, 'ESTORNO', 4, @o, DATE_ADD('2026-11-01 18:17:07', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_caixa, 'ESTORNO', 2, @o, DATE_ADD('2026-11-01 18:17:07', INTERVAL @tz_offset HOUR));

-- #26 | 03/11 ter 21:25 | Ronaldo Pinto | 2x Pizza Quatro Queijos | Dinheiro
SET @o = UUID_TO_BIN(UUID());
INSERT INTO `order` (id, subtotal, shipping_fee, total_cost, creation_date, order_status, payment_method_id, restaurant_id, customer_user_id, address_city_id, address_street, address_number, address_neighborhood, address_zip_code, update_date)
VALUES (@o, 112.00, 7.50, 119.50, DATE_ADD('2026-11-03 21:25:58', INTERVAL @tz_offset HOUR), 'DELIVERED', @pm_dinheiro, @rest, @u_rp, @city_rio, 'Rua Teixeira de Melo', '31', 'Ipanema', '22410-010', UTC_TIMESTAMP());
INSERT INTO order_item (id, order_id, product_id, quantity, unit_price, total_price, note)
VALUES (UUID_TO_BIN(UUID()), @o, @p_Q, 2, 56.00, 112.00, NULL);
INSERT INTO ingredient_movement (id, ingredient_id, type, quantity, order_id, created_at) VALUES
(UUID_TO_BIN(UUID()), @i_farinha, 'SAIDA_PEDIDO', 600, @o, DATE_ADD('2026-11-03 21:25:58', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_molho, 'SAIDA_PEDIDO', 240, @o, DATE_ADD('2026-11-03 21:25:58', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_mussarela, 'SAIDA_PEDIDO', 240, @o, DATE_ADD('2026-11-03 21:25:58', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_provolone, 'SAIDA_PEDIDO', 140, @o, DATE_ADD('2026-11-03 21:25:58', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_gorgonzola, 'SAIDA_PEDIDO', 120, @o, DATE_ADD('2026-11-03 21:25:58', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_parmesao, 'SAIDA_PEDIDO', 80, @o, DATE_ADD('2026-11-03 21:25:58', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_azeite, 'SAIDA_PEDIDO', 20, @o, DATE_ADD('2026-11-03 21:25:58', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_oregano, 'SAIDA_PEDIDO', 4, @o, DATE_ADD('2026-11-03 21:25:58', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_caixa, 'SAIDA_PEDIDO', 2, @o, DATE_ADD('2026-11-03 21:25:58', INTERVAL @tz_offset HOUR));

-- #27 | 04/11 qua 20:42 | Sebastião Martins | 3x Pizza Quatro Queijos | Pix
SET @o = UUID_TO_BIN(UUID());
INSERT INTO `order` (id, subtotal, shipping_fee, total_cost, creation_date, order_status, payment_method_id, restaurant_id, customer_user_id, address_city_id, address_street, address_number, address_neighborhood, address_zip_code, update_date)
VALUES (@o, 168.00, 7.50, 175.50, DATE_ADD('2026-11-04 20:42:06', INTERVAL @tz_offset HOUR), 'DELIVERED', @pm_pix, @rest, @u_sm, @city_rio, 'Rua Vinícius de Moraes', '129', 'Ipanema', '22411-010', UTC_TIMESTAMP());
INSERT INTO order_item (id, order_id, product_id, quantity, unit_price, total_price, note)
VALUES (UUID_TO_BIN(UUID()), @o, @p_Q, 3, 56.00, 168.00, 'Bem assada');
INSERT INTO ingredient_movement (id, ingredient_id, type, quantity, order_id, created_at) VALUES
(UUID_TO_BIN(UUID()), @i_farinha, 'SAIDA_PEDIDO', 900, @o, DATE_ADD('2026-11-04 20:42:06', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_molho, 'SAIDA_PEDIDO', 360, @o, DATE_ADD('2026-11-04 20:42:06', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_mussarela, 'SAIDA_PEDIDO', 360, @o, DATE_ADD('2026-11-04 20:42:06', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_provolone, 'SAIDA_PEDIDO', 210, @o, DATE_ADD('2026-11-04 20:42:06', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_gorgonzola, 'SAIDA_PEDIDO', 180, @o, DATE_ADD('2026-11-04 20:42:06', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_parmesao, 'SAIDA_PEDIDO', 120, @o, DATE_ADD('2026-11-04 20:42:06', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_azeite, 'SAIDA_PEDIDO', 30, @o, DATE_ADD('2026-11-04 20:42:06', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_oregano, 'SAIDA_PEDIDO', 6, @o, DATE_ADD('2026-11-04 20:42:06', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_caixa, 'SAIDA_PEDIDO', 3, @o, DATE_ADD('2026-11-04 20:42:06', INTERVAL @tz_offset HOUR));

-- #28 | 04/11 qua 22:24 | José Souza | 3x Pizza Calabresa | Pix | CANCELADO (estorno)
SET @o = UUID_TO_BIN(UUID());
INSERT INTO `order` (id, subtotal, shipping_fee, total_cost, creation_date, order_status, payment_method_id, restaurant_id, customer_user_id, address_city_id, address_street, address_number, address_neighborhood, address_zip_code, update_date)
VALUES (@o, 156.00, 7.50, 163.50, DATE_ADD('2026-11-04 22:24:56', INTERVAL @tz_offset HOUR), 'CANCELED', @pm_pix, @rest, @u_js, @city_rio, 'Rua Joana Angélica', '83', 'Ipanema', '22420-030', UTC_TIMESTAMP());
INSERT INTO order_item (id, order_id, product_id, quantity, unit_price, total_price, note)
VALUES (UUID_TO_BIN(UUID()), @o, @p_C, 3, 52.00, 156.00, NULL);
INSERT INTO ingredient_movement (id, ingredient_id, type, quantity, order_id, created_at) VALUES
(UUID_TO_BIN(UUID()), @i_farinha, 'SAIDA_PEDIDO', 900, @o, DATE_ADD('2026-11-04 22:24:56', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_molho, 'SAIDA_PEDIDO', 360, @o, DATE_ADD('2026-11-04 22:24:56', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_mussarela, 'SAIDA_PEDIDO', 600, @o, DATE_ADD('2026-11-04 22:24:56', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_calabresa, 'SAIDA_PEDIDO', 450, @o, DATE_ADD('2026-11-04 22:24:56', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_cebola, 'SAIDA_PEDIDO', 120, @o, DATE_ADD('2026-11-04 22:24:56', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_azeite, 'SAIDA_PEDIDO', 30, @o, DATE_ADD('2026-11-04 22:24:56', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_oregano, 'SAIDA_PEDIDO', 6, @o, DATE_ADD('2026-11-04 22:24:56', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_caixa, 'SAIDA_PEDIDO', 3, @o, DATE_ADD('2026-11-04 22:24:56', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_farinha, 'ESTORNO', 900, @o, DATE_ADD('2026-11-04 22:36:56', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_molho, 'ESTORNO', 360, @o, DATE_ADD('2026-11-04 22:36:56', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_mussarela, 'ESTORNO', 600, @o, DATE_ADD('2026-11-04 22:36:56', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_calabresa, 'ESTORNO', 450, @o, DATE_ADD('2026-11-04 22:36:56', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_cebola, 'ESTORNO', 120, @o, DATE_ADD('2026-11-04 22:36:56', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_azeite, 'ESTORNO', 30, @o, DATE_ADD('2026-11-04 22:36:56', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_oregano, 'ESTORNO', 6, @o, DATE_ADD('2026-11-04 22:36:56', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_caixa, 'ESTORNO', 3, @o, DATE_ADD('2026-11-04 22:36:56', INTERVAL @tz_offset HOUR));

-- #29 | 05/11 qui 12:41 | João da Silva | 1x Pizza Quatro Queijos | Dinheiro
SET @o = UUID_TO_BIN(UUID());
INSERT INTO `order` (id, subtotal, shipping_fee, total_cost, creation_date, order_status, payment_method_id, restaurant_id, customer_user_id, address_city_id, address_street, address_number, address_neighborhood, address_zip_code, update_date)
VALUES (@o, 56.00, 7.50, 63.50, DATE_ADD('2026-11-05 12:41:56', INTERVAL @tz_offset HOUR), 'DELIVERED', @pm_dinheiro, @rest, @u_jd, @city_rio, 'Rua Barão de Jaguaripe', '280', 'Ipanema', '22421-000', UTC_TIMESTAMP());
INSERT INTO order_item (id, order_id, product_id, quantity, unit_price, total_price, note)
VALUES (UUID_TO_BIN(UUID()), @o, @p_Q, 1, 56.00, 56.00, NULL);
INSERT INTO ingredient_movement (id, ingredient_id, type, quantity, order_id, created_at) VALUES
(UUID_TO_BIN(UUID()), @i_farinha, 'SAIDA_PEDIDO', 300, @o, DATE_ADD('2026-11-05 12:41:56', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_molho, 'SAIDA_PEDIDO', 120, @o, DATE_ADD('2026-11-05 12:41:56', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_mussarela, 'SAIDA_PEDIDO', 120, @o, DATE_ADD('2026-11-05 12:41:56', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_provolone, 'SAIDA_PEDIDO', 70, @o, DATE_ADD('2026-11-05 12:41:56', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_gorgonzola, 'SAIDA_PEDIDO', 60, @o, DATE_ADD('2026-11-05 12:41:56', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_parmesao, 'SAIDA_PEDIDO', 40, @o, DATE_ADD('2026-11-05 12:41:56', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_azeite, 'SAIDA_PEDIDO', 10, @o, DATE_ADD('2026-11-05 12:41:56', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_oregano, 'SAIDA_PEDIDO', 2, @o, DATE_ADD('2026-11-05 12:41:56', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_caixa, 'SAIDA_PEDIDO', 1, @o, DATE_ADD('2026-11-05 12:41:56', INTERVAL @tz_offset HOUR));

-- #30 | 05/11 qui 20:05 | Maria Joaquina | 1x Pizza Quatro Queijos | Pix
SET @o = UUID_TO_BIN(UUID());
INSERT INTO `order` (id, subtotal, shipping_fee, total_cost, creation_date, order_status, payment_method_id, restaurant_id, customer_user_id, address_city_id, address_street, address_number, address_neighborhood, address_zip_code, update_date)
VALUES (@o, 56.00, 7.50, 63.50, DATE_ADD('2026-11-05 20:05:25', INTERVAL @tz_offset HOUR), 'DELIVERED', @pm_pix, @rest, @u_mj, @city_rio, 'Rua Farme de Amoedo', '34', 'Ipanema', '22420-020', UTC_TIMESTAMP());
INSERT INTO order_item (id, order_id, product_id, quantity, unit_price, total_price, note)
VALUES (UUID_TO_BIN(UUID()), @o, @p_Q, 1, 56.00, 56.00, 'Cortar em 12 pedaços');
INSERT INTO ingredient_movement (id, ingredient_id, type, quantity, order_id, created_at) VALUES
(UUID_TO_BIN(UUID()), @i_farinha, 'SAIDA_PEDIDO', 300, @o, DATE_ADD('2026-11-05 20:05:25', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_molho, 'SAIDA_PEDIDO', 120, @o, DATE_ADD('2026-11-05 20:05:25', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_mussarela, 'SAIDA_PEDIDO', 120, @o, DATE_ADD('2026-11-05 20:05:25', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_provolone, 'SAIDA_PEDIDO', 70, @o, DATE_ADD('2026-11-05 20:05:25', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_gorgonzola, 'SAIDA_PEDIDO', 60, @o, DATE_ADD('2026-11-05 20:05:25', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_parmesao, 'SAIDA_PEDIDO', 40, @o, DATE_ADD('2026-11-05 20:05:25', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_azeite, 'SAIDA_PEDIDO', 10, @o, DATE_ADD('2026-11-05 20:05:25', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_oregano, 'SAIDA_PEDIDO', 2, @o, DATE_ADD('2026-11-05 20:05:25', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_caixa, 'SAIDA_PEDIDO', 1, @o, DATE_ADD('2026-11-05 20:05:25', INTERVAL @tz_offset HOUR));

-- #31 | 05/11 qui 20:58 | João Mohammed Pendezza | 2x Pizza Quatro Queijos | Pix
SET @o = UUID_TO_BIN(UUID());
INSERT INTO `order` (id, subtotal, shipping_fee, total_cost, creation_date, order_status, payment_method_id, restaurant_id, customer_user_id, address_city_id, address_street, address_number, address_neighborhood, address_zip_code, update_date)
VALUES (@o, 112.00, 7.50, 119.50, DATE_ADD('2026-11-05 20:58:02', INTERVAL @tz_offset HOUR), 'DELIVERED', @pm_pix, @rest, @u_jm, @city_rio, 'Rua Aníbal de Mendonça', '112', 'Ipanema', '22410-050', UTC_TIMESTAMP());
INSERT INTO order_item (id, order_id, product_id, quantity, unit_price, total_price, note)
VALUES (UUID_TO_BIN(UUID()), @o, @p_Q, 2, 56.00, 112.00, 'Bem assada');
INSERT INTO ingredient_movement (id, ingredient_id, type, quantity, order_id, created_at) VALUES
(UUID_TO_BIN(UUID()), @i_farinha, 'SAIDA_PEDIDO', 600, @o, DATE_ADD('2026-11-05 20:58:02', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_molho, 'SAIDA_PEDIDO', 240, @o, DATE_ADD('2026-11-05 20:58:02', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_mussarela, 'SAIDA_PEDIDO', 240, @o, DATE_ADD('2026-11-05 20:58:02', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_provolone, 'SAIDA_PEDIDO', 140, @o, DATE_ADD('2026-11-05 20:58:02', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_gorgonzola, 'SAIDA_PEDIDO', 120, @o, DATE_ADD('2026-11-05 20:58:02', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_parmesao, 'SAIDA_PEDIDO', 80, @o, DATE_ADD('2026-11-05 20:58:02', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_azeite, 'SAIDA_PEDIDO', 20, @o, DATE_ADD('2026-11-05 20:58:02', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_oregano, 'SAIDA_PEDIDO', 4, @o, DATE_ADD('2026-11-05 20:58:02', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_caixa, 'SAIDA_PEDIDO', 2, @o, DATE_ADD('2026-11-05 20:58:02', INTERVAL @tz_offset HOUR));

-- #32 | 05/11 qui 21:43 | Ronaldo Pinto | 1x Pizza Calabresa | Dinheiro
SET @o = UUID_TO_BIN(UUID());
INSERT INTO `order` (id, subtotal, shipping_fee, total_cost, creation_date, order_status, payment_method_id, restaurant_id, customer_user_id, address_city_id, address_street, address_number, address_neighborhood, address_zip_code, update_date)
VALUES (@o, 52.00, 7.50, 59.50, DATE_ADD('2026-11-05 21:43:30', INTERVAL @tz_offset HOUR), 'DELIVERED', @pm_dinheiro, @rest, @u_rp, @city_rio, 'Rua Teixeira de Melo', '31', 'Ipanema', '22410-010', UTC_TIMESTAMP());
INSERT INTO order_item (id, order_id, product_id, quantity, unit_price, total_price, note)
VALUES (UUID_TO_BIN(UUID()), @o, @p_C, 1, 52.00, 52.00, NULL);
INSERT INTO ingredient_movement (id, ingredient_id, type, quantity, order_id, created_at) VALUES
(UUID_TO_BIN(UUID()), @i_farinha, 'SAIDA_PEDIDO', 300, @o, DATE_ADD('2026-11-05 21:43:30', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_molho, 'SAIDA_PEDIDO', 120, @o, DATE_ADD('2026-11-05 21:43:30', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_mussarela, 'SAIDA_PEDIDO', 200, @o, DATE_ADD('2026-11-05 21:43:30', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_calabresa, 'SAIDA_PEDIDO', 150, @o, DATE_ADD('2026-11-05 21:43:30', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_cebola, 'SAIDA_PEDIDO', 40, @o, DATE_ADD('2026-11-05 21:43:30', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_azeite, 'SAIDA_PEDIDO', 10, @o, DATE_ADD('2026-11-05 21:43:30', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_oregano, 'SAIDA_PEDIDO', 2, @o, DATE_ADD('2026-11-05 21:43:30', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_caixa, 'SAIDA_PEDIDO', 1, @o, DATE_ADD('2026-11-05 21:43:30', INTERVAL @tz_offset HOUR));

-- #33 | 05/11 qui 22:06 | Sebastião Martins | 1x Pizza Calabresa | Pix
SET @o = UUID_TO_BIN(UUID());
INSERT INTO `order` (id, subtotal, shipping_fee, total_cost, creation_date, order_status, payment_method_id, restaurant_id, customer_user_id, address_city_id, address_street, address_number, address_neighborhood, address_zip_code, update_date)
VALUES (@o, 52.00, 7.50, 59.50, DATE_ADD('2026-11-05 22:06:28', INTERVAL @tz_offset HOUR), 'DELIVERED', @pm_pix, @rest, @u_sm, @city_rio, 'Rua Vinícius de Moraes', '129', 'Ipanema', '22411-010', UTC_TIMESTAMP());
INSERT INTO order_item (id, order_id, product_id, quantity, unit_price, total_price, note)
VALUES (UUID_TO_BIN(UUID()), @o, @p_C, 1, 52.00, 52.00, NULL);
INSERT INTO ingredient_movement (id, ingredient_id, type, quantity, order_id, created_at) VALUES
(UUID_TO_BIN(UUID()), @i_farinha, 'SAIDA_PEDIDO', 300, @o, DATE_ADD('2026-11-05 22:06:28', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_molho, 'SAIDA_PEDIDO', 120, @o, DATE_ADD('2026-11-05 22:06:28', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_mussarela, 'SAIDA_PEDIDO', 200, @o, DATE_ADD('2026-11-05 22:06:28', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_calabresa, 'SAIDA_PEDIDO', 150, @o, DATE_ADD('2026-11-05 22:06:28', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_cebola, 'SAIDA_PEDIDO', 40, @o, DATE_ADD('2026-11-05 22:06:28', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_azeite, 'SAIDA_PEDIDO', 10, @o, DATE_ADD('2026-11-05 22:06:28', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_oregano, 'SAIDA_PEDIDO', 2, @o, DATE_ADD('2026-11-05 22:06:28', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_caixa, 'SAIDA_PEDIDO', 1, @o, DATE_ADD('2026-11-05 22:06:28', INTERVAL @tz_offset HOUR));

-- #34 | 06/11 sex 20:26 | José Souza | 2x Pizza Calabresa | Dinheiro
SET @o = UUID_TO_BIN(UUID());
INSERT INTO `order` (id, subtotal, shipping_fee, total_cost, creation_date, order_status, payment_method_id, restaurant_id, customer_user_id, address_city_id, address_street, address_number, address_neighborhood, address_zip_code, update_date)
VALUES (@o, 104.00, 7.50, 111.50, DATE_ADD('2026-11-06 20:26:50', INTERVAL @tz_offset HOUR), 'DELIVERED', @pm_dinheiro, @rest, @u_js, @city_rio, 'Rua Joana Angélica', '83', 'Ipanema', '22420-030', UTC_TIMESTAMP());
INSERT INTO order_item (id, order_id, product_id, quantity, unit_price, total_price, note)
VALUES (UUID_TO_BIN(UUID()), @o, @p_C, 2, 52.00, 104.00, NULL);
INSERT INTO ingredient_movement (id, ingredient_id, type, quantity, order_id, created_at) VALUES
(UUID_TO_BIN(UUID()), @i_farinha, 'SAIDA_PEDIDO', 600, @o, DATE_ADD('2026-11-06 20:26:50', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_molho, 'SAIDA_PEDIDO', 240, @o, DATE_ADD('2026-11-06 20:26:50', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_mussarela, 'SAIDA_PEDIDO', 400, @o, DATE_ADD('2026-11-06 20:26:50', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_calabresa, 'SAIDA_PEDIDO', 300, @o, DATE_ADD('2026-11-06 20:26:50', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_cebola, 'SAIDA_PEDIDO', 80, @o, DATE_ADD('2026-11-06 20:26:50', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_azeite, 'SAIDA_PEDIDO', 20, @o, DATE_ADD('2026-11-06 20:26:50', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_oregano, 'SAIDA_PEDIDO', 4, @o, DATE_ADD('2026-11-06 20:26:50', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_caixa, 'SAIDA_PEDIDO', 2, @o, DATE_ADD('2026-11-06 20:26:50', INTERVAL @tz_offset HOUR));

-- #35 | 06/11 sex 21:27 | João da Silva | 1x Pizza Quatro Queijos | Cartão de Débito
SET @o = UUID_TO_BIN(UUID());
INSERT INTO `order` (id, subtotal, shipping_fee, total_cost, creation_date, order_status, payment_method_id, restaurant_id, customer_user_id, address_city_id, address_street, address_number, address_neighborhood, address_zip_code, update_date)
VALUES (@o, 56.00, 7.50, 63.50, DATE_ADD('2026-11-06 21:27:32', INTERVAL @tz_offset HOUR), 'DELIVERED', @pm_debito, @rest, @u_jd, @city_rio, 'Rua Barão de Jaguaripe', '280', 'Ipanema', '22421-000', UTC_TIMESTAMP());
INSERT INTO order_item (id, order_id, product_id, quantity, unit_price, total_price, note)
VALUES (UUID_TO_BIN(UUID()), @o, @p_Q, 1, 56.00, 56.00, NULL);
INSERT INTO ingredient_movement (id, ingredient_id, type, quantity, order_id, created_at) VALUES
(UUID_TO_BIN(UUID()), @i_farinha, 'SAIDA_PEDIDO', 300, @o, DATE_ADD('2026-11-06 21:27:32', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_molho, 'SAIDA_PEDIDO', 120, @o, DATE_ADD('2026-11-06 21:27:32', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_mussarela, 'SAIDA_PEDIDO', 120, @o, DATE_ADD('2026-11-06 21:27:32', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_provolone, 'SAIDA_PEDIDO', 70, @o, DATE_ADD('2026-11-06 21:27:32', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_gorgonzola, 'SAIDA_PEDIDO', 60, @o, DATE_ADD('2026-11-06 21:27:32', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_parmesao, 'SAIDA_PEDIDO', 40, @o, DATE_ADD('2026-11-06 21:27:32', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_azeite, 'SAIDA_PEDIDO', 10, @o, DATE_ADD('2026-11-06 21:27:32', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_oregano, 'SAIDA_PEDIDO', 2, @o, DATE_ADD('2026-11-06 21:27:32', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_caixa, 'SAIDA_PEDIDO', 1, @o, DATE_ADD('2026-11-06 21:27:32', INTERVAL @tz_offset HOUR));

-- #36 | 06/11 sex 22:05 | Maria Joaquina | 1x Pizza Quatro Queijos | Pix
SET @o = UUID_TO_BIN(UUID());
INSERT INTO `order` (id, subtotal, shipping_fee, total_cost, creation_date, order_status, payment_method_id, restaurant_id, customer_user_id, address_city_id, address_street, address_number, address_neighborhood, address_zip_code, update_date)
VALUES (@o, 56.00, 7.50, 63.50, DATE_ADD('2026-11-06 22:05:00', INTERVAL @tz_offset HOUR), 'DELIVERED', @pm_pix, @rest, @u_mj, @city_rio, 'Rua Farme de Amoedo', '34', 'Ipanema', '22420-020', UTC_TIMESTAMP());
INSERT INTO order_item (id, order_id, product_id, quantity, unit_price, total_price, note)
VALUES (UUID_TO_BIN(UUID()), @o, @p_Q, 1, 56.00, 56.00, 'Bem assada');
INSERT INTO ingredient_movement (id, ingredient_id, type, quantity, order_id, created_at) VALUES
(UUID_TO_BIN(UUID()), @i_farinha, 'SAIDA_PEDIDO', 300, @o, DATE_ADD('2026-11-06 22:05:00', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_molho, 'SAIDA_PEDIDO', 120, @o, DATE_ADD('2026-11-06 22:05:00', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_mussarela, 'SAIDA_PEDIDO', 120, @o, DATE_ADD('2026-11-06 22:05:00', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_provolone, 'SAIDA_PEDIDO', 70, @o, DATE_ADD('2026-11-06 22:05:00', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_gorgonzola, 'SAIDA_PEDIDO', 60, @o, DATE_ADD('2026-11-06 22:05:00', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_parmesao, 'SAIDA_PEDIDO', 40, @o, DATE_ADD('2026-11-06 22:05:00', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_azeite, 'SAIDA_PEDIDO', 10, @o, DATE_ADD('2026-11-06 22:05:00', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_oregano, 'SAIDA_PEDIDO', 2, @o, DATE_ADD('2026-11-06 22:05:00', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_caixa, 'SAIDA_PEDIDO', 1, @o, DATE_ADD('2026-11-06 22:05:00', INTERVAL @tz_offset HOUR));

-- #37 | 07/11 sáb 13:20 | João Mohammed Pendezza | 2x Pizza Calabresa | Pix
SET @o = UUID_TO_BIN(UUID());
INSERT INTO `order` (id, subtotal, shipping_fee, total_cost, creation_date, order_status, payment_method_id, restaurant_id, customer_user_id, address_city_id, address_street, address_number, address_neighborhood, address_zip_code, update_date)
VALUES (@o, 104.00, 7.50, 111.50, DATE_ADD('2026-11-07 13:20:43', INTERVAL @tz_offset HOUR), 'DELIVERED', @pm_pix, @rest, @u_jm, @city_rio, 'Rua Maria Quitéria', '85', 'Ipanema', '22410-040', UTC_TIMESTAMP());
INSERT INTO order_item (id, order_id, product_id, quantity, unit_price, total_price, note)
VALUES (UUID_TO_BIN(UUID()), @o, @p_C, 2, 52.00, 104.00, NULL);
INSERT INTO ingredient_movement (id, ingredient_id, type, quantity, order_id, created_at) VALUES
(UUID_TO_BIN(UUID()), @i_farinha, 'SAIDA_PEDIDO', 600, @o, DATE_ADD('2026-11-07 13:20:43', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_molho, 'SAIDA_PEDIDO', 240, @o, DATE_ADD('2026-11-07 13:20:43', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_mussarela, 'SAIDA_PEDIDO', 400, @o, DATE_ADD('2026-11-07 13:20:43', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_calabresa, 'SAIDA_PEDIDO', 300, @o, DATE_ADD('2026-11-07 13:20:43', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_cebola, 'SAIDA_PEDIDO', 80, @o, DATE_ADD('2026-11-07 13:20:43', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_azeite, 'SAIDA_PEDIDO', 20, @o, DATE_ADD('2026-11-07 13:20:43', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_oregano, 'SAIDA_PEDIDO', 4, @o, DATE_ADD('2026-11-07 13:20:43', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_caixa, 'SAIDA_PEDIDO', 2, @o, DATE_ADD('2026-11-07 13:20:43', INTERVAL @tz_offset HOUR));

-- #38 | 07/11 sáb 20:32 | Ronaldo Pinto | 2x Pizza Quatro Queijos | Dinheiro
SET @o = UUID_TO_BIN(UUID());
INSERT INTO `order` (id, subtotal, shipping_fee, total_cost, creation_date, order_status, payment_method_id, restaurant_id, customer_user_id, address_city_id, address_street, address_number, address_neighborhood, address_zip_code, update_date)
VALUES (@o, 112.00, 7.50, 119.50, DATE_ADD('2026-11-07 20:32:49', INTERVAL @tz_offset HOUR), 'DELIVERED', @pm_dinheiro, @rest, @u_rp, @city_rio, 'Rua Teixeira de Melo', '31', 'Ipanema', '22410-010', UTC_TIMESTAMP());
INSERT INTO order_item (id, order_id, product_id, quantity, unit_price, total_price, note)
VALUES (UUID_TO_BIN(UUID()), @o, @p_Q, 2, 56.00, 112.00, 'Bem assada');
INSERT INTO ingredient_movement (id, ingredient_id, type, quantity, order_id, created_at) VALUES
(UUID_TO_BIN(UUID()), @i_farinha, 'SAIDA_PEDIDO', 600, @o, DATE_ADD('2026-11-07 20:32:49', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_molho, 'SAIDA_PEDIDO', 240, @o, DATE_ADD('2026-11-07 20:32:49', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_mussarela, 'SAIDA_PEDIDO', 240, @o, DATE_ADD('2026-11-07 20:32:49', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_provolone, 'SAIDA_PEDIDO', 140, @o, DATE_ADD('2026-11-07 20:32:49', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_gorgonzola, 'SAIDA_PEDIDO', 120, @o, DATE_ADD('2026-11-07 20:32:49', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_parmesao, 'SAIDA_PEDIDO', 80, @o, DATE_ADD('2026-11-07 20:32:49', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_azeite, 'SAIDA_PEDIDO', 20, @o, DATE_ADD('2026-11-07 20:32:49', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_oregano, 'SAIDA_PEDIDO', 4, @o, DATE_ADD('2026-11-07 20:32:49', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_caixa, 'SAIDA_PEDIDO', 2, @o, DATE_ADD('2026-11-07 20:32:49', INTERVAL @tz_offset HOUR));

-- #39 | 08/11 dom 21:30 | Sebastião Martins | 1x Pizza Quatro Queijos | Pix
SET @o = UUID_TO_BIN(UUID());
INSERT INTO `order` (id, subtotal, shipping_fee, total_cost, creation_date, order_status, payment_method_id, restaurant_id, customer_user_id, address_city_id, address_street, address_number, address_neighborhood, address_zip_code, update_date)
VALUES (@o, 56.00, 7.50, 63.50, DATE_ADD('2026-11-08 21:30:04', INTERVAL @tz_offset HOUR), 'DELIVERED', @pm_pix, @rest, @u_sm, @city_rio, 'Rua Vinícius de Moraes', '129', 'Ipanema', '22411-010', UTC_TIMESTAMP());
INSERT INTO order_item (id, order_id, product_id, quantity, unit_price, total_price, note)
VALUES (UUID_TO_BIN(UUID()), @o, @p_Q, 1, 56.00, 56.00, NULL);
INSERT INTO ingredient_movement (id, ingredient_id, type, quantity, order_id, created_at) VALUES
(UUID_TO_BIN(UUID()), @i_farinha, 'SAIDA_PEDIDO', 300, @o, DATE_ADD('2026-11-08 21:30:04', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_molho, 'SAIDA_PEDIDO', 120, @o, DATE_ADD('2026-11-08 21:30:04', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_mussarela, 'SAIDA_PEDIDO', 120, @o, DATE_ADD('2026-11-08 21:30:04', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_provolone, 'SAIDA_PEDIDO', 70, @o, DATE_ADD('2026-11-08 21:30:04', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_gorgonzola, 'SAIDA_PEDIDO', 60, @o, DATE_ADD('2026-11-08 21:30:04', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_parmesao, 'SAIDA_PEDIDO', 40, @o, DATE_ADD('2026-11-08 21:30:04', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_azeite, 'SAIDA_PEDIDO', 10, @o, DATE_ADD('2026-11-08 21:30:04', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_oregano, 'SAIDA_PEDIDO', 2, @o, DATE_ADD('2026-11-08 21:30:04', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_caixa, 'SAIDA_PEDIDO', 1, @o, DATE_ADD('2026-11-08 21:30:04', INTERVAL @tz_offset HOUR));

-- #40 | 08/11 dom 21:47 | José Souza | 1x Pizza Calabresa | Cartão de Débito
SET @o = UUID_TO_BIN(UUID());
INSERT INTO `order` (id, subtotal, shipping_fee, total_cost, creation_date, order_status, payment_method_id, restaurant_id, customer_user_id, address_city_id, address_street, address_number, address_neighborhood, address_zip_code, update_date)
VALUES (@o, 52.00, 7.50, 59.50, DATE_ADD('2026-11-08 21:47:32', INTERVAL @tz_offset HOUR), 'DELIVERED', @pm_debito, @rest, @u_js, @city_rio, 'Rua Joana Angélica', '83', 'Ipanema', '22420-030', UTC_TIMESTAMP());
INSERT INTO order_item (id, order_id, product_id, quantity, unit_price, total_price, note)
VALUES (UUID_TO_BIN(UUID()), @o, @p_C, 1, 52.00, 52.00, 'Bem assada');
INSERT INTO ingredient_movement (id, ingredient_id, type, quantity, order_id, created_at) VALUES
(UUID_TO_BIN(UUID()), @i_farinha, 'SAIDA_PEDIDO', 300, @o, DATE_ADD('2026-11-08 21:47:32', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_molho, 'SAIDA_PEDIDO', 120, @o, DATE_ADD('2026-11-08 21:47:32', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_mussarela, 'SAIDA_PEDIDO', 200, @o, DATE_ADD('2026-11-08 21:47:32', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_calabresa, 'SAIDA_PEDIDO', 150, @o, DATE_ADD('2026-11-08 21:47:32', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_cebola, 'SAIDA_PEDIDO', 40, @o, DATE_ADD('2026-11-08 21:47:32', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_azeite, 'SAIDA_PEDIDO', 10, @o, DATE_ADD('2026-11-08 21:47:32', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_oregano, 'SAIDA_PEDIDO', 2, @o, DATE_ADD('2026-11-08 21:47:32', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_caixa, 'SAIDA_PEDIDO', 1, @o, DATE_ADD('2026-11-08 21:47:32', INTERVAL @tz_offset HOUR));

-- #41 | 11/10 dom 21:57 | João da Silva | 2x Pizza Quatro Queijos | Pix
SET @o = UUID_TO_BIN(UUID());
INSERT INTO `order` (id, subtotal, shipping_fee, total_cost, creation_date, order_status, payment_method_id, restaurant_id, customer_user_id, address_city_id, address_street, address_number, address_neighborhood, address_zip_code, update_date)
VALUES (@o, 112.00, 7.50, 119.50, DATE_ADD('2026-10-11 21:57:28', INTERVAL @tz_offset HOUR), 'DELIVERED', @pm_pix, @rest, @u_jd, @city_rio, 'Rua Barão de Jaguaripe', '280', 'Ipanema', '22421-000', UTC_TIMESTAMP());
INSERT INTO order_item (id, order_id, product_id, quantity, unit_price, total_price, note)
VALUES (UUID_TO_BIN(UUID()), @o, @p_Q, 2, 56.00, 112.00, NULL);
INSERT INTO ingredient_movement (id, ingredient_id, type, quantity, order_id, created_at) VALUES
(UUID_TO_BIN(UUID()), @i_farinha, 'SAIDA_PEDIDO', 600, @o, DATE_ADD('2026-10-11 21:57:28', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_molho, 'SAIDA_PEDIDO', 240, @o, DATE_ADD('2026-10-11 21:57:28', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_mussarela, 'SAIDA_PEDIDO', 240, @o, DATE_ADD('2026-10-11 21:57:28', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_provolone, 'SAIDA_PEDIDO', 140, @o, DATE_ADD('2026-10-11 21:57:28', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_gorgonzola, 'SAIDA_PEDIDO', 120, @o, DATE_ADD('2026-10-11 21:57:28', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_parmesao, 'SAIDA_PEDIDO', 80, @o, DATE_ADD('2026-10-11 21:57:28', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_azeite, 'SAIDA_PEDIDO', 20, @o, DATE_ADD('2026-10-11 21:57:28', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_oregano, 'SAIDA_PEDIDO', 4, @o, DATE_ADD('2026-10-11 21:57:28', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_caixa, 'SAIDA_PEDIDO', 2, @o, DATE_ADD('2026-10-11 21:57:28', INTERVAL @tz_offset HOUR));

-- #42 | 18/10 dom 22:15 | Maria Joaquina | 2x Pizza Quatro Queijos | Pix
SET @o = UUID_TO_BIN(UUID());
INSERT INTO `order` (id, subtotal, shipping_fee, total_cost, creation_date, order_status, payment_method_id, restaurant_id, customer_user_id, address_city_id, address_street, address_number, address_neighborhood, address_zip_code, update_date)
VALUES (@o, 112.00, 7.50, 119.50, DATE_ADD('2026-10-18 22:15:09', INTERVAL @tz_offset HOUR), 'DELIVERED', @pm_pix, @rest, @u_mj, @city_rio, 'Rua Farme de Amoedo', '34', 'Ipanema', '22420-020', UTC_TIMESTAMP());
INSERT INTO order_item (id, order_id, product_id, quantity, unit_price, total_price, note)
VALUES (UUID_TO_BIN(UUID()), @o, @p_Q, 2, 56.00, 112.00, NULL);
INSERT INTO ingredient_movement (id, ingredient_id, type, quantity, order_id, created_at) VALUES
(UUID_TO_BIN(UUID()), @i_farinha, 'SAIDA_PEDIDO', 600, @o, DATE_ADD('2026-10-18 22:15:09', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_molho, 'SAIDA_PEDIDO', 240, @o, DATE_ADD('2026-10-18 22:15:09', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_mussarela, 'SAIDA_PEDIDO', 240, @o, DATE_ADD('2026-10-18 22:15:09', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_provolone, 'SAIDA_PEDIDO', 140, @o, DATE_ADD('2026-10-18 22:15:09', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_gorgonzola, 'SAIDA_PEDIDO', 120, @o, DATE_ADD('2026-10-18 22:15:09', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_parmesao, 'SAIDA_PEDIDO', 80, @o, DATE_ADD('2026-10-18 22:15:09', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_azeite, 'SAIDA_PEDIDO', 20, @o, DATE_ADD('2026-10-18 22:15:09', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_oregano, 'SAIDA_PEDIDO', 4, @o, DATE_ADD('2026-10-18 22:15:09', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_caixa, 'SAIDA_PEDIDO', 2, @o, DATE_ADD('2026-10-18 22:15:09', INTERVAL @tz_offset HOUR));

-- #43 | 24/10 sáb 14:06 | João Mohammed Pendezza | 2x Pizza Quatro Queijos | Cartão de Débito
SET @o = UUID_TO_BIN(UUID());
INSERT INTO `order` (id, subtotal, shipping_fee, total_cost, creation_date, order_status, payment_method_id, restaurant_id, customer_user_id, address_city_id, address_street, address_number, address_neighborhood, address_zip_code, update_date)
VALUES (@o, 112.00, 7.50, 119.50, DATE_ADD('2026-10-24 14:06:17', INTERVAL @tz_offset HOUR), 'DELIVERED', @pm_debito, @rest, @u_jm, @city_rio, 'Rua Maria Quitéria', '85', 'Ipanema', '22410-040', UTC_TIMESTAMP());
INSERT INTO order_item (id, order_id, product_id, quantity, unit_price, total_price, note)
VALUES (UUID_TO_BIN(UUID()), @o, @p_Q, 2, 56.00, 112.00, 'Bem assada');
INSERT INTO ingredient_movement (id, ingredient_id, type, quantity, order_id, created_at) VALUES
(UUID_TO_BIN(UUID()), @i_farinha, 'SAIDA_PEDIDO', 600, @o, DATE_ADD('2026-10-24 14:06:17', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_molho, 'SAIDA_PEDIDO', 240, @o, DATE_ADD('2026-10-24 14:06:17', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_mussarela, 'SAIDA_PEDIDO', 240, @o, DATE_ADD('2026-10-24 14:06:17', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_provolone, 'SAIDA_PEDIDO', 140, @o, DATE_ADD('2026-10-24 14:06:17', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_gorgonzola, 'SAIDA_PEDIDO', 120, @o, DATE_ADD('2026-10-24 14:06:17', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_parmesao, 'SAIDA_PEDIDO', 80, @o, DATE_ADD('2026-10-24 14:06:17', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_azeite, 'SAIDA_PEDIDO', 20, @o, DATE_ADD('2026-10-24 14:06:17', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_oregano, 'SAIDA_PEDIDO', 4, @o, DATE_ADD('2026-10-24 14:06:17', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_caixa, 'SAIDA_PEDIDO', 2, @o, DATE_ADD('2026-10-24 14:06:17', INTERVAL @tz_offset HOUR));

-- #44 | 25/10 dom 20:14 | Ronaldo Pinto | 1x Pizza Calabresa | Dinheiro | CANCELADO (estorno)
SET @o = UUID_TO_BIN(UUID());
INSERT INTO `order` (id, subtotal, shipping_fee, total_cost, creation_date, order_status, payment_method_id, restaurant_id, customer_user_id, address_city_id, address_street, address_number, address_neighborhood, address_zip_code, update_date)
VALUES (@o, 52.00, 7.50, 59.50, DATE_ADD('2026-10-25 20:14:12', INTERVAL @tz_offset HOUR), 'CANCELED', @pm_dinheiro, @rest, @u_rp, @city_rio, 'Rua Teixeira de Melo', '31', 'Ipanema', '22410-010', UTC_TIMESTAMP());
INSERT INTO order_item (id, order_id, product_id, quantity, unit_price, total_price, note)
VALUES (UUID_TO_BIN(UUID()), @o, @p_C, 1, 52.00, 52.00, 'Cortar em 12 pedaços');
INSERT INTO ingredient_movement (id, ingredient_id, type, quantity, order_id, created_at) VALUES
(UUID_TO_BIN(UUID()), @i_farinha, 'SAIDA_PEDIDO', 300, @o, DATE_ADD('2026-10-25 20:14:12', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_molho, 'SAIDA_PEDIDO', 120, @o, DATE_ADD('2026-10-25 20:14:12', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_mussarela, 'SAIDA_PEDIDO', 200, @o, DATE_ADD('2026-10-25 20:14:12', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_calabresa, 'SAIDA_PEDIDO', 150, @o, DATE_ADD('2026-10-25 20:14:12', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_cebola, 'SAIDA_PEDIDO', 40, @o, DATE_ADD('2026-10-25 20:14:12', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_azeite, 'SAIDA_PEDIDO', 10, @o, DATE_ADD('2026-10-25 20:14:12', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_oregano, 'SAIDA_PEDIDO', 2, @o, DATE_ADD('2026-10-25 20:14:12', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_caixa, 'SAIDA_PEDIDO', 1, @o, DATE_ADD('2026-10-25 20:14:12', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_farinha, 'ESTORNO', 300, @o, DATE_ADD('2026-10-25 20:26:12', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_molho, 'ESTORNO', 120, @o, DATE_ADD('2026-10-25 20:26:12', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_mussarela, 'ESTORNO', 200, @o, DATE_ADD('2026-10-25 20:26:12', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_calabresa, 'ESTORNO', 150, @o, DATE_ADD('2026-10-25 20:26:12', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_cebola, 'ESTORNO', 40, @o, DATE_ADD('2026-10-25 20:26:12', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_azeite, 'ESTORNO', 10, @o, DATE_ADD('2026-10-25 20:26:12', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_oregano, 'ESTORNO', 2, @o, DATE_ADD('2026-10-25 20:26:12', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_caixa, 'ESTORNO', 1, @o, DATE_ADD('2026-10-25 20:26:12', INTERVAL @tz_offset HOUR));

-- #45 | 26/10 seg 18:17 | Sebastião Martins | 1x Pizza Calabresa | Pix
SET @o = UUID_TO_BIN(UUID());
INSERT INTO `order` (id, subtotal, shipping_fee, total_cost, creation_date, order_status, payment_method_id, restaurant_id, customer_user_id, address_city_id, address_street, address_number, address_neighborhood, address_zip_code, update_date)
VALUES (@o, 52.00, 7.50, 59.50, DATE_ADD('2026-10-26 18:17:01', INTERVAL @tz_offset HOUR), 'DELIVERED', @pm_pix, @rest, @u_sm, @city_rio, 'Rua Vinícius de Moraes', '129', 'Ipanema', '22411-010', UTC_TIMESTAMP());
INSERT INTO order_item (id, order_id, product_id, quantity, unit_price, total_price, note)
VALUES (UUID_TO_BIN(UUID()), @o, @p_C, 1, 52.00, 52.00, NULL);
INSERT INTO ingredient_movement (id, ingredient_id, type, quantity, order_id, created_at) VALUES
(UUID_TO_BIN(UUID()), @i_farinha, 'SAIDA_PEDIDO', 300, @o, DATE_ADD('2026-10-26 18:17:01', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_molho, 'SAIDA_PEDIDO', 120, @o, DATE_ADD('2026-10-26 18:17:01', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_mussarela, 'SAIDA_PEDIDO', 200, @o, DATE_ADD('2026-10-26 18:17:01', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_calabresa, 'SAIDA_PEDIDO', 150, @o, DATE_ADD('2026-10-26 18:17:01', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_cebola, 'SAIDA_PEDIDO', 40, @o, DATE_ADD('2026-10-26 18:17:01', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_azeite, 'SAIDA_PEDIDO', 10, @o, DATE_ADD('2026-10-26 18:17:01', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_oregano, 'SAIDA_PEDIDO', 2, @o, DATE_ADD('2026-10-26 18:17:01', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_caixa, 'SAIDA_PEDIDO', 1, @o, DATE_ADD('2026-10-26 18:17:01', INTERVAL @tz_offset HOUR));

-- #46 | 28/10 qua 19:51 | José Souza | 1x Pizza Calabresa | Cartão de Débito
SET @o = UUID_TO_BIN(UUID());
INSERT INTO `order` (id, subtotal, shipping_fee, total_cost, creation_date, order_status, payment_method_id, restaurant_id, customer_user_id, address_city_id, address_street, address_number, address_neighborhood, address_zip_code, update_date)
VALUES (@o, 52.00, 7.50, 59.50, DATE_ADD('2026-10-28 19:51:45', INTERVAL @tz_offset HOUR), 'DELIVERED', @pm_debito, @rest, @u_js, @city_rio, 'Rua Joana Angélica', '83', 'Ipanema', '22420-030', UTC_TIMESTAMP());
INSERT INTO order_item (id, order_id, product_id, quantity, unit_price, total_price, note)
VALUES (UUID_TO_BIN(UUID()), @o, @p_C, 1, 52.00, 52.00, NULL);
INSERT INTO ingredient_movement (id, ingredient_id, type, quantity, order_id, created_at) VALUES
(UUID_TO_BIN(UUID()), @i_farinha, 'SAIDA_PEDIDO', 300, @o, DATE_ADD('2026-10-28 19:51:45', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_molho, 'SAIDA_PEDIDO', 120, @o, DATE_ADD('2026-10-28 19:51:45', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_mussarela, 'SAIDA_PEDIDO', 200, @o, DATE_ADD('2026-10-28 19:51:45', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_calabresa, 'SAIDA_PEDIDO', 150, @o, DATE_ADD('2026-10-28 19:51:45', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_cebola, 'SAIDA_PEDIDO', 40, @o, DATE_ADD('2026-10-28 19:51:45', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_azeite, 'SAIDA_PEDIDO', 10, @o, DATE_ADD('2026-10-28 19:51:45', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_oregano, 'SAIDA_PEDIDO', 2, @o, DATE_ADD('2026-10-28 19:51:45', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_caixa, 'SAIDA_PEDIDO', 1, @o, DATE_ADD('2026-10-28 19:51:45', INTERVAL @tz_offset HOUR));

-- #47 | 01/11 dom 21:06 | João da Silva | 2x Pizza Calabresa | Dinheiro
SET @o = UUID_TO_BIN(UUID());
INSERT INTO `order` (id, subtotal, shipping_fee, total_cost, creation_date, order_status, payment_method_id, restaurant_id, customer_user_id, address_city_id, address_street, address_number, address_neighborhood, address_zip_code, update_date)
VALUES (@o, 104.00, 7.50, 111.50, DATE_ADD('2026-11-01 21:06:36', INTERVAL @tz_offset HOUR), 'DELIVERED', @pm_dinheiro, @rest, @u_jd, @city_rio, 'Rua Barão de Jaguaripe', '280', 'Ipanema', '22421-000', UTC_TIMESTAMP());
INSERT INTO order_item (id, order_id, product_id, quantity, unit_price, total_price, note)
VALUES (UUID_TO_BIN(UUID()), @o, @p_C, 2, 52.00, 104.00, 'Bem assada');
INSERT INTO ingredient_movement (id, ingredient_id, type, quantity, order_id, created_at) VALUES
(UUID_TO_BIN(UUID()), @i_farinha, 'SAIDA_PEDIDO', 600, @o, DATE_ADD('2026-11-01 21:06:36', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_molho, 'SAIDA_PEDIDO', 240, @o, DATE_ADD('2026-11-01 21:06:36', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_mussarela, 'SAIDA_PEDIDO', 400, @o, DATE_ADD('2026-11-01 21:06:36', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_calabresa, 'SAIDA_PEDIDO', 300, @o, DATE_ADD('2026-11-01 21:06:36', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_cebola, 'SAIDA_PEDIDO', 80, @o, DATE_ADD('2026-11-01 21:06:36', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_azeite, 'SAIDA_PEDIDO', 20, @o, DATE_ADD('2026-11-01 21:06:36', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_oregano, 'SAIDA_PEDIDO', 4, @o, DATE_ADD('2026-11-01 21:06:36', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_caixa, 'SAIDA_PEDIDO', 2, @o, DATE_ADD('2026-11-01 21:06:36', INTERVAL @tz_offset HOUR));

-- #48 | 03/11 ter 19:52 | Maria Joaquina | 2x Pizza Calabresa | Pix
SET @o = UUID_TO_BIN(UUID());
INSERT INTO `order` (id, subtotal, shipping_fee, total_cost, creation_date, order_status, payment_method_id, restaurant_id, customer_user_id, address_city_id, address_street, address_number, address_neighborhood, address_zip_code, update_date)
VALUES (@o, 104.00, 7.50, 111.50, DATE_ADD('2026-11-03 19:52:07', INTERVAL @tz_offset HOUR), 'DELIVERED', @pm_pix, @rest, @u_mj, @city_rio, 'Rua Farme de Amoedo', '34', 'Ipanema', '22420-020', UTC_TIMESTAMP());
INSERT INTO order_item (id, order_id, product_id, quantity, unit_price, total_price, note)
VALUES (UUID_TO_BIN(UUID()), @o, @p_C, 2, 52.00, 104.00, 'Cortar em 8 pedaços');
INSERT INTO ingredient_movement (id, ingredient_id, type, quantity, order_id, created_at) VALUES
(UUID_TO_BIN(UUID()), @i_farinha, 'SAIDA_PEDIDO', 600, @o, DATE_ADD('2026-11-03 19:52:07', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_molho, 'SAIDA_PEDIDO', 240, @o, DATE_ADD('2026-11-03 19:52:07', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_mussarela, 'SAIDA_PEDIDO', 400, @o, DATE_ADD('2026-11-03 19:52:07', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_calabresa, 'SAIDA_PEDIDO', 300, @o, DATE_ADD('2026-11-03 19:52:07', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_cebola, 'SAIDA_PEDIDO', 80, @o, DATE_ADD('2026-11-03 19:52:07', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_azeite, 'SAIDA_PEDIDO', 20, @o, DATE_ADD('2026-11-03 19:52:07', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_oregano, 'SAIDA_PEDIDO', 4, @o, DATE_ADD('2026-11-03 19:52:07', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_caixa, 'SAIDA_PEDIDO', 2, @o, DATE_ADD('2026-11-03 19:52:07', INTERVAL @tz_offset HOUR));

-- #49 | 05/11 qui 21:12 | João Mohammed Pendezza | 2x Pizza Quatro Queijos | Dinheiro
SET @o = UUID_TO_BIN(UUID());
INSERT INTO `order` (id, subtotal, shipping_fee, total_cost, creation_date, order_status, payment_method_id, restaurant_id, customer_user_id, address_city_id, address_street, address_number, address_neighborhood, address_zip_code, update_date)
VALUES (@o, 112.00, 7.50, 119.50, DATE_ADD('2026-11-05 21:12:32', INTERVAL @tz_offset HOUR), 'DELIVERED', @pm_dinheiro, @rest, @u_jm, @city_rio, 'Rua Maria Quitéria', '85', 'Ipanema', '22410-040', UTC_TIMESTAMP());
INSERT INTO order_item (id, order_id, product_id, quantity, unit_price, total_price, note)
VALUES (UUID_TO_BIN(UUID()), @o, @p_Q, 2, 56.00, 112.00, NULL);
INSERT INTO ingredient_movement (id, ingredient_id, type, quantity, order_id, created_at) VALUES
(UUID_TO_BIN(UUID()), @i_farinha, 'SAIDA_PEDIDO', 600, @o, DATE_ADD('2026-11-05 21:12:32', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_molho, 'SAIDA_PEDIDO', 240, @o, DATE_ADD('2026-11-05 21:12:32', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_mussarela, 'SAIDA_PEDIDO', 240, @o, DATE_ADD('2026-11-05 21:12:32', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_provolone, 'SAIDA_PEDIDO', 140, @o, DATE_ADD('2026-11-05 21:12:32', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_gorgonzola, 'SAIDA_PEDIDO', 120, @o, DATE_ADD('2026-11-05 21:12:32', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_parmesao, 'SAIDA_PEDIDO', 80, @o, DATE_ADD('2026-11-05 21:12:32', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_azeite, 'SAIDA_PEDIDO', 20, @o, DATE_ADD('2026-11-05 21:12:32', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_oregano, 'SAIDA_PEDIDO', 4, @o, DATE_ADD('2026-11-05 21:12:32', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_caixa, 'SAIDA_PEDIDO', 2, @o, DATE_ADD('2026-11-05 21:12:32', INTERVAL @tz_offset HOUR));

-- #50 | 08/11 dom 20:07 | Ronaldo Pinto | 1x Pizza Quatro Queijos | Cartão de Débito
SET @o = UUID_TO_BIN(UUID());
INSERT INTO `order` (id, subtotal, shipping_fee, total_cost, creation_date, order_status, payment_method_id, restaurant_id, customer_user_id, address_city_id, address_street, address_number, address_neighborhood, address_zip_code, update_date)
VALUES (@o, 56.00, 7.50, 63.50, DATE_ADD('2026-11-08 20:07:05', INTERVAL @tz_offset HOUR), 'DELIVERED', @pm_debito, @rest, @u_rp, @city_rio, 'Rua Teixeira de Melo', '31', 'Ipanema', '22410-010', UTC_TIMESTAMP());
INSERT INTO order_item (id, order_id, product_id, quantity, unit_price, total_price, note)
VALUES (UUID_TO_BIN(UUID()), @o, @p_Q, 1, 56.00, 56.00, 'Cortar em 8 pedaços');
INSERT INTO ingredient_movement (id, ingredient_id, type, quantity, order_id, created_at) VALUES
(UUID_TO_BIN(UUID()), @i_farinha, 'SAIDA_PEDIDO', 300, @o, DATE_ADD('2026-11-08 20:07:05', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_molho, 'SAIDA_PEDIDO', 120, @o, DATE_ADD('2026-11-08 20:07:05', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_mussarela, 'SAIDA_PEDIDO', 120, @o, DATE_ADD('2026-11-08 20:07:05', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_provolone, 'SAIDA_PEDIDO', 70, @o, DATE_ADD('2026-11-08 20:07:05', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_gorgonzola, 'SAIDA_PEDIDO', 60, @o, DATE_ADD('2026-11-08 20:07:05', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_parmesao, 'SAIDA_PEDIDO', 40, @o, DATE_ADD('2026-11-08 20:07:05', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_azeite, 'SAIDA_PEDIDO', 10, @o, DATE_ADD('2026-11-08 20:07:05', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_oregano, 'SAIDA_PEDIDO', 2, @o, DATE_ADD('2026-11-08 20:07:05', INTERVAL @tz_offset HOUR)),
(UUID_TO_BIN(UUID()), @i_caixa, 'SAIDA_PEDIDO', 1, @o, DATE_ADD('2026-11-08 20:07:05', INTERVAL @tz_offset HOUR));

-- ---------------------------------------------------------------------
-- 21. SALDO ATUAL = soma do histórico (entradas + estornos + ajustes - saídas)
-- ---------------------------------------------------------------------
UPDATE ingredient i
SET i.stock_quantity = (
    SELECT COALESCE(SUM(CASE m.type WHEN 'SAIDA_PEDIDO' THEN -m.quantity ELSE m.quantity END), 0)
    FROM ingredient_movement m WHERE m.ingredient_id = i.id
)
WHERE i.restaurant_id = @rest;

-- Saldos esperados após o UPDATE (para conferir):
--   Farinha de trigo     4000 g  (mínimo 2000)
--   Molho de tomate      3600 ml (mínimo 1800)
--   Mussarela            2560 g  (mínimo 1200)
--   Calabresa fatiada    1050 g  (mínimo 1600)  <- estoque baixo
--   Cebola                430 g  (mínimo 200)
--   Provolone            1410 g  (mínimo 700)
--   Gorgonzola            280 g  (mínimo 450)  <- estoque baixo
--   Parmesão ralado       520 g  (mínimo 250)
--   Azeite de oliva       300 ml (mínimo 150)
--   Orégano                60 g  (mínimo 30)
--   Caixa de pizza         30 un (mínimo 50)  <- estoque baixo