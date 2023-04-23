package org.ripoll.service;

import org.ripoll.model.TotalForRelatedTransactions;
import org.ripoll.model.TransactionRequest;

import java.util.Set;

public interface ITransactionService {
    void saveTransaction(long transactionId, TransactionRequest transactionRequest);

    Set<Long> getAllTransactionsByType(String type);

    TotalForRelatedTransactions getTotalForRelatedTransactions(long transactionId);
}
