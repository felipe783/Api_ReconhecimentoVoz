# Sobre os Arquivos

`PersistTransactionUseCase`, `ListTransactionByCategoryUseCase` e os DTOs `PersistTransactionInput` / `TransactionOutput` formam a **camada de aplicação**.
# Funcionalidades

## PersistTransactionUseCase

````java
@Tool(name = "persist-transaction", description = "persiste uma nova transacao financeira")
public TransactionOutput execute(PersistTransactionInput input) {
    var transaction = transactionRepository.save(
        new Transaction(input.description(), input.amount(), input.category()));
    return TransactionOutput.from(transaction);
}
````

## ListTransactionByCategoryUseCase

````java
@Tool(name = "list-trasactions-by-category", description = "Lista das transacoes financeiras por categoria")
public List<TransactionOutput> execute(@ToolParam(description = "Categoria de uma transacao") Category category)
````

## DTOs

| Record | Campos |
|---|---|
| `PersistTransactionInput` | `description`, `amount` (`Long`), `category`, cada um com `@ToolParam` para o modelo |
| `TransactionOutput` | `id` (texto), `description`, `category` (nome do enum), `value` (`double` com 2 casas, `HALF_UP`) |

## Como a IA usa

O `@Tool` gera, a partir da assinatura e das descrições, o *schema* JSON que o Spring AI envia à OpenAI. O modelo decide qual tool chamar e com quais argumentos, e o Spring AI executa o método Java e devolve o resultado ao modelo. Ver [[Transaction Controller]].

> A descrição de cada `@ToolParam` é o que o modelo lê: ela precisa explicar unidade e formato (ex.: "valor em reais, inteiro").
