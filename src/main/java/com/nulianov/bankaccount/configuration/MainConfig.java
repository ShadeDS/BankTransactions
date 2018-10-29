package com.nulianov.bankaccount.configuration;


import com.nulianov.bankaccount.service.AccountService;
import com.nulianov.bankaccount.service.impl.AccountServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

    @Bean
    public List<ExecutorService> getExecutorsPool() {
        List<ExecutorService> executors = new ArrayList<>();
        for (int i = 0; i <= Runtime.getRuntime().availableProcessors(); i++) {
            executors.add(Executors.newSingleThreadExecutor());
        }
        return executors;
    }
}
