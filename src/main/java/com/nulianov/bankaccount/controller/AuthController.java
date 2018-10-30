package com.nulianov.bankaccount.controller;

import com.nulianov.bankaccount.domain.User;
import com.nulianov.bankaccount.service.AccountAuthenticationService;
import com.nulianov.bankaccount.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping(value = "/auth")
public class AuthController {
    private final static Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AccountAuthenticationService authentication;

    @Autowired
    UserService userService;

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<String> register(@RequestBody User user){
        try {
            userService.register(user.getUsername(), user.getPassword());
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (BadCredentialsException e) {
            logger.error("Error occurred while register user {}: {}", user.getUsername(), e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<String> login(
            @RequestParam("username") final String username,
            @RequestParam("password") final String password) {
        logger.info("User tries to login with username {}", username);
        return new ResponseEntity<>(authentication
                .login(username, password)
                .orElseThrow(() -> new RuntimeException("invalid login and/or password")), HttpStatus.OK);
    }
}
