package com.example.testingdemo.fortestexample;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class PlayerStatisticsTest_usingAssertJ {

    @Test
    public void playerNameEquals() {
        Player player1 = new Player("Leo", 32);
        Player player2 = new Player("Leo", 27);

        assertEquals(player1, player2);

        // using assertJ
        assertThat(player2).isEqualTo(player1);
    }

    @Test
    public void playerNameNotEquals() {
        Player player1 = new Player("Leo", 32);
        Player player2 = new Player("Don", 27);

        assertNotEquals(player1, player2);
        
        // using assertJ
        assertThat(player2).isNotEqualTo(player1);
    }

    @Test
    public void youngerPlayerSame() {
        Player player1 = new Player("Leo", 32);
        Player player2 = new Player("Leo", 27);

        assertSame(player2, PlayerStatistics.getYoungerPlayer(player1, player2));

        // using assertJ
        assertThat(player2).isSameAs(PlayerStatistics.getYoungerPlayer(player1, player2));
    }
    

    @Test
    public void underThirtyTrue() {
        Player player1 = new Player("Leo", 27);
        PlayerStatistics statistics = new PlayerStatistics(player1, 3, 3);

        assertTrue(statistics.underThirty()); // more readable than
//        assertEquals(true, statistics.underThirty());

        // using assertJ
        assertThat(statistics.underThirty()).isTrue();
    }

    @Test
    public void underThirtyFalse() {
        Player player1 = new Player("Leo", 35);
        PlayerStatistics statistics = new PlayerStatistics(player1, 3, 3);

        assertFalse(statistics.underThirty());

        // using assertJ
        assertThat(statistics.underThirty()).isFalse();
    }

    @Test
    public void scvReportNull() {
        Player player1 = new Player("Leo", 35);
        PlayerStatistics statistics = new PlayerStatistics(player1, 0, 0);

        // because player has 0 games and 0 goals, we expect createScvRecord return null
        assertNull(statistics.createScvRecord());

        // using assertJ
        assertThat(statistics.createScvRecord()).isNull();
    }

    @Test
    public void scvReportNotNull() {
        Player player1 = new Player("Leo", 35);
        PlayerStatistics statistics = new PlayerStatistics(player1, 3, 2);

        // because player has 3 games and 2 goals, we expect createScvRecord return not null
        assertNotNull(statistics.createScvRecord());

        // using assertJ
        assertThat(statistics.createScvRecord()).isNotNull();
    }

    @Test
    public void getCsvStatsRecord() {
        Player player1 = new Player("Leo", 35);
        PlayerStatistics statistics = new PlayerStatistics(player1, 3, 6);

        Double[] expectedArray = {2D, 0.5d};
        
        assertArrayEquals(expectedArray, statistics.createScvRecord());

        // using assertJ
        assertThat(statistics.createScvRecord()). isEqualTo(expectedArray);
    }

}