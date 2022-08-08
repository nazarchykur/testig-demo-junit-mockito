package com.example.testingdemo.testlifecycle;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DefaultTestLifeCycleTest {
    
    @BeforeAll
    static void beforeAll() {
        System.out.println("beforeAll()");
    }
    @BeforeEach
    void beforeEach() {
        System.out.println("beforeEach()");
    }
    @Test
    void firstTest() {
        System.out.println("firstTest()");
    }
    @Test
    void secondTest() {
        System.out.println("secondTest()");
    }
    @AfterEach
    void afterEach() {
        System.out.println("afterEach()");
    }
    @AfterAll
    static void afterAll() {
        System.out.println("afterAll()");
    }
}

/*
    output:
            beforeAll()
            
            beforeEach()
                firstTest()
            afterEach()
            
            beforeEach()
                secondTest()
            afterEach()
            
            afterAll()
 */