package com.nulianov.bankaccount.service;

import com.nulianov.bankaccount.domain.TransactionDetails;
import com.nulianov.bankaccount.exception.IllegalAmountOfMoneyForTransactionException;
import com.nulianov.bankaccount.exception.InsufficientFundsException;
import com.nulianov.bankaccount.exception.RequestProcessingException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Service
public interface AccountService {
    /**
     * If account exists in storage, returns actual balance,
     * throws exception otherwise
     * @param username username of current logged user
     * @param accountId id for bank account
     * @return user account balance
     * @throws EntityNotFoundException account was not found in storage
     * @throws RequestProcessingException internal error occurred
     */
    BigDecimal getBalance(String username, UUID accountId) throws EntityNotFoundException, RequestProcessingException;

    /**
     * If account exists in storage, returns statement as list of transactions,
     * throws exception otherwise
     * @param username username of current logged users
     * @param accountId id for bank account
     * @return list of transactions
     * @throws EntityNotFoundException account was not found in storage
     * @throws RequestProcessingException internal error occurred
     */
    List<TransactionDetails> getStatement(String username, UUID accountId) throws EntityNotFoundException, RequestProcessingException;

    /**
     * If account exists in storage and amount of money to deposit is correct, processes deposit and returns actual balance,
     * throws exception otherwise
     * @param transactionDetails details about transaction
     * @return user account balance
     * @throws EntityNotFoundException account was not found in storage
     * @throws IllegalAmountOfMoneyForTransactionException amount of money to deposit is incorrect
     * @throws RequestProcessingException internal error occurred
     */
    BigDecimal deposit(String username, TransactionDetails transactionDetails) throws EntityNotFoundException, IllegalAmountOfMoneyForTransactionException, RequestProcessingException;

    /**
     * If account exists in storage, has sufficient funds and amount of money to withdraw is correct,
     * processes deposit and returns actual balance, throws exception otherwise
     * @param transactionDetails details about transaction
     * @return user account balance
     * @throws EntityNotFoundException account was not found in storage
     * @throws IllegalAmountOfMoneyForTransactionException amount of money to deposit is incorrect
     * @throws InsufficientFundsException account doesn't have sufficient funds to withdraw
     * @throws RequestProcessingException internal error occurred
     */
    BigDecimal withdraw(String username, TransactionDetails transactionDetails) throws EntityNotFoundException, IllegalAmountOfMoneyForTransactionException, InsufficientFundsException, RequestProcessingException;

    /**
     * Creates new account for provided user
     * @param username name of user
     * @return id of new account
     * @throws EntityNotFoundException user was not found in storage
     * @throws RequestProcessingException internal error occurred
     */
    UUID create(String username) throws EntityNotFoundException, RequestProcessingException;
}
