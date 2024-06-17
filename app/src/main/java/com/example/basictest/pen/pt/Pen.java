package com.example.basictest.pen.pt;

import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.view.MotionEvent;

/**
 * Created by dongqiangqiang on 2024/4/9
 */
public abstract class Pen{

    private class RPointF extends PointF {

        protected float r;

        private RPointF() {
        }

        public void setValue(float x, float y, float r) {
            this.x = x;
            this.y = y;
            this.r = r;
        }

        public void copy(RPointF rPointF) {
            this.x = rPointF.x;
            this.y = rPointF.y;
            this.r = rPointF.r;
        }

    }

    private Paint paint = new Paint();
    private Path path = new Path();

    private RPointF[] cPoint = new RPointF[2];
    private RPointF[] vPoint = new RPointF[4];

    private RPointF curPoint = new RPointF();
    private RPointF midZ1Point = new RPointF();
    private RPointF midZ2Point = new RPointF();
    private RPointF prePoint = new RPointF();

    private float width;

    private boolean enableAddPath = false;

    private boolean enableRecordTrack = true;

    private boolean enableHeadCircle = false;

    public Pen(float width) {
        initPaint();
        initVCPoint();
        setWidth(width);
    }

    private void initVCPoint() {
        int indexI = 0;
        int indexJ = 0;
        while (true) {
            RPointF[] pointFArr = this.vPoint;
            if (indexJ >= pointFArr.length) {
                break;
            }
            pointFArr[indexJ] = new RPointF();
            indexJ++;
        }
        while (true) {
            RPointF[] pointFArr2 = this.cPoint;
            if (indexI < pointFArr2.length) {
                pointFArr2[indexI] = new RPointF();
                indexI++;
            } else {
                return;
            }
        }
    }

    private void initPaint() {
        paint.setAntiAlias(true);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeJoin(Paint.Join.ROUND);
    }

