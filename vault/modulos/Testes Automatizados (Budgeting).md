# Sobre os Arquivos

`src/test/java/dio/budgeting/` tem **6 classes**, todas `@SpringBootTest`, ou seja, sobem o contexto inteiro (banco e OpenAI).
# Testes

| Classe | Condição | Verifica |
|---|---|---|
| `BudgetingApplicationTests` | sempre | o contexto carrega (`contextLoads`) |
| `OpenAiChatClientIT` | `OPENAI_API_KEY` no ambiente | "10 + 20 − 30" responde contendo `0` |
| `OpenAiChatModelT` | `OPENAI_API_KEY` | o modelo gera um registro de gasto não vazio |
| `OpenAiSpeechModelIT` | `OPENAI_API_KEY` | o TTS gera mais de 1024 bytes |
| `OpenAiTranscriptionModelIT` | `OPENAI_API_KEY` | `record-1.m4a`/`record-2.m4a` contêm a palavra esperada |
| `ToolCallingIT` | `OPENAI_API_KEY` | o modelo usa uma tool de soma/subtração |

````powershell
.\gradlew.bat test
````

- `@EnabledIfEnvironmentVariable` olha a **variável de ambiente** do processo, não o `.env`: sem `OPENAI_API_KEY` exportada, os 5 testes de IA são pulados.
- No Gradle, o sufixo `IT` não separa nada: todos rodam na task `test`.
- Não há testes de unidade dos casos de uso nem do controller.

> Em 2026-10-17 nenhum teste foi executado (JDK 25 ausente, sem MySQL e com custo na OpenAI).
