package com.nulianov.bankaccount.domain;

import com.nulianov.bankaccount.exception.IllegalAmountOfMoneyForTransaction;
import com.nulianov.bankaccount.exception.InsufficientFundsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;

@Entity
public class Account implements UserDetails {
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
            throw new InsufficientFundsException(username);
        } else {
            balance = balance.subtract(amount);
            return balance;
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new ArrayList<>();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
