package com.haojie.badmintonscorecounter;

import android.content.Context;
import android.graphics.Bitmap;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.File;
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

    public ArrayList<Player> getPlayersWithoutDefault() {
        ArrayList<Player> result = new ArrayList<>();
        for (Player p : mPlayers)
        {
            if (!p.getName().startsWith("Player "))
                result.add(p);
        }
        return result;
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

    public void removePlayer(String name)
    {
        Player player = getPlayerWithName(name);
        if (player == null)
            throw new IllegalArgumentException("Player not found");
        mPlayers.remove(player);
    }

    public void deserialize(Context context)
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


    public void serialize(Context context) throws IOException
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

    public static String writeBitmapToDisk(Bitmap bitmap)
    {
        String fileName = null;
        try {
            fileName = File.createTempFile(".pht", "").getPath();
            writeBitmapToDisk(bitmap, fileName);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return fileName;
    }

    public static void writeBitmapToDisk(Bitmap bitmap, String fileName)
    {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(fileName);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
            // PNG is a lossless format, the compression factor (100) is ignored
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public void updateOrAddPlayer(String name, Bitmap picture)
    {
        Player player = getPlayerWithName(name);
        if (player == null)
        {
            player = new Player(name);

            if (picture != null)
            {
                String fileName = Database.writeBitmapToDisk(picture);
                player.setImagePath(fileName);
            }
            addPlayer(player);
        }
        else
        {
            // update the image for the player in the database
            if (picture != null)
            {
                String fileName = Database.writeBitmapToDisk(picture);
                player.setImagePath(fileName);
            }
            else
                player.setImagePath(null);
        }
    }

    private ArrayList<Game> mGames = new ArrayList<Game>();
    private ArrayList<Player> mPlayers = new ArrayList<Player>();


}
