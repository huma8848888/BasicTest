package com.huitongzhiyuan.testapplication.pen

import android.content.Context
import android.graphics.Bitmap
import android.view.MotionEvent
import android.view.PointerIcon
import android.widget.FrameLayout
import com.example.basictest.pen.pt.EraserPenByLine
import com.example.basictest.pen.pt.InkPen
import com.example.basictest.pen.pt.OneNote
import com.example.basictest.pen.pt.WritePage

/**
 * @author: ypp
 * @date: 2024/4/28 11:33
 * @description：
 */
class PracticeViewGroup(context: Context) : FrameLayout(context) {

    private val baseWidth = 4f
    private var pen: InkPen = InkPen(baseWidth)
    private val eraserPen2 = EraserPenByLine(baseWidth)
    private var handWriteView: HandWriteView = HandWriteView(context)
    var dispatchTouchEventDelegate: ((ev: MotionEvent?) -> Unit)? = null

    init {
        handWriteView.pen = pen
        handWriteView.eraserPen2 = eraserPen2
    }

    fun addHandWriteView(x: Int, y: Int, width: Int, height: Int) {
        val layoutParams = LayoutParams(width, height)
        layoutParams.width = width
        layoutParams.height = height
        layoutParams.leftMargin = x
        layoutParams.topMargin = y
        addView(handWriteView, layoutParams)
    }

    //替换掉 hover 的 icon
    var hoverIcon = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
    override fun onResolvePointerIcon(event: MotionEvent?, pointerIndex: Int): PointerIcon {
        return PointerIcon.create(hoverIcon, 0.5f, 0.5f)
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        ev?.let {
            //此处事件分发给 webView，该 View 的宽高一定要和 webView 一致，否则会出现坐标偏移的问题。
            dispatchTouchEventDelegate?.invoke(ev)
        }
        //事件继续往下传给手写 view ，手写 view 会消费此事件。
        return super.dispatchTouchEvent(ev)
    }

    fun setNote(oneNote: OneNote) {
        pen.setCurrentOneNote(oneNote)
    }

    fun setCurrentPage(writePage: WritePage) {
        pen.setCurrentWritePage(writePage)
        eraserPen2.currentWritePage = writePage
    }

    fun recovery(page: WritePage?) {
        page?.let {
            handWriteView.recovery(it)
        }
    }

    fun clear() {
        handWriteView.clear()
    }


}