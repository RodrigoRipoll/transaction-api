package org.ripoll.e2e;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.ripoll.model.Transaction;
import org.ripoll.model.TransactionRequest;
import org.ripoll.repository.ITransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Random;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class TransactionControllerE2E {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ITransactionRepository ITransactionRepository;

    @Test
    public void saveTransaction_shouldReturnOk() throws Exception {
        // Given
        long transactionId = new Random().nextLong();
        TransactionRequest transactionRequest = new TransactionRequest(10.0, "cars", null);
        String requestBody = objectMapper.writeValueAsString(transactionRequest);
        int initialCount = ITransactionRepository.getTransactionsDataStorage().size();

        // When
        mockMvc.perform(put("/transactions/{transaction_id}", transactionId)
                        .contentType(APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        int finalCount = ITransactionRepository.getTransactionsDataStorage().size();
        assertEquals(1, finalCount - initialCount);
    }

    @Test
    public void getAllTransactionsByType_shouldReturnEmptyList() throws Exception {
        // Given
        String transactionType = "bikes";

        // When
        mockMvc.perform(get("/transactions/types/{type}", transactionType))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    public void getAllTransactionsByType_shouldReturnAList() throws Exception {
        // Given
        String transactionType = "bikes";
        Transaction transactionBikes1 = new Transaction(1L, 10.0, "bikes", null);
        Transaction transactionBikes2 = new Transaction(2L,20.0, "bikes", null);
        ITransactionRepository.save(transactionBikes1);
        ITransactionRepository.save(transactionBikes2);

        // When
        mockMvc.perform(get("/transactions/types/{type}", transactionType))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.*", containsInAnyOrder(1, 2)))
                .andReturn();
    }


    @Test
    public void getTotalForRelatedTransactions_shouldReturnOk() throws Exception {
        // Given
        Long transactionId1 = 1L;
        Long transactionId2 = 2L;
        Long transactionId3 = 3L;
        Long transactionId4 = 4L;
        Transaction transaction1 = new Transaction(transactionId1, new TransactionRequest(10.0, "cars", null));
        Transaction transaction2 = new Transaction(transactionId2, new TransactionRequest(20.0, "cars", transactionId1));
        Transaction transaction3 = new Transaction(transactionId3, new TransactionRequest(30.1, "shopping", transactionId1));
        Transaction transaction4 = new Transaction(transactionId4, new TransactionRequest(5.5, "tech", transactionId3));
        ITransactionRepository.save(transaction1);
        ITransactionRepository.save(transaction2);
        ITransactionRepository.save(transaction3);
        ITransactionRepository.save(transaction4);

        // Act and Assert for Transaction1
        mockMvc.perform(get("/transactions/sum/{transaction_id}", transactionId1))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.sum").value(65.6))
                .andReturn();

        // Act and Assert for Transaction2
        mockMvc.perform(get("/transactions/sum/{transaction_id}", transactionId2))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.sum").value(20.0))
                .andReturn();

        // Act and Assert for Transaction3
        mockMvc.perform(get("/transactions/sum/{transaction_id}", transactionId3))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.sum").value(35.6))
                .andReturn();

        // Act and Assert for Transaction4
        mockMvc.perform(get("/transactions/sum/{transaction_id}", transactionId4))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.sum").value(5.5))
                .andReturn();
    }

    @Test
    public void getTotalForRelatedTransactions_shouldReturnNotFound() throws Exception {
        // Given
        Long transactionId = new Random().nextLong();

        // Act and Assert
        mockMvc.perform(get("/transactions/sum/{transaction_id}", transactionId))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(404))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", containsString("could not get any transaction related to")))
                .andReturn();
    }
}
