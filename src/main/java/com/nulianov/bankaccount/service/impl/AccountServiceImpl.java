package com.nulianov.bankaccount.service.impl;

import com.nulianov.bankaccount.domain.Account;
import com.nulianov.bankaccount.domain.TransactionDetails;
import com.nulianov.bankaccount.repository.AccountRepository;
import com.nulianov.bankaccount.repository.TransactionDetailsRepository;
import com.nulianov.bankaccount.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


public class AccountServiceImpl implements AccountService {
    private static final String username = "john";

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionDetailsRepository transactionDetailsRepository;

    @Override
    public BigDecimal getBalance() {
        return accountRepository.findByUsername(username).getBalance();
    }

    @Override
    public List<TransactionDetails> getStatement() {
        Account account = accountRepository.findByUsername(username);
        System.out.println("Found user with name " + username + " and id " + account.getId());
        List<TransactionDetails> td = transactionDetailsRepository.findByUserId(account.getId());
        System.out.println("Found transfers for user with name " + username + ": " + td);
        return td;
    }

    @Override
    public BigDecimal deposit(TransactionDetails transactionDetails) throws Exception {
        Account account = accountRepository.findById(transactionDetails.getUserId()).orElseThrow(() -> new Exception("User with id " + transactionDetails.getId() + " doesn't exist"));
        BigDecimal currentBalance = account.deposit(transactionDetails.getAmount());
        if (transactionDetails.getTimeStampMillis() == 0) {
            transactionDetails.setCurrentTime();
        }
        transactionDetailsRepository.save(transactionDetails);
        accountRepository.save(account);
        return currentBalance;
    }

    @Override
    public BigDecimal withdraw(TransactionDetails transactionDetails) throws Exception {
        Account account = accountRepository.findById(transactionDetails.getUserId()).orElseThrow(() -> new Exception("User with id " + transactionDetails.getId() + " doesn't exist"));
        BigDecimal currentBalance = account.withdraw(transactionDetails.getAmount());
        if (transactionDetails.getTimeStampMillis() == 0) {
            transactionDetails.setCurrentTime();
        }
        transactionDetailsRepository.save(transactionDetails.convertToWithdraw());
        accountRepository.save(account);
        return currentBalance;
    }
}
