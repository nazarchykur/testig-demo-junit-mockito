package com.example.testingdemo.testingexception;

import org.junit.Test;

public class CashierTest {

    @Test(expected = IllegalArgumentException.class)
    public void validateTransaction_throws_IllegalArgumentException() {
        Cashier.validateTransaction("UAH", 100);
    }

    @Test(expected = InvalidTransactionAmountException.class)
    public void validateTransaction_amountNegative_throws_InvalidTransactionAmountException() {
        Cashier.validateTransaction("USD", -100);
    }
}