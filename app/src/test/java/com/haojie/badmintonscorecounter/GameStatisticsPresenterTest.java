package com.haojie.badmintonscorecounter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

/**
 * Created by Haojie on 2/25/2017.
 * Tests for the GameStatisticsPresenter class
 */
@RunWith(MockitoJUnitRunner.class)
public class GameStatisticsPresenterTest {
    @Test
    public void getTopNPlayers_empty_database(){

        IDatabase IDatabase = new Database();
        GameStatisticsPresenter presenter = new GameStatisticsPresenter(IDatabase);

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

    @Mock
    IDatabase mockDatabase;

    @Test
    public void getTopNPlayers_get_two_but_only_one_win(){
        Player p1 = new Player("Test1");
        Player p2 = new Player("Test2");

        ArrayList<Player> players = new ArrayList<>();
        players.add(p1);
        players.add(p2);
        when(mockDatabase.getPlayersWithoutDefault()).thenReturn(players);

        ArrayList<Game> games = new ArrayList<>();
        games.add(createGameWithWinner(p1, p2, 1));
        when(mockDatabase.getGames()).thenReturn(games);
        when(mockDatabase.getPlayerWithName("Test1")).thenReturn(p1);
        when(mockDatabase.getPlayerWithName("Test2")).thenReturn(p2);

        GameStatisticsPresenter presenter = new GameStatisticsPresenter(mockDatabase);

        presenter.calculate();

        ArrayList<GameStatisticsPresenter.PlayerWinEntry> result = presenter.getTopNPlayers(2);
        assertEquals(1, result.size());
        assertEquals(p1, result.get(0).getPlayer());
        assertEquals(1, result.get(0).getWins());
    }


    @Test
    public void getTopNPlayers_get_three_but_only_two(){

        Database database = new Database();
        Player p1 = new Player("Test1");
        Player p2 = new Player("Test2");
        database.addPlayer(p1);
        database.addPlayer(p2);

        database.addGame(createGameWithWinner(p1, p2, 1));
        database.addGame(createGameWithWinner(p1, p2, 1));
        database.addGame(createGameWithWinner(p1, p2, 2));

        GameStatisticsPresenter presenter = new GameStatisticsPresenter(database);

        presenter.calculate();

        ArrayList<GameStatisticsPresenter.PlayerWinEntry> result = presenter.getTopNPlayers(2);
        assertEquals(2, result.size());
        assertEquals(p1, result.get(0).getPlayer());
        assertEquals(2, result.get(0).getWins());
        assertEquals(p2, result.get(1).getPlayer());
        assertEquals(1, result.get(1).getWins());
    }


    private Game createGameWithWinner(Player p1, Player p2, int teamWin)
    {
        Game game = new Game(Game.GameType.Singles, 1);
        game.setPlayer(Game.PlayerPosition.Team1Right, p1);
        game.setPlayer(Game.PlayerPosition.Team2Right, p2);
        setGameWinner(game, teamWin);

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