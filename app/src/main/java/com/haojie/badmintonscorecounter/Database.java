package com.haojie.badmintonscorecounter;

import android.content.Context;
import android.os.Debug;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
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
        return null;
    }

    public void addPlayer(Player player)
    {
        if (getPlayerWithName(player.getName()) != null)
            throw new IllegalArgumentException("Player already exists");

        mPlayers.add(player);
    }

    public void Deserialize(Context context)
    {
        try {
            FileInputStream stream = context.openFileInput("persist.db");
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            StringBuilder out = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                out.append(line);
            }
            Database db = new Gson().fromJson(out.toString(), Database.class);
            mPlayers = db.mPlayers;
            mGames = db.mGames;

            stream.close();
        }
        catch (IOException e)
        {
        }
    }


    public void Serialize(Context context) throws IOException
    {
        String serialized = new Gson().toJson(this);
        try
        {
            FileOutputStream stream = context.openFileOutput("persist.db", Context.MODE_PRIVATE);
            stream.write(serialized.getBytes());
            stream.close();
        }
        catch (FileNotFoundException e)
        {
            // not possible, since it overwrites
        }
    }

    ArrayList<Game> mGames = new ArrayList<Game>();
    ArrayList<Player> mPlayers = new ArrayList<Player>();


}
