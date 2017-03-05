package com.haojie.badmintonscorecounter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.widget.ImageButton;

/**
 * Created by Haojie on 1/2/2017.
 */

class BitmapUtils {

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
        return Bitmap.createScaledBitmap(cropImg, 480, 480, true);
    }


    public static Bitmap resizePhotoToButtonSize(Bitmap original)
    {
        return Bitmap.createScaledBitmap(original, 96, 96, true);
    }

    private static Drawable convertDrawableToGrayScale(Drawable drawable) {
        if (drawable == null) {
            return null;
        }
        Drawable res = drawable.mutate();
        res.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);
        return res;
    }

    public static void setImageButtonEnabled(Context ctxt, boolean enabled, ImageButton item,
                                             int iconResId) {
        item.setEnabled(enabled);
        Drawable originalIcon = ctxt.getResources().getDrawable(iconResId);
        Drawable icon = enabled ? originalIcon : convertDrawableToGrayScale(originalIcon);
        item.setImageDrawable(icon);
    }

}
