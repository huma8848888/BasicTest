package com.example.basictest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;

public class MainActivity2 extends AppCompatActivity {

    private static final String TAG = "MainActivity2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Log.d("MY_TEST", "onCreate: " + getIntent().getStringExtra("VALUE"));
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("MY_TEST", "onStart2");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("MY_TEST", "onResume2");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("MY_TEST", "onPause2");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("MY_TEST", "onStop2");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("MY_TEST", "onDestroy2");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("MY_TEST", "onActivityResult2");
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.i("MY_TEST", "onNewIntent2");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i("MY_TEST", "onSaveInstanceState2" + "一个参数");
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        Log.i("MY_TEST", "onSaveInstanceState2" + "两个参数");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.i("MY_TEST", "onRestoreInstanceState2");
    }

    public void onClickSetResult(View view){
        this.setResult(111, new Intent().putExtra("resultValue", "value"));
    }
    public void onClickFinish(View view){
        finish();
    }



}
