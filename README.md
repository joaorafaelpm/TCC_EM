<div align="center">

# 🍕 Pendezza Pizza (PEPI) 🍕

### *La gestione della pizzeria, semplice come una Margherita*

🇮🇹 🍅 🧀 🌿 🍕 🌿 🧀 🍅 🇮🇹

> Documentação Técnica de Desenvolvimento — plataforma de gestão para pizzarias de pequeno e médio porte.

</div>

---

## 👨‍🍳 Integrantes (nossos cozinheiros)

- João Rafael Pendezza Medeiros
- João Ricardo Frazão Romano Rocha
- João Victor Soteras
- Mateus Borges Nogueira
- Otavio Matias Medeiros
- Rafael Johann Garrido

**🎓 Orientador:** Prof. Mateus Amendola Redivo

## 🏫 Turma

3º ano B — Curso Técnico em Informática — Colégio Técnico Bento Quirino, Campinas - SP, 2026.

## 📖 Título do projeto

**Pendezza Pizza: Documentação Técnica de Desenvolvimento**

## 🍽️ Descrição do projeto

A Pendezza Pizza (PEPI) é uma plataforma web de gestão para pizzarias de pequeno e médio porte. Ela reúne em um único sistema o controle de estoque e a frente de caixa (PDV), ajudando o gestor a ter visibilidade sobre vendas e ingredientes, sem depender de planilhas e anotações manuais.

### 🧀 Funcionalidades da versão entregue (MVP)

- 📊 **Painel de controle e métricas:** vendas diárias, horário de pico e insumos em baixa.
- 📦 **Gestão automática de estoque:** baixa automática dos ingredientes conforme os pedidos.
- 🧾 **Frente de caixa (PDV):** lançamento rápido de pedidos presenciais e telefônicos.
- 🍕 **Recomendação de cardápio:** sugestões simples baseadas no histórico e na sazonalidade dos pedidos.
- 🔐 **Autenticação segura:** OAuth2 com PKCE.

### 🍅 Ingredientes da receita (tecnologias)

| Camada | Tecnologias |
|---|---|
| 🔥 Back end | Java, Spring Boot (API REST, arquitetura MVC em camadas), Loggly |
| 🗄️ Banco de dados | MySQL (relacional), Redis (cache e sessões) |
| 🎨 Front end | React |
| 🚚 Infraestrutura | Docker, nginx (balanceador de carga) |
| 🧪 Testes | Cypress, Mochawesome, Cucumber |
| 🛠️ Apoio | VSCode, Postman, Git |

### 🗂️ Estrutura do repositório

```
pendezzapizza-api/   # API back end (Spring Boot): api, core, infrastructure, domain
FrontEnd/            # Interface React: pages, components, hooks, services, utils
Testes/              # Testes e2e, support e fixtures
```
<!-- Ajuste os nomes das pastas conforme estão no repositório. -->

## 👩‍🍳 Instruções para execução e utilização (modo de preparo)

### 🧂 Pré-requisitos

- Git
- Java (JDK) e Maven ou Gradle, conforme o projeto da API
- Node.js e npm
- Docker e Docker Compose

### 1️⃣ Clonar o repositório

```bash
git clone [LINK DO REPOSITÓRIO]
cd [NOME DA PASTA]
```

### 2️⃣ Subir os serviços de apoio (MySQL e Redis)

```bash
docker compose up -d
```

Isso inicia o MySQL e o Redis usados pela API.

### 3️⃣ Executar a API (back end)

```bash
cd pendezzapizza-api
mvn clean install -DskipTests
mvn compile -Pdocker jib:dockerBuild
docker compose up --scale pendezzapizza-api=2 --build -d
```

Configure as variáveis de ambiente (conexão com MySQL, Redis, credenciais OAuth2) conforme o arquivo de configuração da API.
Em produção, o nginx balanceia as requisições entre duas instâncias da API.

### 4️⃣ Executar o front end

```bash
cd frontend
npm install
npm run dev
```

Acesse a URL exibida no terminal (normalmente `http://localhost:[PORTA]`).

### 5️⃣ Executar os testes

```bash
cd tests
npm install
npx cypress run
```

### 🍕 Utilização

1. Acesse a aplicação e faça login com e-mail e senha.
2. No **Painel**, acompanhe vendas, horário de pico e estoque.
3. Em **Insumos**, cadastre e consulte os ingredientes.
4. Na **Frente de Caixa (PDV)**, registre pedidos; a baixa no estoque é automática.
5. Consulte o **relatório de estoque**.

*Buon appetito!* 🇮🇹

---

## 📜 Termos de Uso e Compartilhamento

**Autores:** João Rafael Pendezza Medeiros, João Ricardo Frazão Romano Rocha, João Victor Soteras, Mateus Borges Nogueira, Otavio Matias Medeiros, Rafael Johann Garrido
**Orientador:** Prof. Mateus Amendola Redivo
**Projeto:** Pendezza Pizza (PEPI), TCC Técnico em Informática, Colégio Técnico Bento Quirino, 2026

© 2026 João Rafael Pendezza Medeiros, João Ricardo Frazão Romano Rocha, João Victor Soteras, Mateus Borges Nogueira, Otavio Matias Medeiros, Rafael Johann Garrido. Todos os direitos reservados, exceto o que está expressamente permitido abaixo.

### ✅ Permitido

- Consultar e estudar o código para fins educacionais.
- Uso para avaliação do TCC e apresentação acadêmica.
- Uso não comercial por terceiros, desde que respeitadas as condições de crédito abaixo.

### 📌 Condições

1. **Crédito obrigatório:** qualquer uso, cópia, adaptação ou divulgação deve citar os autores pelo nome e incluir link para este repositório.
2. **Sem fins lucrativos:** é proibido usar, vender, licenciar ou oferecer este código (ou derivados) como produto ou serviço comercial sem contratar os autores previamente.
3. **Uso institucional:** o uso pela instituição de ensino além da avaliação do TCC (outros projetos, sistemas internos, divulgação) depende de autorização prévia e por escrito dos autores.
4. **Derivados:** trabalhos derivados devem manter este aviso e indicar o que foi alterado.

### 📬 Contato

Para solicitar autorização ou contratar os autores:

| Autor | E-mail | LinkedIn | GitHub |
|---|---|---|---|
| João Rafael Pendezza Medeiros | joaorafaelpmed@gmail.com | [LinkedIn](https://www.linkedin.com/in/joaorafaelpmed) | [GitHub](https://github.com/joaorafaelpm) |
| João Ricardo Frazão Romano Rocha | joaorfrrocha@gmail.com | [LinkedIn](https://www.linkedin.com/in/joaorromano/) | [GitHub](https://github.com/joaorromano19) |
| João Victor Soteras | jvstrs1011@gmail.com | [LinkedIn](https://www.linkedin.com/in/jo%C3%A3o-victor-soteras-aa9907370/) | [GitHub](https://github.com/jvstrs10) |
| Mateus Borges Nogueira | mateusborgesno@gmail.com | — | [GitHub](https://github.com/MateusMBN) |
| Otavio Matias Medeiros | otaviomed2008@gmail.com | — | [GitHub](https://github.com/Kotaviozx1n) |
| Rafael Johann Garrido | rafaelgarridov8@gmail.com | [LinkedIn](https://www.linkedin.com/in/rafael-johann-garrido-7619b8441/) | [GitHub](https://github.com/RafaelGaJO) |

### ⚠️ Isenção de garantia

O software é fornecido "como está", sem garantias de qualquer tipo.
