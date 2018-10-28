package com.nulianov.bankaccount.repository;

import com.nulianov.bankaccount.domain.Account;
import org.springframework.data.repository.CrudRepository;

public interface AccountRepository extends CrudRepository<Account, Long> {
    Account findByUsername(String username);
}
