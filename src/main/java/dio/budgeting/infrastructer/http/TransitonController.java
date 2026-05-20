package dio.budgeting.infrastructer.http;

import dio.budgeting.aplication.ListTransactionByCategoryUseCase;
import dio.budgeting.aplication.PersistTransactionUseCase;
import dio.budgeting.domain.Category;
import dio.budgeting.infrastructer.http.request.TransactionRequest;
import dio.budgeting.infrastructer.http.response.TransactionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransitonController {
    private final PersistTransactionUseCase persistTransactionUseCase;
    private final ListTransactionByCategoryUseCase listTransactionByCategoryUseCase;

    public TransitonController(PersistTransactionUseCase persistTransactionUseCase, ListTransactionByCategoryUseCase listTransactionByCategoryUseCase) {
        this.persistTransactionUseCase = persistTransactionUseCase;
        this.listTransactionByCategoryUseCase = listTransactionByCategoryUseCase;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TransactionResponse createTransaction(@RequestBody TransactionRequest request){
        var transaction = persistTransactionUseCase.execute(request.toInput());
        return TransactionResponse.from(transaction);
    }

    @GetMapping("/{category}")
    public List<TransactionResponse> readTransactions(@PathVariable Category category){
        return listTransactionByCategoryUseCase.execute(category).stream().map(TransactionResponse::from).toList();
    }
}

