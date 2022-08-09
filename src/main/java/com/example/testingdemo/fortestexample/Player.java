package com.example.testingdemo.fortestexample;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
public class Player {
    private String name;
    private int age;
    
    @Override
    public boolean equals(Object o) {
        Player player = (Player) o;
        return Objects.equals(player.getName(), this.getName());
    }
}
