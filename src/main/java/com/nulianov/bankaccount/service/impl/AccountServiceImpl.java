package com.nulianov.bankaccount.service.impl;

import com.nulianov.bankaccount.domain.Account;
import com.nulianov.bankaccount.domain.TransactionDetails;
import com.nulianov.bankaccount.domain.User;
import com.nulianov.bankaccount.exception.IllegalAmountOfMoneyForTransactionException;
import com.nulianov.bankaccount.exception.InsufficientFundsException;
import com.nulianov.bankaccount.repository.AccountRepository;
import com.nulianov.bankaccount.repository.TransactionDetailsRepository;
import com.nulianov.bankaccount.repository.UserRepository;
import com.nulianov.bankaccount.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;


public class AccountServiceImpl implements AccountService {
    private final static Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionDetailsRepository transactionDetailsRepository;

    @Override
    public BigDecimal getBalance(String username, UUID accountId) throws EntityNotFoundException {
        logger.debug("Get balance for user {} and account {}", username, accountId);
        User currentUser = getCurrentUserFromStorage(username);

        return getAccountForUserById(currentUser, accountId).getBalance();
    }

    @Override
    public List<TransactionDetails> getStatement(String username, UUID accountId) throws EntityNotFoundException {
        logger.debug("Get statement for user {} and account {}", username, accountId);

        User currentUser = getCurrentUserFromStorage(username);
        getAccountForUserById(currentUser, accountId);

        List<TransactionDetails> td = transactionDetailsRepository.findByAccountIdOrderByTimeStampMillis(accountId);
        logger.debug("Found {} transfers for user {}", td.size(), username);
        return td;
    }

    @Override
    public BigDecimal deposit(String username, TransactionDetails transactionDetails) throws EntityNotFoundException, IllegalAmountOfMoneyForTransactionException {
        logger.debug("Deposit for user {}", username);
        User currentUser = getCurrentUserFromStorage(username);
        Account account = getAccountForUserById(currentUser, transactionDetails.getAccountId());

        BigDecimal currentBalance = account.deposit(transactionDetails.getAmount());
        if (transactionDetails.getTimeStampMillis() == 0) {
            transactionDetails.setCurrentTime();
        }
        transactionDetailsRepository.save(transactionDetails);
        accountRepository.save(account);
        logger.debug("Deposit completed for user {}", transactionDetails.getAccountId());
        return currentBalance;
    }

    @Override
    public BigDecimal withdraw(String username, TransactionDetails transactionDetails) throws EntityNotFoundException, IllegalAmountOfMoneyForTransactionException, InsufficientFundsException {
        logger.debug("Withdraw for user {}",  username);
        User currentUser = getCurrentUserFromStorage(username);
        Account account = getAccountForUserById(currentUser, transactionDetails.getAccountId());

        BigDecimal currentBalance = account.withdraw(transactionDetails.getAmount());
        if (transactionDetails.getTimeStampMillis() == 0) {
            transactionDetails.setCurrentTime();
        }
        transactionDetailsRepository.save(transactionDetails.convertToWithdraw());
        accountRepository.save(account);
        logger.debug("Withdraw completed for user {}", transactionDetails.getAccountId());
        return currentBalance;
    }

    @Override
    public UUID create(String username) throws EntityNotFoundException {
        User currentUser = getCurrentUserFromStorage(username);
        Account account = new Account(currentUser, new BigDecimal(0));
        accountRepository.save(account);

        return account.getId();
    }

    private User getCurrentUserFromStorage(String username) throws EntityNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User " + username + " doesn't exist"));
    }

    private Account getAccountForUserById(User user, UUID accountId) throws EntityNotFoundException {
        return user.getAccounts().stream()
                .filter(account -> account.getId().equals(accountId))
                .findFirst()
                .orElseThrow(
                        () -> new EntityNotFoundException("Account " + accountId + " doesn't exist")
                );
    }


}
