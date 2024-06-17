package com.example.basictest.pen.obj.wobj;

import com.example.basictest.pen.obj.dobj.DrawObj;

/**
 * Created by dongqiangqiang on 2024/4/11
 * 轨迹对象
 * 抽象
 * 可以存图片
 * 可以存轨迹
 * 可以存文字
 * 可以存图形
 * 可以存音频
 * 可以存视频
 * 可以存其他
 */
public class WriteObj<T extends DrawObj> {

    public static final int TYPE_PATH = 1;

    public static final int TYPE_IMAGE = 2;
    public static final int TYPE_TEXT = 3;
    public static final int TYPE_GRAPH = 4;
    public static final int TYPE_AUDIO = 5;
    public static final int TYPE_VIDEO = 6;
    public static final int TYPE_OTHER = 7;

    public T drawObj;

    protected int type;

    // 数据存储ID
    protected long actionId;

    public WriteObj(){

    }

    public WriteObj(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }


    public T getDrawObj() {
        return drawObj;
    }

    public long getActionId() {
        return actionId;
    }

    public void setActionId(long actionId) {
        this.actionId = actionId;
    }
}
