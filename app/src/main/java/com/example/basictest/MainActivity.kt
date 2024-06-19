package com.example.basictest

import ai.onnxruntime.OnnxTensor
import ai.onnxruntime.OrtEnvironment
import ai.onnxruntime.OrtSession
import android.Manifest
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.os.SystemClock
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.basictest.databinding.ActivityMainBinding
import com.example.basictest.pen.pt.InkPen
import com.huitongzhiyuan.testapplication.HandWriteBitmap
import com.huitongzhiyuan.testapplication.pen.HandWriteView
import com.huitongzhiyuan.testapplication.pen.obj.NoteDataManager
import com.jakewharton.rxbinding.view.RxView
import com.tbruyelle.rxpermissions.RxPermissions
import rx.functions.Action1
import java.util.*

class MainActivity : AppCompatActivity() {
    val A = "A"
    val B = "B"
    val C = "C"
    val D = "D"
    var ortEnv: OrtEnvironment? = null

    //0：A，1：B，2：C，3：D
    private var binding: ActivityMainBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        toolCofig()
        initListener()
        createOrtSession()
    }

    // Create a new ORT session in background
    fun createOrtSession()  {
        ortEnv = OrtEnvironment.getEnvironment()
    }

    // Read ort model into a ByteArray, run in background
    fun readModel(): ByteArray  {
        val modelID = R.raw.cnocr_v2_3_scene_densenet_lite_136_gru_epoch_004_ft_model
        return resources.openRawResource(modelID).readBytes()
    }

    /**
     * 配置
     */
    var handWriteView: HandWriteView? = null
    private fun toolCofig() {
        RxPermissions(this)
                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe { aBoolean ->
                    if (aBoolean) {
                        //添加手写区域
                        val baseWidth = 20f
                        var pen: InkPen = InkPen(baseWidth)
                        handWriteView = HandWriteView(this)
                        handWriteView?.pen = pen

                        handWriteView?.setBackgroundColor(Color.WHITE)

                        val layoutParams = FrameLayout.LayoutParams(534, 503)
                        layoutParams.width = 534
                        layoutParams.height = 503
                        layoutParams.leftMargin = 0
                        layoutParams.topMargin = 0

                        val componentContainer =  findViewById<FrameLayout>(R.id.CustomSignatureView_MainActivity_Canvas)
                        componentContainer.addView(handWriteView, layoutParams)
                        NoteDataManager.insertAndGetPage("1234").let {
                            pen.setCurrentWritePage(it)
                            handWriteView?.recovery(it)
                        }
                    }
                }
    }

    /**
     * 设置监听
     */
    private fun initListener() {
        /**
         * 保存签名文件
         */
        RxView.clicks(binding!!.recognize)
                .subscribe(Action1 {
//                    if (binding!!.CustomSignatureViewMainActivityCanvas.) {
//                        Toast.makeText(this@MainActivity, "还没有画轨迹", Toast.LENGTH_SHORT).show()
//                        return@Action1
//                    }
                    val ortSession = ortEnv?.createSession(readModel())
                    val analyzer = ORTAnalyzer(this, ortSession)
                    val croppedBitmap: Bitmap? = HandWriteBitmap.cropTransparentArea(handWriteView?.offscreenBitmap,32,10)
//                    if (croppedBitmap != null) {
//                        HandWriteBitmap.saveBitmapToDisk(croppedBitmap, "croppedBitmap_1111111.png")
//                    }
                    analyzer.analyze(croppedBitmap){ timecost, probability, result ->
                        binding!!.timecost.text = "inference result:${result}, probability:${probability}, timecost:${timecost}ms"
                    }
                })
        /**
         * 清除签名
         */
        RxView.clicks(binding!!.TextViewMainActivityClear)
                .subscribe { handWriteView?.clear() }
    }
}