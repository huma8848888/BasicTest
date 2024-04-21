package com.example.basictest;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.jakewharton.rxbinding.view.RxView;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.io.File;

import rx.functions.Action1;

public class MainActivity extends AppCompatActivity {
    /*签名控件*/private CustomSignatureView mCustomSignatureView;

    /*保存签名*/private TextView mSaveSignatureTex;

    /*清除签名*/private TextView mClearSignatureTex;

    /*清除签名*/private TextView mCenterSignatureTex;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        toolCofig();
        initListener();
    }


    /**
     * 初始化控件
     */

    private void initView() {
        mCustomSignatureView= (CustomSignatureView) findViewById(R.id.CustomSignatureView_MainActivity_Canvas);
        mSaveSignatureTex= (TextView) findViewById(R.id.TextView_MainActivity_Save);
        mClearSignatureTex= (TextView) findViewById(R.id.TextView_MainActivity_clear);
        mCenterSignatureTex= (TextView) findViewById(R.id.TextView_MainActivity_center);

    }

    /**
     * 配置
     */
    private void toolCofig(){
        new RxPermissions(this)
                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        if(aBoolean){
                            mCustomSignatureView
                                    .tooSetTextColor(R.color.cardview_dark_background)//设置签名字体颜色
                                    .toolSetCanvasColor(R.color.cardview_light_background);//设置签名背景颜色
                        }
                    }
                });
    }
    /**
     * 设置监听
     */
    private void initListener() {
        /**
         * 保存签名文件
         */
        RxView.clicks(mSaveSignatureTex)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if(mCustomSignatureView!=null){
                            if( mCustomSignatureView.toolSaveSignatureFile(new
                                    File(Environment.getExternalStorageDirectory().getAbsolutePath(),"aaa.png"))){
                                Toast.makeText(MainActivity.this,"保存成功",Toast.LENGTH_SHORT)
                                        .show();
                            }
                        }
                    }
                });


        /**
         * 清除签名
         */
        RxView.clicks(mClearSignatureTex)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if(mCustomSignatureView!=null){
                            mCustomSignatureView.toolClearCanvas();
                        }
                    }
                });

        /**
         * 居中签名
         */
        RxView.clicks(mCenterSignatureTex)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if(mCustomSignatureView!=null){
                            /*还有点问题-暂时不使用这个功能*/
                            mCustomSignatureView.toolMoveToCenter();
                        }
                    }
                });

    }
}
