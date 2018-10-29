package com.nulianov.bankaccount.service.impl;

import com.nulianov.bankaccount.domain.Account;
import com.nulianov.bankaccount.domain.TransactionDetails;
import com.nulianov.bankaccount.repository.AccountRepository;
import com.nulianov.bankaccount.repository.TransactionDetailsRepository;
import com.nulianov.bankaccount.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.*;


public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionDetailsRepository transactionDetailsRepository;

    @Autowired
    List<ExecutorService> executors;

    @Override
    public BigDecimal getBalance(String username) throws Exception {
        System.out.println("Get balance for user " + username + " on executor " + username.hashCode() % executors.size());
        return executors.get(username.hashCode() % executors.size()).submit(() -> getBalanceInternal(username)).get();
    }

    private BigDecimal getBalanceInternal(String username) throws Exception {
        System.out.println("Balance getting completed for " + username);
        return accountRepository.findByUsername(username).orElseThrow(() -> new Exception("User " + username + " doesn't exist")).getBalance();
    }

    @Override
    public List<TransactionDetails> getStatement(String username) throws Exception {
        System.out.println("Get statement for user " + username + " on executor " + username.hashCode() % executors.size());
        return executors.get(username.hashCode() % executors.size()).submit(() -> getStatementInternal(username)).get();
    }

    public List<TransactionDetails> getStatementInternal(String username) {
        List<TransactionDetails> td = transactionDetailsRepository.findByUserName(username);
        System.out.println("Found transfers for user with name " + username + ": " + td);
        return td;
    }

    @Override
    public BigDecimal deposit(TransactionDetails transactionDetails) throws Exception {
        System.out.println("Deposit for user " + transactionDetails.getUserName() + " on executor " + transactionDetails.getUserName().hashCode() % executors.size());
        return executors.get(
                transactionDetails.getUserName().hashCode() % executors.size())
                .submit(() -> depositInternal(transactionDetails)).get();
    }

    private BigDecimal depositInternal(TransactionDetails transactionDetails) throws Exception {
        Account account = accountRepository.findByUsername(transactionDetails.getUserName()).orElseThrow(() -> new Exception("User " + transactionDetails.getUserName() + " doesn't exist"));
        BigDecimal currentBalance = account.deposit(transactionDetails.getAmount());
        if (transactionDetails.getTimeStampMillis() == 0) {
            transactionDetails.setCurrentTime();
        }
        transactionDetailsRepository.save(transactionDetails);
        accountRepository.save(account);
        System.out.println("Deposit completed for " + transactionDetails.getUserName());
        return currentBalance;
    }

    @Override
    public BigDecimal withdraw(TransactionDetails transactionDetails) throws Exception {
        System.out.println("Withdraw for user " + transactionDetails.getUserName() + " on executor " + transactionDetails.getUserName().hashCode() % executors.size());
        return executors.get(
                transactionDetails.getUserName().hashCode() % executors.size())
                .submit(() -> withdrawInternal(transactionDetails)).get();
    }

    private BigDecimal withdrawInternal(TransactionDetails transactionDetails) throws Exception {
        Account account = accountRepository.findByUsername(transactionDetails.getUserName()).orElseThrow(() -> new Exception("User " + transactionDetails.getUserName() + " doesn't exist"));
        BigDecimal currentBalance = account.withdraw(transactionDetails.getAmount());
        if (transactionDetails.getTimeStampMillis() == 0) {
            transactionDetails.setCurrentTime();
        }
        transactionDetailsRepository.save(transactionDetails.convertToWithdraw());
        accountRepository.save(account);
        System.out.println("Withdraw completed for " + transactionDetails.getUserName());
        return currentBalance;
    }
}
