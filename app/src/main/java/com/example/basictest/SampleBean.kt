package com.example.basictest

data class SampleBean(val text : String, val target : Int, val count : Int = 0, val isPositiveSample : Boolean = false) {
    override fun toString(): String {
        return super.toString()
    }
}
