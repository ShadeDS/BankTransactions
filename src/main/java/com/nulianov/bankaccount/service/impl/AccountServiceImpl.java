package com.nulianov.bankaccount.service.impl;

import com.nulianov.bankaccount.domain.BankTransactionDetails;
import com.nulianov.bankaccount.repository.AccountRepository;
import com.nulianov.bankaccount.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class AccountServiceImpl implements AccountService {
    private static final String username = "john";

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public BigDecimal getBalance() {
        return accountRepository.findByUsername(username).getBalance();
    }

    @Override
    public List<BankTransactionDetails> getStatement() {
        return new ArrayList<>();
    }

    @Override
    public BigDecimal deposit(BankTransactionDetails transactionDetails) throws Exception {
        if (transactionDetails.getAmount() == null || transactionDetails.getAmount().signum() < 0) {
            throw new Exception("Incorrect amount money to deposit");
        }
        return transactionDetails.getAmount();
    }

    @Override
    public BigDecimal withdraw(BankTransactionDetails transactionDetails) throws Exception {
        if (transactionDetails.getAmount() == null || transactionDetails.getAmount().signum() < 0) {
            throw new Exception("Incorrect amount money to withdraw");
        }
        return transactionDetails.getAmount();
    }
}
