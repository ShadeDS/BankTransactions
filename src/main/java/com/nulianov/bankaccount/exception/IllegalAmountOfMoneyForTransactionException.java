package com.nulianov.bankaccount.exception;

import java.math.BigDecimal;

public class IllegalAmountOfMoneyForTransactionException extends IllegalArgumentException {
    private final String message;

    public IllegalAmountOfMoneyForTransactionException(BigDecimal amount) {
        this.message = "Illegal amount money to process: " + amount;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
