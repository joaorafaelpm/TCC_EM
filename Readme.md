# 🍕 Pizzaria Delivery com IA Integrada

## 📖 Visão Geral
Este projeto é o **TCC** de um sistema de **Delivery de Pizzaria** com serviços inteligentes, oferecendo:
- **Gestão de estoque personalizada**: gráficos e sugestões automáticas de compras para o restaurante.
- **Agentes de IA**: chatbot para suporte ao cliente e recomendação de pedidos.
- **Dashboard analítico**: visualização de métricas em tempo real para otimizar decisões.

## 🏗️ Tecnologias Principais
- **Frontend**: [Next.js](https://nextjs.org/) (React)
- **Backend**: [Java Spring Boot (REST API)](https://spring.io/projects/spring-boot)
- **Inteligência Artificial**: [Python](https://www.python.org/) (modelos e automação)
- **Testes Automatizados**:
  - End-to-End (E2E): [Cypress](https://www.cypress.io/) + [Cucumber](https://cucumber.io/) (JavaScript)
  - Testes Integrados: [Cypress](https://www.cypress.io/) + [Cucumber](https://cucumber.io/) (JavaScript)

## 📊 Funcionalidades Planejadas
- Autenticação com JWT e **2FA**.  
- Painel administrativo para controle de pedidos e estoque.  
- Sugestões automáticas de pedidos personalizadas para clientes.  
- Dashboard com gráficos de consumo e relatórios.  
- Testes E2E e integrados para garantir qualidade do sistema.  

## 📂 Estrutura do Projeto
```
pizzaria-delivery/
├─ backend/         # API em Spring Boot
├─ frontend/        # Aplicação Next.js
├─ ai-services/     # Serviços em Python (IA e automação)
├─ tests/           # Testes E2E (Cypress + Cucumber)
└─ docs/            # Documentação detalhada
```

## 🚀 Como Executar
### 📋 Pré-requisitos
- Docker e Docker Compose  
- Node.js 20+  
- Java 21+  
- Python 3.11+  

### ▶️ Passos Iniciais
```bash
# Clone o repositório
git clone https://github.com/joaorafaelpm/TCC_EM
cd TCC_EM-main

# Subir containers (MySQL, backend e frontend)
docker-compose up --build -d #🤓☝
```

## 🧭 Roadmap
- [ ] Desenvolver a API Rest e micro serviços.  
- [ ] Implementar autenticação JWT e 2FA.  
- [ ] Integrar IA de sugestão de estoque.  
- [ ] Criar dashboard de analytics.  
- [ ] Configurar testes E2E com Cypress e Cucumber.  
- [ ] Finalizar documentação para o TCC.  

## 🛡️ Licença
Este projeto é de uso educacional e faz parte do TCC para **[Bento Quirino]**.
