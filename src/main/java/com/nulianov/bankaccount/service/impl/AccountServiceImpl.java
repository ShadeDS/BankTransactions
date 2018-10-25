package com.nulianov.bankaccount.service.impl;

import com.nulianov.bankaccount.domain.BankTransactionDetails;
import com.nulianov.bankaccount.service.AccountService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class AccountServiceImpl implements AccountService {

    @Override
    public BigDecimal getBalance() {
        return new BigDecimal(0);
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
