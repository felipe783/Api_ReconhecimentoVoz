---
Tipo: projeto
Projeto: Budgeting
Status: concluido
Stack:
  - Java 25
  - Spring Boot 4.0.6
  - Spring AI 2.0.0-M4 (OpenAI)
  - Spring Data JPA
  - MySQL
  - Gradle
Codigo: "budgeting"
Repositorio: "https://github.com/felipe783/Api_ReconhecimentoVoz"
Atualizado: 2026-10-17
---
# Budgeting

API **Spring Boot** de **controle de gastos por voz**: o usuário envia um áudio dizendo o gasto, a API transcreve com o Whisper, um modelo da OpenAI (`gpt-4o-mini`) entende o pedido e usa *tools* para **registrar** ou **listar** transações no MySQL, e a resposta volta **em áudio** (TTS). Também expõe as operações por REST comum e endpoints de teste dos modelos de IA.

- **Código:** raiz deste repositório; a documentação fica em `vault/`
- **Entrada:** HTTP na porta padrão do Spring (8080): JSON em `/transactions`, áudio `multipart/form-data` em `/transactions/ai`
- **Saída:** JSON das transações; áudio MP3 nas rotas de voz; registros na tabela de transações do MySQL
- **Origem:** pacote `dio.budgeting` e último commit "Final fixes, project completed": projeto do **Bootcamp NTT DATA Backend Java com Spring AI** (DIO) [PROVÁVEL]

## Status

| Funcionalidade | Estado |
|---|---|
| Registrar transação por REST (`POST /transactions`) ([[BG-RF 01]]) | Implementado |
| Listar transações por categoria (`GET /transactions/{category}`) ([[BG-RF 02]]) | Implementado |
| Assistente por voz: áudio → texto → IA com tools → áudio (`POST /transactions/ai`) ([[BG-RF 03]]) | Implementado |
| Endpoints de teste de IA: chat, transcrição e síntese de voz ([[BG-RF 04]]) | Implementado |
| Documentação OpenAPI (springdoc) | Dependência presente; compatibilidade com o Boot 4 não verificada |
| Banco MySQL local por Docker Compose ([[BG-RF 05]]) | **Quebrado**: o `compose.yml` foi removido no commit `ba19d94` e não há `spring.datasource.*` |
| Autenticação | Inexistente |
| Testes | 1 teste de contexto + 5 testes de integração com a OpenAI (só com `OPENAI_API_KEY`) |

## Como rodar

```powershell
# Requer JDK 25 (o Gradle toolchain procura/baixa) e um MySQL acessível
# .env na raiz (não versionado):
#   OPENAI_API_KEY=<sua chave>
.\gradlew.bat bootRun          # Linux/macOS: ./gradlew bootRun
.\gradlew.bat test             # testes (os de IA só rodam com OPENAI_API_KEY no ambiente)
.\gradlew.bat build            # jar em build/libs/
```

```bash
# exemplos
curl -X POST localhost:8080/transactions -H "Content-Type: application/json" \
     -d '{"description":"Mercado","category":"GROCERIES","amount":120}'
curl localhost:8080/transactions/GROCERIES
curl -X POST localhost:8080/transactions/ai -F "file=@gasto.m4a" -o resposta.mp3
```

> Em 2026-10-17 **nada foi compilado nem executado**: o Java do PATH era o 1.8 (o projeto exige o 25), o `compose.yml` não existe mais e os testes de IA consomem créditos da OpenAI. O conteúdo desta documentação foi lido do código.

## Mapa das notas

