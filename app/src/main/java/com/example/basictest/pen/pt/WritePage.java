package com.example.basictest.pen.pt;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.example.basictest.pen.listener.IRefreshScreen;
import com.example.basictest.pen.listener.IUserClearScreen;
import com.example.basictest.pen.obj.wobj.WriteObj;
import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dongqiangqiang on 2024/4/10
 */
public class WritePage implements IUserClearScreen {

    public List<WriteObj<?>> writeObjList = new ArrayList<>();
    @Expose(serialize = false, deserialize = false)
    transient List<WriteObj<?>> recoveryWriteObjList = new ArrayList<>();

    @Expose(serialize = false, deserialize = false)
    transient List<WriteObj<?>> undoWriteObjList = new ArrayList<>();
    @Expose(serialize = false, deserialize = false)
    transient List<WriteObj<?>> gobackWriteObjList = new ArrayList<>();

    public void copyToRecovery() {
        recoveryWriteObjList.addAll(writeObjList);
        writeObjList.clear();
    }

    public int pageNo;

    /**
     * 数据库中页，唯一id
     */
    public String pageId;

    public float width;

    public float height;

    @Expose(serialize = false, deserialize = false)
    public transient Canvas offscreenCanvas;
    @Expose(serialize = false, deserialize = false)
    public transient Bitmap offscreenBitmap;
    @Expose(serialize = false, deserialize = false)
    public transient Canvas onscreenCanvas;

    @Expose(serialize = false, deserialize = false)
    public transient IRefreshScreen refreshScreen;

    public float zoom = 1f;
    public float maxZoom = 1024f;

    public float minZoom = 0.00001F;


    public boolean isMaxZoom() {
        return zoom >= maxZoom;
    }

    public boolean isMinZoom() {
        return zoom <= minZoom;
    }

    public float getZoom() {
        return zoom;
    }

    public float getNextZoomOutScale() {
        zoom = zoom * 1.1f;
        if (zoom > maxZoom) {
            zoom = maxZoom;
        }
        return zoom;
    }

    public float getNextZoomInScale() {
        zoom = zoom / 1.1f;
        if (zoom < minZoom) {
            zoom = minZoom;
        }
        return zoom;
    }

    public void addWriteObj(WriteObj<?> writeObj) {
        writeObjList.add(writeObj);
    }

    public void removeWriteObj(WriteObj<?> writeObj) {
        writeObjList.remove(writeObj);
    }

    public void clear() {
        writeObjList.clear();
    }

    public List<WriteObj<?>> getWriteObjList() {
        return writeObjList;
    }

    public WriteObj<?> popWriteObj() {
        if (!writeObjList.isEmpty()) {
            return writeObjList.remove(writeObjList.size() - 1);
        }
        return null;
    }

    @Override
    public void clearScreen() {
        clear();
    }

    public void addGoBackWriteObj(WriteObj<?> writeObj) {
        gobackWriteObjList.add(writeObj);
    }

    public void addUndoWriteObj(WriteObj<?> writeObj) {
        undoWriteObjList.add(writeObj);
    }

    public WriteObj<?> popUndoWriteObj() {
        if (!undoWriteObjList.isEmpty()) {
            WriteObj<?> writeObj = undoWriteObjList.remove(undoWriteObjList.size() - 1);
            return writeObj;
        }
        return null;
    }

    public WriteObj<?> popGoBackWriteObj() {
        if (!gobackWriteObjList.isEmpty()) {
            WriteObj<?> writeObj = gobackWriteObjList.remove(gobackWriteObjList.size() - 1);
            return writeObj;
        }
        return null;
    }

    @Override
    public String toString() {
        return "WritePage{" + "pageNo=" + pageNo + ", pageId=" + pageId + '}';
    }
}
