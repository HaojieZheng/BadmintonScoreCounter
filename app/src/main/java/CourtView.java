import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Haojie on 12/25/2016.
 */

public class CourtView extends View {

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
    }

}
