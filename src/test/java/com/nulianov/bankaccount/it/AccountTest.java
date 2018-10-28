package com.nulianov.bankaccount.it;


import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nulianov.bankaccount.domain.Account;
import com.nulianov.bankaccount.domain.TransactionDetails;
import com.nulianov.bankaccount.repository.AccountRepository;
import com.nulianov.bankaccount.repository.TransactionDetailsRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AccountTest {
    private static final Account userToDeposit = new Account("john", "doe", new BigDecimal(100));
    private static final Account userToWithdraw = new Account("mike", "doe", new BigDecimal(100));
    private static final String amount = "5.00";
    private static final String amountAfterDeposit = "105.00";
    private static final String amountAfterWithdraw = "95.00";
    private static final TransactionDetails deposit = new TransactionDetails(new BigDecimal(amount));
    private static final TransactionDetails withdraw = new TransactionDetails(new BigDecimal(amount));

    @Autowired
    private MockMvc mvc;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionDetailsRepository transactionDetailsRepository;

    @Before
    public void setUp() {
        accountRepository.save(userToDeposit);
        accountRepository.save(userToWithdraw);
        deposit.setCurrentTime();
        deposit.setUserId(accountRepository.findByUsername(userToDeposit.getUsername()).getId());
        transactionDetailsRepository.save(deposit);

        withdraw.setCurrentTime();
        withdraw.setUserId(accountRepository.findByUsername(userToWithdraw.getUsername()).getId());
    }

    @Test
    public void getBalance() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/account/balance"))
                .andExpect(status().isOk())
                .andExpect(content().string("100.00"));
    }

    @Test
    public void getStatementWithDeposit() throws Exception {
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/account/statement"))
                .andExpect(status().isOk()).andReturn();
        Type listType = new TypeToken<ArrayList<TransactionDetails>>(){}.getType();
        List<TransactionDetails> statement = new Gson().fromJson(result.getResponse().getContentAsString(), listType);

        Assert.assertEquals(deposit.getAmount(), statement.get(0).getAmount());
    }

    @Test
    public void makeDeposit() throws Exception {

        String body = new Gson().toJson(deposit);
        System.out.println(body);
        mvc.perform(MockMvcRequestBuilders.post("/account/deposit").content(body).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo(amountAfterDeposit)));
    }

    @Test
    public void makeWithdraw() throws Exception {
        String body = new Gson().toJson(withdraw);
        System.out.println(body);
        mvc.perform(MockMvcRequestBuilders.post("/account/withdraw").content(body).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo(amountAfterWithdraw)));
    }
}
