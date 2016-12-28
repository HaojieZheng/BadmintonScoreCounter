package com.haojie.badmintonscorecounter;

/**
 * Created by Haojie on 12/28/2016.
 */

public class Player
{
    public Player(String name)
    {
        setName(name);
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    String mName;

}
