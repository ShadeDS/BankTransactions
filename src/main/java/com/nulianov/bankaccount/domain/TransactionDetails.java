package com.nulianov.bankaccount.domain;

import org.springframework.lang.NonNull;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
public class TransactionDetails {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    @NonNull
    private UUID accountId;

    @NonNull
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

    public UUID getAccountId() {
        return accountId;
    }

    public void setAccountId(UUID accountId) {
        this.accountId = accountId;
    }

    public void setCurrentTime(){
        this.timeStampMillis = Instant.now().toEpochMilli();
    }

    public TransactionDetails convertToWithdraw(){
        this.amount = this.amount.negate();
        return this;
    }
}
