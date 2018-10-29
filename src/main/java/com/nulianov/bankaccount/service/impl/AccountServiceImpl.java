package com.nulianov.bankaccount.service.impl;

import com.nulianov.bankaccount.domain.Account;
import com.nulianov.bankaccount.domain.TransactionDetails;
import com.nulianov.bankaccount.exception.IllegalAmountOfMoneyForTransaction;
import com.nulianov.bankaccount.exception.InsufficientFundsException;
import com.nulianov.bankaccount.repository.AccountRepository;
import com.nulianov.bankaccount.repository.TransactionDetailsRepository;
import com.nulianov.bankaccount.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.List;


public class AccountServiceImpl implements AccountService {
    private final static Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionDetailsRepository transactionDetailsRepository;

    @Override
    public BigDecimal getBalance(String username) throws EntityNotFoundException {
        logger.debug("Get balance for user {}", username);
        return accountRepository.findByUsername(username).orElseThrow(() -> new EntityNotFoundException("User " + username + " doesn't exist")).getBalance();
    }

    @Override
    public List<TransactionDetails> getStatement(String username) throws EntityNotFoundException {
        logger.debug("Get statement for user {}", username);
        accountRepository.findByUsername(username).orElseThrow(() -> new EntityNotFoundException("User " + username + " doesn't exist"));
        List<TransactionDetails> td = transactionDetailsRepository.findByUserNameOrderByTimeStampMillis(username);
        logger.debug("Found {} transfers for user {}", td.size(), username);
        return td;
    }

    @Override
    public BigDecimal deposit(TransactionDetails transactionDetails) throws EntityNotFoundException, IllegalAmountOfMoneyForTransaction {
        logger.debug("Deposit for user {}", transactionDetails.getUserName());
        Account account = accountRepository.findByUsername(transactionDetails.getUserName()).orElseThrow(() -> new EntityNotFoundException("User " + transactionDetails.getUserName() + " doesn't exist"));
        BigDecimal currentBalance = account.deposit(transactionDetails.getAmount());
        if (transactionDetails.getTimeStampMillis() == 0) {
            transactionDetails.setCurrentTime();
        }
        transactionDetailsRepository.save(transactionDetails);
        accountRepository.save(account);
        logger.debug("Deposit completed for user {}", transactionDetails.getUserName());
        return currentBalance;
    }

    @Override
    public BigDecimal withdraw(TransactionDetails transactionDetails) throws EntityNotFoundException, IllegalAmountOfMoneyForTransaction, InsufficientFundsException {
        logger.debug("Withdraw for user {}",  transactionDetails.getUserName());
        Account account = accountRepository.findByUsername(transactionDetails.getUserName()).orElseThrow(() -> new EntityNotFoundException("User " + transactionDetails.getUserName() + " doesn't exist"));
        BigDecimal currentBalance = account.withdraw(transactionDetails.getAmount());
        if (transactionDetails.getTimeStampMillis() == 0) {
            transactionDetails.setCurrentTime();
        }
        transactionDetailsRepository.save(transactionDetails.convertToWithdraw());
        accountRepository.save(account);
        logger.debug("Withdraw completed for user {}", transactionDetails.getUserName());
        return currentBalance;
    }
}
