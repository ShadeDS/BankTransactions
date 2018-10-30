package com.nulianov.bankaccount.repository;

import com.nulianov.bankaccount.domain.Account;
import com.nulianov.bankaccount.domain.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface AccountRepository extends CrudRepository<Account, UUID> {
}
