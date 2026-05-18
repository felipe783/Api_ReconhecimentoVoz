package dio.budgeting.aplication.input;

import dio.budgeting.domain.Category;

public record PersistTransactionInput(String description, Long amount, Category category) {


}
