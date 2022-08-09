package com.example.testingdemo.fortestexample;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class PlayerStatisticsTest_2_usingBeforeAnotation_reduceLinesOfCode {
    Player playerLeoUnderThirty;
    PlayerStatistics statsOfLeoUnderThirty;
    
    @Before
    public void setUp() {
        playerLeoUnderThirty = new Player("Leo", 32);
        statsOfLeoUnderThirty = new PlayerStatistics(playerLeoUnderThirty, 3, 3);
    }

    @Test
    public void playerNameEquals() {
        Player player2 = new Player("Leo", 27);

        assertEquals(playerLeoUnderThirty, player2);
    }

    @Test
    public void playerNameNotEquals() {
        Player player2 = new Player("Don", 27);

        assertNotEquals(playerLeoUnderThirty, player2);
    }

    @Test
    public void youngerPlayerSame() {
        Player player2 = new Player("Leo", 32);

        assertSame(playerLeoUnderThirty, PlayerStatistics.getYoungerPlayer(playerLeoUnderThirty, player2));
    }

    @Test
    public void underThirtyTrue() {
        Player player1 = new Player("Leo", 27);
        PlayerStatistics statistics = new PlayerStatistics(player1, 3, 3);

        assertTrue(statistics.underThirty()); // more readable than
//        assertEquals(true, statistics.underThirty());
    }

    @Test
    public void underThirtyFalse() {
        assertFalse(statsOfLeoUnderThirty.underThirty());
    }

    @Test
    public void scvReportNull() {
        PlayerStatistics statistics = new PlayerStatistics(playerLeoUnderThirty, 0, 0);

        // because player has 0 games and 0 goals, we expect createScvRecord return null
        assertNull(statistics.createScvRecord());
    }

    @Test
    public void scvReportNotNull() {
        PlayerStatistics statistics = new PlayerStatistics(playerLeoUnderThirty, 3, 2);

        // because player has 3 games and 2 goals, we expect createScvRecord return not null
        assertNotNull(statistics.createScvRecord());
    }

    @Test
    public void getCsvStatsRecord() {
        PlayerStatistics statistics = new PlayerStatistics(playerLeoUnderThirty, 3, 6);

        Double[] expectedArray = {2D, 0.5d};
        
        assertArrayEquals(expectedArray, statistics.createScvRecord());
    }

}