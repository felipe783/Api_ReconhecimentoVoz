package dio.budgeting.infrastructer.http.response;

import dio.budgeting.aplication.output.TransactionOutput;



public record TransactionResponse(String id, String category,String description , double amount) {

    public static TransactionResponse from(TransactionOutput output) {
        return new TransactionResponse(output.id(), output.category(), output.description(), output.value());
    }

}
