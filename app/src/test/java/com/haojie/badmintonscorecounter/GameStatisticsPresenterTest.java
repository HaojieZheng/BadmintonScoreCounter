package com.haojie.badmintonscorecounter;

import android.util.Pair;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by Haojie on 2/25/2017.
 */
public class GameStatisticsPresenterTest {
    @Test
    public void getTopNPlayers_empty_database(){

        Database database = new Database();
        GameStatisticsPresenter presenter = new GameStatisticsPresenter(database);

        presenter.calculate();

        ArrayList<GameStatisticsPresenter.PlayerWinEntry> result = presenter.getTopNPlayers(1);

        assertTrue(result.isEmpty());
    }

    @Test
    public void getTopNPlayers_get_zero(){

        Database database = new Database();
        Player p1 = new Player("Test1");
        Player p2 = new Player("Test2");
        database.addPlayer(p1);
        database.addPlayer(p2);

        database.addGame(createGameWithWinner(p1, p2, 1));

        GameStatisticsPresenter presenter = new GameStatisticsPresenter(database);

        presenter.calculate();

        ArrayList<GameStatisticsPresenter.PlayerWinEntry> result = presenter.getTopNPlayers(0);

        assertTrue(result.isEmpty());
    }

    @Test
    public void getTopNPlayers_get_two_but_only_one_win(){

        Database database = new Database();
        Player p1 = new Player("Test1");
        Player p2 = new Player("Test2");
        database.addPlayer(p1);
        database.addPlayer(p2);

        database.addGame(createGameWithWinner(p1, p2, 1));

        GameStatisticsPresenter presenter = new GameStatisticsPresenter(database);

        presenter.calculate();

        ArrayList<GameStatisticsPresenter.PlayerWinEntry> result = presenter.getTopNPlayers(2);
        assertEquals(1, result.size());
        assertEquals(p1, result.get(0).getPlayer());

    }


    private Game createGameWithWinner(Player p1, Player p2, int teamWin)
    {
        Game game = new Game(Game.GameType.Singles, 1);
        game.setPlayer(Game.PlayerPosition.Team1Right, p1);
        game.setPlayer(Game.PlayerPosition.Team2Right, p2);
        setGameWinner(game, 1);

        return game;
    }


    private void setGameWinner(Game game, int team)
    {
        for (int  i = 0; i < 21; i++) {
            if (team == 1)
                game.onTeam1Score();
            else
                game.onTeam2Score();
        }

    }

}