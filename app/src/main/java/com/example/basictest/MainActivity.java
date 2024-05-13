package com.example.basictest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.example.basictest.databinding.ActivityMainBinding;
import com.jakewharton.rxbinding.view.RxView;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.vmadalin.easypermissions.EasyPermissions;

import java.io.File;
import java.util.List;

import rx.functions.Action1;

public class MainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {
    public static final String PARENT_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
    final String A = "A";
    final String B = "B";
    final String C = "C";
    final String D = "D";
    //0：A，1：B，2：C，3：D
    private String inputMode = A;
    private String base_file_name = "pic.png";
    private final int MAX_NUM = 10;
    private int aNum = 0;
    private int bNum = 0;
    private int cNum = 0;
    private int dNum = 0;
    final String A_NUM = "aNum";
    final String B_NUM = "bNum";
    final String C_NUM = "cNum";
    final String D_NUM = "dNum";
    private ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        requestPermission();
        StorageUtils.Companion.init(this);
        aNum = StorageUtils.Companion.getNumber(A_NUM);
        bNum = StorageUtils.Companion.getNumber(B_NUM);
        cNum = StorageUtils.Companion.getNumber(C_NUM);
        dNum = StorageUtils.Companion.getNumber(D_NUM);
        initView();
        toolCofig();
        initListener();
        handleShowText(inputMode);
    }



    /**
     * 初始化控件
     */

    private void initView() {
        binding.aNum.setText(String.valueOf(aNum));
        binding.bNums.setText(String.valueOf(bNum));
        binding.cNums.setText(String.valueOf(cNum));
        binding.dNums.setText(String.valueOf(dNum));
        binding.itemCount.setText("每一项需要绘制" + MAX_NUM + "个");
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
                            binding.CustomSignatureViewMainActivityCanvas
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
        RxView.clicks(binding.save)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (binding.CustomSignatureViewMainActivityCanvas.isDrawingCacheEmpty()){
                            Toast.makeText(MainActivity.this,"还没有画轨迹",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        String fileName = inputMode + File.separator + aNum + "_" + base_file_name;
                        switch (inputMode){
                            case A:
                                aNum++;
                                binding.aNum.setText(String.valueOf(aNum));
                                StorageUtils.Companion.saveNumber(A_NUM, aNum);
                                break;
                            case B:
                                bNum++;
                                binding.bNums.setText(String.valueOf(bNum));
                                StorageUtils.Companion.saveNumber(B_NUM, bNum);
                                break;
                            case C:
                                cNum++;
                                binding.cNums.setText(String.valueOf(cNum));
                                StorageUtils.Companion.saveNumber(C_NUM, cNum);
                                break;
                            case D:
                                dNum++;
                                binding.dNums.setText(String.valueOf(dNum));
                                StorageUtils.Companion.saveNumber(D_NUM, dNum);
                                break;
                        }

                        handleShowText(inputMode);
                        if( binding.CustomSignatureViewMainActivityCanvas.toolSaveSignatureFile(new
                                File(PARENT_PATH + File.separator + "raw_pics",fileName))){
//                                Toast.makeText(MainActivity.this,"保存成功:" + fileName,Toast.LENGTH_SHORT)
//                                        .show();
                            binding.CustomSignatureViewMainActivityCanvas.savePointList(inputMode);
                            binding.CustomSignatureViewMainActivityCanvas.toolClearCanvas();
                        } else {
                            Toast.makeText(MainActivity.this,"不能保存文件:" + fileName,Toast.LENGTH_SHORT)
                                    .show();
                        }
                    }
                });


        /**
         * 清除签名
         */
        RxView.clicks(binding.TextViewMainActivityClear)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                            binding.CustomSignatureViewMainActivityCanvas.toolClearCanvas();
                    }
                });

    }

    private void handleShowText(String inputMode){
        int currCount = 0;
        if (TextUtils.equals(inputMode, A)){
            currCount = aNum;
        } else if (TextUtils.equals(inputMode, B)){
            currCount = bNum;
        } else if (TextUtils.equals(inputMode, C)){
            currCount = cNum;
        } else if (TextUtils.equals(inputMode, D)){
            currCount = dNum;
        }
        if (currCount > MAX_NUM){
            if (TextUtils.equals(inputMode, A)){
                this.inputMode = B;
            } else if (TextUtils.equals(inputMode, B)){
                this.inputMode = C;
            } else if (TextUtils.equals(inputMode, C)){
                this.inputMode = D;
            }
            binding.icon.setText(this.inputMode);
        }
    }
}
