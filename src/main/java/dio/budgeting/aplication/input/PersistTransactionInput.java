package dio.budgeting.aplication.input;

import dio.budgeting.domain.Category;
import org.springframework.ai.tool.annotation.ToolParam;

public record PersistTransactionInput(@ToolParam(description = "Descricao do gasto") String description,
                                      @ToolParam(description = "Preco do gasto") Long amount,
                                      @ToolParam(description = "Categoria do gasto") Category category) {


}
