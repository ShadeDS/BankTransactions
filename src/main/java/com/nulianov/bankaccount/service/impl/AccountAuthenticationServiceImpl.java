package com.nulianov.bankaccount.service.impl;

import com.nulianov.bankaccount.domain.Account;
import com.nulianov.bankaccount.repository.AccountRepository;
import com.nulianov.bankaccount.service.AccountAuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class AccountAuthenticationServiceImpl implements AccountAuthenticationService {
    private final static Logger logger = LoggerFactory.getLogger(AccountAuthenticationServiceImpl.class);
    private static final Map<String, String> tokenRepository = new ConcurrentHashMap<>();

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AccountRepository accountRepository;

    @Override
    public Optional<String> login(String username, String password) {
        logger.debug("Check credentials for user {}", username);
        Optional<Account> account = accountRepository.findByUsername(username);
        if (account.isPresent()) {
            logger.debug("User {} was found", username);
            if (passwordEncoder.matches(password, account.get().getPassword())) {
                logger.debug("Credentials are correct, start to create token");
                String id = UUID.randomUUID().toString();
                tokenRepository.put(id, username);
                logger.debug("For user {} token {} was created", username, id);
                return Optional.of(id);
            }
        }
        logger.error("User {} was not found in database", username);
        return Optional.empty();
    }

    @Override
    public Optional<Account> findByToken(String token) {
        logger.debug("Try to find user by token {}", token);
        return accountRepository.findByUsername(tokenRepository.get(token));
    }
}
