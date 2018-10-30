package com.nulianov.bankaccount.it;


import static org.hamcrest.Matchers.equalTo;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nulianov.bankaccount.domain.Account;
import com.nulianov.bankaccount.domain.TransactionDetails;
import com.nulianov.bankaccount.domain.User;
import com.nulianov.bankaccount.repository.AccountRepository;
import com.nulianov.bankaccount.repository.TransactionDetailsRepository;
import com.nulianov.bankaccount.repository.UserRepository;
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
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AccountTest {
    private User userToDeposit;
    private User userToWithdraw;
    private static Account accountToDeposit;
    private static Account accountToWithdraw;
    private static Account createdAccount;
    private static final String userToDepositPassword = "doe";
    private static final String userToWithdrawPassword = "12345";
    private static final String amount = "5.00";
    private static final String amountAfterDeposit = "105.00";
    private static final String amountAfterWithdraw = "95.00";
    private static final TransactionDetails deposit = new TransactionDetails(new BigDecimal(amount));
    private static final TransactionDetails withdraw = new TransactionDetails(new BigDecimal(amount));

    private static final TransactionDetails firstWithdraw = new TransactionDetails(new BigDecimal(60));
    private static final TransactionDetails secondWithdraw = new TransactionDetails(new BigDecimal(50));

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionDetailsRepository transactionDetailsRepository;

    @Autowired
    private UserRepository userRepository;

    @Before
    public void setUp() {
        userToDeposit = new User("john", encoder.encode(userToDepositPassword));
        userToWithdraw = new User("mike", encoder.encode(userToWithdrawPassword));
        userRepository.save(userToDeposit);
        userRepository.save(userToWithdraw);


        accountToDeposit = new Account(userToDeposit, new BigDecimal(100));
        accountToWithdraw = new Account(userToWithdraw, new BigDecimal(100));
        accountRepository.save(accountToDeposit);
        accountRepository.save(accountToWithdraw);

        deposit.setCurrentTime();
        deposit.setAccountId(accountToDeposit.getId());
        transactionDetailsRepository.save(deposit);

        withdraw.setCurrentTime();
        withdraw.setAccountId(accountToWithdraw.getId());
        transactionDetailsRepository.save(withdraw);

        firstWithdraw.setAccountId(accountToWithdraw.getId());
        secondWithdraw.setAccountId(accountToWithdraw.getId());
    }

    @After
    public void tearDown() {
        if (createdAccount != null) {
            accountRepository.delete(createdAccount);
            createdAccount = null;
        }
        accountRepository.delete(accountToDeposit);
        accountRepository.delete(accountToWithdraw);
        userRepository.delete(userToDeposit);
        userRepository.delete(userToWithdraw);

        transactionDetailsRepository.delete(deposit);
        transactionDetailsRepository.delete(withdraw);
    }

    @Test
    public void getBalance() throws Exception {
        String token = auth(userToDeposit.getUsername(), userToDepositPassword);
        mvc.perform(MockMvcRequestBuilders.get("/account/balance")
        .header(AUTHORIZATION, "Bearer " + token)
        .param("accountId", accountToDeposit.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(content().string("100.00"));
    }

    @Test
    public void getStatementWithDeposit() throws Exception {
        List<TransactionDetails> statement = getStatement(userToDeposit, userToDepositPassword, accountToDeposit);
        Assert.assertEquals(deposit.getAmount(), statement.get(0).getAmount());
    }

    @Test
    public void getStatementWithWithdraw() throws Exception {
        List<TransactionDetails> statement = getStatement(userToWithdraw, userToWithdrawPassword, accountToWithdraw);
        Assert.assertEquals(withdraw.getAmount(), statement.get(0).getAmount());
    }

    @Test
    public void makeDeposit() throws Exception {
        String token = auth(userToDeposit.getUsername(), userToDepositPassword);
        String body = new Gson().toJson(deposit);
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
        mvc.perform(
                MockMvcRequestBuilders.post("/account/withdraw")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header(AUTHORIZATION, "Bearer " + token)
        ).andExpect(status().isOk()).andExpect(content().string(equalTo(amountAfterWithdraw)));
    }

    @Test
    public void createNewAccount() throws Exception {
        String token = auth(userToDeposit.getUsername(), userToDepositPassword);
        String accountId = mvc.perform(
                MockMvcRequestBuilders.post("/account/create")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header(AUTHORIZATION, "Bearer " + token)
        ).andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();

        System.out.println(accountId);

        createdAccount = new Account(UUID.fromString(accountId), userToDeposit, new BigDecimal(0));

        mvc.perform(MockMvcRequestBuilders.get("/account/balance")
                .header(AUTHORIZATION, "Bearer " + token)
                .param("accountId", accountId))
                .andExpect(status().isOk())
                .andExpect(content().string("0.00"));

    }

    @Test
    public void synchronization() throws Exception {
        String token = auth(userToWithdraw.getUsername(), userToWithdrawPassword);

        Thread firstWithdrawThread = new Thread(() -> withdraw(token, firstWithdraw));
        Thread secondWithdrawThread = new Thread(() -> withdraw(token, secondWithdraw));


        firstWithdrawThread.start();
        secondWithdrawThread.start();
        firstWithdrawThread.join();
        secondWithdrawThread.join();

        String result = mvc.perform(MockMvcRequestBuilders.get("/account/balance")
                .header(AUTHORIZATION, "Bearer " + token)
                .param("accountId", accountToWithdraw.getId().toString()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        boolean correct = result.equals("40.00") || result.equals("50.00");

        Assert.assertTrue(correct);
    }

    private void withdraw(String token, TransactionDetails withdraw) {
        try {
            String body = new Gson().toJson(withdraw);
            mvc.perform(
                    MockMvcRequestBuilders.post("/account/withdraw")
                            .content(body)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .header(AUTHORIZATION, "Bearer " + token)
            );
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    private String auth(String username, String password) throws Exception {
        return mvc.perform(MockMvcRequestBuilders.post("/auth/login")
                .param("username",username).param("password", password)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn().getResponse().getContentAsString();
    }

    private List<TransactionDetails> getStatement(User user, String accountPassword, Account account) throws Exception {
        String token = auth(user.getUsername(), accountPassword);
        MvcResult result = mvc.perform(
                MockMvcRequestBuilders.get("/account/statement")
                        .header(AUTHORIZATION, "Bearer " + token)
                        .param("accountId", account.getId().toString())
        ).andExpect(status().isOk()).andReturn();
        Type listType = new TypeToken<ArrayList<TransactionDetails>>(){}.getType();
        return new Gson().fromJson(result.getResponse().getContentAsString(), listType);
    }
}
