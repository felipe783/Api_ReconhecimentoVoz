package dio.budgeting.infrastructer.http.request;

import dio.budgeting.aplication.input.PersistTransactionInput;
import dio.budgeting.domain.Category;

public record TransactionRequest(String description, Category category , long amount) {
    public PersistTransactionInput toInput() {
        return new PersistTransactionInput(description,amount, category);
    }
}
