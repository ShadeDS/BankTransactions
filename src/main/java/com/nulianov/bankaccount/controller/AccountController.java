package com.nulianov.bankaccount.controller;

import com.nulianov.bankaccount.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/account")
public class AccountController {

    @Autowired
    AccountService accountService;

    @RequestMapping(value = "/balance", method = RequestMethod.GET)
    public String balance() {
        return accountService.getBalance();
    }

    @RequestMapping(value = "/statement", method = RequestMethod.GET)
    public String statement() {
        return accountService.getStatement();
    }

    @RequestMapping(value = "/deposit", method = RequestMethod.POST)
    public String deposit() {
        return accountService.deposit();
    }

    @RequestMapping(value = "/withdraw", method = RequestMethod.POST)
    public String withdraw() {
        return accountService.withdraw();
    }
}
