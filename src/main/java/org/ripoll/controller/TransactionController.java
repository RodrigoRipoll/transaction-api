package org.ripoll.controller;

import org.ripoll.model.TotalForRelatedTransactions;
import org.ripoll.model.TransactionRequest;
import org.ripoll.service.ITransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired private final ITransactionService transactionService;

    public TransactionController(ITransactionService transactionService) {
        this.transactionService = transactionService;
    }
    @PutMapping("/{transaction_id}")
    public ResponseEntity<Void> saveTransaction(@PathVariable(name = "transaction_id") long transactionId,
                                                @Validated @RequestBody TransactionRequest transactionRequest) {
        transactionService.saveTransaction(transactionId, transactionRequest);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/types/{type}")
    public ResponseEntity<Set<Long>> getAllTransactionsByType(@PathVariable String type) {
        return Optional.of(transactionService.getAllTransactionsByType(type))
                .filter(transactionsByType -> !transactionsByType.isEmpty())
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/sum/{transaction_id}")
    public ResponseEntity<TotalForRelatedTransactions> getTotalForRelatedTransactions(@PathVariable(name = "transaction_id") long transactionId) {
        TotalForRelatedTransactions total = transactionService.getTotalForRelatedTransactions(transactionId);
        return ResponseEntity.ok(total);
    }
}
