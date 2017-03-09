package com.haojie.badmintonscorecounter;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Haojie on 2/21/2017.
 * Tests for the GamePresenter class
 */
public class GamePresenterTest {
    @Test
    public void getAnnouncementText() throws Exception {
        Game game = new Game(Game.GameType.Singles, 1);
        assertEquals("0 all", GamePresenter.getAnnouncementText(game, true));

        game.onTeam1Score();
        assertEquals("1 0", GamePresenter.getAnnouncementText(game, true));

        game.onTeam2Score();
        assertEquals("Service Over 1 all", GamePresenter.getAnnouncementText(game, true));

        game.onTeam1Score();
        assertEquals("Service Over 2 1", GamePresenter.getAnnouncementText(game, true));

        for (int i = 0; i < 18; i++)
            game.onTeam1Score();

        assertEquals("20 Game Point 1", GamePresenter.getAnnouncementText(game, true));

        game.onTeam1Score();
        assertEquals("Team 1 wins 21 1", GamePresenter.getAnnouncementText(game, true));

        game.undo();

        for (int i = 0; i < 21; i++)
            game.onTeam2Score();

        assertEquals("Team 2 wins 22 20", GamePresenter.getAnnouncementText(game, true));

    }



}