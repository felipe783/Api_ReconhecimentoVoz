# Arquitetura

## Camadas

```mermaid
flowchart TD
    subgraph infrastructer
        HTTP[TransitonController<br/>/transactions]
        JPA[JpaTransactionRepository]
        ER[TransactionEntityRepository<br/>CrudRepository]
        ENT[TransactionEntity @Entity]
    end
    subgraph aplication
        P[PersistTransactionUseCase<br/>@Tool persist-transaction]
        L[ListTransactionByCategoryUseCase<br/>@Tool list-trasactions-by-category]
    end
    subgraph domain
        T[Transaction / TransactionId]
        C[Category]
        R[TransactionRepository]
    end
    HTTP --> P & L
    P & L --> R
    JPA -.implementa.-> R
    JPA --> ER --> ENT
    ER --> DB[(MySQL)]
```

- O **domínio** não importa Spring Data nem Web; a infraestrutura converte `Transaction` ↔ `TransactionEntity`.
- Os **controllers de IA** (`/api/*`) ficam no pacote raiz e falam direto com os modelos, fora das camadas.

## Fluxo do assistente por voz (`POST /transactions/ai`)

```mermaid
sequenceDiagram
    participant U as Cliente
    participant C as TransitonController
    participant W as Whisper (whisper-1)
    participant G as ChatClient (gpt-4o-mini)
    participant T as Tools (casos de uso)
    participant S as TTS (gpt-4o-mini-tts)
    U->>C: multipart file (áudio)
    C->>W: transcribe(áudio) — idioma pt
    W-->>C: texto
    C->>G: system.st + texto
    G->>T: persist-transaction / list-trasactions-by-category
    T-->>G: TransactionOutput
    G-->>C: resposta em texto
    C->>S: call(texto) — voz nova, 1.2x
    S-->>C: bytes MP3
    C-->>U: audio/mp3 (attachment audio.mp3)
```

## Tratamento de erros

Não há `@ControllerAdvice` nem validação (`@Valid`): uma categoria inválida no caminho ou no JSON gera o erro padrão do Spring (400), e falhas da OpenAI ou do banco viram 500.
