package com.example.testingdemo.testweblayer.controller;

import com.example.testingdemo.testweblayer.service.WelcomeService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class WelcomeControllerUnitTest {

    /* 
        це тільки для прикладу як ми тестуємо UnitTest
        
        1) мок наш сервіс 
        2) мокаємо виклик потрібного методу і повертаємо очікуване значення
        3) створюємо контролер з потрібними залежностями
        
        4) перевіряємо чи результат виконання методу контролера є рівне очікуваному
     */
    @Test
    void shouldWelcome() {
        WelcomeService welcomeService = Mockito.mock(WelcomeService.class);

        when(welcomeService.getWelcomeMessage("John")).thenReturn("Welcome John");

        WelcomeController welcomeController = new WelcomeController(welcomeService);
        assertThat("Welcome John").isEqualTo(welcomeController.welcome("John"));
    }
}