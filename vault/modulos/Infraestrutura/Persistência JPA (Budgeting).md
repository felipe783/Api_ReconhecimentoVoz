# Sobre os Arquivos

`TransactionEntity`, `TransactionEntityRepository` e `JpaTransactionRepository` fazem a **persistência em MySQL** via Spring Data JPA.
# Funcionalidades

## TransactionEntity

````java
@Entity @Data @AllArgsConstructor @NoArgsConstructor
public class TransactionEntity {
    @Id private UUID Id;
    private String description;
    private long amount;
    @Enumerated(EnumType.STRING) private Category category;
    public static TransactionEntity from(Transaction t) { ... }
    public Transaction toDomain() { ... }
}
````

- A categoria é gravada como **texto** (`STRING`), então reordenar o enum não corrompe os dados.
- O schema é criado e alterado pelo Hibernate: `spring.jpa.hibernate.ddl-auto=update`. Não há migrations.
- `spring.jpa.show-sql=true` imprime o SQL no console.

## Repositórios

````java
public interface TransactionEntityRepository extends CrudRepository<TransactionEntity, UUID> {
    List<TransactionEntity> findAllByCategory(Category category);   // query derivada do nome
}
````

`JpaTransactionRepository` (`@Repository`) implementa o `TransactionRepository` do domínio e converte entidade ↔ domínio.

## Banco

O `compose.yml` removido definia MySQL 9.6, banco `transaction`, usuário `app`, porta `3307:3306` e volume `transaction_data`. Sem ele, configure o datasource:

````properties
spring.datasource.url=jdbc:mysql://localhost:3307/transaction
spring.datasource.username=app
spring.datasource.password=${DB_PASSWORD}
````

> [!warning] Observações
> - Não há `spring.datasource.*` no `application.properties`. Hoje o banco depende do Docker Compose, cujo arquivo foi apagado.
> - O campo `Id` com maiúscula funciona, mas foge da convenção Java (`id`).
> - Há uma alteração local não commitada em `JpaTransactionRepository.java` (2 linhas em branco removidas).
