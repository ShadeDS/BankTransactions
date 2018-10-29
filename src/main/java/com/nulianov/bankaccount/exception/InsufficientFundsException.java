package com.nulianov.bankaccount.exception;

/**
 * Shows that account has no sufficient funds to withdraw
 */
public class InsufficientFundsException extends IllegalArgumentException  {
    private final String message;

    public InsufficientFundsException(String username) {
        this.message = "Account " + username + " has no sufficient funds to withdraw";
    }

    @Override
    public String getMessage() {
        return message;
    }
}
