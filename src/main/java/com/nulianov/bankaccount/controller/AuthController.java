package com.nulianov.bankaccount.controller;

import com.nulianov.bankaccount.service.AccountAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping(value = "/auth")
public class AuthController {
    @Autowired
    private AccountAuthenticationService authentication;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<String> login(
            @RequestParam("username") final String username,
            @RequestParam("password") final String password) {
        return new ResponseEntity<>(authentication
                .login(username, password)
                .orElseThrow(() -> new RuntimeException("invalid login and/or password")), HttpStatus.OK);
    }
}
