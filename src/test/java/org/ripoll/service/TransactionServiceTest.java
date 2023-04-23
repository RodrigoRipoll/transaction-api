package org.ripoll.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.ripoll.exception.RelatedTransactionsNotFoundException;
import org.ripoll.model.TotalForRelatedTransactions;
import org.ripoll.model.Transaction;
import org.ripoll.model.TransactionRequest;
import org.ripoll.repository.ITransactionRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransactionServiceTest {

    @Mock
    ITransactionRepository transactionRepository;

    ITransactionService transactionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        transactionService = new TransactionService(transactionRepository);
    }

    @Test
    void testSaveTransaction() {
        TransactionRequest transactionRequest = new TransactionRequest(100.0, "cars", null);
        long transactionId = 1L;
        Transaction transaction = new Transaction(transactionId, transactionRequest);

        transactionService.saveTransaction(transactionId, transactionRequest);

        verify(transactionRepository, times(1)).save(transaction);
    }

    @Test
    void testGetAllTransactionsByType_withData() {
        String typeTest = "cars";
        Transaction transaction1 = new Transaction(
                1L,
                new TransactionRequest(100.0, typeTest, null)
        );

        Transaction transaction2 = new Transaction(
                2L,
                new TransactionRequest(20.0, typeTest, null)
        );

        Set<Transaction> transactions = Set.of(transaction1, transaction2);

        when(transactionRepository.getAllTransactionsByType(typeTest)).thenReturn(transactions);

        Set<Long> expected = Set.of(1L, 2L);
        Set<Long> actual = transactionService.getAllTransactionsByType(typeTest);

        assertEquals(expected, actual);
    }

    @Test
    void testGetAllTransactionsByType_withoutData() {
        String typeTest = "cars";

        when(transactionRepository.getAllTransactionsByType(typeTest)).thenReturn(Set.of());

        Set<Long> actual = transactionService.getAllTransactionsByType(typeTest);

        assertTrue(actual.isEmpty());
    }

    @Test
    void testGetTotalForRelatedTransactions() {
        double firstAmount = 100.0;
        double secondAmount = 20.0;
        BigDecimal total = new BigDecimal(firstAmount + secondAmount);
        Transaction transaction1 = new Transaction(
                1L,
                new TransactionRequest(firstAmount, "cars", null)
        );
        Transaction transaction2 = new Transaction(
                2L,
                new TransactionRequest(secondAmount, "bikes", 1L)
        );

        Set<Transaction> transactions = new HashSet<>(Arrays.asList(transaction1, transaction2));
        when(transactionRepository.getAllAssociatedTransactions(1L)).thenReturn(transactions);

        TotalForRelatedTransactions expected = new TotalForRelatedTransactions(total);
        TotalForRelatedTransactions actual = transactionService.getTotalForRelatedTransactions(1L);

        assertEquals(
                expected.sum().setScale(2, RoundingMode.UP),
                actual.sum().setScale(2, RoundingMode.UP)
        );
    }

    @Test
    void testGetTotalForRelatedTransactions_throwRelatedTransactionsNotFoundException_whenDataNotFound() {
        // Arrange
        when(transactionRepository.getAllAssociatedTransactions(1L)).thenReturn(Set.of());

        // Act & Assert
        assertThrows(
                RelatedTransactionsNotFoundException.class, () -> transactionService.getTotalForRelatedTransactions(1L)
        );
    }
}