- [[Visão Geral (Budgeting)]]: o que o sistema faz.
- [[Arquitetura (Budgeting)]]: camadas, fluxo da voz e diagrama das tools.
- [[Resumo Principal (Budgeting)]]: rotas e fluxos.
- [[Estrutura (Budgeting)]]: árvore de pastas.
- [[Dependencias (Budgeting)]]: bibliotecas e versões.
- Requisitos: [[BG-RF 01]] · [[BG-RF 02]] · [[BG-RF 03]] · [[BG-RF 04]] · [[BG-RF 05]]
- Camadas:
  - [[!Modulo Domain (Budgeting)]]: [[Transação e Categoria]].
  - [[!Modulo Application (Budgeting)]]: [[Casos de Uso e Tools]].
  - [[!Modulo Infraestrutura (Budgeting)]]: [[Transaction Controller]], [[Persistência JPA (Budgeting)]].
  - [[!Modulo IA (Budgeting)]]: [[Configuração Spring AI]], [[Endpoints de IA]].
  - [[Testes Automatizados (Budgeting)]].

## Problemas conhecidos

Encontrados lendo o código em 2026-10-17. Nada foi executado.

- **Sem banco configurado**: o `compose.yml` (MySQL 9.6, banco `transaction`, porta 3307) foi criado em `4fdfb90` e removido em `ba19d94`. O `spring-boot-docker-compose` não tem o que subir e não há `spring.datasource.*`, então a aplicação não deve iniciar sem configurar o banco à mão. [PROVÁVEL]
- **Credencial no histórico do Git**: o `healthcheck` do `compose.yml` removido tinha a senha do usuário do MySQL de desenvolvimento em texto claro. O valor não foi reproduzido aqui.
- **Sem autenticação**: qualquer pessoa que alcance a API grava transações e consome créditos da OpenAI (chat, Whisper e TTS).
- **Valores inteiros**: `amount` é `long`. A saída o converte para `double` com 2 casas, mas os centavos não existem na entrada, e a IA pode tentar enviar `12.50`.
- `login.level.org.springframework.ai=DEBUG` deveria ser `logging.level...`; do jeito que está, não tem efeito.
- **Dependências de versão nova/instável**: Spring Boot `4.0.6` com Spring AI `2.0.0-M4` (milestone). O `springdoc-openapi 2.8.6` é da linha do Boot 3. [NÃO VERIFICADO]
- O teste `BudgetingApplicationTests` (`@SpringBootTest`) precisa do MySQL e da chave da OpenAI para carregar o contexto.
- Erros de grafia nos nomes: `aplication`, `infrastructer`, `TransitonController`, `/sinthesize`, tool `list-trasactions-by-category`.
- O `.env` local tem um espaço antes do `=` (`OPENAI_API_KEY =`); funciona no formato `.properties`, mas pode quebrar em outras ferramentas que leiam `.env`.
- O nome do repositório remoto (`Api_ReconhecimentoVoz`) é diferente do nome do projeto (`budgeting`).

## Decisões

- **Arquitetura em camadas** (`domain` → `aplication` → `infrastructer`): o domínio (`Transaction`, `Category`, `TransactionRepository`) não conhece JPA nem HTTP; a persistência implementa a interface do domínio.
- **Casos de uso como tools da IA**: os mesmos `@Service` usados pelo REST são anotados com `@Tool`, então a IA chama exatamente a mesma regra que o endpoint.
- **System prompt em arquivo** (`prompts/system.st`), carregado pelo controller, e não no código.
- **UUID gerado no domínio** (`TransactionId`), não pelo banco.
- **Chave da OpenAI fora do código**: `.env` + `spring-dotenv` + `spring.config.import`.

## Próximos passos sugeridos

1. Restaurar um `compose.yml` (sem senha no `healthcheck`, usando variáveis) ou documentar `spring.datasource.*` no `.env`.
2. Adicionar autenticação (ou ao menos *rate limit*) antes de expor a API.
3. Guardar valores em centavos (`long` em centavos) ou `BigDecimal`, e ajustar a tool.
4. Corrigir `logging.level` e os nomes com erro de grafia.
5. Fixar versões estáveis (Spring AI GA, springdoc compatível com o Boot 4).
6. Separar testes de unidade (com *mocks* do `ChatClient`) dos testes que chamam a OpenAI.
