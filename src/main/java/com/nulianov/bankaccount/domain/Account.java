package com.nulianov.bankaccount.domain;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class Account {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    @Column(unique=true)
    private String username;

    private String password;

    private BigDecimal balance;

    public Account() {
    }

    public Account(String username, String password, BigDecimal balance) {
        this.username = username;
        this.password = password;
        this.balance = balance;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public BigDecimal deposit(BigDecimal amount) throws Exception{
        if (amount == null || amount.signum() < 0) {
            throw new Exception("Incorrect amount money to deposit");
        } else {
            balance = balance.add(amount);
            return balance;
        }
    }

    public BigDecimal withdraw(BigDecimal amount) throws Exception{
        if (amount == null || amount.signum() < 0) {
            throw new Exception("Incorrect amount money to deposit");
        } else if (amount.compareTo(balance) > 0) {
            throw new Exception("No sufficient funds");
        } else {
            balance = balance.subtract(amount);
            return balance;
        }
    }
}
