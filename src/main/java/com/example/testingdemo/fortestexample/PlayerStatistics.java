package com.example.testingdemo.fortestexample;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PlayerStatistics {
    private Player player;
    private int games;
    private int goals;
    
    public double gamesPerGoal() {
        return (double) this.games / (double) this.goals;
    }

    public double goalsPerGame() {
        return (double) this.goals / (double) this.games;
    }
    
    public boolean underThirty() {
        return player.getAge() < 30;
    }

    public Double[] createScvRecord() {
        if (this.games == 0) {
            return null;
        } else {
            return new Double[]{goalsPerGame(), gamesPerGoal()}; 
        }
    }
    
    public static Player getYoungerPlayer(Player player1, Player player2) {
        if (player2.getAge() < player1.getAge()) {
            return player2;
        }
        return player1;
    }
    
}
