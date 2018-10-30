package com.nulianov.bankaccount.controller;

import com.nulianov.bankaccount.domain.TransactionDetails;
import com.nulianov.bankaccount.domain.User;
import com.nulianov.bankaccount.exception.IllegalAmountOfMoneyForTransactionException;
import com.nulianov.bankaccount.exception.InsufficientFundsException;
import com.nulianov.bankaccount.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/account")
public class AccountController {
    private final static Logger logger = LoggerFactory.getLogger(AccountController.class);

    @Autowired
    AccountService accountService;

    @RequestMapping(value = "/balance", method = RequestMethod.GET)
    public ResponseEntity<BigDecimal> balance(@RequestParam("accountId") final UUID accountId,
                                              @AuthenticationPrincipal final User user) {
        try {
            logger.info("Received request to get balance for user: {} and account {}", user.getUsername(), accountId);
            return new ResponseEntity<>(accountService.getBalance(user.getUsername(), accountId), HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            logger.error("Account {} was not found", accountId);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/statement", method = RequestMethod.GET)
    public ResponseEntity<List<TransactionDetails>> statement(@RequestParam("accountId") final UUID accountId,
                                                              @AuthenticationPrincipal final User user) {
        try {
            logger.info("Received request to get statement for user: {} and account {}", user.getUsername(), accountId);
            return new ResponseEntity<>(accountService.getStatement(user.getUsername(), accountId), HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            logger.error("Account {} was not found", accountId);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/deposit", method = RequestMethod.POST)
    public ResponseEntity<BigDecimal> deposit(@RequestBody TransactionDetails transactionDetails,
                                              @AuthenticationPrincipal final User user) {
        try {
            logger.info("Received request to deposit for user: " + user.getUsername());
            return new ResponseEntity<>(accountService.deposit(user.getUsername(), transactionDetails), HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            logger.error("Account {} was not found", transactionDetails.getId());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalAmountOfMoneyForTransactionException e) {
            logger.error("Exception occurred while processing deposit for user {}: {}", user.getUsername(), e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @RequestMapping(value = "/withdraw", method = RequestMethod.POST)
    public ResponseEntity<BigDecimal> withdraw(@RequestBody TransactionDetails transactionDetails,
                                               @AuthenticationPrincipal final User user) {
        try {
            logger.info("Received request to withdraw for user: " + user.getUsername());
            return new ResponseEntity<>(accountService.withdraw(user.getUsername(), transactionDetails), HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            logger.error("Account {} was not found", transactionDetails.getId());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalAmountOfMoneyForTransactionException e) {
            logger.error("Exception occurred while processing deposit for user {}: {}", user.getUsername(), e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        } catch (InsufficientFundsException e) {
            logger.error("Exception occurred while processing deposit for user {}: {}", user.getUsername(), e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity<String> create(@AuthenticationPrincipal final User user) {
        logger.info("Received request to create new account for user: " + user.getUsername());
        try {
            return new ResponseEntity<>(accountService.create(user.getUsername()).toString(), HttpStatus.CREATED);
        } catch (EntityNotFoundException e) {
            logger.error("User {} was not found", user.getUsername());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
