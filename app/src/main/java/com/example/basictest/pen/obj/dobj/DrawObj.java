
package com.example.basictest.pen.obj.dobj;

import android.graphics.Canvas;
import android.graphics.RectF;

/**
 * Created by dongqiangqiang on 2024/4/11
 */
public class DrawObj {

    public float x;
    public float y;
    public float width;
    public float height;


    public RectF rectF = new RectF();

    public RectF getRectF() {
        rectF.set(x, y, x + width, y + height);
        return rectF;
    }

    public void move(float dx, float dy) {
        rectF.offset(dx, dy);
    }


    public void onDraw(Canvas canvas) {

    }


}
