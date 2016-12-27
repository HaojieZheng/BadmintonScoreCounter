package com.haojie.badmintonscorecounter;

import java.security.InvalidParameterException;
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
            };
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
            };
            return -1;
        }
    }


    class GameState
    {
        public GameState(String [] playerNames, PlayerPosition currentServer, int team1Score, int team2Score)
        {
            mPlayerNames = playerNames.clone();
            mCurrentServer = currentServer;
            mTeam1Score = team1Score;
            mTeam2Score = team2Score;
        }

        public String[] getPlayerNames()
        {
            return mPlayerNames;
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

        private String[] mPlayerNames;
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
        mPlays.push(new GameState(mPlayerNames, mCurrentServer, mTeam1Score, mTeam2Score));
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
        mPlays.push(new GameState(mPlayerNames, mCurrentServer, mTeam1Score, mTeam2Score));
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
        mPlayerNames = last.getPlayerNames().clone();
        mCurrentServer = last.getCurrentServer();
    }

    public boolean isUndoable()
    {
        return !mPlays.isEmpty();
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

    public String getPlayerName(PlayerPosition position)
    {
        String result = mPlayerNames[position.getValue()];
        if (result == null)
            result = "";

        return result;
    }

    public void setPlayerName(PlayerPosition position, String name)
    {
        if (mGameType != mGameType.Doubles && (position == PlayerPosition.Team1Left || position == PlayerPosition.Team2Left))
            throw new InvalidParameterException();

        mPlayerNames[position.getValue()] = name;
    }

    private void swapTeam1Position()
    {
        String temp = mPlayerNames[0];
        mPlayerNames[0] = mPlayerNames[1];
        mPlayerNames[1] = temp;
    }

    private void swapTeam2Position()
    {
        String temp = mPlayerNames[2];
        mPlayerNames[2] = mPlayerNames[3];
        mPlayerNames[3] = temp;
    }

    // member variables
    GameType mGameType;
    int mTeam1Score = 0;
    int mTeam2Score = 0;
    Stack<GameState> mPlays = new Stack<GameState>();
    String [] mPlayerNames = new String[4];
    PlayerPosition mCurrentServer;




}
