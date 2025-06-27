package com.example.transaction;

import com.example.transaction.model.Transaction;
import com.example.transaction.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class TransactionApplicationTests {

    @Autowired
    TransactionRepository repository;

    @Test
    void testCreateTransaction() {
        Transaction t = Transaction.builder()
                .id(UUID.randomUUID())
                .type("DEPOSIT")
                .amount(BigDecimal.valueOf(1000))
                .description("Initial deposit")
                .timestamp(java.time.LocalDateTime.now())
                .build();

        repository.save(t);
        assertThat(repository.findById(t.getId())).isPresent();
    }
}