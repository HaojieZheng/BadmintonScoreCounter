package com.haojie.badmintonscorecounter;

import org.junit.Test;

import static org.junit.Assert.*;


/**
 * Created by Haojie on 1/3/2017.
 */

public class DatabaseTest {

    @Test
    public void Database_GetPlayers_ReturnsAllPlayers() throws Exception {
        Database database = new Database();
        database.addPlayer(new Player("Player 1"));
        database.addPlayer(new Player("Player 2"));
        database.addPlayer(new Player("Player 3"));
        database.addPlayer(new Player("Player 4"));

        assertEquals(4, database.getPlayers().size());
    }

    @Test
    public void Database_GetPlayersWithoutDefault_ReturnsOnlyNamedPlayers() throws Exception {
        Database database = new Database();
        database.addPlayer(new Player("Player 1"));
        database.addPlayer(new Player("Player 2"));
        database.addPlayer(new Player("Player 3"));
        database.addPlayer(new Player("Player 4"));
        database.addPlayer(new Player("Real Player"));

        assertEquals(1, database.getPlayersWithoutDefault().size());
        assertEquals("Real Player", database.getPlayersWithoutDefault().get(0).getName());
    }

    @Test(expected= IllegalArgumentException.class)
    public void Database_addPlayer_duplicatePlayer() {
        Database database = new Database();
        database.addPlayer(new Player("Player 1"));
        database.addPlayer(new Player("player 1"));
    }

    @Test(expected= IllegalArgumentException.class)
    public void Database_removePlayer_nonExisting() {
        Database database = new Database();
        database.removePlayer("Player 1");

    }

}
