package com.nulianov.bankaccount.it;


import static org.hamcrest.Matchers.equalTo;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nulianov.bankaccount.domain.Account;
import com.nulianov.bankaccount.domain.TransactionDetails;
import com.nulianov.bankaccount.repository.AccountRepository;
import com.nulianov.bankaccount.repository.TransactionDetailsRepository;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private static Account userToDeposit;
    private static Account userToWithdraw;
    private static final String userToDepositPassword = "doe";
    private static final String userToWithdrawPassword = "12345";
    private static final String amount = "5.00";
    private static final String amountAfterDeposit = "105.00";
    private static final String amountAfterWithdraw = "95.00";
    private static final TransactionDetails deposit = new TransactionDetails(new BigDecimal(amount));
    private static final TransactionDetails withdraw = new TransactionDetails(new BigDecimal(amount));

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionDetailsRepository transactionDetailsRepository;

    @Before
    public void setUp() {
        userToDeposit = new Account("john", encoder.encode(userToDepositPassword), new BigDecimal(100));
        userToWithdraw = new Account("mike", encoder.encode(userToWithdrawPassword), new BigDecimal(100));
        accountRepository.save(userToDeposit);
        accountRepository.save(userToWithdraw);
        deposit.setCurrentTime();
        deposit.setUserName(userToDeposit.getUsername());
        transactionDetailsRepository.save(deposit);

        withdraw.setCurrentTime();
        withdraw.setUserName(userToWithdraw.getUsername());
        transactionDetailsRepository.save(withdraw);
    }

    @After
    public void tearDown() {
        accountRepository.delete(userToDeposit);
        accountRepository.delete(userToWithdraw);

        transactionDetailsRepository.delete(deposit);
        transactionDetailsRepository.delete(withdraw);
    }

    @Test
    public void getBalance() throws Exception {
        String token = auth(userToDeposit.getUsername(), userToDepositPassword);
        mvc.perform(MockMvcRequestBuilders.get("/account/balance")
        .header(AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(content().string("100.00"));
    }

    @Test
    public void getStatementWithDeposit() throws Exception {
        List<TransactionDetails> statement = getStatement(userToDeposit, userToDepositPassword);
        Assert.assertEquals(deposit.getAmount(), statement.get(0).getAmount());
    }

    @Test
    public void getStatementWithWithdraw() throws Exception {
        List<TransactionDetails> statement = getStatement(userToWithdraw, userToWithdrawPassword);
        Assert.assertEquals(withdraw.getAmount(), statement.get(0).getAmount());
    }

    @Test
    public void makeDeposit() throws Exception {
        String token = auth(userToDeposit.getUsername(), userToDepositPassword);
        String body = new Gson().toJson(deposit);
        System.out.println(body);
        mvc.perform(
                MockMvcRequestBuilders.post("/account/deposit")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header(AUTHORIZATION, "Bearer " + token)
        ).andExpect(status().isOk()).andExpect(content().string(equalTo(amountAfterDeposit)));
    }

    @Test
    public void makeWithdraw() throws Exception {
        String token = auth(userToWithdraw.getUsername(), userToWithdrawPassword);
        String body = new Gson().toJson(withdraw);
        System.out.println(body);
        mvc.perform(
                MockMvcRequestBuilders.post("/account/withdraw")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header(AUTHORIZATION, "Bearer " + token)
        ).andExpect(status().isOk()).andExpect(content().string(equalTo(amountAfterWithdraw)));
    }

    private String auth(String username, String password) throws Exception {
        return mvc.perform(MockMvcRequestBuilders.post("/auth/login")
                .param("username",username).param("password", password)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn().getResponse().getContentAsString();
    }

    private List<TransactionDetails> getStatement(Account account, String accountPassword) throws Exception {
        String token = auth(account.getUsername(), accountPassword);
        MvcResult result = mvc.perform(
                MockMvcRequestBuilders.get("/account/statement")
                        .header(AUTHORIZATION, "Bearer " + token)
        ).andExpect(status().isOk()).andReturn();
        Type listType = new TypeToken<ArrayList<TransactionDetails>>(){}.getType();
        return new Gson().fromJson(result.getResponse().getContentAsString(), listType);
    }
}
