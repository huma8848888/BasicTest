package com.example.basictest.pen.pt;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;

import com.example.basictest.R;


/**
 * Created by dongqiangqiang on 2024/4/10
 */
public class EarserPen extends Pen {

    private static final String TAG = "EarserPen";
    private float cR;
    private Bitmap bitmap;
    private RectF rectF;
    private RectF eRectF;

    public EarserPen(float width, Context context) {
        super(width);
        setStyle(Paint.Style.FILL);
        setWidth(width);

        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_note_cachu);

        cR = 40 * 1f;

        eRectF = new RectF();

    }

    @Override
    public Path getPath() {
        return null;
    }

    public void dispatchLayerDraw(Canvas canvas, float x, float y, float r, int action) {

        Log.d(TAG, "dispatchDraw: ");

        if (rectF == null) {
            rectF = new RectF();
        }
        rectF.set(x - cR / 2, y - cR / 2, x + cR / 2, y + cR / 2);
        eRectF.set(x - cR / 4, y - cR / 4, x + cR / 4, y + cR / 4);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                canvas.drawBitmap(bitmap, null, rectF, getPaint());
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
    }

    public RectF getCurrentRectF() {
        return eRectF;
    }

}
