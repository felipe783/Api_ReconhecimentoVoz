# Sobre o Arquivo

`infrastructer/http/TransitonController.java` é o **controller de transações** (`/transactions`) e o **orquestrador do assistente por voz**.
# Funcionalidades

## Montagem do ChatClient

````java
this.chatClient = chatClientBuilder
        .defaultSystem(systemPrompt.getContentAsString(Charset.defaultCharset()))
        .defaultTools(persistTransactionUseCase, listTransactionByCategoryUseCase)
        .build();
````

O system prompt vem de `classpath:/prompts/system.st`:

> "Voce e uma assistente financeira. Sua tarefa e extrair dados de transacoes e usar as ferramentas disponiveis para manipular transacoes. Ao registrar uma transacao, escolha a categoria que melhor se adapta ao contexto."

## Rotas

````java
@PostMapping @ResponseStatus(HttpStatus.CREATED)
public TransactionResponse createTransaction(@RequestBody TransactionRequest request)

@GetMapping("/{category}")
public List<TransactionResponse> readTransactions(@PathVariable Category category)

@PostMapping(value = "/ai", consumes = MULTIPART_FORM_DATA_VALUE, produces = "audio/mp3")
ResponseEntity<Resource> transcribe(@RequestParam("file") MultipartFile file) {
    var userMessage = transcriptionModel.transcribe(file.getResource());
    var result = chatClient.prompt().user(userMessage).call().content();
    byte[] audio = textToSpeechModel.call(result);
    ...  // Content-Disposition: attachment; filename="audio.mp3"
}
````

- `TransactionRequest(description, category, amount)` → `toInput()`.
- `TransactionResponse(id, category, description, amount)`.

> `Charset.defaultCharset()` depende do sistema operacional: no Windows antigo, acentos do `system.st` podem sair errados. O arquivo hoje não tem acentos.
