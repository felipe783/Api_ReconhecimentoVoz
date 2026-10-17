### Linguagem Principal

``Java``
### Rotas

| Método e rota | Corpo | Resposta |
|---|---|---|
| `POST /transactions` | JSON `{"description", "category", "amount"}` | `201` `{"id", "category", "description", "amount"}` |
| `GET /transactions/{category}` | — | lista de transações da categoria |
| `POST /transactions/ai` | `multipart/form-data`, campo `file` (áudio) | `audio/mp3` |
| `GET /api/chat?prompt=` | — | texto (`ChatClient`) |
| `GET /api/chat-model?prompt=` | — | texto (`OpenAiChatModel`) |
| `POST /api/transcribe` | `multipart`, campo `file` | texto transcrito |
| `POST /api/sinthesize` | JSON `{"text"}` | `audio/mp3` |

Categorias aceitas: `GROCERIES`, `PHARMA`, `AUTO`.

## Fluxo Geral — Registro por voz

1. O cliente envia o áudio em `POST /transactions/ai` ([[Transaction Controller]]).
2. O Whisper transcreve em português ([[Configuração Spring AI]]).
3. O `ChatClient` com o system prompt decide chamar a tool `persist-transaction` ou `list-trasactions-by-category` ([[Casos de Uso e Tools]]).
4. O caso de uso grava ou consulta pelo repositório ([[Persistência JPA (Budgeting)]]).
5. O texto final da IA vira áudio MP3 e é devolvido.

## Fluxo Geral — REST

1. `POST /transactions` converte `TransactionRequest` em `PersistTransactionInput`.
2. `PersistTransactionUseCase` cria `Transaction` com UUID novo e salva.
3. A resposta é `TransactionResponse` com o valor formatado com 2 casas.
