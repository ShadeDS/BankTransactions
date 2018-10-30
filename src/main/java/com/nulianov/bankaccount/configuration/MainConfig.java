package com.nulianov.bankaccount.configuration;

import com.nulianov.bankaccount.service.AccountService;
import com.nulianov.bankaccount.service.UserService;
import com.nulianov.bankaccount.service.impl.AccountServiceImpl;
import com.nulianov.bankaccount.service.impl.UserServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;

@Configuration
public class MainConfig {
    @Bean
    public AccountService getAccountService(){
        return new AccountServiceImpl();
    }

    @Bean
    public UserService getUserService() {
        return new UserServiceImpl();
    }

    @Bean
    public DataSource dataSource() {
        EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
        return builder
                .setType(EmbeddedDatabaseType.H2)
                .build();
    }
}
