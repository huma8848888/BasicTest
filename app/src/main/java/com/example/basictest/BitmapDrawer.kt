package com.example.basictest

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path

class BitmapDrawer(private val width: Int, private val height: Int) {
    private val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    private val canvas = Canvas(bitmap).apply {
        this.drawColor(BACKGROUND_COLOR)
    }
    private val paint = Paint().apply {
        color = Color.BLACK
        style = Paint.Style.STROKE
        strokeWidth = TRACE_WIDTH
        isAntiAlias = true
    }
    private val path = Path()

    fun addPoint(x: Float, y: Float) {
        if (path.isEmpty) {
            path.moveTo(x, y)
        } else {
            path.lineTo(x, y)
        }
    }

    fun draw() {
        canvas.drawPath(path, paint)
        path.reset() // 重置Path以便绘制新的轨迹
    }

    fun clear(){
        bitmap.recycle()
        path.reset()
    }

    fun getBitmap(): Bitmap {
        return bitmap
    }
}