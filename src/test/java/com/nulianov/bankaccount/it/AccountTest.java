package com.nulianov.bankaccount.it;


import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.google.gson.Gson;
import com.nulianov.bankaccount.domain.Account;
import com.nulianov.bankaccount.domain.BankTransactionDetails;
import com.nulianov.bankaccount.repository.AccountRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.time.Instant;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AccountTest {
    private static final Account user = new Account("john", "doe", new BigDecimal(100));
    private static final String amount = "5";
    private static final BankTransactionDetails transactionDetails = new BankTransactionDetails(new BigDecimal(amount), Instant.now().toEpochMilli());

    @Autowired
    private MockMvc mvc;

    @Autowired
    private AccountRepository accountRepository;

    @Before
    public void setUp() {
        accountRepository.save(user);
    }

    @Test
    public void getBalance() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/account/balance"))
                .andExpect(status().isOk())
                .andExpect(content().string("100.00"));
    }

    @Test
    public void getStatement() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/account/statement"))
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));
    }

    @Test
    public void makeDeposit() throws Exception {
        String body = new Gson().toJson(transactionDetails);
        mvc.perform(MockMvcRequestBuilders.post("/account/deposit").content(body).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo(amount)));
    }

    @Test
    public void makeWithdraw() throws Exception {
        String body = new Gson().toJson(transactionDetails);
        mvc.perform(MockMvcRequestBuilders.post("/account/withdraw").content(body).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo(amount)));
    }
}
