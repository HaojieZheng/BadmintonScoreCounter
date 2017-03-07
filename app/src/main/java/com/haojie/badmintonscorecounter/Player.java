package com.haojie.badmintonscorecounter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.gson.annotations.Expose;

import java.io.File;

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

    public String getImagePath() {
        return mImagePath;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored") // can't do anything if the temp file cannot be deleted
    public void setImagePath(String image) {
        if (mImagePath != null) {
            // delete the old image 1st
            new File(mImagePath).delete();
        }
        mImagePath = image;
    }

    public Bitmap getImage()
    {
        if (mImagePath != null) {
            return BitmapFactory.decodeFile(mImagePath);
        }
        else
            return null;
    }


    @Expose
    private String mName;
    @Expose
    private String mImagePath;

}
