package com.haojie.badmintonscorecounter;

import android.content.Context;
import android.graphics.Bitmap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Haojie on 12/28/2016.
 */

class Database {

    public Database()
    {
        // because when the DB was created in 1.1, there is no version number.
        // So, to make sure the DB is converted correctly from 1.1, initialize the version to 1
        // This will be set to the latest version when the database is serialized
        mVersion = 1;
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
            ConvertDataBase(db.mVersion);

            stream.close();
        }
        catch (IOException e)
        {
        }
    }

    /**
     * Convert the database to the latest version
     * @param version current database version
     */
    private void ConvertDataBase(int version) {
        if (version < 2) {
            for (Game game : mGames) {
                if (game.getDate() == null)
                    game.setDate(new Date());
            }
        }
    }



    public void serialize(Context context) throws IOException
    {
        mVersion = CURRENT_RELEASE_VERSION;
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        String serialized = gson.toJson(this);
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

    int getVersion()
    {
        return CURRENT_RELEASE_VERSION;
    }


    @Expose
    private ArrayList<Game> mGames = new ArrayList<>();
    @Expose
    private ArrayList<Player> mPlayers = new ArrayList<>();
    @Expose
    private int mVersion;
    private static final int CURRENT_RELEASE_VERSION = 2;


}
