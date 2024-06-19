package com.tal.pad.note.pen.obj.dobj

import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import com.alibaba.fastjson.annotation.JSONField
import com.example.basictest.pen.PenStyleUtil
import com.example.basictest.pen.obj.dobj.DrawObj
import com.example.basictest.pen.obj.dobj.MovePathMerger
import com.example.basictest.pen.obj.dobj.MoveSegment
import com.google.gson.annotations.Expose
import java.util.function.Consumer

/**
 * Created by dongqiangqiang on 2024/4/10
 */
class MovePathDrawObj : DrawObj() {
    @JvmField
    var movePathMerger = MovePathMerger()

    @JvmField
    @Expose(serialize = false, deserialize = false)
    @Transient
    var currentMoveSegment: MoveSegment? = null

    @JSONField(serialize = false, deserialize = false)
    @Expose(serialize = false, deserialize = false)
    @Transient
    private var paint: Paint? = null
    var penColor = 0
    var penType = 0
    var penWidth = 0f
    var penStyle = 0

    var startTime = 0L
    var endTime = 0L

    fun init(penColor: Int, penType: Int, penWidth: Float, penStyle: Int) {
        this.penColor = penColor
        this.penType = penType
        this.penWidth = penWidth
        this.penStyle = penStyle
        movePathMerger.init(penColor, penType, penWidth, penStyle)
    }

    fun addMoveSegmentConfig(
        x: Float,
        y: Float,
        r: Float,
        action: Int,
        penType: Int,
        penColor: Int,
        style: Paint.Style?
    ) {
        currentMoveSegment = MoveSegment(x, y, r, action, penType, penColor, style)
    }

    fun addLastMoveSegmentConfig(x: Float, y: Float, r: Float, action: Int) {
        if (currentMoveSegment != null) {
            currentMoveSegment!!.setLastMoveSegment(x, y, r, action)
        }
    }

    fun addPathSegment(path: Path?) {
        if (path != null) {
            currentMoveSegment!!.path = Path(path)
        }
        movePathMerger.addSegment(currentMoveSegment)
    }

    fun setPathSegment(path: Path?) {
        if (path != null && currentMoveSegment != null) {
            currentMoveSegment!!.path = Path(path)
        }
    }

    override fun getRectF(): RectF {
        val rf = RectF()
        val path = movePathMerger.toPath()
        path.computeBounds(rf, false)
        return rf
    }

    override fun move(dx: Float, dy: Float) {
        movePathMerger.moveSegment.forEach(Consumer { moveSegment ->
            moveSegment.x = moveSegment.x + dx
            moveSegment.y = moveSegment.y + dy
            if (moveSegment.path != null) {
                moveSegment.path.offset(dx, dy)
            }
        })
    }

    fun getPaint(): Paint {
        if (paint == null) {
            paint = Paint()
            paint!!.isAntiAlias = true
            paint!!.setColor(penColor)
            paint!!.strokeWidth = width
            PenStyleUtil.setStyle(paint, penStyle)
            paint!!.strokeCap = Paint.Cap.ROUND
            paint!!.strokeJoin = Paint.Join.ROUND
            paint!!.isFilterBitmap = true
        }
        return paint!!
    }


}
