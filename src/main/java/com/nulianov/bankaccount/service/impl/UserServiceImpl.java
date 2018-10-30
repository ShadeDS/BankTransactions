package com.nulianov.bankaccount.service.impl;

import com.nulianov.bankaccount.domain.User;
import com.nulianov.bankaccount.repository.UserRepository;
import com.nulianov.bankaccount.service.UserService;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;


public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder encoder;

    @Override
    public void register(String username, String password) throws BadCredentialsException {
        if (usernameIsValidEmail(username)) {
            if (!password.equals("")) {
                try {
                    userRepository.save(new User(username, encoder.encode(password)));
                } catch (DataIntegrityViolationException e) {
                    throw new BadCredentialsException("Username " + username + " already exists");
                }
            } else {
                throw new BadCredentialsException("Password cannot be empty");
            }
        } else {
            throw new BadCredentialsException("Username " + username + " is not a valid email");
        }
    }

    private boolean usernameIsValidEmail(String username) {
        return EmailValidator.getInstance().isValid(username);
    }
}
