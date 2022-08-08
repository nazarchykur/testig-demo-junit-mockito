package com.example.testingdemo.junitrules;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/*
    ExpectedException Rules
    
    The ExpectedException Rule allows having more control over expected exception types and messages.
 */

public class ExpectedExceptionRule {
    
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        
        throw new NullPointerException();
    }

    @Test
    public void throwsNullPointerExceptionWithMessage() {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("Null Pointer Problem!");
        
        throw new NullPointerException("Null Pointer Problem!");
    }

    //The new way of assertion Exceptions after 4.13 version of JUnit 4.
    //ExpectedException.none() is deprecated instead of this you can use Assert.assertThrows
    @Test
    public void throwsNullPointerExceptionNew() {
        Assert.assertThrows(NumberFormatException.class, () -> Integer.parseInt("Hello"));
        Assert.assertThrows(IllegalArgumentException.class, () -> Integer.parseInt("Hello"));
    }
}


/*

    https://www.swtestacademy.com/junit-rules/
    
    JUnit Rules allow you to write code to do some before and after work. 
    Thus, you don’t repeat to write the same code in various test classes. 
    They are very useful to add more functionalities to all test methods in a test class. 
    You can extend or reuse provided rules or write your own custom rules.
    
    The base rules are listed below and you can find details about them on https://github.com/junit-team/junit/wiki/Rules web page.
    
        TemporaryFolder Rule
        ExternalResource Rules
        ErrorCollector Rule
        Verifier Rule
        TestWatchman/TestWatcher Rules
        TestName Rule
        Timeout Rule
        ExpectedException Rules
        ClassRule
        RuleChain
        Custom Rules
    
    In JUnit https://github.com/junit-team/junit4/wiki/Rules link, you can find all details of each rule. 
    Also, their core mechanism and flow are described in this article very well. I want to demonstrate the usage of some rules with examples.
 */