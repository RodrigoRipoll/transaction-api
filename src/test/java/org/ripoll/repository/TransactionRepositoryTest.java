package org.ripoll.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.ripoll.model.Transaction;

import java.util.Random;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class TransactionRepositoryTest {

    private ITransactionRepository ITransactionRepository;

    @BeforeEach
    void setUp() {
        ITransactionRepository = new TransactionRepository();
    }

    @Test
    void save() {
        // Arrange
        Transaction transaction = new Transaction(new Random().nextLong(), 100, "type", null);

        // Act
        ITransactionRepository.save(transaction);

        // Assert
        assertEquals(
                transaction,
                ITransactionRepository.getTransactionsDataStorage().get(transaction.transactionId()),
                "asd"
                );
        assertNotEquals(
                transaction,
                ITransactionRepository.getTransactionsDataStorage().get(new Random().nextLong()),
                ""
        );
    }

    @Test
    void getTransactionsByType() {
        // Arrange
        Transaction transaction1 = new Transaction(1L, 100, "type", null);
        Transaction transaction2 = new Transaction(2L, 200, "type", null);
        Transaction transaction3 = new Transaction(3L, 200, "cars", null);
        ITransactionRepository.save(transaction1);
        ITransactionRepository.save(transaction2);
        ITransactionRepository.save(transaction3);

        // Act
        Set<Transaction> transactionsType = ITransactionRepository.getAllTransactionsByType("type");

        // Assert
        Assertions.assertAll(
                "asdasd",
                () -> assertEquals(2, transactionsType.size()),
                () -> assertTrue(transactionsType.contains(transaction1)),
                () -> assertTrue(transactionsType.contains(transaction2))
        );

        // Act
        Set<Transaction> transactionsCars = ITransactionRepository.getAllTransactionsByType("cars");

        // Assert
        Assertions.assertAll(
                "asdasd",
                () -> assertEquals(1, transactionsCars.size()),
                () -> assertTrue(transactionsCars.contains(transaction3))
        );
    }

    @Test
    void getAllAssociatedTransactions() {
        // Arrange
        Transaction transaction1 = new Transaction(1L, 100, "type", null);
        Transaction transaction2 = new Transaction(2L, 200, "type", 1L);
        Transaction transaction3 = new Transaction(3L, 300, "type", 2L);
        Transaction transaction4 = new Transaction(4L, 1000, "type", 2L);
        ITransactionRepository.save(transaction1);
        ITransactionRepository.save(transaction2);
        ITransactionRepository.save(transaction3);
        ITransactionRepository.save(transaction4);

        // Act for transaction1
        Set<Transaction> transactionsFor1 = ITransactionRepository.getAllAssociatedTransactions(1L);

        // Assert for transaction1
        Assertions.assertAll(
                "asdasd",
                () -> assertEquals(4, transactionsFor1.size()),
                () -> assertTrue(transactionsFor1.contains(transaction1)),
                () -> assertTrue(transactionsFor1.contains(transaction2)),
                () -> assertTrue(transactionsFor1.contains(transaction3)),
                () -> assertTrue(transactionsFor1.contains(transaction4))
        );

        // Act for transaction2
        Set<Transaction> transactionsFor2 = ITransactionRepository.getAllAssociatedTransactions(2L);

        // Assert for transaction2
        Assertions.assertAll(
                "asdasd",
                () -> assertEquals(3, transactionsFor2.size()),
                () -> assertTrue(transactionsFor2.contains(transaction2)),
                () -> assertTrue(transactionsFor2.contains(transaction3)),
                () -> assertTrue(transactionsFor2.contains(transaction4))
        );

        // Act for transaction2
        Set<Transaction> transactionsFor3 = ITransactionRepository.getAllAssociatedTransactions(3L);

        // Assert for transaction3
        Assertions.assertAll(
                "asdasd",
                () -> assertEquals(1, transactionsFor3.size()),
                () -> assertTrue(transactionsFor3.contains(transaction3))
        );

        // Act for transaction4
        Set<Transaction> transactionsFor4 = ITransactionRepository.getAllAssociatedTransactions(4L);

        // Assert for transaction2
        Assertions.assertAll(
                "asdasd",
                () -> assertEquals(1, transactionsFor4.size()),
                () -> assertTrue(transactionsFor4.contains(transaction4))
        );
    }
}