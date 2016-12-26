package com.haojie.badmintonscorecounter;

import java.util.Stack;

/**
 * Created by Haojie on 12/26/2016.
 */

public class Game {
    public Game(Boolean isSingles)
    {
        mIsSingles = isSingles;
    }

    public Boolean getIsSingles()
    {
        return mIsSingles;
    }

    public  Boolean getIsDoubles()
    {
        return !mIsSingles;
    }

    public int getTeam1Score()
    {
        return mTeam1Score;
    }

    public int getTeam2Score()
    {
        return mTeam2Score;
    }

    public void onTeam1Score()
    {
        if (getWinner() != 0)
            return; // no scoring after winning

        mPlays.add(1);
        mTeam1Score++;
    }

    public void onTeam2Score()
    {
        if (getWinner() != 0)
            return; // no scoring after winning
        mPlays.add(2);
        mTeam2Score++;
    }

    public void undo()
    {
        int last = mPlays.pop();
        if (last == 1)
            mTeam1Score--;
        else
            mTeam2Score--;
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


    // member variables
    Boolean mIsSingles;
    int mTeam1Score = 0;
    int mTeam2Score = 0;
    Stack<Integer> mPlays = new Stack<Integer>();




}
