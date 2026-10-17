# Sobre o Arquivo

`src/main/resources/application.properties` configura o **acesso à OpenAI** e os modelos usados.
# Funcionalidades

## Chave

````properties
spring.config.import=optional:file:.env[.properties]
spring.ai.openai.api-key=${OPENAI_API_KEY}
````

- O `.env` da raiz é importado como arquivo de propriedades (e também lido pelo `spring-dotenv`). A chave nunca fica no código.
- `optional:` faz a aplicação subir sem o arquivo, mas aí a chave fica vazia e qualquer chamada à OpenAI falha.

## Modelos

| Uso | Propriedade | Valor |
|---|---|---|
| Chat | `spring.ai.openai.chat.options.model` | `gpt-4o-mini` (resposta `TEXT`) |
| Transcrição | `spring.ai.openai.audio.transcription.options.model` | `whisper-1`, idioma `pt`, temperatura 0, prompt "áudio em português… gastos financeiros" |
| Voz | `spring.ai.model.audio.speech.options.model` | `gpt-4o-mini-tts`, voz `nova`, velocidade `1.2`, `mp3` |

## Bean ChatClient

````java
@Bean
ChatClient chatChatClient(ChatClient.Builder builder) { return builder.build(); }
````

É usado pelo `/api/chat`. O [[Transaction Controller]] monta um `ChatClient` próprio, com system prompt e tools.

> [!warning] Observações
> - `login.level.org.springframework.ai=DEBUG` não tem efeito: o prefixo certo é `logging.level`.
> - O prompt de transcrição tem um caractere com encoding inválido ("�udio").
