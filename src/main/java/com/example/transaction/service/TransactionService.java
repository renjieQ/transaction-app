package com.example.transaction.service;

import com.example.transaction.model.Transaction;
import com.example.transaction.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository repository;

    @Transactional
    public Transaction create(Transaction transaction) {
        transaction.setId(UUID.randomUUID());
        transaction.setTimestamp(java.time.LocalDateTime.now());
        return repository.save(transaction);
    }

    @Transactional
    public Transaction update(UUID id, Transaction updated) {
        Transaction transaction = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Transaction not found"));
        transaction.setAmount(updated.getAmount());
        transaction.setType(updated.getType());
        transaction.setDescription(updated.getDescription());
        return repository.save(transaction);
    }

    @Transactional
    public void delete(UUID id) {
        if (!repository.existsById(id)) {
            throw new NoSuchElementException("Transaction not found");
        }
        repository.deleteById(id);
    }

    @Cacheable("transactions")
    public Page<Transaction> list(int page, int size) {
        return repository.findAll(PageRequest.of(page, size));
    }

    public Transaction get(UUID id) {
        return repository.findById(id).orElseThrow();
    }
}