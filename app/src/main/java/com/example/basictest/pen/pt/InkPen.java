package com.example.basictest.pen.pt;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.view.MotionEvent;

import com.example.basictest.pen.listener.IDataOptSupportListener;
import com.example.basictest.pen.obj.dobj.DrawObj;
import com.example.basictest.pen.obj.dobj.MovePathMerger;
import com.example.basictest.pen.obj.dobj.MoveSegment;
import com.example.basictest.pen.obj.wobj.MovePathWriteObj;
import com.tal.pad.note.pen.obj.dobj.MovePathDrawObj;

import java.util.List;

/**
 * Created by dongqiangqiang on 2024/4/9
 */
public class InkPen extends Pen {

    private static final boolean DEBUG = false;

    private PointF[] mC = new PointF[2];
    private RPointF mCurPoint = new RPointF();
    private RPointF mM1 = new RPointF();
    private RPointF mM2 = new RPointF();
    private PointF[] mP = new PointF[4];
    private Path mPath = new Path();
    private Path tempPath = new Path();
    private RPointF mPrePoint = new RPointF();
    private float mRadius = 3.0f;
    int mType;

    private MovePathWriteObj writeObj;

    public void setWriteObj(MovePathWriteObj writeObj) {
        this.writeObj = writeObj;
    }

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

    @Override
    public Path getPath() {
        return this.mPath;
    }

    @Override
    public void setPath(Path path) {
        this.mPath = path;
    }

