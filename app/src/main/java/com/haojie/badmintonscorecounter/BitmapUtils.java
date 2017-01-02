package com.haojie.badmintonscorecounter;

import android.graphics.Bitmap;

/**
 * Created by Haojie on 1/2/2017.
 */

public class BitmapUtils {

    public static Bitmap resizeAndCropPhoto(Bitmap original)
    {
        int width = original.getWidth();
        int height = original.getHeight();
        int newWidth = (height > width) ? width : height;
        int newHeight = (height > width)? height - ( height - width) : height;
        int cropW = (width - height) / 2;
        cropW = (cropW < 0)? 0: cropW;
        int cropH = (height - width) / 2;
        cropH = (cropH < 0)? 0: cropH;
        Bitmap cropImg = Bitmap.createBitmap(original, cropW, cropH, newWidth, newHeight);
        Bitmap resizedImg = Bitmap.createScaledBitmap(cropImg, 480, 480, true);

        return resizedImg;
    }


    public static Bitmap resizePhotoToButtonSize(Bitmap original)
    {
        return Bitmap.createScaledBitmap(original, 96, 96, true);
    }

}
