
**Java 25**
Definido por `java.toolchain.languageVersion = 25` no `build.gradle`; o Gradle usa ou provisiona esse JDK.

[**Spring Boot 4.0.6**](https://spring.io/projects/spring-boot)
`spring-boot-starter`, `starter-web` (REST), `starter-data-jpa` (JPA/Hibernate) e `starter-test`.

[**Spring AI 2.0.0-M4**](https://docs.spring.io/spring-ai/reference/)
BOM `spring-ai-bom` + `spring-ai-starter-model-openai`: `ChatClient`, `OpenAiChatModel`, `TranscriptionModel` (Whisper), `TextToSpeechModel` e `@Tool`. É uma versão **milestone**.

[**MySQL Connector/J**](https://dev.mysql.com/doc/connector-j/en/)
Driver JDBC (`runtimeOnly`).

[**spring-boot-docker-compose**](https://docs.spring.io/spring-boot/reference/features/dev-services.html)
`developmentOnly`: sobe o `compose.yml` ao rodar a aplicação e configura o datasource sozinho. O arquivo foi removido do repositório.

[**spring-dotenv 4.0.0**](https://github.com/paulschwarz/spring-dotenv)
Lê o `.env` como fonte de propriedades.

[**springdoc-openapi 2.8.6**](https://springdoc.org/)
Swagger UI e OpenAPI.

[**Lombok**](https://projectlombok.org/) (plugin `io.freefair.lombok` 9.5.0)
`@Getter`, `@Data`, construtores.

**Testes**: JUnit 5 (`useJUnitPlatform()`) e AssertJ.


Compilar e baixar tudo:
````
.\gradlew.bat build
````
