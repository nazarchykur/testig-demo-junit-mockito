package com.example.testingdemo.testingexception;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class CashierTest_usingRule {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void validateTransaction_throws_IllegalArgumentException() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Currency UAH not within accepted currencies list.");
        
        Cashier.validateTransaction("UAH", 100);
    }

    @Test
    public void validateTransaction_amountNegative_throws_InvalidTransactionAmountException() {
        thrown.expect(InvalidTransactionAmountException.class);
        thrown.expectMessage("Transaction amount must be greater than 0");
        
        Cashier.validateTransaction("USD", -100);
    }
}