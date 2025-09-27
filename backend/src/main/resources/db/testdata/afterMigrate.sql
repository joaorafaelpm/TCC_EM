SET FOREIGN_KEY_CHECKS = 0;

DELETE FROM cidade;
DELETE FROM estado;
DELETE FROM forma_pagamento;
DELETE FROM grupo;
DELETE FROM grupo_permissao;
DELETE FROM permissao;
DELETE FROM produto;
DELETE FROM restaurante;
DELETE FROM restaurante_forma_pagamento;
DELETE FROM usuario;
DELETE FROM usuario_grupo;

SET FOREIGN_KEY_CHECKS = 1;

-- =====================
-- ESTADOS
-- =====================
INSERT INTO estado (id, nome) VALUES (UUID_TO_BIN(UUID()), 'São Paulo');
SET @estado_sp = (SELECT id FROM estado WHERE nome = 'São Paulo');

INSERT INTO estado (id, nome) VALUES (UUID_TO_BIN(UUID()), 'Rio de Janeiro');
SET @estado_rj = (SELECT id FROM estado WHERE nome = 'Rio de Janeiro');

-- =====================
-- CIDADES
-- =====================
INSERT INTO cidade (id, nome, estado_id) VALUES (UUID_TO_BIN(UUID()), 'São Paulo', @estado_sp);
SET @cidade_sp = (SELECT id FROM cidade WHERE nome = 'São Paulo');

INSERT INTO cidade (id, nome, estado_id) VALUES (UUID_TO_BIN(UUID()), 'Campinas', @estado_sp);
SET @cidade_campinas = (SELECT id FROM cidade WHERE nome = 'Campinas');

INSERT INTO cidade (id, nome, estado_id) VALUES (UUID_TO_BIN(UUID()), 'Rio de Janeiro', @estado_rj);
SET @cidade_rj = (SELECT id FROM cidade WHERE nome = 'Rio de Janeiro');

-- =====================
-- RESTAURANTES (PIZZARIAS)
-- =====================
INSERT INTO restaurante (id, nome, taxa_frete, data_cadastro, data_atualizacao,
                         endereco_cidade_id, endereco_cep, endereco_logradouro, endereco_numero, endereco_bairro)
VALUES (UUID_TO_BIN(UUID()), 'Pizzaria Bella Itália', 8.50, UTC_TIMESTAMP(), UTC_TIMESTAMP(),
        @cidade_sp, '01000-000', 'Av. Paulista', '1500', 'Centro');
SET @restaurante_bella = (SELECT id FROM restaurante WHERE nome = 'Pizzaria Bella Itália');

INSERT INTO restaurante (id, nome, taxa_frete, data_cadastro, data_atualizacao,
                         endereco_cidade_id, endereco_cep, endereco_logradouro, endereco_numero, endereco_bairro)
VALUES (UUID_TO_BIN(UUID()), 'Forno de Pedra', 6.90, UTC_TIMESTAMP(), UTC_TIMESTAMP(),
        @cidade_campinas, '13000-111', 'Rua das Flores', '200', 'Jardins');
SET @restaurante_forno = (SELECT id FROM restaurante WHERE nome = 'Forno de Pedra');

INSERT INTO restaurante (id, nome, taxa_frete, data_cadastro, data_atualizacao,
                         endereco_cidade_id, endereco_cep, endereco_logradouro, endereco_numero, endereco_bairro)
VALUES (UUID_TO_BIN(UUID()), 'Sabor da Vila', 5.00, UTC_TIMESTAMP(), UTC_TIMESTAMP(),
        @cidade_rj, '22000-222', 'Rua Atlântica', '50', 'Copacabana');
SET @restaurante_vila = (SELECT id FROM restaurante WHERE nome = 'Sabor da Vila');

-- =====================
-- FORMAS DE PAGAMENTO
-- =====================
INSERT INTO forma_pagamento (id, descricao) VALUES (UUID_TO_BIN(UUID()), 'Cartão de crédito');
SET @fp_credito = (SELECT id FROM forma_pagamento WHERE descricao = 'Cartão de crédito');

INSERT INTO forma_pagamento (id, descricao) VALUES (UUID_TO_BIN(UUID()), 'Cartão de débito');
SET @fp_debito = (SELECT id FROM forma_pagamento WHERE descricao = 'Cartão de débito');

INSERT INTO forma_pagamento (id, descricao) VALUES (UUID_TO_BIN(UUID()), 'Dinheiro');
SET @fp_dinheiro = (SELECT id FROM forma_pagamento WHERE descricao = 'Dinheiro');

-- =====================
-- RELAÇÃO RESTAURANTE x FORMA PAGAMENTO
-- =====================
INSERT INTO restaurante_forma_pagamento (restaurante_id, forma_pagamento_id) VALUES
(@restaurante_bella, @fp_credito),
(@restaurante_bella, @fp_debito),
(@restaurante_forno, @fp_dinheiro),
(@restaurante_vila, @fp_credito),
(@restaurante_vila, @fp_dinheiro);

