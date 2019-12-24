package com.example.basictest;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.d(TAG, "handleMessage: msg" + msg.what);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i("MY_TEST", "onCreate1");

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("MY_TEST", "onStart1");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("MY_TEST", "onResume1");
        Log.i(TAG, "onResume: " + "添加了一行代码");

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("MY_TEST", "onPause1");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("MY_TEST", "onStop1");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("MY_TEST", "onDestroy1");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("MY_TEST", "onActivityResult1");
        Log.i("MY_TEST", "resultCode:" + resultCode);
        Log.i("MY_TEST", "data" + data.getStringExtra("resultValue"));

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.i("MY_TEST", "onNewIntent1");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i("MY_TEST", "onSaveInstanceState1" + "一个参数");
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        Log.i("MY_TEST", "onSaveInstanceState1" + "两个参数");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.i("MY_TEST", "onRestoreInstanceState1" + "一个参数");
    }

    @Override
    public void onRestoreInstanceState(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onRestoreInstanceState(savedInstanceState, persistentState);
        Log.i("MY_TEST", "onRestoreInstanceState1" + "两个参数");
    }

    public void jumpTwo(View view) {
        startActivityForResult(new Intent(this, MainActivity2.class), 2);
    }

    public void startCamera(View view){

    }
}
