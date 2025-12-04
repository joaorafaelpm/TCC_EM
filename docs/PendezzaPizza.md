# Pendezza Pizza -- Roadmap de Desenvolvimento

## 1. Visão Geral do Sistema

**Pendezza Pizza** é um sistema de administração para pizzarias, com
suporte a IA, projetado para auxiliar pequenos restaurantes na gestão de
finanças, criação e manutenção de cardápios, controle de pedidos e
visualização de recursos de forma dinâmica e intuitiva.

------------------------------------------------------------------------

## 2. Organização da Aplicação

A aplicação contará com um conjunto de recursos fixos e permissões
configuráveis. O usuário poderá:

-   Criar usuários autenticados.
-   Criar e administrar restaurantes e seus cardápios.
-   Gerenciar itens do cardápio, ativando ou desativando itens e
    restaurantes.
-   Configurar o sistema de fidelidade.
-   Emitir relatórios que serão enviados por e-mail aos administradores.

### Dashboard do Restaurante

O dashboard terá foco em métricas essenciais para gestão:

-   **Total de vendas** (bege)
-   **Total faturado** (verde claro)
-   **Produtos mais vendidos e quantidades** (ciano)
-   **Previsões de vendas** (amarelo)

------------------------------------------------------------------------

## 3. Dados Acessados pela Aplicação

Para permitir controle total e suportar evoluções futuras, é importante
definir precisamente quais dados serão coletados e como serão
utilizados.

### Pontos a definir

-   **Informações armazenadas**: despesas, dados pessoais de clientes,
    receitas, ingredientes, número de vendas, pedidos, formas de
    pagamento etc.
-   **Sistema de fidelidade** baseado em CPF (obrigatório ou opcional?).
-   **Detalhamento dos relatórios**: possibilidade de gerar
    recomendações, bônus e benefícios personalizados para clientes.
-   **Receitas e ingredientes**:
    -   Funcionalidade opcional.
    -   Necessidade de padronização dos dados.
    -   Se não persistido, será interpretado por IA a partir de JSON.

#### Trade-off

Persistir receitas e ingredientes aumenta a complexidade da aplicação,
mas permite relatórios mais ricos e recomendações baseadas no histórico
de vendas.

------------------------------------------------------------------------

## 4. Orçamento Mensal

A funcionalidade será incluída e exigirá:

-   **CRUD de despesas** vinculado ao restaurante.
-   **Limite de gastos mensal** exibido nos relatórios.
-   **Sistema de avisos por e-mail** quando próximo do limite.
-   Suporte a controle **mensal e anual**.

------------------------------------------------------------------------

## 5. Comentários na Aplicação

Caso implementado:

-   Apenas usuários autenticados (com CPF e endereço) poderão comentar.
-   Comentários serão armazenados no recurso Restaurante, junto com
    notas/avaliações.

------------------------------------------------------------------------

## 6. Usuários e Login

-   **Login tradicional** por e-mail e senha.
-   **Login com Google**, caso permita inclusão de CPF.
-   **Níveis de acesso** definidos via OAuth2 + JWT.

------------------------------------------------------------------------

## 7. Checklist de Implementação

-   [ ] Definir padrões de desenvolvimento como linguagem e versão
-   [ ] Definir modelagem de dados
-   [ ] Definir regras de negócio do sistema de fidelidade
-   [ ] Definir regras de comentários
-   [ ] Definir regras de segurança dos endpoints
-   [ ] Implementar sistema de despesas
-   [ ] Definir o fluxo dos testes da aplicação para versionamento no github
-   [ ] Definir e alinhar todo o desenvolvimento dos agentes de IA do projeto
-   [ ] Definir autenticação
-   [ ] Selecionar serviços de terceiros
-   [ ] Definir ordem de desenvolvimento
-   [ ] Realizar deploy em nuvem
