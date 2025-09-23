-- Administradores (id=1) têm acesso total
INSERT INTO grupo_permissao (grupo_id, permissao_id) VALUES
(1,1),(1,2),(1,3),(1,4),(1,5),(1,6),(1,7),(1,8),(1,9),(1,10);

-- Clientes Premium (id=2) - acesso básico e pedidos
INSERT INTO grupo_permissao (grupo_id, permissao_id) VALUES
(2,6),  -- PROCESSAR_PEDIDOS
(2,7),  -- ACESSAR_DASHBOARD
(2,9);  -- RESPONDER_CLIENTES

-- Equipe de Suporte (id=3) - foco em responder clientes e relatórios
INSERT INTO grupo_permissao (grupo_id, permissao_id) VALUES
(3,4),  -- VISUALIZAR_RELATORIOS
(3,9);  -- RESPONDER_CLIENTES

-- Marketing e Vendas (id=4) - promoções e relatórios
INSERT INTO grupo_permissao (grupo_id, permissao_id) VALUES
(4,4),  -- VISUALIZAR_RELATORIOS
(4,8),  -- GERENCIAR_PROMOCOES
(4,7);  -- ACESSAR_DASHBOARD

-- Desenvolvedores (id=5) - gerenciar sistema e cardápio
INSERT INTO grupo_permissao (grupo_id, permissao_id) VALUES
(5,5),  -- GERENCIAR_CARDAPIO
(5,7),  -- ACESSAR_DASHBOARD
(5,10); -- CONFIGURAR_SISTEMA
