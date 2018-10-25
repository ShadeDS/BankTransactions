package com.nulianov.bankaccount.domain;

import java.math.BigDecimal;

public class BankTransactionDetails {
    private BigDecimal amount;
    private long timeStampMillis;

    public BankTransactionDetails() {
    }

    public BankTransactionDetails(BigDecimal amount, long timeStampMillis) {
        this.amount = amount;
        this.timeStampMillis = timeStampMillis;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public long getTimeStampMillis() {
        return timeStampMillis;
    }
}
