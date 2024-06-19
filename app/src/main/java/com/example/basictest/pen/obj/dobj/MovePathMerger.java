package com.example.basictest.pen.obj.dobj;

import android.graphics.Path;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dongqiangqiang on 2024/4/10
 */
public class MovePathMerger {

    private int penColor;

    private int penType;

    private float penWidth;
    private int penStyle;

    public List<MoveSegment> moveSegment = new ArrayList<>();

    @Expose(serialize = false, deserialize = false)
    transient private Path path;

    public void init(int penColor, int penType, float penWidth, int penStyle) {
        this.penColor = penColor;
        this.penType = penType;
        this.penWidth = penWidth;
        this.penStyle = penStyle;
    }

    public void addSegment(MoveSegment path) {
        moveSegment.add(path);
    }

    public Path toPath() {
        if (path == null) {
            path = new Path();
            for (MoveSegment segment : moveSegment) {
                path.addPath(segment.path);
            }
        }

        return path;
    }

    /**
     * 拆分成多个path
     *
     * @return paths
     */
    public List<Path> toPathList() {
        List<Path> paths = new ArrayList<>();
        int j = 0;
        int step = 5;
        while (j < moveSegment.size()) {
            Path path = new Path();
            for (int i = j; i < j + step; i++) {
                if (i < moveSegment.size()) {

                    Path path1 = moveSegment.get(i).path;
                    if (path1 != null) {
                        path.addPath(path1);
                    }
                } else {
                    break;
                }
            }
            paths.add(path);
            j = j + step;
        }
        return paths;
    }

    public void clear() {
        moveSegment.clear();
    }

}
