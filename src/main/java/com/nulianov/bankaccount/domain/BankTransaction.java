package com.nulianov.bankaccount.domain;

import java.math.BigDecimal;

public class BankTransaction {
    private BigDecimal amount;
    private long timeStampMillis;

    public BankTransaction(BigDecimal amount, long timeStampMillis) {
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
