package com.example.basictest.pen.obj.wobj;

import com.tal.pad.note.pen.obj.dobj.MovePathDrawObj;

/**
 * Created by dongqiangqiang on 2024/4/11
 */
public class MovePathWriteObj extends WriteObj<MovePathDrawObj> {

    public int penColor;

    public int penType;

    public float penWidth;
    public int penStyle;

    public void init(int penColor, int penType, float penWidth, int penStyle) {
        this.penColor = penColor;
        this.penType = penType;
        this.penWidth = penWidth;
        this.penStyle = penStyle;
    }

    public MovePathWriteObj() {
        super(TYPE_PATH);
        drawObj = new MovePathDrawObj();
    }


    @Override
    public String toString() {
        return "MovePathWriteObj{" +
                "penColor=" + penColor +
                ", penType=" + penType +
                ", penWidth=" + penWidth +
                ", penStyle=" + penStyle +
                ", type=" + type +
                ", actionId=" + actionId +
                '}';
    }
}
