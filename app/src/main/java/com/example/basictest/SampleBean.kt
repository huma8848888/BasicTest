package com.example.basictest

import android.graphics.Color

data class SampleBean(val text : String, val target : Int, var count : Int = 0, val isPositiveSample : Boolean = false, val color : Int= Color.WHITE)
