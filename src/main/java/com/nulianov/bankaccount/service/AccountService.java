package com.nulianov.bankaccount.service;

import com.nulianov.bankaccount.domain.TransactionDetails;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public interface AccountService {
    BigDecimal getBalance(String username) throws Exception;

    List<TransactionDetails> getStatement(String username) throws Exception;

    BigDecimal deposit(TransactionDetails transactionDetails) throws Exception;

    BigDecimal withdraw(TransactionDetails transactionDetails) throws Exception;
}
