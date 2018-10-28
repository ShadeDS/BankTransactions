package com.nulianov.bankaccount.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
public class TransactionDetails {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    private Long userId;
    private BigDecimal amount;
    private long timeStampMillis;

    public TransactionDetails() {
    }

    public TransactionDetails(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public long getTimeStampMillis() {
        return timeStampMillis;
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setCurrentTime(){
        this.timeStampMillis = Instant.now().toEpochMilli();
    }

    public TransactionDetails convertToWithdraw(){
        this.amount = this.amount.negate();
        return this;
    }
}
