package com.nulianov.bankaccount.service;

import com.nulianov.bankaccount.domain.TransactionDetails;
import com.nulianov.bankaccount.exception.IllegalAmountOfMoneyForTransaction;
import com.nulianov.bankaccount.exception.InsufficientFundsException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.List;

@Service
public interface AccountService {
    BigDecimal getBalance(String username) throws EntityNotFoundException;

    List<TransactionDetails> getStatement(String username) throws EntityNotFoundException;

    BigDecimal deposit(TransactionDetails transactionDetails) throws EntityNotFoundException, IllegalAmountOfMoneyForTransaction;

    BigDecimal withdraw(TransactionDetails transactionDetails) throws EntityNotFoundException, IllegalAmountOfMoneyForTransaction, InsufficientFundsException;
}
