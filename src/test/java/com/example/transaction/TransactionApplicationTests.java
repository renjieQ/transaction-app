package com.example.transaction;

import com.example.transaction.model.Transaction;
import com.example.transaction.repository.TransactionRepository;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class TransactionApplicationTests {

    @Autowired
    TransactionRepository repository;

    @Test
    void testCreateTransaction() {
        Transaction t = new Transaction(
                UUID.randomUUID(),
                "DEPOSIT",
                BigDecimal.valueOf(1000),
                "Initial deposit",
                LocalDateTime.now()
        );

        repository.save(t);
        assertThat(repository.findById(t.getId())).isPresent();
    }

    @RepeatedTest(5)
    void stressTestCreateTransaction() throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(10);

        for (int i = 0; i < 100; i++) {
            executor.execute(() -> {
                Transaction tx = new Transaction(
                        UUID.randomUUID(),
                        "DEPOSIT",
                        BigDecimal.valueOf(Math.random() * 1000),
                        "Stress test",
                        LocalDateTime.now()
                );
                repository.save(tx);
            });
        }

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);

        assertThat(repository.count()).isGreaterThanOrEqualTo(100);
    }

    @Test
    void stressTestConcurrentReadAndPagination() throws InterruptedException {
        for (int i = 0; i < 200; i++) {
            repository.save(new Transaction(UUID.randomUUID(), "READ", BigDecimal.ONE, "pagination", LocalDateTime.now()));
        }

        ExecutorService executor = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 50; i++) {
            final int page = i % 5;
            executor.execute(() -> {
                List<Transaction> list = repository.findAll().subList(page * 10, page * 10 + 10);
                assertThat(list).hasSize(10);
            });
        }
        executor.shutdown();
        executor.awaitTermination(30, TimeUnit.SECONDS);
    }

    @Test
    void stressTestConcurrentDelete() throws InterruptedException {
        for (int i = 0; i < 50; i++) {
            repository.save(new Transaction(UUID.randomUUID(), "DEL", BigDecimal.TEN, "delete", LocalDateTime.now()));
        }

        List<Transaction> all = repository.findAll();
        ExecutorService executor = Executors.newFixedThreadPool(5);
        for (Transaction t : all) {
            executor.execute(() -> {
                Optional<Transaction> tx = repository.findById(t.getId());
                tx.ifPresent(value -> repository.deleteById(value.getId()));
            });
        }
        executor.shutdown();
        executor.awaitTermination(30, TimeUnit.SECONDS);
        assertThat(repository.findAll()).isEmpty();
    }

    @Test
    void stressTestConcurrentUpdate() throws InterruptedException {
        UUID txId = UUID.randomUUID();
        repository.save(new Transaction(txId, "UPDATE", BigDecimal.valueOf(100), "original", LocalDateTime.now()));

        ExecutorService executor = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 50; i++) {
            int finalI = i;
            executor.execute(() -> {
                Optional<Transaction> optionalTx = repository.findById(txId);
                optionalTx.ifPresent(tx -> {
                    tx.setDescription("Updated-" + finalI);
                    tx.setAmount(BigDecimal.valueOf(100 + finalI));
                    repository.save(tx);
                });
            });
        }

        executor.shutdown();
        executor.awaitTermination(30, TimeUnit.SECONDS);

        Optional<Transaction> updated = repository.findById(txId);
        assertThat(updated).isPresent();
        System.out.println("Final updated transaction: " + updated.get().getDescription() + " / " + updated.get().getAmount());
    }
}
