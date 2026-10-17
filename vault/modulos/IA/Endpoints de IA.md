# Sobre os Arquivos

`ChatClientController`, `ChatModelController`, `TranscriptionController` e `TextToSpeechController` são **endpoints de teste** de cada modelo, sem ligação com as transações.
# Funcionalidades

| Rota | Classe | O que faz |
|---|---|---|
| `GET /api/chat?prompt=` | `ChatClientController` | `chatClient.prompt().user(prompt).call().content()` |
| `GET /api/chat-model?prompt=` | `ChatModelController` | `openAiChatModel.call(prompt)`, a API de baixo nível |
| `POST /api/transcribe` | `TranscriptionController` | `transcriptionModel.transcribe(file.getResource())`, devolve o texto |
| `POST /api/sinthesize` | `TextToSpeechController` | `{"text"}` → MP3 (`attachment; filename="audio.mp3"`) |

````bash
curl "localhost:8080/api/chat?prompt=Quanto%20e%202%2B2"
curl -X POST localhost:8080/api/transcribe -F "file=@src/test/resources/audio/record-1.m4a"
curl -X POST localhost:8080/api/sinthesize -H "Content-Type: application/json" -d '{"text":"Olá"}' -o ola.mp3
````

> Cada chamada consome créditos da OpenAI e nenhuma rota exige autenticação.
