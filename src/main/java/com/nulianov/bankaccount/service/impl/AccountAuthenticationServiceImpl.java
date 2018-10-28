package com.nulianov.bankaccount.service.impl;

import com.nulianov.bankaccount.domain.Account;
import com.nulianov.bankaccount.repository.AccountRepository;
import com.nulianov.bankaccount.service.AccountAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class AccountAuthenticationServiceImpl implements AccountAuthenticationService {
    private static final Map<String, String> tokenRepository = new ConcurrentHashMap<>();

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AccountRepository accountRepository;

    @Override
    public Optional<String> login(String username, String password) {
        Optional<Account> account = accountRepository.findByUsername(username);
        if (account.isPresent()) {
            if (passwordEncoder.matches(password, account.get().getPassword())) {
                String id = UUID.randomUUID().toString();
                tokenRepository.put(id, username);
                return Optional.of(id);
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<Account> findByToken(String token) {
        return accountRepository.findByUsername(tokenRepository.get(token));
    }

    @Override
    public void logout(String token) {
        tokenRepository.remove(token);
    }
}
