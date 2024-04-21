package com.example.basictest

import android.content.Context
import android.content.SharedPreferences

class StorageUtils {
    companion object{
        lateinit var sharedPreferences: SharedPreferences
        fun init(context: Context){
            sharedPreferences = context.getSharedPreferences("app", 0)
        }

        fun saveString(key: String, value: String){
            sharedPreferences.edit().putString(key, value).apply()
        }
        fun saveNumber(key: String, value: Int){
            sharedPreferences.edit().putInt(key, value).apply()
        }
    }
}