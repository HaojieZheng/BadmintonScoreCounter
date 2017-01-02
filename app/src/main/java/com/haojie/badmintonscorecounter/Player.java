package com.haojie.badmintonscorecounter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.ContactsContract;

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
            Bitmap bitmap = BitmapFactory.decodeFile(mImagePath);
            return bitmap;
        }
        else
            return null;
    }


    String mName;
    String mImagePath;

}
