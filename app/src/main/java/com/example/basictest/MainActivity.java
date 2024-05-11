package com.example.basictest;

import androidx.annotation.NonNull;
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
import com.vmadalin.easypermissions.EasyPermissions;

import java.io.File;
import java.util.List;

import rx.functions.Action1;

public class MainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {
    public static final String PARENT_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
    /*签名控件*/private CustomSignatureView mCustomSignatureView;

    /*保存签名*/private TextView mSaveSignatureTex;

    /*清除签名*/private TextView mClearSignatureTex;

    /*清除签名*/private TextView mCenterSignatureTex;
    private TextView rightNumTv;
    private TextView wrongNumTv;
    private TextView iconTv;
    private boolean isRightMode = true;
    private String base_file_name = "pic.png";
    private final int MAX_NUM = 200;
    private int rightNum = 0;
    private int wrongNum = 0;
    final String RIGHT_NUM = "rightNum";
    final String WRONG_NUM = "wrongNum";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestPermission();
        StorageUtils.Companion.init(this);
        rightNum = StorageUtils.Companion.getNumber(RIGHT_NUM);
        wrongNum = StorageUtils.Companion.getNumber(WRONG_NUM);
        initView();
        toolCofig();
        initListener();
        if (rightNum > MAX_NUM){
            isRightMode = false;
            iconTv.setText("❎");
        }
    }


    /**
     * 初始化控件
     */

    private void initView() {
        mCustomSignatureView= (CustomSignatureView) findViewById(R.id.CustomSignatureView_MainActivity_Canvas);
        mSaveSignatureTex= (TextView) findViewById(R.id.save);
        mClearSignatureTex= (TextView) findViewById(R.id.TextView_MainActivity_clear);
        mCenterSignatureTex= (TextView) findViewById(R.id.TextView_MainActivity_center);
        rightNumTv = findViewById(R.id.right_icon_nums);
        wrongNumTv = findViewById(R.id.wrong_icon_nums);
        iconTv = findViewById(R.id.icon);
        rightNumTv.setText(String.valueOf(rightNum));
        wrongNumTv.setText(String.valueOf(wrongNum));
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsDenied(int i, @NonNull List<String> list) {
        finish();
    }

    @Override
    public void onPermissionsGranted(int i, @NonNull List<String> list) {

    }

    private void requestPermission(){
        if (EasyPermissions.hasPermissions(this, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {
            // Already have permission, do the thing
            // ...
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this, "需要读写权限",
                    123, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE);
        }
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
                        if (mCustomSignatureView.isDrawingCacheEmpty()){
                            Toast.makeText(MainActivity.this,"还没有画轨迹",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        String fileName = "";
                        if (isRightMode){
                            fileName = "right" +File.separator+ "right_" + rightNum + "_" + base_file_name;
                            rightNum++;
                            rightNumTv.setText(String.valueOf(rightNum));
                            StorageUtils.Companion.saveNumber(RIGHT_NUM, rightNum);
                        } else {
                            fileName = "wrong" +File.separator+"wrong_" + wrongNum + "_" + base_file_name;
                            wrongNum++;
                            wrongNumTv.setText(String.valueOf(wrongNum));
                            StorageUtils.Companion.saveNumber(WRONG_NUM, wrongNum);
                        }
                        if (rightNum > MAX_NUM){
                            isRightMode = false;
                            iconTv.setText("❎");
                        }
                        if(mCustomSignatureView!=null){
                            if( mCustomSignatureView.toolSaveSignatureFile(new
                                    File(PARENT_PATH + File.separator + "raw_pics",fileName))){
//                                Toast.makeText(MainActivity.this,"保存成功:" + fileName,Toast.LENGTH_SHORT)
//                                        .show();
                                mCustomSignatureView.savePointList(isRightMode);
                                mCustomSignatureView.toolClearCanvas();
                            } else {
                                Toast.makeText(MainActivity.this,"不能保存文件:" + fileName,Toast.LENGTH_SHORT)
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
//                            mCustomSignatureView.toolMoveToCenter();
                        }
                    }
                });

    }
}
