package com.example.basictest.pen;

import android.graphics.Paint;

/**
 * Created by dongqiangqiang on 2024/4/25
 */
public class PenStyleUtil {

    public static void setStyle(Paint paint, int penStyle) {
        if (penStyle == 0) {
            paint.setStyle(Paint.Style.FILL);
        } else if (penStyle == 1) {
            paint.setStyle(Paint.Style.STROKE);
        } else {
            paint.setStyle(Paint.Style.FILL_AND_STROKE);
        }
    }
}
