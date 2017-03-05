package com.haojie.badmintonscorecounter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import java.util.ArrayList;

/**
 * Created by Haojie on 12/25/2016.
 * Control for display the badminton court
 */

public class CourtView extends View {

    
    public interface CourtViewTouchListener
    {
        void onTeam1Score();
        void onTeam2Score();
    }

    public enum Position
    {
        None,
        TopLeft,
        TopRight,
        BottomLeft,
        BottomRight
    }

    public enum Orientation
    {
        None,
        Protrait,
        Landscape
    }

    // Constructors
    public CourtView(Context context)
    {
        super(context);
    }

    public CourtView(Context context, AttributeSet attributeSet)
    {
        super(context, attributeSet);
    }

    public CourtView(Context context, AttributeSet attributeSet, int defStyle)
    {
        super(context, attributeSet, defStyle);
    }

    ////////////////////////////
    // Getters and Setters
    //////////////////////////////

    public void registerListener(CourtViewTouchListener listener)
    {
        mListeners.add(listener);
    }

    public void setTopRightName(String name)
    {
        mTopRightName = name;
    }

    public void setTopLeftName(String name)
    {
        mTopLeftName = name;
    }

    public void setBottomRightName(String name)
    {
        mBottomRightName = name;
    }

    public void setBottomLeftName(String name)
    {
        mBottomLeftName = name;
    }

    public void setServicePosition(Position position)
    {
        mServicePosition = position;
    }

    public String getTopLeftName()
    {
        return mTopLeftName;
    }

    public String getTopRightName()
    {
        return mTopRightName;
    }

    public String getBottomLeftName()
    {
        return mBottomLeftName;
    }

    public String getBottomRightName()
    {
        return mBottomRightName;
    }

    public Position getServicePosition()
    {
        return mServicePosition;
    }

    private ArrayList<CourtViewTouchListener> mListeners = new ArrayList<CourtViewTouchListener>();

    private String mTopLeftName = "";
    private String mTopRightName = "";
    private String mBottomLeftName = "";
    private String mBottomRightName = "";

    private Position mServicePosition = Position.None;

    static final double courtRatio =  13.4 / 6.1;
    static final double longServiceLine = 0.8 / 13.4;
    static final double shortServiceLine = 4.68 / 13.4;
    static final double singlesSideLine = 0.46 / 6.1;

    private Bitmap mTopLeftPic;
    private Bitmap mTopRightPic;
    private Bitmap mBottomLeftPic;
    private Bitmap mBottomRightPic;
    private Orientation mOrientation = Orientation.None;

    public Bitmap getTopLeftPic() {
        return mTopLeftPic;
    }

    public void setTopLeftPic(Bitmap topLeftPic) {
        mTopLeftPic = topLeftPic;
    }

    public Bitmap getTopRightPic() {
        return mTopRightPic;
    }

    public void setTopRightPic(Bitmap topRightPic) {
        mTopRightPic = topRightPic;
    }

    public Bitmap getBottomLeftPic() {
        return mBottomLeftPic;
    }

    public void setBottomLeftPic(Bitmap bottomLeftPic) {
        mBottomLeftPic = bottomLeftPic;
    }

    public Bitmap getBottomRightPic() {
        return mBottomRightPic;
    }

    public void setBottomRightPic(Bitmap bottomRightPic) {
        mBottomRightPic = bottomRightPic;
    }





    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        double x = getWidth();
        double y = getHeight();

