# Mercado Express — Web (Thymeleaf + Spring Security)

Checkpoint 4 — Parte 2 (Spring Web, Spring MVC, Security e Deploy)
FIAP — Curso de Tecnologia em Análise e Desenvolvimento de Sistemas (TDS)

Aplicação web para gerenciamento de produtos de um "mercado express", desenvolvida em **Spring Boot** com **Spring MVC**, **Thymeleaf** como motor de templates e **Spring Security** para autenticação e autorização de rotas. Persistência no mesmo banco **Oracle** utilizado na Parte 1.

## Integrantes

- Matheus Moya de Oliveira — RM 562822
- Luisa Ganasevici de Abreu — RM 563403
- Ana Carolina Pereira Fontes — RM 562145

**IDE utilizada:** IntelliJ IDEA

---

## Índice

1. [Tecnologias utilizadas](#tecnologias-utilizadas)
2. [Arquitetura do projeto](#arquitetura-do-projeto)
3. [Modelagem da entidade `Mercado`](#modelagem-da-entidade-mercado)
4. [Configuração do banco de dados](#configuração-do-banco-de-dados)
5. [Spring Security — rotas públicas e privadas](#spring-security--rotas-públicas-e-privadas)
6. [Telas e funcionalidades (CRUD via interface Web)](#telas-e-funcionalidades-crud-via-interface-web)
7. [Endpoints](#endpoints)
8. [Tratamento de erros](#tratamento-de-erros)
9. [Pré-requisitos e como executar localmente](#pré-requisitos-e-como-executar-localmente)
10. [Segurança e boas práticas](#segurança-e-boas-práticas)
11. [Deploy](#deploy)

---

## Tecnologias utilizadas

| Tecnologia | Finalidade |
|---|---|
| Java 21 (LTS) | Linguagem |
| Spring Boot | Framework principal |
| Maven | Gerenciador de dependências e build |
| Spring MVC | Controllers Web, roteamento de páginas |
| Thymeleaf | Motor de templates HTML |
| Spring Security | Autenticação, autorização e controle de rotas públicas/privadas |
| Spring Data JPA | Persistência de dados via Hibernate |
| Lombok | Redução de código boilerplate (getters, setters, construtores) |
| Oracle Driver (JDBC) | Conexão com o banco de dados Oracle (ORACLE_FIAP) |
| Docker | Empacotamento da aplicação para o deploy |

---

## Arquitetura do projeto

```
br.com.fiap.mercadoexpress
├── controller     -> Recebe requisições HTTP, monta o Model e retorna o nome da página Thymeleaf
├── service        -> Regras de negócio, orquestra o fluxo entre Controller e Repository
├── repository     -> Acesso a dados via Spring Data JPA
├── entity         -> Representação das tabelas do banco de dados (Mercado, Usuario)
├── config         -> Configuração do Spring Security
└── exception      -> Tratamento centralizado de erros (ex: recurso não encontrado)
```

Diferente da Parte 1 (API REST pura, retornando JSON), aqui os `Controllers` retornam o **nome de um template Thymeleaf**, que é renderizado no servidor e devolvido como HTML pronto para o navegador — por isso a anotação usada é `@Controller` (não `@RestController`).

---

## Modelagem da entidade `Mercado`

Mesma tabela utilizada na Parte 1 (`TDS_TB_mercado`), no banco Oracle da FIAP, reaproveitada nesta Parte 2:

| Campo | Tipo Java | Coluna no Oracle | Observação |
|---|---|---|---|
| id | `Long` | Id | Gerado via `SEQUENCE` do Oracle (`MERCADO_SEQ`) |
| nome | `String` | Nome | — |
| tipo | `String` | Tipo | — |
| setor | `String` | Setor | — |
| tamanho | `String` | Tamanho | — |
| cor | `String` | Cor | — |
| preco | `BigDecimal` | Preco | `BigDecimal` para evitar erros de arredondamento em valores monetários |
| estoque | `Integer` | Estoque | Controlado também pela tela de compra (baixa automática do estoque) |
| disponivel | `Boolean` | Disponivel | Usado para "exclusão lógica" (ver seção de CRUD abaixo) |

Também existe a entidade `Usuario`, usada pelo Spring Security para autenticação (login, cadastro, papéis `ADMIN`/`CLIENTE`).

---

## Spring Security — rotas públicas e privadas

A aplicação define três níveis de acesso, configurados em `SecurityConfig`:

| Nível | Rotas | Quem acessa |
|---|---|---|
| **Público** | `/`, `/home`, `/cadastro`, `/login`, `/logout`, `/acesso-negado`, `/css/**`| Qualquer visitante, sem login |
| **Público (leitura)** | `GET /produtos`, `GET /produtos/{id}` (somente ID numérico) | Qualquer visitante pode navegar e ver os produtos, sem precisar de conta |
| **Autenticado** | `/produtos/{id}/comprar` | Qualquer usuário logado (`CLIENTE` ou `ADMIN`) |
| **Somente ADMIN** | `/produtos/**` (criar, editar, excluir), `/usuarios/**` | Apenas usuários com papel `ADMIN` |

**Login:** implementado via `formLogin` do Spring Security, com uma página de login customizada em Thymeleaf (`login.html`). Em caso de usuário/senha inválidos, o usuário é redirecionado de volta para `/login?error=true`, exibindo uma mensagem de erro na própria tela — sem precisar de uma página separada.

**Acesso negado:** usuários autenticados sem permissão suficiente para acessar uma rota (ex: `CLIENTE` tentando acessar rota de `ADMIN`) são direcionados para a página `/acesso-negado`, estilizada com o mesmo padrão visual da aplicação.

**Senhas:** armazenadas com hash `BCrypt` (`BCryptPasswordEncoder`), nunca em texto puro no banco.

**Papéis (roles):** todo novo cadastro nasce com o papel `CLIENTE`. Um usuário `ADMIN` pode promover ou rebaixar outros usuários pela tela `/usuarios`, com uma trava que impede o próprio admin de se rebaixar por engano.

---

## Telas e funcionalidades (CRUD via interface Web)

Todas as operações abaixo são acionadas por **botões/links na interface**, sem necessidade de ferramentas externas (Postman, etc.):

### Create
Botão **"Novo produto"** (visível apenas para `ADMIN`, na tela `/produtos`) → abre o formulário `produto-form.html` → salva via `POST /produtos`.

### Read
- Tela `/produtos`: lista todos os produtos cadastrados, com busca por nome (`?nome=...`), que filtra a lista dinamicamente e exibe uma mensagem de "produto não encontrado" quando a busca não retorna resultados.
- Botão **"Comprar"**: abre a tela de compra de um produto específico (`GET /produtos/{id}/comprar`).

### Update
- Botão **"Editar"** (ADMIN): abre `produto-editar.html`, pré-preenchido com os dados atuais, salvando via `POST /produtos/{id}/editar`.
- Atualização rápida de estoque via `POST /produtos/{id}/estoque`, também usada automaticamente quando um cliente finaliza uma compra (baixa do estoque comprado).

### Delete
Botão **"Excluir"** (ADMIN) → `POST /produtos/{id}/excluir`.

> **Sobre a estratégia de exclusão:** optamos por uma soft delete em vez de remover fisicamente o registro do banco — o produto é marcado com `disponivel = false` e deixa de aparecer disponível para compra, mas o registro é preservado. Essa decisão evita a perda de histórico de vendas/estoque associado ao produto e reflete uma prática comum em sistemas de e-commerce reais.

### Cadastro e Login de usuários
- `/cadastro`: formulário de criação de conta (papel inicial `CLIENTE`).
- `/login`: autenticação via Spring Security, com tela de erro amigável em caso de credenciais inválidas.
- `/usuarios` (ADMIN): lista usuários e permite promover/rebaixar entre `CLIENTE` e `ADMIN`.

---

## Endpoints

| Método | Rota | Acesso | Descrição |
|---|---|---|---|
| GET | `/` | Público | Redireciona para `/home` |
| GET | `/home` | Público | Página inicial |
| GET | `/home/privado` | Autenticado | Página inicial de usuário logado |
| GET | `/acesso-negado` | Público | Página exibida quando um usuário autenticado tenta acessar uma rota sem permissão |
| GET | `/login` | Público | Formulário de login |
| POST | `/login` | Público | Processa login (gerenciado pelo Spring Security) |
| GET | `/cadastro` | Público | Formulário de cadastro de usuário |
| POST | `/cadastro` | Público | Cria novo usuário (papel `CLIENTE`) |
| GET | `/produtos` | Público | Lista/busca produtos |
| GET | `/produtos/{id}` | Público | Detalhe de um produto (somente ID numérico) |
| GET | `/produtos/novo` | ADMIN | Formulário de novo produto |
| POST | `/produtos` | ADMIN | Cria produto |
| GET | `/produtos/editar` | ADMIN | Formulário de edição |
| POST | `/produtos/{id}/editar` | ADMIN | Salva edição |
| POST | `/produtos/{id}/excluir` | ADMIN | Exclusão lógica do produto |
| POST | `/produtos/{id}/estoque` | ADMIN | Atualização rápida de estoque |
| GET | `/produtos/{id}/comprar` | Autenticado | Formulário de compra |
| POST | `/produtos/{id}/comprar` | Autenticado | Processa a compra, baixa estoque |
| GET | `/usuarios` | ADMIN | Lista usuários |
| POST | `/usuarios/{id}/promover` | ADMIN | Promove usuário a `ADMIN` |
| POST | `/usuarios/{id}/rebaixar` | ADMIN | Rebaixa usuário a `CLIENTE` |

---

## Tratamento de erros

- **Login inválido:** redireciona para `/login?error=true`, exibindo mensagem de erro na própria tela (sem expor detalhes técnicos ao usuário).
- **Acesso negado:** usuários sem permissão para uma rota são redirecionados para `/acesso-negado`, uma página dedicada e estilizada (não a página padrão de erro do Spring).
- **Produto/usuário não encontrado:** tratado de forma centralizada, evitando erros genéricos (`500`) na tela.
- **Busca sem resultado:** exibe mensagem amigável ("Nenhum produto encontrado para '...'") no lugar da tabela, em vez de deixar a tela em branco.

---

## Pré-requisitos e como executar localmente

**Pré-requisitos:**
- Java 21 (JDK)
- Maven (ou usar o `./mvnw` incluso no projeto)
- Acesso ao banco Oracle FIAP (`ORACLE_FIAP`), com usuário e senha válidos

**Passos:**
1. Clone o repositório.
2. Configure as variáveis de ambiente `DB_USERNAME` e `DB_PASSWORD` com suas credenciais do Oracle FIAP.
3. Execute a classe `MercadoExpressApplication`.
4. A aplicação sobe em `http://localhost:8082`.
5. Acesse pelo navegador — não é necessário Postman/Insomnia, toda a navegação é feita pela interface Web.

---

## Segurança e boas práticas

- **Nenhuma credencial é versionada no código.** Usuário e senha do Oracle são lidos via variáveis de ambiente (`${DB_USERNAME}`, `${DB_PASSWORD}`), tanto localmente quanto em produção (painel de variáveis do Render).
- **Senhas de usuários** armazenadas com hash `BCrypt`, nunca em texto puro.
- **Controle de acesso por papel (role-based)** via Spring Security, com rotas administrativas protegidas por `hasRole("ADMIN")`.
- **Path variables de ID restritas a números** (`{id:[0-9]+}`) nas rotas públicas de leitura de produto, evitando que segmentos de texto (como `novo` ou `editar`) sejam incorretamente reconhecidos como um ID e liberados publicamente.
- **CSRF habilitado** (padrão do Spring Security) — todos os formulários de POST incluem o token CSRF.

---

## Deploy

🔗 Link da aplicação em produção (Render): https://mercado-express-957x.onrender.com

Deploy realizado via **Docker** no [Render](https://render.com/), com um `Dockerfile` multi-stage: a primeira etapa compila o projeto com Maven, a segunda roda apenas o `.jar` final sobre uma imagem JRE enxuta.

**Observações sobre o ambiente de produção:**
- O plano utilizado é o **Free Tier** do Render, que hiberna a aplicação após períodos de inatividade — a primeira requisição após esse período pode demorar cerca de 1 minuto para responder, enquanto a instância "acorda".
- A porta é configurada dinamicamente via variável de ambiente `PORT`, injetada automaticamente pelo Render (`server.port=${PORT:8082}`).