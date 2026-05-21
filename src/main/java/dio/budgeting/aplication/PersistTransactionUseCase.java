package dio.budgeting.aplication;

import dio.budgeting.aplication.input.PersistTransactionInput;
import dio.budgeting.aplication.output.TransactionOutput;
import dio.budgeting.domain.Transaction;
import dio.budgeting.domain.TransactionRepository;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;


@Service
public class PersistTransactionUseCase {
    private final TransactionRepository transactionRepository;

    public PersistTransactionUseCase(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Tool(name = "persist-transaction" , description = "persiste uma nova transacao financeira")
    public TransactionOutput execute(PersistTransactionInput input){
        var transaction = transactionRepository.save(new Transaction(input.description(), input.amount(), input.category()));

        return TransactionOutput.from(transaction);
    }


}
