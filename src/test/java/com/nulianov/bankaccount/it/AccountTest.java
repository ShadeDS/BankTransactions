package com.nulianov.bankaccount.it;


import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AccountTest {
    @Autowired
    private MockMvc mvc;

    @Test
    public void getBalance() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/account/balance").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("balance")));
    }

    @Test
    public void getStatement() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/account/statement").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("statement")));
    }

    @Test
    public void makeDeposit() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/account/deposit").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("deposit")));
    }

    @Test
    public void makeWithdraw() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/account/withdraw").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("withdraw")));
    }
}
