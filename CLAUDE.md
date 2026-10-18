# CLAUDE.md — Budgeting

API **Spring Boot** de **controle de gastos por voz**: áudio → transcrição (Whisper) → modelo da OpenAI com *tools* que registram ou listam transações no MySQL → resposta em áudio (TTS). Também expõe as operações por REST. Projeto do Bootcamp NTT DATA (DIO), marcado como **concluído**. O repositório remoto se chama `Api_ReconhecimentoVoz`.

O perfil do autor e as preferências gerais estão no `CLAUDE.md` de `!Faculdade`. Este arquivo trata só do que é específico deste repositório.

## Documentação (fonte de verdade)

O índice abaixo é carregado em toda sessão. As notas detalhadas ficam em `vault/` e devem ser lidas **sob demanda**:

@vault/_Budgeting.md

| Assunto | Nota |
|---|---|
| Camadas, fluxo da voz e diagrama das tools | `vault/Arquitetura (Budgeting).md` |
| Rotas e fluxos | `vault/Requisitos/Resumo Principal (Budgeting).md` |
| `Transaction`, `Category`, `TransactionId` | `vault/modulos/Domain/Transação e Categoria.md` |
| Casos de uso expostos como `@Tool` | `vault/modulos/Application/Casos de Uso e Tools.md` |
| Controller REST e de voz | `vault/modulos/Infraestrutura/Transaction Controller.md` |
| JPA, MySQL e o `compose.yml` removido | `vault/modulos/Infraestrutura/Persistência JPA (Budgeting).md` |
| Spring AI, chave e `application.properties` | `vault/modulos/IA/Configuração Spring AI.md` |
| Endpoints de teste dos modelos | `vault/modulos/IA/Endpoints de IA.md` |
| Testes e quais chamam a OpenAI | `vault/modulos/Testes Automatizados (Budgeting).md` |

Conceitos transversais (camadas, configuração, segredos, persistência…), com aprendizados deste e de outros projetos: `E:/Downloads/!Faculdade/Contexto Projetos/Conhecimento/_Conhecimento.md`. Leia sob demanda, quando a tarefa tocar o assunto.

## Mapa do código

Pacote `src/main/java/dio/budgeting/`:
- `domain/`: `Transaction`, `Category`, `TransactionId` e a interface `TransactionRepository`. Não conhece JPA nem HTTP.
- `aplication/` (sic): `PersistTransactionUseCase` e `ListTransactionByCategoryUseCase`, que também são as *tools* da IA; DTOs em `input/` e `output/`.
- `infrastructer/` (sic): `http/TransitonController` (sic) e DTOs; `persistence/` com a entidade JPA e o `JpaTransactionRepository`, que implementa o repositório do domínio.
- Raiz do pacote: `ChatClientController`, `ChatModelController`, `TextToSpeechController`, `TranscriptionController` (testes dos modelos de IA).
- `src/main/resources/`: `application.properties` e `prompts/system.st` (system prompt).
- `src/test/`: `BudgetingApplicationTests` (contexto) e `*IT`/`OpenAiChatModelT` (chamam a OpenAI).

## Comandos

```powershell
.\gradlew.bat bootRun     # exige JDK 25 e um MySQL acessível
.\gradlew.bat test        # BudgetingApplicationTests precisa de MySQL; os de IA só rodam com OPENAI_API_KEY no ambiente
.\gradlew.bat build
```

## Regras deste projeto

- **Não rode testes que chamam a OpenAI** (`*IT`, `OpenAiChatModelT`, ou `test` com `OPENAI_API_KEY` no ambiente) sem pedir: eles consomem créditos.
- **Segredos**: não leia nem exiba o `.env`. A chave da OpenAI entra por `.env` + `spring-dotenv`; documente só o nome das variáveis.
- **Mesma regra para REST e IA**: os casos de uso de `aplication/` servem aos dois. Uma mudança de regra vale para ambos, então não duplique lógica no controller.
- **Domínio sem framework**: `domain/` não importa Spring, JPA nem HTTP.
- Os nomes com erro de grafia (`aplication`, `infrastructer`, `TransitonController`, `/sinthesize`) são o estado atual. Não renomeie sem pedido: isso muda pacotes e rotas.

## Manter a documentação

Ao alterar comportamento, atualize na mesma tarefa:
1. `vault/_Budgeting.md`: **Status**, **Problemas conhecidos** e `Atualizado` no frontmatter.
2. A nota do módulo afetado (tabela acima).
3. O `README.md`, se mudar comando, rota ou variável de ambiente.

Convenções do vault (o mesmo do Obsidian em `Contexto Projetos/Budgeting`, via junction): frontmatter conforme a tabela de **Convenções** de `E:/Downloads/!Faculdade/Contexto Projetos/Contexto Geral.md` (única definição); nomes de nota únicos no vault inteiro (notas genéricas levam `(Budgeting)`, requisitos `BG-RF NN`); links `[[Nome da Nota]]`; separe o que foi **executado** do que foi **lido no código**, com data.
