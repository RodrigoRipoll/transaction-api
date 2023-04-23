package org.ripoll.service;


import org.ripoll.exception.RelatedTransactionsNotFoundException;
import org.ripoll.model.Transaction;
import org.ripoll.model.TotalForRelatedTransactions;
import org.ripoll.model.TransactionRequest;
import org.ripoll.repository.ITransactionRepository;

import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;

public class TransactionService implements ITransactionService {

    final ITransactionRepository transactionRepository;

    public TransactionService(ITransactionRepository TransactionRepository) {
        this.transactionRepository = TransactionRepository;
    }

    @Override
    public void saveTransaction(long transactionId, TransactionRequest transactionRequest) {
        transactionRepository.save(new Transaction(transactionId, transactionRequest));
    }

    @Override
    public Set<Long> getAllTransactionsByType(String type) {
        return transactionRepository.getAllTransactionsByType(type)
                .stream()
                .map(Transaction::transactionId)
                .collect(Collectors.toSet());
    }

    @Override
    public TotalForRelatedTransactions getTotalForRelatedTransactions(long transactionId) {
        Set<Transaction> allAssociatedTransactions = transactionRepository.getAllAssociatedTransactions(transactionId);

        if (allAssociatedTransactions.isEmpty()) {
            throw new RelatedTransactionsNotFoundException(transactionId);
        }

        BigDecimal total =  allAssociatedTransactions.stream()
                .map(Transaction::amount)
                .map(BigDecimal::valueOf)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return new TotalForRelatedTransactions(total);
    }
}
