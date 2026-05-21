package dio.budgeting.aplication;


import dio.budgeting.aplication.output.TransactionOutput;
import dio.budgeting.domain.Category;
import dio.budgeting.domain.TransactionRepository;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListTransactionByCategoryUseCase  {

    private final TransactionRepository transactionRepository;

    public ListTransactionByCategoryUseCase(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Tool(name = "list-trasactions-by-category" , description = "Lista das transacoes financeiras por categoria")
    public List<TransactionOutput> execute(@ToolParam(description = "Categoria de uma transacao") Category category){
        return transactionRepository.findAllbyCategory(category).stream().map(TransactionOutput::from).toList();
    }
}