    public InkPen(float width) {
        super(width);
        setEnableHeadCircle(true);
        setEnableAddPath(false);
        setPenType(PenType.INK);
        setPenColor(Color.BLACK);
        setStyle(Paint.Style.FILL);
        setWidth(width);
        int i2 = 0;
        int i3 = 0;
        while (true) {
            PointF[] pointFArr = this.mP;
            if (i3 >= pointFArr.length) {
                break;
            }
            pointFArr[i3] = new PointF();
            i3++;
        }
        while (true) {
            PointF[] pointFArr2 = this.mC;
            if (i2 < pointFArr2.length) {
                pointFArr2[i2] = new PointF();
                i2++;
            } else {
                return;
            }
        }
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

    private PointF[] getTangencyPoints(RPointF rPointF, RPointF rPointF2) {
        float f = rPointF2.x - rPointF.x;
        float f2 = rPointF2.y - rPointF.y;
        double distance = getDistance(rPointF, rPointF2);
        double d = (double) f2;
        double d2 = (double) f;
        this.mP[0].x = (float) (((double) rPointF.x) - ((((double) rPointF.r) / distance) * d));
        this.mP[0].y = (float) (((double) rPointF.y) + ((((double) rPointF.r) / distance) * d2));
        this.mP[1].x = (float) (((double) rPointF.x) + ((((double) rPointF.r) / distance) * d));
        this.mP[1].y = (float) (((double) rPointF.y) - ((((double) rPointF.r) / distance) * d2));
        this.mP[2].x = (float) (((double) rPointF2.x) + ((((double) rPointF2.r) / distance) * d));
        this.mP[2].y = (float) (((double) rPointF2.y) - ((((double) rPointF2.r) / distance) * d2));
        this.mP[3].x = (float) (((double) rPointF2.x) - ((((double) rPointF2.r) / distance) * d));
        this.mP[3].y = (float) (((double) rPointF2.y) + ((((double) rPointF2.r) / distance) * d2));
        return this.mP;
    }

    private PointF[] getControlPoints(RPointF rPointF, float f, float f2, double d) {
        this.mC[0].x = (float) (((double) rPointF.x) + (((double) (rPointF.r * f2)) / d));
        this.mC[0].y = (float) (((double) rPointF.y) - (((double) (rPointF.r * f)) / d));
        this.mC[1].x = (float) (((double) rPointF.x) - (((double) (rPointF.r * f2)) / d));
        this.mC[1].y = (float) (((double) rPointF.y) + (((double) (rPointF.r * f)) / d));
        return this.mC;
    }

    @Override
    public void setWidth(float width) {
        super.setWidth(width);
        setRadius(width / 2.0f);
    }

    protected OneNote currentOneNote;

    protected WritePage currentWritePage;

    public void setCurrentWritePage(WritePage writePage) {
        this.currentWritePage = writePage;
    }

    protected IDataOptSupportListener dataOptSupportListener;

    public void setIDataOptSupportListener(IDataOptSupportListener listener) {
        this.dataOptSupportListener = listener;
    }

    public void setCurrentOneNote(OneNote oneNote) {
        this.currentOneNote = oneNote;
    }


    private void setRadius(float f) {
        this.mRadius = f;
    }

    private float getRadius() {
        return this.mRadius;
    }


    public float lastX;
    public float lastY;
    public float lastR;
    public int lastAction;

    @Override
    public Path onDrawEvent(float x, float y, float nr, int action) {
        if (currentWritePage == null) {
            return mPath;
        }
        float r = nr * 1.1f;
        if (action == MotionEvent.ACTION_DOWN) {
            writeObj = new MovePathWriteObj();
            writeObj.init(getColor(), getPenType().ordinal(), getWidth(), getStyle().ordinal());
            writeObj.getDrawObj().init(getColor(), getPenType().ordinal(), getWidth(), getStyle().ordinal());
        }
        if (writeObj == null || writeObj.getDrawObj() == null) {
            return mPath;
        }
        writeObj.getDrawObj().addMoveSegmentConfig(x, y, nr, action, getPenType().ordinal(), getColor(), getStyle());
        if (action == MotionEvent.ACTION_MOVE) {
            writeObj.getDrawObj().addLastMoveSegmentConfig(lastX, lastY, lastR, lastAction);
        }

        lastX = x;
        lastY = y;
        lastR = nr;
        lastAction = action;

        this.mCurPoint.setValue(x, y, r * getRadius());
        this.mM1.copy(this.mM2);
        if (action == 0) {
            this.mPath = new Path();
            this.mPrePoint.copy(this.mCurPoint);
            this.mM2.copy(this.mCurPoint);

            this.mPath.addCircle(x, y, r, Path.Direction.CW);
            writeObj.getDrawObj().setStartTime(System.currentTimeMillis());
            writeObj.getDrawObj().addPathSegment(this.mPath);

        } else if (action == 2) {
            if (isEnableAddPath()) {
                mPath.reset();
            }

            tempPath.reset();
            this.mM2 = getMidPoint(this.mPrePoint, this.mCurPoint);
            double distance = getDistance(this.mM1, this.mM2);
            if (distance > ((double) Math.abs(this.mM1.r - this.mM2.r))) {
                PointF[] tangencyPoints = getTangencyPoints(this.mM1, this.mM2);
                PointF[] controlPoints = getControlPoints(this.mPrePoint, this.mM2.x - this.mM1.x, this.mM2.y - this.mM1.y, distance);
                this.tempPath.moveTo(tangencyPoints[0].x, tangencyPoints[0].y);
                this.tempPath.lineTo(tangencyPoints[1].x, tangencyPoints[1].y);
                this.tempPath.quadTo(controlPoints[0].x, controlPoints[0].y, tangencyPoints[2].x, tangencyPoints[2].y);
                this.tempPath.lineTo(tangencyPoints[3].x, tangencyPoints[3].y);
                this.tempPath.quadTo(controlPoints[1].x, controlPoints[1].y, tangencyPoints[0].x, tangencyPoints[0].y);
                this.tempPath.addCircle(this.mM2.x, this.mM2.y, this.mM2.r, Path.Direction.CW);

                if (DEBUG) {
                    this.tempPath.addCircle(this.mM2.x, this.mM2.y, this.mM2.r * 10, Path.Direction.CW);
                }

                this.mPrePoint.copy(this.mCurPoint);
                this.mM1.copy(this.mM2);
            }

            mPath.addPath(tempPath);
            writeObj.getDrawObj().addPathSegment(this.tempPath);

        } else if (action == MotionEvent.ACTION_UP) {
            if (writeObj != null) {
                writeObj.getDrawObj().setEndTime(System.currentTimeMillis());
                writeObj.getDrawObj().addPathSegment(this.mPath);
                currentWritePage.addWriteObj(writeObj);
            }
        }
        return this.mPath;
    }

    public Path onRecoveryDrawEvent(float x, float y, float nr, int action) {
        float r = nr * 1.1f;
        this.mCurPoint.setValue(x, y, r * getRadius());
        this.mM1.copy(this.mM2);
        if (action == 0) {
            this.mPath = new Path();
            this.mPrePoint.copy(this.mCurPoint);
            this.mM2.copy(this.mCurPoint);

            this.mPath.addCircle(x, y, r, Path.Direction.CW);

            writeObj.getDrawObj().setPathSegment(this.mPath);

        } else if (action == 2) {

            if (isEnableAddPath()) {
                mPath.reset();
            }

            tempPath.reset();
            this.mM2 = getMidPoint(this.mPrePoint, this.mCurPoint);
            double distance = getDistance(this.mM1, this.mM2);
            if (distance > ((double) Math.abs(this.mM1.r - this.mM2.r))) {
                PointF[] tangencyPoints = getTangencyPoints(this.mM1, this.mM2);
                PointF[] controlPoints = getControlPoints(this.mPrePoint, this.mM2.x - this.mM1.x, this.mM2.y - this.mM1.y, distance);
                this.tempPath.moveTo(tangencyPoints[0].x, tangencyPoints[0].y);
                this.tempPath.lineTo(tangencyPoints[1].x, tangencyPoints[1].y);
                this.tempPath.quadTo(controlPoints[0].x, controlPoints[0].y, tangencyPoints[2].x, tangencyPoints[2].y);
                this.tempPath.lineTo(tangencyPoints[3].x, tangencyPoints[3].y);
                this.tempPath.quadTo(controlPoints[1].x, controlPoints[1].y, tangencyPoints[0].x, tangencyPoints[0].y);
                this.tempPath.addCircle(this.mM2.x, this.mM2.y, this.mM2.r, Path.Direction.CW);

                if (DEBUG) {
                    this.tempPath.addCircle(this.mM2.x, this.mM2.y, this.mM2.r * 10, Path.Direction.CW);
                }

                this.mPrePoint.copy(this.mCurPoint);
                this.mM1.copy(this.mM2);
            }
            mPath.addPath(tempPath);
            writeObj.getDrawObj().setPathSegment(this.tempPath);

        } else if (action == MotionEvent.ACTION_UP) {

            writeObj.getDrawObj().setPathSegment(this.mPath);
        }
        return this.mPath;
    }

    public void dispatchRecoveryDraw(DrawObj data) {
        if (data instanceof MovePathDrawObj) {
            MovePathDrawObj movePathDrawObj = (MovePathDrawObj) data;
            MovePathMerger movePathMerger = movePathDrawObj.movePathMerger;
            List<MoveSegment> moveSegment = movePathMerger.moveSegment;
            for (MoveSegment segment : moveSegment) {
                movePathDrawObj.currentMoveSegment = segment;
                onRecoveryDrawEvent(segment.x, segment.y, segment.r, segment.action);
                // TODO: 2024/5/6 重绘
//                recoveryGroup.canvas.drawPath(mPath, getPaint());
            }
        }
    }


}
