package com.nulianov.bankaccount.controller;

import com.nulianov.bankaccount.domain.Account;
import com.nulianov.bankaccount.domain.TransactionDetails;
import com.nulianov.bankaccount.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping(value = "/account")
public class AccountController {
    private final static Logger logger = LoggerFactory.getLogger(AccountController.class);

    @Autowired
    AccountService accountService;

    @RequestMapping(value = "/balance", method = RequestMethod.GET)
    public ResponseEntity<BigDecimal> balance(@AuthenticationPrincipal final Account account) {
        try {
            logger.info("Received request to get balance for user: " + account.getUsername());
            return new ResponseEntity<>(accountService.getBalance(account.getUsername()), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("User {} was not found", account.getUsername());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/statement", method = RequestMethod.GET)
    public ResponseEntity<List<TransactionDetails>> statement(@AuthenticationPrincipal final Account account) {
        try {
            logger.info("Received request to get statement for user: " + account.getUsername());
            return new ResponseEntity<>(accountService.getStatement(account.getUsername()), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("User {} was not found", account.getUsername());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/deposit", method = RequestMethod.POST)
    public ResponseEntity<BigDecimal> deposit(@RequestBody TransactionDetails transactionDetails,
                                              @AuthenticationPrincipal final Account account) {
        try {
            logger.info("Received request to deposit for user: " + account.getUsername());
            transactionDetails.setUserName(account.getUsername());
            return new ResponseEntity<>(accountService.deposit(transactionDetails), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Exception occurred while processing deposit for user {}: {}", account.getUsername(), e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/withdraw", method = RequestMethod.POST)
    public ResponseEntity<BigDecimal> withdraw(@RequestBody TransactionDetails transactionDetails,
                                               @AuthenticationPrincipal final Account account) {
        try {
            logger.info("Received request to withdraw for user: " + account.getUsername());
            transactionDetails.setUserName(account.getUsername());
            return new ResponseEntity<>(accountService.withdraw(transactionDetails), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Exception occurred while processing deposit for user {}: {}", account.getUsername(), e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
