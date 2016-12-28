package com.haojie.badmintonscorecounter;

import android.content.Context;

import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Haojie on 12/28/2016.
 */

public class Database {

    public Database()
    {

    }

    public ArrayList<Player> getPlayers() {
        return mPlayers;
    }


    public ArrayList<Game> getGames() {
        return mGames;
    }

    ArrayList<Game> mGames = new ArrayList<Game>();
    ArrayList<Player> mPlayers = new ArrayList<Player>();

    public void addGame(Game game)
    {
        mGames.add(game);
    }


    public Player getPlayerWithName(String name)
    {
        for (Player player : mPlayers)
        {
            if (player.getName().compareToIgnoreCase(name) == 0)
                return player;
        }
        Player p = new Player(name);
        mPlayers.add(p);

        return p;
    }




    public void Serialize(Context context) throws IOException
    {
        String serialized = new Gson().toJson(this);
        try
        {
            FileOutputStream stream = context.openFileOutput("persist.db", Context.MODE_PRIVATE);
            stream.write(serialized.getBytes());
        }
        catch (FileNotFoundException e)
        {
            // not possible, since it overwrites
        }
    }

}
