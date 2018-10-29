package com.nulianov.bankaccount.service;

import com.nulianov.bankaccount.domain.TransactionDetails;
import com.nulianov.bankaccount.exception.IllegalAmountOfMoneyForTransaction;
import com.nulianov.bankaccount.exception.InsufficientFundsException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public interface AccountService {
    /**
     * If account exists in storage, returns actual balance,
     * throws exception otherwise
     * @param username username of current logged user
     * @return user account balance
     * @throws EntityNotFoundException account was not found in storage
     */
    BigDecimal getBalance(String username, UUID accountId) throws EntityNotFoundException;

    /**
     * If account exists in storage, returns statement as list of transactions,
     * throws exception otherwise
     * @param username username of current logged users
     * @return list of transactions
     * @throws EntityNotFoundException account was not found in storage
     */
    List<TransactionDetails> getStatement(String username, UUID accountId) throws EntityNotFoundException;

    /**
     * If account exists in storage and amount of money to deposit is correct, processes deposit and returns actual balance,
     * throws exception otherwise
     * @param transactionDetails details about transaction
     * @return user account balance
     * @throws EntityNotFoundException account was not found in storage
     * @throws IllegalAmountOfMoneyForTransaction amount of money to deposit is incorrect
     */
    BigDecimal deposit(String username, TransactionDetails transactionDetails) throws EntityNotFoundException, IllegalAmountOfMoneyForTransaction;

    /**
     * If account exists in storage, has sufficient funds and amount of money to withdraw is correct,
     * processes deposit and returns actual balance, throws exception otherwise
     * @param transactionDetails details about transaction
     * @return user account balance
     * @throws EntityNotFoundException account was not found in storage
     * @throws IllegalAmountOfMoneyForTransaction amount of money to deposit is incorrect
     * @throws InsufficientFundsException account doesn't have sufficient funds to withdraw
     */
    BigDecimal withdraw(String username, TransactionDetails transactionDetails) throws EntityNotFoundException, IllegalAmountOfMoneyForTransaction, InsufficientFundsException;
}
