package com.nulianov.bankaccount.service;

import com.nulianov.bankaccount.domain.BankTransactionDetails;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public interface AccountService {
    BigDecimal getBalance();

    List<BankTransactionDetails> getStatement();

    BigDecimal deposit(BankTransactionDetails transactionDetails) throws Exception;

    BigDecimal withdraw(BankTransactionDetails transactionDetails) throws Exception;
}
