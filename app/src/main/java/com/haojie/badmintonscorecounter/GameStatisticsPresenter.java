package com.haojie.badmintonscorecounter;

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
        Map<String, Integer> playerWins = new HashMap<>();

        for (Player player : mDatabase.getPlayersWithoutDefault())
        {
            playerWins.put(player.getName(), 0);
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
        mPlayersByWins = sortByValue(playerWins, true);
    }

    private void addWin(Map<String, Integer> playerWins, Player player)
    {
        if (player == null || !playerWins.containsKey(player.getName()))
            return;

        String playerName = player.getName();
        playerWins.put(playerName, playerWins.get(playerName) + 1);
    }

    public class PlayerWinEntry
    {
        public PlayerWinEntry(Player player, int wins)
        {
            mPlayer = player;
            mWins = wins;
        }

        public Player getPlayer() { return mPlayer; }
        public int getWins() { return mWins;}

        final Player mPlayer;
        final int mWins;
    }

    public ArrayList<PlayerWinEntry> getTopNPlayers(int n)
    {
        int count = 0;
        ArrayList<PlayerWinEntry> result = new ArrayList<>();

        for(Map.Entry<String, Integer> entry : mPlayersByWins.entrySet()) {
            if (count >= n)
                break;

            Player player = mDatabase.getPlayerWithName(entry.getKey());
            int wins = entry.getValue();
            if (wins > 0) {
                PlayerWinEntry playerWinEntry = new PlayerWinEntry(player, wins);
                result.add(playerWinEntry);
                count++;
            }
            else
                break;
        }

        return result;
    }


    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue( Map<K, V> map , boolean reverse)
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

        if (reverse)
            Collections.reverse(list);
        Map<K, V> result = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : list)
        {
            result.put( entry.getKey(), entry.getValue() );
        }
        return result;
    }



    final Database mDatabase;
    Map<String, Integer> mPlayersByWins;



}
