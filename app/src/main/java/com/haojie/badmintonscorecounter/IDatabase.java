package com.haojie.badmintonscorecounter;

import java.util.ArrayList;

/**
 * Created by Haojie on 3/13/2017.
 */

interface IDatabase {
    ArrayList<Player> getPlayers();

    ArrayList<Player> getPlayersWithoutDefault();

    ArrayList<Game> getGames();

    Player getPlayerWithName(String name);
}
