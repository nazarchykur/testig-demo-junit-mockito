package com.example.testingdemo.employeeproj.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NoSuchElementFoundException extends RuntimeException {
    
    public NoSuchElementFoundException() {
    }

    public NoSuchElementFoundException(String s) {
        super(s);
    }

    public NoSuchElementFoundException(String s, Throwable throwable) {
        super(s, throwable);
    }
}
