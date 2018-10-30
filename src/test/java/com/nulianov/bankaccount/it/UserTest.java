package com.nulianov.bankaccount.it;

import com.google.gson.Gson;
import com.nulianov.bankaccount.domain.User;
import com.nulianov.bankaccount.repository.UserRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserTest {
    private static final String validEmail = "john@gmail.com";
    private static final String validEmailForDuplicationUser = "mike@gmail.com";
    private static final String validPassword = "1234";
    private User validUser = new User(validEmail, validPassword);
    private User duplicationUser = new User(validEmailForDuplicationUser, validPassword);

    @Autowired
    private MockMvc mvc;

    @Autowired
    UserRepository userRepository;

    @Test
    public void registerWithValidUser() throws Exception {
        String body = new Gson().toJson(validUser);

        mvc.perform(
                MockMvcRequestBuilders.post("/auth/register")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andExpect(status().isCreated());

        String token = mvc.perform(MockMvcRequestBuilders.post("/auth/login")
                .param("username", validUser.getUsername()).param("password", validPassword)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn().getResponse().getContentAsString();

        Assert.assertNotEquals("", token);

    }

    @Test
    public void registerDuplicationUser() throws Exception {
        String body = new Gson().toJson(duplicationUser);

        mvc.perform(
                MockMvcRequestBuilders.post("/auth/register")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andExpect(status().isCreated());

        mvc.perform(
                MockMvcRequestBuilders.post("/auth/register")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andExpect(status().isBadRequest());
    }
}
