package com.example.basictest;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private Button button;
    private ObjectAnimator objectAnimator;
    private CustomButton customButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i("MY_TEST", "onCreate1");
//        button = findViewById(R.id.button);
        customButton = findViewById(R.id.customBtn);
//        objectAnimator = ObjectAnimator.ofInt(button, "height", 500, 0, 0, 500);
    }

    public void action(View view) {
        final Thread thread = new Thread() {
            @Override
            public void run() {
                super.run();
                Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                startActivity(intent);
            }
        };
        thread.start();
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
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.i("MY_TEST", "onNewIntent1");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i("MY_TEST", "onSaveInstanceState1" + outState.toString());
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


}
