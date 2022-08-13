package com.example.testingdemo.testingexception;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;


/*
    For JUnit 4.13+ ... JUnit 5
    
    Метод assertThrows() стверджує, що виконання виконуваного блоку або  лямбда - виразу  викликає виняток типу  expectedType. 


    static <T extends Throwable>T assertThrows(Class<T> expectedType, Executable executable)
    static <T extends Throwable>T assertThrows(Class<T> expectedType, Executable executable, String message)
    static <T extends Throwable>T assertThrows(Class<T> expectedType, Executable executable, Supplier<String> messageSupplier)

 */

public class CashierTest_usingJUnit_4_13_and_Higher {
    

    @Test
    public void validateTransaction_throws_IllegalArgumentException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> Cashier.validateTransaction("UAH", 100));

        assertEquals("Currency UAH not within accepted currencies list.", exception.getMessage());
    }

    @Test
    public void validateTransaction_amountNegative_throws_InvalidTransactionAmountException() {
        InvalidTransactionAmountException exception = assertThrows(InvalidTransactionAmountException.class, 
                () -> Cashier.validateTransaction("USD", -100));

        assertEquals("Transaction amount must be greater than 0", exception.getMessage());
    }
}