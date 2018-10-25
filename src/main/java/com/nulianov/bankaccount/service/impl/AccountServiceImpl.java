package com.nulianov.bankaccount.service.impl;

import com.nulianov.bankaccount.domain.BankTransaction;
import com.nulianov.bankaccount.service.AccountService;

import java.math.BigDecimal;
import java.util.List;


public class AccountServiceImpl implements AccountService {

    @Override
    public String getBalance() {
        return "balance";
    }

    @Override
    public String getStatement() {
        return "statement";
    }

    @Override
    public String deposit() {
        return "deposit";
    }

    @Override
    public String withdraw() {
        return "withdraw";
    }
}
