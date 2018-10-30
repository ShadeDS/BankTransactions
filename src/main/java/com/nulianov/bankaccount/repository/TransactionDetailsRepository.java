package com.nulianov.bankaccount.repository;

import com.nulianov.bankaccount.domain.TransactionDetails;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface TransactionDetailsRepository extends CrudRepository<TransactionDetails, Long> {
    List<TransactionDetails> findByAccountIdOrderByTimeStampMillis(UUID accountId);
}
