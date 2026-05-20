package dio.budgeting.infrastructer.http;

import dio.budgeting.aplication.ListTransactionByCategoryUseCase;
import dio.budgeting.aplication.PersistTransactionUseCase;
import dio.budgeting.domain.Category;
import dio.budgeting.infrastructer.http.request.TransactionRequest;
import dio.budgeting.infrastructer.http.response.TransactionResponse;
import org.springframework.ai.audio.transcription.TranscriptionModel;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequestMapping("/transactions")
public class TransitonController {
    private final PersistTransactionUseCase persistTransactionUseCase;
    private final ListTransactionByCategoryUseCase listTransactionByCategoryUseCase;
    private final TranscriptionModel transcriptionModel;
    private final ChatClient chatClient;

    @Value("classpath:/prompts/system.st")
    private Resource systemPrompt;

    public TransitonController(PersistTransactionUseCase persistTransactionUseCase,
                               ListTransactionByCategoryUseCase listTransactionByCategoryUseCase,
                               TranscriptionModel transcriptionModel,
                               ChatClient.Builder chatClientBuilder,

    ) {
        this.persistTransactionUseCase = persistTransactionUseCase;
        this.listTransactionByCategoryUseCase = listTransactionByCategoryUseCase;
        this.transcriptionModel = transcriptionModel;
        this.chatClient = chatClientBuilder
                .defaultSystem(systemPrompt)
                .defaultTools(PersistTransactionUseCase.class, ListTransactionByCategoryUseCase.class )
                .build();
    }

    public TransitonController(
            PersistTransactionUseCase persistTransactionUseCase,
            ListTransactionByCategoryUseCase listTransactionByCategoryUseCase,
            TranscriptionModel transcriptionModel,
            ChatClient chatClient) {
        this.persistTransactionUseCase = persistTransactionUseCase;
        this.listTransactionByCategoryUseCase = listTransactionByCategoryUseCase;
        this.transcriptionModel = transcriptionModel;
        this.chatClient = chatClient;
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

    @PostMapping(value = "/ai", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    String transcribe(@RequestParam("file")MultipartFile file){
        var resource = file.getResource();
        var userMessage = transcriptionModel.transcribe(resource);

        var result = chatClient.prompt().user(userMessage).call().content();

        return result;
    }
}

