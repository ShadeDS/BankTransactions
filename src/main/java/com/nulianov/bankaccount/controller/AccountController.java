package com.nulianov.bankaccount.controller;

import com.nulianov.bankaccount.domain.TransactionDetails;
import com.nulianov.bankaccount.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping(value = "/account")
public class AccountController {

    @Autowired
    AccountService accountService;

    @RequestMapping(value = "/balance", method = RequestMethod.GET)
    public ResponseEntity<BigDecimal> balance() {
        return new ResponseEntity<>(accountService.getBalance(), HttpStatus.OK);
    }

    @RequestMapping(value = "/statement", method = RequestMethod.GET)
    public ResponseEntity<List<TransactionDetails>> statement() {
        return new ResponseEntity<>(accountService.getStatement(), HttpStatus.OK);
    }

    @RequestMapping(value = "/deposit", method = RequestMethod.POST)
    public ResponseEntity<BigDecimal> deposit(@RequestBody TransactionDetails transactionDetails) {
        try {
            return new ResponseEntity<>(accountService.deposit(transactionDetails), HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/withdraw", method = RequestMethod.POST)
    public ResponseEntity<BigDecimal> withdraw(@RequestBody TransactionDetails transactionDetails) {
        try {
            return new ResponseEntity<>(accountService.withdraw(transactionDetails), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
