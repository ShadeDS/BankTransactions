package com.nulianov.bankaccount.service;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    /**
     * If username and password are valid creates new user in storage,
     * throw exception otherwise
     * @param username user email
     * @param password user password
     * @throws BadCredentialsException user/password are incorrect or user is already exists
     */
    void register(String username, String password) throws BadCredentialsException;
}
