# GameScore Hub (Desktop)

## Status do Projeto

🚧 **Em Desenvolvimento** (Projeto Integrador — Módulo A / UC15 — Técnico em Desenvolvimento de Sistemas, SENAC EAD).

\---

## Tecnologias Aplicadas

* **Linguagem:** Java (JDK 11+)
* **Interface Gráfica Desktop:** Java Swing (com práticas de UX/UI e acessibilidade WCAG 2.1)
* **IDE:** Apache NetBeans (com Apache Maven)
* **Banco de Dados:** MySQL / MySQL Workbench
* **Integração com Banco:** JDBC (`mysql-connector-j`)
* **Modelagem e Design:** UML (Diagrama de Classes) e Wireframes Desktop
* **Versionamento:** Git e GitHub

\---

## Time de Desenvolvedores

* **Guilherme Aguiar dos Santos** — Análise de Requisitos, Modelagem UML/BD, Design UX/UI e Desenvolvimento Java/MySQL.

\---

## Objetivo do Software

O **GameScore Hub** é um software desktop desenvolvido para centralizar o catálogo e o sistema de avaliação de jogos eletrônicos em ambiente local ou de rede. Ele resolve a dispersão de análises e notas ao reunir sinopses, capas, links de trailers e avaliações em um único aplicativo, permitindo que jogadores consultem separadamente a **Nota da Crítica Especializada** e a **Nota da Comunidade de Jogadores** com integridade de dados garantida.

\---

## Funcionalidades do Sistema (Requisitos)

* **RF001 — Cadastro e Gestão de Jogos:** Moderadores e Administradores podem cadastrar, editar e excluir jogos no catálogo (título, sinopse, capa local/URL e link de trailer no YouTube).
* **RF002 — Avaliação de Jogos:** Jogadores comuns e críticos atribuem notas de 1 a 5 estrelas e escrevem comentários descritivos.
* **RF003 — Anexo de Mídia na Avaliação:** Permite vincular imagens de gameplay ou conquistas às avaliações registradas.
* **RF004 — Programa de Críticos Especializados:** Jogadores podem preencher o formulário de solicitação de selo de Crítico (veículo de imprensa, portfólio e justificativa); Administradores analisam e aprovam ou recusam, promovendo automaticamente o perfil no banco de dados.
* **RF005 — Cálculo Separado de Médias:** Calcula e exibe automaticamente a **Nota da Crítica** separada da **Nota dos Jogadores** diretamente via consultas SQL (`AVG`).
* **RF006 — Autenticação e Controle de Acesso (RBAC):** Login por e-mail e senha com liberação de funcionalidades conforme o perfil (`jogador`, `critico`, `moderador`, `administrador`).
* **RN001 — Regra de Negócio (Avaliação Única):** Jogadores comuns só podem avaliar cada jogo uma única vez, impedindo manipulação artificial das médias.

\---

## Como Executar o Projeto

1. Abra o **MySQL Workbench** e execute o script localizado em `sql/gamescore\_db\_completo.sql`.
2. Abra a pasta `GameScoreHub\_Etapa4` no **Apache NetBeans** (*File → Open Project*).
3. Edite `src/main/resources/db.properties` informando o usuário e a senha do seu MySQL local.
4. Clique com o botão direito no projeto → **Clean and Build** (o Maven baixará o driver MySQL automaticamente) e depois clique em **Run (F6)**.

