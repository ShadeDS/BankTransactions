package com.nulianov.bankaccount.service;

import com.nulianov.bankaccount.domain.BankTransaction;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public interface AccountService {
    String getBalance();

    String getStatement();

    String deposit();

    String withdraw();
}
