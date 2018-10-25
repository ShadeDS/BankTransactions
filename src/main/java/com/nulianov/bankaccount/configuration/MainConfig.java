package com.nulianov.bankaccount.configuration;

import com.nulianov.bankaccount.service.AccountService;
import com.nulianov.bankaccount.service.impl.AccountServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MainConfig {
    @Bean
    public AccountService helloWorld(){
        return new AccountServiceImpl();
    }
}
