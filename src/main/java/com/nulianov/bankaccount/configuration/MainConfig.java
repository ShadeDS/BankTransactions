package com.nulianov.bankaccount.configuration;

import com.nulianov.bankaccount.service.AccountAuthenticationService;
import com.nulianov.bankaccount.service.AccountService;
import com.nulianov.bankaccount.service.impl.AccountAuthenticationServiceImpl;
import com.nulianov.bankaccount.service.impl.AccountServiceImpl;
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
    public DataSource dataSource() {
        EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
        return builder
                .setType(EmbeddedDatabaseType.H2)
                .build();
    }
}
