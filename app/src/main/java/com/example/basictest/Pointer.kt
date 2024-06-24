package com.example.basictest

data class Pointer(val x : Float, val y : Float){
    override fun toString(): String {
        return "($x, $y)"
    }
}
