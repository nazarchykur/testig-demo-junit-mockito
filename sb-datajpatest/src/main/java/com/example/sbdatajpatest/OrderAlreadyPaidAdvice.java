package com.example.sbdatajpatest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class OrderAlreadyPaidAdvice {
    
    @ExceptionHandler
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public String handleOrderAlreadyPaid(OrderAlreadyPaid orderAlreadyPaid) {
        return orderAlreadyPaid.getMessage();
    }
}