-- =====================
-- PRODUTOS (CARDÁPIO PIZZA)
-- =====================
-- Bella Itália
INSERT INTO produto (id, nome, descricao, preco, ativo, restaurante_id)
VALUES (UUID_TO_BIN(UUID()), 'Pizza Margherita', 'Molho de tomate, mussarela de búfala, manjericão fresco', 45.00, true, @restaurante_bella);

INSERT INTO produto (id, nome, descricao, preco, ativo, restaurante_id)
VALUES (UUID_TO_BIN(UUID()), 'Pizza Quattro Formaggi', 'Mussarela, gorgonzola, parmesão e provolone', 55.00, true, @restaurante_bella);

-- Forno de Pedra
INSERT INTO produto (id, nome, descricao, preco, ativo, restaurante_id)
VALUES (UUID_TO_BIN(UUID()), 'Pizza Calabresa', 'Molho de tomate, mussarela e calabresa fatiada', 42.00, true, @restaurante_forno);

INSERT INTO produto (id, nome, descricao, preco, ativo, restaurante_id)
VALUES (UUID_TO_BIN(UUID()), 'Pizza Portuguesa', 'Presunto, ovo, cebola, azeitona e mussarela', 50.00, true, @restaurante_forno);

-- Sabor da Vila
INSERT INTO produto (id, nome, descricao, preco, ativo, restaurante_id)
VALUES (UUID_TO_BIN(UUID()), 'Pizza Frango com Catupiry', 'Frango desfiado, catupiry original e mussarela', 48.00, true, @restaurante_vila);

INSERT INTO produto (id, nome, descricao, preco, ativo, restaurante_id)
VALUES (UUID_TO_BIN(UUID()), 'Pizza Doce de Nutella', 'Cobertura de Nutella com morangos frescos', 52.00, true, @restaurante_vila);

-- =====================
-- PERMISSÕES
-- =====================
INSERT INTO permissao (id, nome, descricao) VALUES (UUID_TO_BIN(UUID()), 'CONSULTAR_CARDAPIO', 'Permite consultar o cardápio');
SET @perm_cons_cardapio = (SELECT id FROM permissao WHERE nome = 'CONSULTAR_CARDAPIO');

INSERT INTO permissao (id, nome, descricao) VALUES (UUID_TO_BIN(UUID()), 'EDITAR_CARDAPIO', 'Permite editar o cardápio');
SET @perm_edit_cardapio = (SELECT id FROM permissao WHERE nome = 'EDITAR_CARDAPIO');

INSERT INTO permissao (id, nome, descricao) VALUES (UUID_TO_BIN(UUID()), 'CONSULTAR_PEDIDOS', 'Permite consultar pedidos');
SET @perm_cons_pedidos = (SELECT id FROM permissao WHERE nome = 'CONSULTAR_PEDIDOS');

INSERT INTO permissao (id, nome, descricao) VALUES (UUID_TO_BIN(UUID()), 'GERENCIAR_PEDIDOS', 'Permite gerenciar pedidos');
SET @perm_ger_pedidos = (SELECT id FROM permissao WHERE nome = 'GERENCIAR_PEDIDOS');

-- =====================
-- GRUPOS
-- =====================
INSERT INTO grupo (id, nome) VALUES (UUID_TO_BIN(UUID()), 'Administrador');
SET @grupo_admin = (SELECT id FROM grupo WHERE nome = 'Administrador');

INSERT INTO grupo (id, nome) VALUES (UUID_TO_BIN(UUID()), 'Atendente');
SET @grupo_atendente = (SELECT id FROM grupo WHERE nome = 'Atendente');

INSERT INTO grupo (id, nome) VALUES (UUID_TO_BIN(UUID()), 'Cliente');
SET @grupo_cliente = (SELECT id FROM grupo WHERE nome = 'Cliente');

-- GRUPO x PERMISSÃO
INSERT INTO grupo_permissao (grupo_id, permissao_id) VALUES
(@grupo_admin, @perm_cons_cardapio),
(@grupo_admin, @perm_edit_cardapio),
(@grupo_admin, @perm_cons_pedidos),
(@grupo_admin, @perm_ger_pedidos),
(@grupo_atendente, @perm_cons_pedidos),
(@grupo_atendente, @perm_ger_pedidos),
(@grupo_cliente, @perm_cons_cardapio);

-- =====================
-- USUÁRIOS
-- =====================
INSERT INTO usuario (id, nome, email, senha, data_cadastro)
VALUES (UUID_TO_BIN(UUID()), 'João Pizzaiolo', 'joao@pizzaria.com', '123456', UTC_TIMESTAMP());
SET @user_admin = (SELECT id FROM usuario WHERE email = 'joao@pizzaria.com');

INSERT INTO usuario (id, nome, email, senha, data_cadastro)
VALUES (UUID_TO_BIN(UUID()), 'Maria Cliente', 'maria@teste.com', '123456', UTC_TIMESTAMP());
SET @user_cliente = (SELECT id FROM usuario WHERE email = 'maria@teste.com');

-- USUÁRIO x GRUPO
INSERT INTO usuario_grupo (usuario_id, grupo_id) VALUES
(@user_admin, @grupo_admin),
(@user_cliente, @grupo_cliente);
