package org.ripoll.config;

import org.ripoll.repository.ITransactionRepository;
import org.ripoll.repository.TransactionRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TransactionRepositoryConfig {

    @Bean
    public ITransactionRepository transactionRepository() {
        return new TransactionRepository();
    }
}
