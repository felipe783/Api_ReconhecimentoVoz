# Visão Geral do Sistema

O Budgeting é uma API de **orçamento pessoal comandada por voz**. Em vez de preencher um formulário, o usuário fala "gastei 50 reais na farmácia", e a IA registra a transação na categoria certa e responde em áudio.

## Finalidade

- Registrar gastos com descrição, valor e categoria (`GROCERIES`, `PHARMA`, `AUTO`).
- Consultar os gastos de uma categoria.
- Fazer as duas coisas por voz, usando os modelos da OpenAI via **Spring AI**.
- Servir de laboratório dos recursos do Spring AI: `ChatClient`, `ChatModel`, transcrição, TTS e *tool calling*.

## Entrada principal

- [[Transaction Controller|POST /transactions/ai]]: áudio do usuário.
- [[Transaction Controller|POST /transactions]] e `GET /transactions/{category}`: REST.
- [[Endpoints de IA|/api/*]]: testes isolados de cada modelo.
## Saída principal

- JSON `{id, category, description, amount}`.
- MP3 com a resposta falada.
- Linhas na tabela de transações ([[Persistência JPA (Budgeting)]]).

## Camadas principais

- [[!Modulo Domain (Budgeting)]]: entidade, categoria e contrato do repositório.
- [[!Modulo Application (Budgeting)]]: casos de uso, que também são tools da IA.
- [[!Modulo Infraestrutura (Budgeting)]]: controller REST e JPA.
- [[!Modulo IA (Budgeting)]]: configuração do Spring AI e endpoints de teste.


[[Dependencias (Budgeting)]] · [[Arquitetura (Budgeting)]]
