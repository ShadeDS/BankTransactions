package com.nulianov.bankaccount.service;

import com.nulianov.bankaccount.domain.TransactionDetails;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public interface AccountService {
    BigDecimal getBalance();

    List<TransactionDetails> getStatement();

    BigDecimal deposit(TransactionDetails transactionDetails) throws Exception;

    BigDecimal withdraw(TransactionDetails transactionDetails) throws Exception;
}
