package org.ripoll.config;

import org.ripoll.repository.ITransactionRepository;
import org.ripoll.service.ITransactionService;
import org.ripoll.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TransactionServiceConfig {

    @Bean
    @Autowired
    public ITransactionService transactionService(ITransactionRepository transactionRepository) {
        return new TransactionService(transactionRepository);
    }
}
