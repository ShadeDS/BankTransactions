package com.nulianov.bankaccount.service;

import com.nulianov.bankaccount.domain.Account;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface AccountAuthenticationService {
    Optional<String> login(String username, String password);

    Optional<Account> findByToken(String token);
}
