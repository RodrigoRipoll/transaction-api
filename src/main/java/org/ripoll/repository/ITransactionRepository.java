package org.ripoll.repository;

import org.ripoll.model.Transaction;

import java.util.Map;
import java.util.Set;

public interface ITransactionRepository {
    void save(Transaction transaction);

    Set<Transaction> getAllTransactionsByType(String type);

    Set<Transaction> getAllAssociatedTransactions(long parentId);

    Map<Long, Transaction> getTransactionsDataStorage();
}
