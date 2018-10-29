package com.nulianov.bankaccount.domain;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

public class TransactionDetailsEntityTest {
    private static final BigDecimal amount = new BigDecimal(1000);

    private TransactionDetails transactionDetails;

    @Before
    public void setUp() {
        transactionDetails = new TransactionDetails(amount);
    }

    @Test
    public void convertToWithdraw() {
        transactionDetails.convertToWithdraw();

        Assert.assertEquals(amount.negate(), transactionDetails.getAmount());
    }
}
