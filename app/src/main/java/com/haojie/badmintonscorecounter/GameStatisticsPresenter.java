package com.haojie.badmintonscorecounter;

import android.util.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by Haojie on 2/23/2017.
 */

public class GameStatisticsPresenter {
    public GameStatisticsPresenter(Database database )
    {
        mDatabase = database;
    }


    public void calculate()
    {
        Map<Player, Integer> playerWins = new HashMap<>();

        for (Player player : mDatabase.getPlayersWithoutDefault())
        {
            playerWins.put(player, 0);
        }


        for (Game game : mDatabase.getGames())
        {
            if (game.getWinner()!= 0)
            {
                if (game.getWinner() == 1)
                {
                    Player p1 = game.getPlayer(Game.PlayerPosition.Team1Left);
                    addWin(playerWins, p1);
                    Player p2 = game.getPlayer(Game.PlayerPosition.Team1Right);
                    addWin(playerWins, p2);
                }
                else
                {
                    Player p1 = game.getPlayer(Game.PlayerPosition.Team2Left);
                    addWin(playerWins, p1);
                    Player p2 = game.getPlayer(Game.PlayerPosition.Team2Right);
                    addWin(playerWins, p2);
                }
            }
        }
        mPlayersByWins = sortByValue(playerWins);

    }

    private void addWin(Map<Player, Integer> playerWins, Player player)
    {
        if (player == null || !playerWins.containsKey(player))
            return;
        Integer score = playerWins.get(player);
        playerWins.put(player, score + 1);
    }

    public ArrayList<Pair<Player, Integer>> getTopNPlayers(int n)
    {
        int count = 0;
        ArrayList<Pair<Player, Integer>> result = new ArrayList<Pair<Player, Integer>>();

        for(Map.Entry<Player, Integer> entry : mPlayersByWins.entrySet()) {
            result.add(new Pair<Player, Integer>(entry.getKey(), entry.getValue()));
            count ++;
            if (count > n)
                break;
        }

        return result;
    }


    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue( Map<K, V> map )
    {
        List<Map.Entry<K, V>> list =
                new LinkedList<>( map.entrySet() );
        Collections.sort( list, new Comparator<Map.Entry<K, V>>()
        {
            @Override
            public int compare( Map.Entry<K, V> o1, Map.Entry<K, V> o2 )
            {
                return ( o1.getValue() ).compareTo( o2.getValue() );
            }
        } );

        Map<K, V> result = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : list)
        {
            result.put( entry.getKey(), entry.getValue() );
        }
        return result;
    }



    Database mDatabase;
    Map<Player, Integer> mPlayersByWins;



}
