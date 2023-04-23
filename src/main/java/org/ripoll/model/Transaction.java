package org.ripoll.model;

public record Transaction(long transactionId,
                          double amount,
                          String type,
                          Long parentId) {

    public Transaction(Long transactionId, TransactionRequest transactionRequest) {
        this(transactionId, transactionRequest.amount(), transactionRequest.type(), transactionRequest.parentId());
    }
}
