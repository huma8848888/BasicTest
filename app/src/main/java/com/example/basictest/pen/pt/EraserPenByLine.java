package com.example.basictest.pen.pt;

import android.graphics.Path;
import android.util.Log;
import android.view.MotionEvent;

import com.example.basictest.pen.obj.wobj.MovePathWriteObj;
import com.example.basictest.pen.obj.wobj.WriteObj;
import com.huitongzhiyuan.testapplication.pen.listener.IOnRefreshListener;

import java.util.Iterator;


public class EraserPenByLine {

    private static final String TAG = "EarserPen";

    public EraserPenByLine(float width) {

    }

    private Path path = new Path();

    public Path getPath() {
        return path;
    }

    private IOnRefreshListener onRefreshListener;

    public void setOnRefreshListener(IOnRefreshListener onRefreshListener) {
        this.onRefreshListener = onRefreshListener;
    }

    private MovePathWriteObj writeObj;

    public MovePathWriteObj getWriteObj() {
        return writeObj;
    }

    public void setWriteObj(MovePathWriteObj writeObj) {
        this.writeObj = writeObj;
    }

    protected WritePage currentWritePage;

    public void setCurrentWritePage(WritePage writePage) {
        this.currentWritePage = writePage;
        path.reset();
    }

    public WritePage getCurrentWritePage() {
        return currentWritePage;
    }

    private boolean hasDownAction = false;

    public Path onDrawEvent(float x, float y, float r, int action) {
        if (currentWritePage == null || currentWritePage.writeObjList.isEmpty()) {
            return path;
        }
        if (action == MotionEvent.ACTION_DOWN) {
            hasDownAction = true;
            path.reset();
            path.moveTo(x, y);
        } else if (action == MotionEvent.ACTION_MOVE) {
            //解决手写笔硬件问题
            if (!hasDownAction) {
                hasDownAction = true;
                path.reset();
                path.moveTo(x, y);
            } else {
                path.lineTo(x, y);
            }
        } else {
            path.reset();
            hasDownAction = false;
        }
        Path opPath = new Path();
        Iterator<WriteObj<?>> iterator = currentWritePage.writeObjList.iterator();
        while (iterator.hasNext()) {
            WriteObj<?> writeObj = iterator.next();
            if (writeObj != null && writeObj.getDrawObj() != null) {
                if (writeObj instanceof MovePathWriteObj) {
                    MovePathWriteObj movePathWriteObj = (MovePathWriteObj) writeObj;
                    Path srcPath = movePathWriteObj.getDrawObj().currentMoveSegment.path;
                    opPath.op(srcPath, path, Path.Op.INTERSECT);
                    boolean isIntersected = !opPath.isEmpty();
                    Log.i("xlj", "onDrawEvent: isIntersected = " + isIntersected);
                    if (isIntersected) {
                        iterator.remove();
                        if (onRefreshListener != null) {
                            onRefreshListener.onRefresh();
                        }
                        break;
                    }
                }
            }
        }
        return path;
    }
}
