package com.haojie.badmintonscorecounter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.style.TextAppearanceSpan;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Haojie on 12/25/2016.
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



    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
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

        // draw the borders
        paint.setColor(Color.DKGRAY);
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
        textPaint.setTextSize(40);
        textPaint.setColor(Color.BLACK);
        textPaint.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));

        TextPaint servicePositionPaint = new TextPaint();
        servicePositionPaint.setTextSize(40);
        servicePositionPaint.setColor(Color.RED);
        servicePositionPaint.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));

        int maxWidth = (int)(x/2 - singlesSideLinesX);
        drawPlayerNames(getTopLeftName(), singlesSideLinesX, longServiceY, maxWidth, canvas, mServicePosition == Position.TopLeft ? servicePositionPaint : textPaint);
        drawPlayerNames(getTopRightName(), (float)x/2, longServiceY, maxWidth, canvas, mServicePosition == Position.TopRight ? servicePositionPaint : textPaint);
        drawPlayerNames(getBottomLeftName(), singlesSideLinesX, (float)y - shortServiceY, maxWidth, canvas, mServicePosition == Position.BottomLeft ? servicePositionPaint : textPaint);
        drawPlayerNames(getBottomRightName(), (float)x/2, (float)y - shortServiceY, maxWidth, canvas, mServicePosition == Position.BottomRight ? servicePositionPaint : textPaint);
    }



    private void drawPlayerNames(String text, float x, float y, int maxWidth, Canvas canvas, TextPaint textPaint)
    {
        StaticLayout sl = new StaticLayout(text, textPaint, maxWidth,
                Layout.Alignment.ALIGN_CENTER, 1, 1, true);

        canvas.save();

        //calculate X and Y coordinates - In this case we want to draw the text in the
        //center of canvas so we calculate
        //text height and number of lines to move Y coordinate to center.
        float textHeight = getTextHeight("test", textPaint);
        int numberOfTextLines = sl.getLineCount();
        float textYCoordinate = y;

        //text will be drawn from left
        float textXCoordinate = x;

        canvas.translate(textXCoordinate, textYCoordinate);

        //draws static layout on canvas
        sl.draw(canvas);
        canvas.restore();
    }

    /**
     * @return text height
     */
    private float getTextHeight(String text, Paint paint) {

        Rect rect = new Rect();
        paint.getTextBounds(text, 0, text.length(), rect);
        return rect.height();
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
        if (y < courtRatio * x)
        {
            x = y / courtRatio;
        }
        else
        {
            y = courtRatio * x;
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
                    if (touched_y >  y / 2)
                        team1Score = false;

                    for (CourtViewTouchListener listener: mListeners
                         ) {
                        if (team1Score)
                            listener.onTeam1Score();
                        else
                            listener.onTeam2Score();
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

}
