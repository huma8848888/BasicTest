package com.example.basictest

import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet


class AnimatedTextView : androidx.appcompat.widget.AppCompatTextView{
    private val STOPPED = 0

    private val RUNNING = 1

    private var mPlayingState = STOPPED

    private var number = 0f

    private var fromNumber = 0f

    private var duration: Long = 1000

    /**
     * 1.int 2.float
     */
    private var numberType = 2

    private var flags = true

    val sizeTable = intArrayOf(9, 99, 999, 9999, 99999, 999999, 9999999, 99999999, 999999999, Int.MAX_VALUE)

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)


    fun isRunning(): Boolean {
        return mPlayingState == RUNNING
    }

    private fun runFloat() {
        val valueAnimator = ValueAnimator.ofFloat(fromNumber, number)
        valueAnimator.duration = duration
        valueAnimator.addUpdateListener { valueAnimator ->
            if (flags) {
                setText(Utils.format("##0.00").format(valueAnimator.animatedValue.toString().toDouble()))
                if (valueAnimator.animatedValue.toString().equals(number.toString() + "", ignoreCase = true)) {
                    setText(Utils.format("##0.00").format((number.toString() + "").toDouble()))
                }
            } else {
                setText(Utils.format("##0.00").format(valueAnimator.animatedValue.toString().toDouble()))
                if (valueAnimator.animatedValue.toString().equals(number.toString() + "", ignoreCase = true)) {
                    setText(Utils.format("##0.00").format((number.toString() + "").toDouble()))
                }
            }
            if (valueAnimator.animatedFraction >= 1) {
                mPlayingState = STOPPED
            }
        }
        valueAnimator.start()
    }

    private fun runInt() {
        val valueAnimator = ValueAnimator.ofInt(fromNumber.toInt(), number.toInt())
        valueAnimator.duration = duration
        valueAnimator.addUpdateListener { valueAnimator ->
            text = valueAnimator.animatedValue.toString()
            if (valueAnimator.animatedFraction >= 1) {
                mPlayingState = STOPPED
            }
        }
        valueAnimator.start()
    }

    fun sizeOfInt(x: Int): Int {
        var i = 0
        while (true) {
            if (x <= sizeTable[i]) return i + 1
            i++
        }
    }

    fun start() {
        if (!isRunning()) {
            mPlayingState = RUNNING
            if (numberType == 1) runInt() else runFloat()
        }
    }

    fun withNumber(number: Float, flag: Boolean): AnimatedTextView? {
        this.number = number
        flags = flag
        numberType = 2
        fromNumber = 0f
        return this
    }

    fun withNumber(number: Float): AnimatedTextView? {
        println(number)
        this.number = number
        numberType = 2
        fromNumber = 0f
        return this
    }

    fun withNumber(number: Int): AnimatedTextView? {
        this.number = number.toFloat()
        numberType = 1
        fromNumber = 0f
        return this
    }

    fun setDuration(duration: Long): AnimatedTextView? {
        this.duration = duration
        return this
    }
}