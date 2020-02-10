package com.example.basictest;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;

import androidx.annotation.Nullable;

public class MyImageView extends ImageView {

    private static final String TAG = "MyImageView";
    private Context context;

    public MyImageView(Context context) {
        super(context);
        this.context = context;
    }

    public MyImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public MyImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    public MyImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        Log.d("MainActivity", "View.onTouchEvent: ");
//        switch (event.getAction()){
//            case MotionEvent.ACTION_DOWN:break;
//            case MotionEvent.ACTION_MOVE:
//                Log.d(TAG, "onTouchEvent: " + "x:" + event.getX() + ",y:" + event.getY());
//                Log.d(TAG, "onTouchEvent: " + "rawX:" + event.getRawX() + ",rawY:" + event.getRawY());
//                break;
//            case MotionEvent.ACTION_UP:break;
//        }
//        scrollTo((int)event.getX(), (int)event.getY());
        return true;
    }


    @Override
    public void setOnTouchListener(OnTouchListener l) {
        super.setOnTouchListener(l);
    }
}