        if (y >= x)
            drawProtrait(canvas);
        else
            drawLandScape(canvas);
    }


    float touched_x, touched_y;
    boolean touched = false;

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        touched_x = event.getX();
        touched_y = event.getY();

        double y = getHeight();
        double x = getWidth();
        if (mOrientation == Orientation.Protrait) {
            if (y < courtRatio * x) {
                x = y / courtRatio;
            } else {
                y = courtRatio * x;
            }
        }
        else
        {
            if (x < courtRatio * y)
            {
                y = x / courtRatio;
            }
            else
            {
                x = courtRatio * y;
            }
        }

        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                touched = true;
                break;
            case MotionEvent.ACTION_MOVE:
                touched = true;
                break;
            case MotionEvent.ACTION_UP:
                if (touched)
                {
                    Boolean team1Score = true;
                    if (mOrientation == Orientation.Protrait && touched_x <= x) {
                        if (touched_y > y / 2)
                            team1Score = false;


                        for (CourtViewTouchListener listener: mListeners
                                ) {
                            if (team1Score)
                                listener.onTeam1Score();
                            else
                                listener.onTeam2Score();
                        }
                    }
                    else if (mOrientation == Orientation.Landscape && touched_y <= y)
                    {
                        if (touched_x > x / 2)
                            team1Score = false;


                        for (CourtViewTouchListener listener: mListeners
                                ) {
                            if (team1Score)
                                listener.onTeam1Score();
                            else
                                listener.onTeam2Score();
                        }
                    }

                }

                touched = false;
                break;
            case MotionEvent.ACTION_CANCEL:
                touched = false;
                break;
            case MotionEvent.ACTION_OUTSIDE:
                touched = false;
                break;
            default:
        }
        return true; // processed
    }

    /////////////////////////////////
    // private methods
    ////////////////////////////////////

    private void drawPlayerNames(String text, Bitmap picture, float x, float y, int maxWidth, Canvas canvas, TextPaint textPaint)
    {
        StaticLayout sl = new StaticLayout(text, textPaint, maxWidth,
                Layout.Alignment.ALIGN_CENTER, 1, 1, true);

        canvas.save();

        canvas.translate(x, y);

        //draws static layout on canvas
        sl.draw(canvas);
        canvas.restore();


        if (picture != null) {
            picture = Bitmap.createScaledBitmap(picture, (int)(maxWidth * 0.4), (int)(maxWidth * 0.4), true);
            canvas.drawBitmap(picture, x + (int)(maxWidth * 0.3), y + sl.getHeight()+ 10, null);
        }

    }


    private void drawProtrait(Canvas canvas)
    {
        mOrientation = Orientation.Protrait;

        double x = getWidth();
        double y = getHeight();

        Paint paint = new Paint();
        if (y < courtRatio * x)
        {
            x = y / courtRatio;
        }
        else
        {
            y = courtRatio * x;
        }

        paint.setColor(Color.parseColor("#368F10"));
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(0, 0, (float)x, (float)y, paint);


        // draw the borders
        paint.setColor(Color.parseColor("#DDDDDD"));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth((float)5.0);
        canvas.drawRect(0, 0, (float)x, (float)y, paint);

        // center line
        canvas.drawLine(0, (float)y/2, (float)x, (float)y/2, paint);

        // long service line
        float longServiceY = (float)(longServiceLine * y);
        canvas.drawLine(0, longServiceY, (float)x, longServiceY, paint);
        canvas.drawLine(0, (float)y - longServiceY, (float)x, (float)y - longServiceY, paint);

        // short service line
        float shortServiceY = (float)(shortServiceLine * y);
        canvas.drawLine(0, shortServiceY, (float)x, shortServiceY, paint);
        canvas.drawLine(0, (float)y - shortServiceY, (float)x, (float)y - shortServiceY, paint);

        // players center line
        canvas.drawLine((float)x / 2, 0, (float)x/2, shortServiceY, paint);
        canvas.drawLine((float)x / 2, (float)y - shortServiceY, (float)x/2, (float)y, paint);

        // singles side line
        float singlesSideLinesX = (float)(singlesSideLine * x);
        canvas.drawLine(singlesSideLinesX, 0, singlesSideLinesX, (float)y, paint);
        canvas.drawLine((float)x - singlesSideLinesX, 0, (float)x - singlesSideLinesX, (float)y, paint);

        TextPaint textPaint = new TextPaint();
        textPaint.setTextSize(50);
        textPaint.setColor(Color.BLACK);
        textPaint.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));

        TextPaint servicePositionPaint = new TextPaint();
        servicePositionPaint.setTextSize(60);
        servicePositionPaint.setColor(Color.parseColor("#FFD800"));
        servicePositionPaint.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));

        int maxWidth = (int)(x/2 - singlesSideLinesX);
        drawPlayerNames(getTopLeftName(), getTopLeftPic(), singlesSideLinesX, longServiceY, maxWidth, canvas, mServicePosition == Position.TopLeft ? servicePositionPaint : textPaint);
        drawPlayerNames(getTopRightName(), getTopRightPic (), (float)x/2, longServiceY, maxWidth, canvas, mServicePosition == Position.TopRight ? servicePositionPaint : textPaint);
        drawPlayerNames(getBottomLeftName(), getBottomLeftPic(), singlesSideLinesX, (float)y - shortServiceY, maxWidth, canvas, mServicePosition == Position.BottomLeft ? servicePositionPaint : textPaint);
        drawPlayerNames(getBottomRightName(), getBottomRightPic (), (float)x/2, (float)y - shortServiceY, maxWidth, canvas, mServicePosition == Position.BottomRight ? servicePositionPaint : textPaint);
    }

    private void drawLandScape(Canvas canvas)
    {
        mOrientation = Orientation.Landscape;

        double x = getWidth();
        double y = getHeight();

        Paint paint = new Paint();
        if (x < courtRatio * y)
        {
            y = x / courtRatio;
        }
        else
        {
            x = courtRatio * y;
        }

        paint.setColor(Color.parseColor("#368F10"));
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(0, 0, (float)x, (float)y, paint);


        // draw the borders
        paint.setColor(Color.parseColor("#DDDDDD"));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth((float)5.0);
        canvas.drawRect(0, 0, (float)x, (float)y, paint);

        // center line
        canvas.drawLine((float)x/2, 0, (float)x/2, (float)y, paint);

        // long service line
        float longServiceX = (float)(longServiceLine * x);
        canvas.drawLine(longServiceX, 0, longServiceX, (float)y, paint);
        canvas.drawLine((float)x - longServiceX, 0, (float)x - longServiceX, (float)y, paint);

        // short service line
        float shortServiceX = (float)(shortServiceLine * x);
        canvas.drawLine(shortServiceX, 0, shortServiceX, (float)y, paint);
        canvas.drawLine((float)x - shortServiceX, 0, (float)x - shortServiceX, (float)y, paint);

        // players center line
        canvas.drawLine(0, (float)y / 2, shortServiceX, (float)y / 2, paint);
        canvas.drawLine((float)x - shortServiceX, (float)y / 2, (float)x, (float)y/2, paint);

        // singles side line
        float singlesSideLinesY = (float)(singlesSideLine * y);
        canvas.drawLine(0, singlesSideLinesY, (float)x, singlesSideLinesY, paint);
        canvas.drawLine(0, (float)y - singlesSideLinesY, (float)x, (float)y - singlesSideLinesY, paint);

        TextPaint normalTextPaint = new TextPaint();
        normalTextPaint.setTextSize(50);
        normalTextPaint.setColor(Color.BLACK);
        normalTextPaint.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));

        TextPaint servicePositionPaint = new TextPaint();
        servicePositionPaint.setTextSize(60);
        servicePositionPaint.setColor(Color.parseColor("#FFD800"));
        servicePositionPaint.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));

        int maxWidth = (int)(shortServiceX - longServiceX);
        drawPlayerNames(getTopRightName(), getTopRightPic(), longServiceX, singlesSideLinesY, maxWidth, canvas, mServicePosition == Position.TopRight ? servicePositionPaint : normalTextPaint);
        drawPlayerNames(getTopLeftName(), getTopLeftPic(), longServiceX , (float)y/2, maxWidth, canvas, mServicePosition == Position.TopLeft ? servicePositionPaint : normalTextPaint);
        drawPlayerNames(getBottomLeftName(), getBottomLeftPic(), (float)x - shortServiceX, (float)y/2, maxWidth, canvas, mServicePosition == Position.BottomLeft ? servicePositionPaint : normalTextPaint);
        drawPlayerNames(getBottomRightName(), getBottomRightPic (), (float)x - shortServiceX, singlesSideLinesY, maxWidth, canvas, mServicePosition == Position.BottomRight ? servicePositionPaint : normalTextPaint);
    }

}