    public Paint getPaint() {
        return paint;
    }

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }


    public Path onDrawEvent(float x, float y, float r, int action) {

        this.curPoint.setValue(x, y, r);
        this.midZ1Point.copy(this.midZ2Point);

        if (action == MotionEvent.ACTION_DOWN) {
            this.path = new Path();
            this.prePoint.copy(this.curPoint);
            this.midZ2Point.copy(this.curPoint);

            if (enableHeadCircle) {
                this.path.addCircle(x, y, r, Path.Direction.CW);
            }

        } else if (action == MotionEvent.ACTION_MOVE) {

            if (enableAddPath) {
                path.reset();
            }

            this.midZ2Point = getMidPoint(this.prePoint, this.curPoint);
            double distance = getDistance(this.midZ1Point, this.midZ2Point);
            if (distance > ((double) Math.abs(this.midZ1Point.r - this.midZ2Point.r))) {
                PointF[] tangencyPoints = getTangencyPoints(this.midZ1Point, this.midZ2Point);
                PointF[] controlPoints = getControlPoints(this.prePoint, this.midZ2Point.x - this.midZ1Point.x, this.midZ2Point.y - this.midZ1Point.y, distance);
                this.path.moveTo(tangencyPoints[0].x, tangencyPoints[0].y);
                this.path.lineTo(tangencyPoints[1].x, tangencyPoints[1].y);
                this.path.quadTo(controlPoints[0].x, controlPoints[0].y, tangencyPoints[2].x, tangencyPoints[2].y);
                this.path.lineTo(tangencyPoints[3].x, tangencyPoints[3].y);
                this.path.quadTo(controlPoints[1].x, controlPoints[1].y, tangencyPoints[0].x, tangencyPoints[0].y);

                if (enableHeadCircle) {
                    this.path.addCircle(this.midZ2Point.x, this.midZ2Point.y, this.midZ2Point.r, Path.Direction.CW);
                }

                this.prePoint.copy(this.curPoint);
                this.midZ1Point.copy(this.midZ2Point);
            }
        }

        return path;
    }

    private RPointF getMidPoint(RPointF rPointF, RPointF rPointF2) {
        RPointF rPointF3 = new RPointF();
        rPointF3.x = (rPointF.x + rPointF2.x) / 2.0f;
        rPointF3.y = (rPointF.y + rPointF2.y) / 2.0f;
        rPointF3.r = (rPointF.r + rPointF2.r) / 2.0f;
        return rPointF3;
    }

    private double getDistance(RPointF rPointF, RPointF rPointF2) {
        float f = rPointF2.x - rPointF.x;
        float f2 = rPointF2.y - rPointF.y;
        return Math.sqrt((double) ((f * f) + (f2 * f2)));
    }

    private RPointF[] getTangencyPoints(RPointF rPointF, RPointF rPointF2) {
        float f = rPointF2.x - rPointF.x;
        float f2 = rPointF2.y - rPointF.y;
        double distance = getDistance(rPointF, rPointF2);
        double d = (double) f2;
        double d2 = (double) f;
        this.vPoint[0].x = (float) (((double) rPointF.x) - ((((double) rPointF.r) / distance) * d));
        this.vPoint[0].y = (float) (((double) rPointF.y) + ((((double) rPointF.r) / distance) * d2));
        this.vPoint[1].x = (float) (((double) rPointF.x) + ((((double) rPointF.r) / distance) * d));
        this.vPoint[1].y = (float) (((double) rPointF.y) - ((((double) rPointF.r) / distance) * d2));
        this.vPoint[2].x = (float) (((double) rPointF2.x) + ((((double) rPointF2.r) / distance) * d));
        this.vPoint[2].y = (float) (((double) rPointF2.y) - ((((double) rPointF2.r) / distance) * d2));
        this.vPoint[3].x = (float) (((double) rPointF2.x) - ((((double) rPointF2.r) / distance) * d));
        this.vPoint[3].y = (float) (((double) rPointF2.y) + ((((double) rPointF2.r) / distance) * d2));
        return this.vPoint;
    }

    private PointF[] getControlPoints(RPointF rPointF, float f, float f2, double d) {
        this.cPoint[0].x = (float) (((double) rPointF.x) + (((double) (rPointF.r * f2)) / d));
        this.cPoint[0].y = (float) (((double) rPointF.y) - (((double) (rPointF.r * f)) / d));
        this.cPoint[1].x = (float) (((double) rPointF.x) - (((double) (rPointF.r * f2)) / d));
        this.cPoint[1].y = (float) (((double) rPointF.y) + (((double) (rPointF.r * f)) / d));
        return this.cPoint;
    }

    PenType penType;
    int penColor;

    Paint.Style style;

    public void setPenType(PenType penType) {
        this.penType = penType;
    }

    public PenType getPenType() {
        return penType;
    }

    public void setPenColor(int penColor) {
        this.penColor = penColor;
        paint.setColor(penColor);
    }

    public int getColor() {
        return penColor;
    }

    public void setStyle(Paint.Style style) {
        this.style = style;
        paint.setStyle(style);
    }

    public Paint.Style getStyle() {
        return style;
    }

    public void setWidth(float width) {
        paint.setStrokeWidth(width);
        this.width = width;
    }

    public float getWidth() {
        return width;
    }



    public void setEnableAddPath(boolean enableAddPath) {
        this.enableAddPath = enableAddPath;
    }

    public boolean isEnableAddPath() {
        return enableAddPath;
    }

    public void setEnableRecordTrack(boolean enableRecordTrack) {
        this.enableRecordTrack = enableRecordTrack;
    }

    public boolean isEnableRecordTrack() {
        return enableRecordTrack;
    }

    public void setEnableHeadCircle(boolean enableHeadCircle) {
        this.enableHeadCircle = enableHeadCircle;
    }

    public boolean isEnableHeadCircle() {
        return enableHeadCircle;
    }



}
