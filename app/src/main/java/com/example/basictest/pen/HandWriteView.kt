package com.huitongzhiyuan.testapplication.pen

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PaintFlagsDrawFilter
import android.graphics.PorterDuff
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.example.basictest.Pointer
import com.example.basictest.pen.obj.wobj.MovePathWriteObj
import com.example.basictest.pen.pt.InkPen
import com.example.basictest.pen.pt.WritePage
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.FileWriter
import java.io.IOException


class HandWriteView : View {
    constructor(context: Context) : super(context)
    constructor(context: Context, attributes: AttributeSet) : super(context, attributes)


    var offscreenBitmap: Bitmap? = null
    private var offscreenCanvas: Canvas? = null
    private var drawFlag = false
    var pen: InkPen? = null

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
            //是否是手写笔
            val isStylus = MotionEvent.TOOL_TYPE_STYLUS == toolType
            if (!isStylus) {
                return false
            }
            recordDrawPath(it)
            //手写笔书写
            dealTouchEvent(it)
        }
        return true
    }
    private var cachedPointItem: ArrayList<Pointer> = ArrayList()
    private var cachedPointList: ArrayList<ArrayList<Pointer>> = ArrayList()
    private var mPressingX = 0f
    private var mPressingY = 0f
    private fun recordDrawPath(event: MotionEvent){
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                cachedPointItem = ArrayList<Pointer>()
                mPressingX = event.x
                mPressingY = event.y
                cachedPointItem.add(Pointer(mPressingX, mPressingY))
            }

            MotionEvent.ACTION_MOVE -> {
                val mMoveX = event.x
                val mMoveY = event.y
                mPressingX = mMoveX
                mPressingY = mMoveY
                cachedPointItem.add(Pointer(mPressingX, mPressingY))
            }

            MotionEvent.ACTION_UP ->         // mSignatureCanvas.drawPath(mPath, mTextPaint);
                cachedPointList.add(cachedPointItem)

            else -> {}
        }
    }

    fun isDrawValidPath(): Boolean {
        return cachedPointList.isNotEmpty()
    }


    private fun dealTouchEvent(event: MotionEvent) {
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
        }
    }

    fun clear() {
        post {
            offscreenCanvas?.drawColor(0, PorterDuff.Mode.CLEAR)

            cachedPointList.clear()
            cachedPointItem.clear()
            invalidate()
        }
    }

    fun savePointList(folder_path: String, filename: String) {
        val extension = ".txt"
        val fullFilename = filename + extension
        val jsonObject = JSONObject()
        val arr = JSONArray()
        for (item in cachedPointList) {
            arr.put(item)
        }
        val file: File
        try {
            jsonObject.putOpt("points", arr)
            jsonObject.putOpt("canvas_size", "${width}*${height}")
        } catch (e: Exception) {
            e.printStackTrace()
        }
        file = File(folder_path, fullFilename)
        try {
            if (!File(file.parent).exists()) {
                File(file.parent).mkdirs()
            }
            if (!file.exists()) {
                file.createNewFile()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // 将 JSON 对象写入文件
        try {
            FileWriter(file, true).use { fileWriter ->
                fileWriter.append(jsonObject.toString())
                fileWriter.append("\n")
            }
        } catch (e: IOException) {
            e.printStackTrace()
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
