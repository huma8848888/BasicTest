package com.example.basictest.pen.obj.dobj;


import android.graphics.Paint;
import android.graphics.Path;

import com.google.gson.annotations.Expose;

/**
 * Created by dongqiangqiang on 2024/4/24
 */
public class MoveSegment {

    public float x;
    public float y;
    public float r;

    public int action;

    public float lastX;
    public float lastY;
    public float lastR;
    public int lastAction;

    @Expose(serialize = false, deserialize = false)
    transient public Path path;

    public int penType;

    public int penColor;

    private int penStyle;

    public MoveSegment() {

    }

    public MoveSegment(float x, float y, float r, int action, int penType, int penColor, Paint.Style penStyle) {
        this.x = x;
        this.y = y;
        this.r = r;
        this.action = action;
        this.penType = penType;
        this.penColor = penColor;
        this.penStyle = penStyle.ordinal();
    }

    public void setLastMoveSegment(float lastX, float lastY, float lastR, int lastAction) {
        this.lastX = lastX;
        this.lastY = lastY;
        this.lastR = lastR;
        this.lastAction = lastAction;
    }

    public void setPath(Path path) {
        this.path = path;
    }

}
