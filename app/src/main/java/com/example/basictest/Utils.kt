package com.example.basictest

import android.content.Context
import android.util.TypedValue

class Utils {
    companion object{
        /**
         * dp转px
         */
        fun dp2px(context: Context, dpVal: Float): Int {
            return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    dpVal, context.resources.displayMetrics).toInt()
        }

        fun softMax(list : List<Double>): Array<Double> {
            val labelVals = list.toDoubleArray()
            val max = labelVals.maxOrNull() ?:0.0
            var sum : Double = 0.0

            // Get the reduced sum
            for (i in labelVals.indices) {
                labelVals[i] = Math.exp(labelVals[i] - max)
                sum += labelVals[i]
            }

            if (sum != 0.0) {
                for (i in labelVals.indices) {
                    labelVals[i] /= sum
                }
            }
            return labelVals.toTypedArray()
        }
        fun argmax(list : List<Double>) : Int {
            var max = 0.0
            var index = 0
            for (i in list.indices) {
                if (list[i] > max) {
                    max = list[i]
                    index = i
                }
            }
            return index
        }

        fun maxValue(list : List<Double>) : Double {
            var max = 0.0
            for (i in list.indices) {
                if (list[i] > max) {
                    max = list[i]
                }
            }
            return max
        }

        fun argmin(list : List<Double>) : Int {
            var min = Double.MAX_VALUE
            var index = 0
            for (i in list.indices) {
                if (list[i] < min) {
                    min = list[i]
                    index = i
                }
            }
            return index
        }

        fun minvalue(list : List<Double>) : Double {
            var min = Double.MAX_VALUE
            for (i in list.indices) {
                if (list[i] < min) {
                    min = list[i]
                }
            }
            return min
        }
    }

}