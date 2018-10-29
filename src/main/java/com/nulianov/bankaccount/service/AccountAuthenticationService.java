package com.nulianov.bankaccount.service;

import com.nulianov.bankaccount.domain.Account;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface AccountAuthenticationService {
    /**
     * If received credentials fit to data in storage, generates and returns auth token,
     * returns empty token otherwise
     * @param username user name from request parameters
     * @param password password from request parameters
     * @return auth token
     */
    Optional<String> login(String username, String password);

    /**
     * If received token is present in storage, returns correlated account,
     * returns empty optional otherwise
     * @param token token from request header
     * @return account linked with specified token
     */
    Optional<Account> findByToken(String token);
}
