package com.haojie.badmintonscorecounter;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class GameUnitTest {
    @Test
    public void Game_Init_ScoreZero() throws Exception {
        Game game = new Game(Game.GameType.Singles, 1);
        assertEquals(0, game.getTeam1Score());
        assertEquals(0, game.getTeam2Score());
        assertEquals(Game.PlayerPosition.Team1Right, game.getCurrentServer());
    }

    @Test
    public void Game_onTeam1Score_IncreaseTeam1Points() throws Exception {
        Game game = new Game(Game.GameType.Singles, 2);
        int team1Score = game.getTeam1Score();
        game.onTeam1Score();
        assertEquals(team1Score + 1, game.getTeam1Score());
        assertEquals(Game.PlayerPosition.Team1Left, game.getCurrentServer());
    }

    @Test
    public void Game_onTeam2Score_IncreaseTeam2Points() throws Exception {
        Game game = new Game(Game.GameType.Singles, 1);
        int team2Score = game.getTeam2Score();
        game.onTeam2Score();
        assertEquals(team2Score + 1, game.getTeam2Score());
        assertEquals(Game.PlayerPosition.Team2Left, game.getCurrentServer());
    }

}