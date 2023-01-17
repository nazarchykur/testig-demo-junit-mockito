package com.example.testingdemo.testweblayer.controller;

import com.example.testingdemo.testweblayer.service.WelcomeService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
public class WelcomeController {
    
    private final WelcomeService service;
    
    @GetMapping("/welcome")
    public String welcome(@RequestParam(defaultValue = "Stranger") String name) {
        return service.getWelcomeMessage(name);
    }
}
