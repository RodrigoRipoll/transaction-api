package org.ripoll.repository;

import lombok.Getter;
import org.ripoll.model.Transaction;

import java.util.*;
import java.util.stream.Collectors;

public class TransactionRepository implements ITransactionRepository {

    @Getter
    private final Map<Long, Transaction> transactionsDataStorage;

    public TransactionRepository() {
        this.transactionsDataStorage = new HashMap<>();
    }

    @Override
    public void save(Transaction transaction) {
        transactionsDataStorage.put(transaction.transactionId(), transaction);
    }

    @Override
    public Set<Transaction> getAllTransactionsByType(String type) {
        return transactionsDataStorage.values().stream()
                .filter(transaction -> transaction.type().equals(type))
                .collect(Collectors.toSet());
    }

    @Override
    public Set<Transaction> getAllAssociatedTransactions(long parentId) {
        Set<Transaction> associatedTranasctionsSet = new HashSet<>();
        getAllAssociatedTransactionsHelper(parentId, associatedTranasctionsSet);
        return associatedTranasctionsSet;
    }

    private void getAllAssociatedTransactionsHelper(Long parentId, Set<Transaction> result) {
        Transaction parentTransaction = transactionsDataStorage.get(parentId);
        if (parentTransaction != null) {
            result.add(parentTransaction);
            for (Transaction childTransaction : transactionsDataStorage.values()) {
                if (Objects.equals(childTransaction.parentId(), parentId)) {
                    getAllAssociatedTransactionsHelper(childTransaction.transactionId(), result);
                }
            }
        }
    }
}
