package com.nulianov.bankaccount.service.impl;

import com.nulianov.bankaccount.domain.Account;
import com.nulianov.bankaccount.domain.TransactionDetails;
import com.nulianov.bankaccount.repository.AccountRepository;
import com.nulianov.bankaccount.repository.TransactionDetailsRepository;
import com.nulianov.bankaccount.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;


public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionDetailsRepository transactionDetailsRepository;

    @Override
    public BigDecimal getBalance(String username) throws Exception {
        return accountRepository.findByUsername(username).orElseThrow(() -> new Exception("User " + username + " doesn't exist")).getBalance();
    }

    @Override
    public List<TransactionDetails> getStatement(String username) {
        List<TransactionDetails> td = transactionDetailsRepository.findByUserName(username);
        System.out.println("Found transfers for user with name " + username + ": " + td);
        return td;
    }

    @Override
    public BigDecimal deposit(TransactionDetails transactionDetails) throws Exception {
        Account account = accountRepository.findByUsername(transactionDetails.getUserName()).orElseThrow(() -> new Exception("User " + transactionDetails.getUserName() + " doesn't exist"));
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
        Account account = accountRepository.findByUsername(transactionDetails.getUserName()).orElseThrow(() -> new Exception("User " + transactionDetails.getUserName() + " doesn't exist"));
        BigDecimal currentBalance = account.withdraw(transactionDetails.getAmount());
        if (transactionDetails.getTimeStampMillis() == 0) {
            transactionDetails.setCurrentTime();
        }
        transactionDetailsRepository.save(transactionDetails.convertToWithdraw());
        accountRepository.save(account);
        return currentBalance;
    }
}
