package com.haojie.badmintonscorecounter;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class BadmintonScoreCounterUnitTest {
    @Test
    public void Game_onTeam1Score_IncreaseTeam1Points() throws Exception {
        Game game = new Game(true);
        int team1Score = game.getTeam1Score();
        game.onTeam1Score();
        assertEquals(team1Score + 1, game.getTeam1Score());
    }
}