package com.example.basictest;

import android.os.Bundle;
import android.view.View;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class MainActivity3 extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    @Subscribe
    public void onMessageEvent(BaseMessageEvent event) {
        super.onMessageEvent(event);
    }

    public void doSomething1(View view) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                EventBus.getDefault().post(new MainActivity.MainActivty1Event());
            }
        }.start();
    }

    public void doSomething2(View view) {
        EventBus.getDefault().post(new MainActivity2.MainActivity2Event());
    }


}
