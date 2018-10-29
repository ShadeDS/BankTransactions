package com.nulianov.bankaccount.exception;

import java.math.BigDecimal;

public class IllegalAmountOfMoneyForTransaction extends IllegalArgumentException{
    private final String message;

    public IllegalAmountOfMoneyForTransaction(BigDecimal amount) {
        this.message = "Illegal amount money to process: " + amount;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
