package com.nulianov.bankaccount.domain;


import com.nulianov.bankaccount.exception.IllegalAmountOfMoneyForTransaction;
import com.nulianov.bankaccount.exception.InsufficientFundsException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

public class AccountEntityTest {
    private static final BigDecimal startBalance = new BigDecimal(100);
    private static final BigDecimal amount = new BigDecimal(1000);
    private static final BigDecimal correctAmountToWithdraw = new BigDecimal(10);
    private static final BigDecimal illegalAmount = new BigDecimal(-1000);

    private Account account;

    @Before
    public void setUp() {
        account = new Account(new User("john", "doe"), startBalance);
    }

    @Test(expected = IllegalAmountOfMoneyForTransaction.class)
    public void depositIllegalAmountOfMoney() {
        account.deposit(illegalAmount);
    }

    @Test(expected = IllegalAmountOfMoneyForTransaction.class)
    public void withdrawIllegalAmountOfMoney() {
        account.withdraw(illegalAmount);
    }

    @Test(expected = InsufficientFundsException.class)
    public void withdrawMoreThanBalance() {
        account.withdraw(amount);
    }

    @Test
    public void depositCorrectAmount() {
        BigDecimal actualBalance = account.deposit(amount);

        Assert.assertEquals(startBalance.add(amount), actualBalance);
    }

    @Test
    public void withdrawCorrectAmount() {
        BigDecimal actualBalance = account.withdraw(correctAmountToWithdraw);

        Assert.assertEquals(startBalance.subtract(correctAmountToWithdraw), actualBalance);
    }
}
