package com.example.basictest

import android.Manifest
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.os.Environment
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.basictest.StorageUtils.Companion.init
import com.example.basictest.databinding.ActivityMainBinding
import com.example.basictest.pen.pt.InkPen
import com.huitongzhiyuan.testapplication.HandWriteBitmap
import com.huitongzhiyuan.testapplication.pen.HandWriteView
import com.huitongzhiyuan.testapplication.pen.obj.NoteDataManager
import com.jakewharton.rxbinding.view.RxView
import com.tbruyelle.rxpermissions.RxPermissions
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.EasyPermissions.hasPermissions
import com.vmadalin.easypermissions.EasyPermissions.onRequestPermissionsResult
import com.vmadalin.easypermissions.EasyPermissions.requestPermissions
import rx.functions.Action1

class MainActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {
    private var binding: ActivityMainBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        requestPermission()
        init(this)
        toolCofig()
        initListener()
    }
     var handWriteView: HandWriteView? = null
    /**
     * 配置
     */
    private fun toolCofig() {
        RxPermissions(this)
                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe { aBoolean ->
                    if (aBoolean) {
                        //添加手写区域
                        val baseWidth = 30f
                        var pen: InkPen = InkPen(baseWidth)
                        handWriteView = binding?.canvas
                        handWriteView?.pen = pen

                        handWriteView?.setBackgroundColor(Color.WHITE)
                        NoteDataManager.insertAndGetPage("1234").let {
                            pen.setCurrentWritePage(it)
                            handWriteView?.recovery(it)
                        }
                    }
                }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        // Forward results to EasyPermissions
        onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsDenied(i: Int, list: List<String>) {
        finish()
    }

    override fun onPermissionsGranted(i: Int, list: List<String>) {}
    private fun requestPermission() {
        if (hasPermissions(this, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
            // Already have permission, do the thing
            // ...
        } else {
            // Do not have permissions, request them now
            requestPermissions(this, "需要读写权限",
                    123, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }

    /**
     * 设置监听
     */
    private fun initListener() {
        /**
         * 保存签名文件
         */
        RxView.clicks(binding!!.save)
                .subscribe(Action1 {

                    var offscreenBitmap: Bitmap? = handWriteView?.offscreenBitmap
                    val croppedBitmap: Bitmap? = HandWriteBitmap.cropTransparentArea(offscreenBitmap,224,20)
                    if (croppedBitmap != null) {
                        HandWriteBitmap.saveBitmapToDisk(croppedBitmap, "croppedBitmap_1111111.png")
                    }

                    val croppedBitmap2: Bitmap? = HandWriteBitmap.cropTransparentArea2(offscreenBitmap,224,224,20)
                    if (croppedBitmap2 != null) {
                        HandWriteBitmap.saveBitmapToDisk(croppedBitmap2, "croppedBitmap_2222222.png")
                    }

                    //                        if (binding.canvas.isDrawingCacheEmpty()){
//                            Toast.makeText(MainActivity.this,"还没有画轨迹",Toast.LENGTH_SHORT).show();
//                            return;
//                        }
                    //                        if( binding.canvas.toolSaveSignatureFile(new
//                                File(PARENT_PATH + File.separator + root_folder + File.separator + "raw_pics",fileName))){
//                                Toast.makeText(MainActivity.this,"保存成功:" + fileName,Toast.LENGTH_SHORT)
//                                        .show();
//                            binding.CustomSignatureViewMainActivityCanvas.savePointList(inputMode);
//                            binding.CustomSignatureViewMainActivityCanvas.toolClearCanvas();
                } //                        else {
                        //                            Toast.makeText(MainActivity.this,"不能保存文件:" + fileName,Toast.LENGTH_SHORT)
                        //                                    .show();
                        //                        }
                        //                    }
                )
        /**
         * 清除签名
         */
        RxView.clicks(binding!!.clear)
                .subscribe(object : Action1<Void?> {
                    override fun call(aVoid: Void?) {
                        binding!!.canvas.clear()
                    }
                })
    }

    companion object {
        @JvmField
        val PARENT_PATH = Environment.getExternalStorageDirectory().absolutePath
        @JvmField
        var root_folder = "handwriting_data"
    }
}