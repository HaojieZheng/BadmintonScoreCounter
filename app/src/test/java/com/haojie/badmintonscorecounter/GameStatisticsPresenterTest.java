package com.haojie.badmintonscorecounter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

/**
 * Created by Haojie on 2/25/2017.
 * Tests for the GameStatisticsPresenter class
 */
@RunWith(MockitoJUnitRunner.class)
public class GameStatisticsPresenterTest {

    @Mock
    private IDatabase mockDatabase;

    @Mock
    private Game mockGamePlayer1Wins;

    @Mock
    private Game mockGamePlayer2Wins;

    private final Player mPlayer1 = new Player("Test1");
    private final Player mPlayer2 = new Player("Test2");


    @Before
    public void setUp()
    {
        ArrayList<Player> players = new ArrayList<>();
        players.add(mPlayer1);
        players.add(mPlayer2);
        when(mockDatabase.getPlayersWithoutDefault()).thenReturn(players);
        when(mockDatabase.getPlayerWithName("Test1")).thenReturn(mPlayer1);
        when(mockDatabase.getPlayerWithName("Test2")).thenReturn(mPlayer2);


        when(mockGamePlayer1Wins.getWinner()).thenReturn(1);
        when(mockGamePlayer1Wins.getPlayer(Game.PlayerPosition.Team1Right)).thenReturn(mPlayer1);
        when(mockGamePlayer1Wins.getPlayer(Game.PlayerPosition.Team2Right)).thenReturn(mPlayer2);
        when(mockGamePlayer1Wins.getTeam1Score()).thenReturn(21);
        when(mockGamePlayer1Wins.getTeam2Score()).thenReturn(0);

        when(mockGamePlayer2Wins.getWinner()).thenReturn(2);
        when(mockGamePlayer2Wins.getPlayer(Game.PlayerPosition.Team1Right)).thenReturn(mPlayer1);
        when(mockGamePlayer2Wins.getPlayer(Game.PlayerPosition.Team2Right)).thenReturn(mPlayer2);
        when(mockGamePlayer2Wins.getTeam1Score()).thenReturn(0);
        when(mockGamePlayer2Wins.getTeam2Score()).thenReturn(21);

    }

    @Test
    public void getTopNPlayers_empty_database(){

        IDatabase IDatabase = new Database();
        GameStatisticsPresenter presenter = new GameStatisticsPresenter(IDatabase);

        presenter.calculate();

        List<GameStatisticsPresenter.PlayerWinEntry> result = presenter.getTopNPlayers(1);

        assertTrue(result.isEmpty());
    }

    @Test
    public void getTopNPlayers_get_zero(){

        ArrayList<Game> games = new ArrayList<>();
        games.add(mockGamePlayer1Wins);
        when(mockDatabase.getGames()).thenReturn(games);

        GameStatisticsPresenter presenter = new GameStatisticsPresenter(mockDatabase);

        presenter.calculate();

        List<GameStatisticsPresenter.PlayerWinEntry> result = presenter.getTopNPlayers(0);

        assertTrue(result.isEmpty());
    }


    @Test
    public void getTopNPlayers_get_two_but_only_one_win(){

        ArrayList<Game> games = new ArrayList<>();
        games.add(mockGamePlayer1Wins);
        when(mockDatabase.getGames()).thenReturn(games);

        GameStatisticsPresenter presenter = new GameStatisticsPresenter(mockDatabase);

        presenter.calculate();

        List<GameStatisticsPresenter.PlayerWinEntry> result = presenter.getTopNPlayers(2);
        assertEquals(1, result.size());
        assertEquals(mPlayer1, result.get(0).getPlayer());
        assertEquals(1, result.get(0).getWins());
    }


    @Test
    public void getTopNPlayers_get_three_but_only_two(){

        ArrayList<Game> games = new ArrayList<>();
        games.add(mockGamePlayer1Wins);
        games.add(mockGamePlayer1Wins);
        games.add(mockGamePlayer2Wins);
        when(mockDatabase.getGames()).thenReturn(games);


        GameStatisticsPresenter presenter = new GameStatisticsPresenter(mockDatabase);

        presenter.calculate();

        List<GameStatisticsPresenter.PlayerWinEntry> result = presenter.getTopNPlayers(2);
        assertEquals(2, result.size());
        assertEquals(mPlayer1, result.get(0).getPlayer());
        assertEquals(2, result.get(0).getWins());
        assertEquals(mPlayer2, result.get(1).getPlayer());
        assertEquals(1, result.get(1).getWins());
    }

}