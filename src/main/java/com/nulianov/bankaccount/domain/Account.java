package com.nulianov.bankaccount.domain;

import com.nulianov.bankaccount.exception.IllegalAmountOfMoneyForTransaction;
import com.nulianov.bankaccount.exception.InsufficientFundsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

@Entity
public class Account {
    @Id
    private UUID id;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    private User user;

    private BigDecimal balance;

    public Account() {
    }

    public Account(UUID id, User user, BigDecimal balance) {
        this.id = id;
        this.user = user;
        this.balance = balance;
    }

    public Account(User user, BigDecimal balance) {
        generateId();
        this.user = user;
        this.balance = balance;
    }

    public UUID getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public BigDecimal deposit(BigDecimal amount) throws IllegalAmountOfMoneyForTransaction {
        if (amount == null || amount.signum() < 0) {
            throw new IllegalAmountOfMoneyForTransaction(amount);
        } else {
            balance = balance.add(amount);
            return balance;
        }
    }

    public BigDecimal withdraw(BigDecimal amount) throws IllegalAmountOfMoneyForTransaction, InsufficientFundsException {
        if (amount == null || amount.signum() < 0) {
            throw new IllegalAmountOfMoneyForTransaction(amount);
        } else if (amount.compareTo(balance) > 0) {
            throw new InsufficientFundsException(user.getUsername());
        } else {
            balance = balance.subtract(amount);
            return balance;
        }
    }

    public void generateId() {
        if (this.id == null) {
            this.id = UUID.randomUUID();
        }
    }
}
