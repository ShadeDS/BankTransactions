package com.nulianov.bankaccount.repository;

import com.nulianov.bankaccount.domain.TransactionDetails;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TransactionDetailsRepository extends CrudRepository<TransactionDetails, Long> {
    List<TransactionDetails> findByUserId(Long id);
}
