# Budgeting — Controle de gastos por voz

API **Spring Boot** que registra e consulta gastos pessoais, inclusive **por voz**: você envia um áudio ("gastei 50 reais na farmácia"), a API transcreve com o Whisper, uma IA da OpenAI entende o pedido e usa *tools* para gravar ou listar transações no MySQL, e a resposta volta **em áudio**. Projeto do Bootcamp NTT DATA Backend Java com Spring AI (DIO).

## Funcionalidades

| Rota | O que faz |
|---|---|
| `POST /transactions` | Registra uma transação: `{"description", "category", "amount"}` → `201` com o `id` gerado |
| `GET /transactions/{category}` | Lista as transações de uma categoria (`GROCERIES`, `PHARMA`, `AUTO`) |
| `POST /transactions/ai` | Assistente por voz: áudio (`multipart`, campo `file`) → transcrição → IA com tools → resposta em MP3 |
| `GET /api/chat?prompt=` / `GET /api/chat-model?prompt=` | Teste do chat (`gpt-4o-mini`) |
| `POST /api/transcribe` | Teste da transcrição (Whisper, português) |
| `POST /api/sinthesize` | Teste da síntese de voz: `{"text"}` → MP3 |

## Tecnologias

- Java 25 (Gradle toolchain) e Gradle Wrapper
- Spring Boot 4.0.6: Web, Data JPA
- Spring AI 2.0.0-M4 com OpenAI: `ChatClient`, tool calling, Whisper, TTS
- MySQL (Connector/J) e Hibernate (`ddl-auto=update`)
- Lombok, springdoc-openapi, spring-dotenv, spring-boot-docker-compose

## Pré-requisitos

- JDK 25. O Gradle usa o JDK 25 instalado ou tenta provisioná-lo.
- Um MySQL acessível.
- Uma chave da API da OpenAI, com créditos.

## Configuração local

Crie um arquivo `.env` na raiz. Ele está no `.gitignore` e **nunca** deve ser commitado:

```dotenv
OPENAI_API_KEY=sua_chave_da_openai
```

O `application.properties` importa esse arquivo (`spring.config.import=optional:file:.env[.properties]`) e usa `spring.ai.openai.api-key=${OPENAI_API_KEY}`.

**Banco de dados:** o `compose.yml` que subia o MySQL foi removido do repositório, e não há `spring.datasource.*` configurado. Para rodar, aponte para um MySQL acrescentando ao `application.properties` (ou como variáveis de ambiente):

```properties
spring.datasource.url=jdbc:mysql://localhost:3307/transaction
spring.datasource.username=app
spring.datasource.password=${DB_PASSWORD}
```

Coloque `DB_PASSWORD` no `.env`. A tabela é criada pelo Hibernate na primeira execução.

## Como executar

```powershell
.\gradlew.bat bootRun        # Linux/macOS: ./gradlew bootRun
```

A API sobe na porta 8080:

```bash
curl -X POST localhost:8080/transactions -H "Content-Type: application/json" \
     -d '{"description":"Mercado","category":"GROCERIES","amount":120}'
curl localhost:8080/transactions/GROCERIES
curl -X POST localhost:8080/transactions/ai -F "file=@gasto.m4a" -o resposta.mp3
```

> O valor (`amount`) é um inteiro (`long`), sem centavos. A API não tem autenticação, e cada chamada de IA consome créditos da OpenAI: não a exponha publicamente.

## Testes

```powershell
.\gradlew.bat test
```

- Todos os testes são `@SpringBootTest`, então precisam do banco e da chave para carregar o contexto.
- Os 5 testes de IA (`*IT`, `OpenAiChatModelT`) só rodam com `OPENAI_API_KEY` exportada como **variável de ambiente**; o `.env` não basta para eles. Eles chamam a OpenAI de verdade e usam os áudios de `src/test/resources/audio/`.

## Estrutura

```text
src/main/java/dio/budgeting/
├── domain/           # Transaction, TransactionId, Category, TransactionRepository
├── aplication/       # casos de uso (também tools da IA) e DTOs
├── infrastructer/    # http/ (TransitonController) e persistence/ (JPA)
└── *Controller.java  # endpoints de teste de IA (/api/*)
src/main/resources/
├── application.properties
└── prompts/system.st # system prompt da assistente
```

## Links

- [Spring AI — referência](https://docs.spring.io/spring-ai/reference/)
- [Spring Boot](https://spring.io/projects/spring-boot)
- `HELP.md`: referências geradas pelo Spring Initializr

## 📚 Documentação

A documentação completa da arquitetura, das decisões técnicas e do funcionamento do projeto está no [vault/_Budgeting](vault/_Budgeting.md).
