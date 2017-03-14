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
 * Statistic presenter logic
 */

class GameStatisticsPresenter {

    private final Map<String, Integer> mPlayerWins = new HashMap<>();
    private final Map<String, Integer> mPlayerLoses = new HashMap<>();

    public GameStatisticsPresenter(IDatabase IDatabase)
    {
        mDatabase = IDatabase;
    }


    public void calculate()
    {
        mPlayerWins.clear();
        mPlayerLoses.clear();

        for (Player player : mDatabase.getPlayersWithoutDefault())
        {
            mPlayerWins.put(player.getName(), 0);
            mPlayerLoses.put(player.getName(), 0);
        }


        for (Game game : mDatabase.getGames())
        {
            updateWinLoseTable(game, mPlayerWins, mPlayerLoses);
        }

    }



    private void updateWinLoseTable(Game game, Map<String, Integer> playerWins, Map<String, Integer> playerLoses)
    {
        if (game.getWinner()!= 0)
        {
            if (game.getWinner() == 1)
            {
                Player p1 = game.getPlayer(Game.PlayerPosition.Team1Left);
                addEntry(playerWins, p1);
                Player p2 = game.getPlayer(Game.PlayerPosition.Team1Right);
                addEntry(playerWins, p2);

                Player p3 = game.getPlayer(Game.PlayerPosition.Team2Left);
                addEntry(playerLoses, p3);
                Player p4 = game.getPlayer(Game.PlayerPosition.Team2Right);
                addEntry(playerLoses, p4);
            }
            else
            {
                Player p1 = game.getPlayer(Game.PlayerPosition.Team2Left);
                addEntry(playerWins, p1);
                Player p2 = game.getPlayer(Game.PlayerPosition.Team2Right);
                addEntry(playerWins, p2);

                Player p3 = game.getPlayer(Game.PlayerPosition.Team1Left);
                addEntry(playerLoses, p3);
                Player p4 = game.getPlayer(Game.PlayerPosition.Team1Right);
                addEntry(playerLoses, p4);
            }
        }
    }

    private void addEntry(Map<String, Integer> playerWins, Player player)
    {
        if (player == null || !playerWins.containsKey(player.getName()))
            return;

        String playerName = player.getName();
        playerWins.put(playerName, playerWins.get(playerName) + 1);
    }

    public class PlayerWinEntry
    {
        public PlayerWinEntry(Player player, int wins, int loses)
        {
            mPlayer = player;
            mWins = wins;
            mLoses = loses;
        }

        public Player getPlayer() { return mPlayer; }
        public int getWins() { return mWins; }
        public int getLoses() { return mLoses; }

        public double getWinLoseRatio()
        {
            if (getWins() == 0 && getLoses() == 0)
                return 0.0;

            return getWins() / (getWins() + getLoses());
        }

        final Player mPlayer;
        final int mWins;
        final int mLoses;
    }

    public List<PlayerWinEntry> getTopNPlayersByTotalWins(int n)
    {
        int count = 0;
        ArrayList<PlayerWinEntry> temp = new ArrayList<>();

        for (Player player : mDatabase.getPlayersWithoutDefault())
        {
            int wins = mPlayerWins.get(player.getName());
            int loses = mPlayerLoses.get(player.getName());

            PlayerWinEntry entry = new PlayerWinEntry(player, wins, loses);
            temp.add(entry);
        }

        Collections.sort(temp, mWinComparator);
        int take = n;
        for (take = 0; take < n && take < temp.size(); take++)
        {
            if (temp.get(take).getWins() <= 0)
                break;
        }

        return temp.subList(0, take);
    }

    public List<PlayerWinEntry> getTopNPlayersByWinLoseRatio(int n)
    {
        int count = 0;
        ArrayList<PlayerWinEntry> temp = new ArrayList<>();

        for (Player player : mDatabase.getPlayersWithoutDefault())
        {
            int wins = mPlayerWins.get(player.getName());
            int loses = mPlayerLoses.get(player.getName());

            PlayerWinEntry entry = new PlayerWinEntry(player, wins, loses);
            temp.add(entry);
        }

        Collections.sort(temp, mWinLoseRatioComparator);
        int take = n;
        for (take = 0; take < n && take < temp.size(); take++)
        {
            if (temp.get(take).getWins() <= 0)
                break;
        }

        return temp.subList(0, take);
    }


    private Comparator<PlayerWinEntry> mWinComparator =  new Comparator<PlayerWinEntry>() {
        @Override
        public int compare(PlayerWinEntry o1, PlayerWinEntry o2) {
            if (o1.getWins() > o2.getWins())
                return -1;
            else if (o1.getWins() < o2.getWins())
                return 1;
            else
                return 0;
        }
    };

    private Comparator<PlayerWinEntry> mWinLoseRatioComparator =  new Comparator<PlayerWinEntry>() {
        @Override
        public int compare(PlayerWinEntry o1, PlayerWinEntry o2) {
            if (o1.getWinLoseRatio() > o2.getWinLoseRatio())
                return -1;
            else if (o1.getWinLoseRatio() < o2.getWinLoseRatio())
                return 1;
            else
                return 0;
        }
    };

    private final IDatabase mDatabase;
}
