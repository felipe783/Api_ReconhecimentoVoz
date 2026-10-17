# Sobre os Arquivos

`Transaction.java`, `TransactionId.java`, `Category.java` e `TransactionRepository.java` definem o **modelo de domínio**.
# Funcionalidades

## Transaction

````java
@Getter
@AllArgsConstructor
public class Transaction {
    private TransactionId id;
    private String description;
    private long amount;
    private Category category;

    public Transaction(String description, long amount, Category category) {
        this.id = new TransactionId();   // UUID gerado no domínio
        ...
    }
}
````

## TransactionId

````java
public record TransactionId(UUID uuid) {
    public TransactionId() { this(UUID.randomUUID()); }
}
````

Um tipo próprio para o ID evita trocar um `UUID` qualquer por um identificador de transação.

## Category

````java
public enum Category { GROCERIES, PHARMA, AUTO }
````

## TransactionRepository

````java
public interface TransactionRepository {
    Transaction save(Transaction transaction);
    List<Transaction> findAllbyCategory(Category category);
}
````

Implementado por [[Persistência JPA (Budgeting)|JpaTransactionRepository]].

> [!warning] Valor sem centavos
> `amount` é `long`: "12,50" não tem representação. A saída formata com 2 casas, mas o valor guardado é inteiro.
