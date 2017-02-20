package com.haojie.badmintonscorecounter;

import java.util.Date;
import java.util.Stack;

/**
 * Created by Haojie on 12/26/2016.
 */

public class Game {

    public enum GameType
    {
        Singles,
        Doubles
    }

    public enum PlayerPosition
    {
        Team1Left,
        Team1Right,
        Team2Left,
        Team2Right;

        public int getValue()
        {
            switch (this)
            {
                case Team1Left: return 0;
                case Team1Right: return 1;
                case Team2Left: return 2;
                case Team2Right: return 3;
            }
            return -1;
        }

        public int getTeam()
        {
            switch (this)
            {
                case Team1Left: return 1;
                case Team1Right: return 1;
                case Team2Left: return 2;
                case Team2Right: return 2;
            }
            return -1;
        }
    }


    private class GameState
    {
        public GameState(Player[] players, PlayerPosition currentServer, int team1Score, int team2Score)
        {
            mPlayers = players.clone();
            mCurrentServer = currentServer;
            mTeam1Score = team1Score;
            mTeam2Score = team2Score;
        }

        public Player[] getPlayers()
        {
            return mPlayers;
        }

        public PlayerPosition getCurrentServer()
        {
            return mCurrentServer;
        }

        public int getTeam1Score()
        {
            return mTeam1Score;
        }

        public int getTeam2Score()
        {
            return mTeam2Score;
        }

        private Player[] mPlayers;
        private PlayerPosition mCurrentServer;
        private int mTeam1Score, mTeam2Score;

    }

    public Game(GameType gameType, int firstServiceTeam)
    {
        mGameType = gameType;

        if (firstServiceTeam == 1)
            mCurrentServer = PlayerPosition.Team1Right;
        else if (firstServiceTeam == 2)
            mCurrentServer = PlayerPosition.Team2Right;
        else
            throw new IllegalArgumentException();

        mDate = new Date();
    }

    public Boolean getIsSingles()
    {
        return mGameType == GameType.Singles;
    }

    public  Boolean getIsDoubles()
    {
        return mGameType == GameType.Doubles;
    }

    public int getTeam1Score()
    {
        return mTeam1Score;
    }

    public int getTeam2Score()
    {
        return mTeam2Score;
    }

    public PlayerPosition getCurrentServer()
    {
        return mCurrentServer;
    }

    public void onTeam1Score()
    {
        if (getWinner() != 0)
            return; // no scoring after winning

        // clone the last game state
        mPlays.push(new GameState(mPlayers, mCurrentServer, mTeam1Score, mTeam2Score));
        mTeam1Score++;


        if (mGameType == GameType.Doubles)
        {
            if (mCurrentServer.getTeam() == 1)
            {
                swapTeam1Position();
            }

        }
        else
        {
            if ((mTeam1Score % 2 == 0) == (mCurrentServer.getValue() % 2 == 0))
            {
                swapTeam1Position();
                swapTeam2Position();
            }
        }

        if (mTeam1Score % 2 == 0)
            mCurrentServer = PlayerPosition.Team1Right;
        else
            mCurrentServer = PlayerPosition.Team1Left;

    }

    public void onTeam2Score()
    {
        if (getWinner() != 0)
            return; // no scoring after winning
        mPlays.push(new GameState(mPlayers, mCurrentServer, mTeam1Score, mTeam2Score));
        mTeam2Score++;

        if (mGameType == GameType.Doubles)
        {
            if (mCurrentServer.getTeam() == 2)
            {
                swapTeam2Position();
            }
        }
        else
        {
            if ((mTeam2Score % 2 == 0) == (mCurrentServer.getValue() % 2 == 0))
            {
                swapTeam1Position();
                swapTeam2Position();
            }
        }

        if (mTeam2Score % 2 == 0)
            mCurrentServer = PlayerPosition.Team2Right;
        else
            mCurrentServer = PlayerPosition.Team2Left;

    }

    public void undo()
    {
        GameState last = mPlays.pop();
        mTeam1Score = last.getTeam1Score();
        mTeam2Score = last.getTeam2Score();
        mPlayers = last.getPlayers().clone();
        mCurrentServer = last.getCurrentServer();
    }

    public boolean isUndoable()
    {
        return !mPlays.isEmpty();
    }

    public boolean isServiceChanged()
    {
        if (mPlays.isEmpty())
            return false;

        GameState last = mPlays.peek();
        PlayerPosition lastServer = last.getCurrentServer();
        boolean lastServerTeam1 = (lastServer == PlayerPosition.Team1Left || lastServer == PlayerPosition.Team1Right);
        boolean currentServerTeam1 = (getCurrentServer() == PlayerPosition.Team1Left || getCurrentServer() == PlayerPosition.Team1Right);
        return lastServerTeam1 != currentServerTeam1;
    }

    public boolean isGamePoint(int team)
    {
        if (team != 1 && team != 2)
            throw new IllegalArgumentException("team");
        int teamScore = (team == 1) ? getTeam1Score() : getTeam2Score();
        int opposingTeamScore = (team == 1) ? getTeam2Score() : getTeam1Score();

        if (teamScore == 20 && opposingTeamScore <= 19)
            return true;

        if (teamScore == 1 + opposingTeamScore && opposingTeamScore >= 20)
            return true;

        if (teamScore == 29 && opposingTeamScore == 29)
            return true;

        return false;
    }

    public int getWinner()
    {
        if (mTeam1Score == 30)
            return 1;

        if (mTeam2Score == 30)
            return 2;

        if (mTeam1Score == 21 && mTeam2Score < 20)
            return 1;

        if (mTeam2Score == 21 && mTeam1Score < 20)
            return 2;

        if (mTeam1Score > 21 && mTeam1Score == mTeam2Score + 2)
            return 1;

        if (mTeam2Score > 21 && mTeam2Score == mTeam1Score + 2)
            return 2;

        return 0;
    }

    public Player getPlayer(PlayerPosition position)
    {
        return mPlayers[position.getValue()];
    }

    public void setPlayer(PlayerPosition position, Player player)
    {
        if (mGameType != GameType.Doubles && (position == PlayerPosition.Team1Left || position == PlayerPosition.Team2Left))
            throw new IllegalArgumentException();

        mPlayers[position.getValue()] = player;
    }

    private void swapTeam1Position()
    {
        Player temp = mPlayers[0];
        mPlayers[0] = mPlayers[1];
        mPlayers[1] = temp;
    }

    private void swapTeam2Position()
    {
        Player temp = mPlayers[2];
        mPlayers[2] = mPlayers[3];
        mPlayers[3] = temp;
    }

    public Date getDate()
    {
        return mDate;
    }

    public void setDate(Date date)
    {
        mDate = date;
    }

    // member variables
    GameType mGameType;
    int mTeam1Score = 0;
    int mTeam2Score = 0;
    Stack<GameState> mPlays = new Stack<GameState>();
    Player [] mPlayers = new Player[4];
    PlayerPosition mCurrentServer;
    Date mDate;




}
