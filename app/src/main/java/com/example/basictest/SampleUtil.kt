package com.example.basictest

import android.graphics.Color

class SampleUtil {
    companion object{
        val sampleList = listOf<SampleBean>(
            SampleBean("大", 8000, isPositiveSample = true, color = Color.RED),
            SampleBean("ナ", 3000, color = Color.RED),
            SampleBean("一横一捺", 3000, isPositiveSample = true, pic = R.drawable.yihengyina, color = Color.RED),
            SampleBean("思", 8000),
            SampleBean("六", 3000),
            SampleBean("犬", 3000, color = Color.RED, notice = "不要写连笔"),
            SampleBean("太", 3000, color = Color.RED),
            SampleBean("人", 3000),
            SampleBean("丈", 3000),
            SampleBean("A", 5000),
            SampleBean("夫", 3000, color = Color.RED),
            SampleBean("夭", 3000, color = Color.RED),
            SampleBean("米", 3000),
            SampleBean("的", 3000, color = Color.GREEN),
            SampleBean("是", 3000, color = Color.GREEN),
            SampleBean("我", 3000, color = Color.GREEN),
            SampleBean("了", 3000, color = Color.GREEN),
            SampleBean("你", 3000, color = Color.GREEN),
            SampleBean("你", 3000, color = Color.GREEN),
            SampleBean("在", 3000, color = Color.GREEN),
            SampleBean("他", 3000, color = Color.GREEN),
            SampleBean("有", 3000, color = Color.GREEN),
            SampleBean("这", 3000, color = Color.GREEN),
            SampleBean("就", 3000, color = Color.GREEN),
            SampleBean("都", 3000, color = Color.GREEN),
            SampleBean("一", 3000, color = Color.GREEN),
            SampleBean("也", 3000, color = Color.GREEN),
            SampleBean("个", 3000, color = Color.GREEN),
            SampleBean("人", 3000, color = Color.GREEN),
            SampleBean("地", 3000, color = Color.GREEN),
            SampleBean("很", 3000, color = Color.GREEN),
            SampleBean("着", 3000, color = Color.GREEN),
            SampleBean("么", 3000, color = Color.GREEN),
            SampleBean("为", 3000, color = Color.GREEN),
        )
    }
}