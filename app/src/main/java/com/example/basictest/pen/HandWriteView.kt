package com.huitongzhiyuan.testapplication.pen

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PaintFlagsDrawFilter
import android.graphics.PorterDuff
import android.view.MotionEvent
import android.view.View
import com.example.basictest.pen.obj.wobj.MovePathWriteObj
import com.example.basictest.pen.pt.EraserPenByLine
import com.example.basictest.pen.pt.InkPen
import com.example.basictest.pen.pt.WritePage
import com.huitongzhiyuan.testapplication.pen.listener.IOnRefreshListener


class HandWriteView(context: Context?) : View(context) {
    var offscreenBitmap: Bitmap? = null
    private var offscreenCanvas: Canvas? = null
    private var drawFlag = false

//    private var testPaint = Paint().apply {
//        style = Paint.Style.STROKE
//        color = Color.RED
//        strokeWidth = 3f
//        isAntiAlias = true
//        strokeCap = Paint.Cap.ROUND
//        strokeJoin = Paint.Join.ROUND
//    }

    var pen: InkPen? = null
    var eraserPen2: EraserPenByLine? = null
        set(value) {
            field = value
            field?.setOnRefreshListener(object : IOnRefreshListener {
                override fun onRefresh() {
                    recovery(eraserPen2!!.currentWritePage)
                }
            })
        }

    private var offscreenPaint: Paint = Paint().apply {
        isAntiAlias = true
        strokeCap = Paint.Cap.ROUND
        strokeJoin = Paint.Join.ROUND
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        offscreenBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        offscreenCanvas = Canvas(offscreenBitmap!!)
        offscreenCanvas!!.drawFilter = PaintFlagsDrawFilter(
            0, Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG
        )
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.let {
            val toolType: Int = event.getToolType(0)
            val buttonState = event.buttonState
            //是否是手写笔
            val isStylus = MotionEvent.TOOL_TYPE_STYLUS == toolType
            if (!isStylus) {
                return false
            }
            //todo 经过测试，发现手写笔的buttonState 不准确，在 action_down 的时候buttonState是错误的，导致橡皮擦不准确。
            if (MotionEvent.BUTTON_STYLUS_PRIMARY == buttonState) {
                //按住手写笔侧面按钮，橡皮擦
                dealEraserTouchEvent(it)
            } else {
                //手写笔书写
                dealTouchEvent(it)
            }
        }
        return true
    }

    private fun dealEraserTouchEvent(event: MotionEvent) {
        //手写笔侧面橡皮擦
        val x = event.x
        val y = event.y
        var fixPressure = event.pressure
        if (fixPressure <= 0) {
            fixPressure = 0.001f
        }
        eraserPen2?.onDrawEvent(x, y, fixPressure, event.action)
//        if (event.action == MotionEvent.ACTION_MOVE) {
//            drawFlag = true
//        }
//        invalidate()
    }

     fun dealTouchEvent(event: MotionEvent) {
        val action = event.action
        pen?.let {
            val x = event.x
            val y = event.y
            var fixPressure = event.pressure
            if (fixPressure <= 0) {
                fixPressure = 0.001f
            }
            it.onDrawEvent(x, y, fixPressure, action)
            if (action == MotionEvent.ACTION_MOVE) {
                drawFlag = true
                invalidate()
            } else if (action == MotionEvent.ACTION_UP) {
                drawFlag = false
                offscreenCanvas?.drawPath(it.path, it.paint)
                invalidate()
            }
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        offscreenBitmap?.let {
            canvas.drawBitmap(it, 0f, 0f, offscreenPaint)
        }
        if (drawFlag) {
            pen?.let {
                val path = it.path
                if (path != null) {
                    canvas.drawPath(path, it.paint)
                }
            }
//            eraserPen2?.let {
//                val path = it.path
//                if (path != null) {
//                    canvas.drawPath(path, testPaint)
//                }
//            }
        }
    }

    fun clear() {
        post {
            offscreenCanvas?.drawColor(0, PorterDuff.Mode.CLEAR)
            invalidate()
        }
    }

    fun recovery(page: WritePage) {
        post {
            offscreenCanvas?.drawColor(0, PorterDuff.Mode.CLEAR)
            page.writeObjList.forEach { w ->
                if (w is MovePathWriteObj) {
                    w.getDrawObj().currentMoveSegment?.let {
                        offscreenCanvas?.drawPath(it.path, pen!!.paint)
                    }
                }
            }
            invalidate()
        }
    }


}
