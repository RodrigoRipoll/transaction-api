package org.ripoll.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.ripoll.exception.RelatedTransactionsNotFoundException;
import org.ripoll.model.TotalForRelatedTransactions;
import org.ripoll.model.TransactionRequest;
import org.ripoll.service.ITransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class TransactionControllerTest {

    private static final String TYPE = "cars";

    @Mock
    private ITransactionService transactionService;

    @InjectMocks
    private TransactionController transactionController;

    @Test
    public void saveTransaction_returnsOk() {
        long transactionId = 1L;
        TransactionRequest request = new TransactionRequest(100.0, "type", null);
        ResponseEntity<Void> response = transactionController.saveTransaction(transactionId, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(transactionService, times(1)).saveTransaction(transactionId, request);
    }

    @Test
    public void getAllTransactionsByType_shouldReturnOk() {
        Set<Long> transactionsByType = Set.of(1L, 2L);
        when(transactionService.getAllTransactionsByType(TYPE)).thenReturn(transactionsByType);

        ResponseEntity<Set<Long>> response = transactionController.getAllTransactionsByType(TYPE);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(transactionsByType, response.getBody());
    }

    @Test
    void getAllTransactionsByType_shouldReturnNotFound() {
        when(transactionService.getAllTransactionsByType(TYPE)).thenReturn(Collections.emptySet());

        ResponseEntity<Set<Long>> response = transactionController.getAllTransactionsByType(TYPE);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void getTotalForRelatedTransactions_shouldReturnOk() {
        long transactionId = 1L;
        TotalForRelatedTransactions total = new TotalForRelatedTransactions(BigDecimal.valueOf(120));
        when(transactionService.getTotalForRelatedTransactions(transactionId)).thenReturn(total);

        ResponseEntity<TotalForRelatedTransactions> response = transactionController.getTotalForRelatedTransactions(transactionId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(total, response.getBody());
    }

    @Test
    void getTotalForRelatedTransactions_shouldReturnNotFound() {
        long transactionId = 1L;
        when(transactionService.getTotalForRelatedTransactions(transactionId)).thenThrow(new RelatedTransactionsNotFoundException(transactionId));

        assertThrows(
                RelatedTransactionsNotFoundException.class, () -> transactionController.getTotalForRelatedTransactions(transactionId)
        );
    }
}