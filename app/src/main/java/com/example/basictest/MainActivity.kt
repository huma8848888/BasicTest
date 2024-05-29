package com.example.basictest

import ai.onnxruntime.OnnxTensor
import ai.onnxruntime.OrtEnvironment
import ai.onnxruntime.OrtSession
import android.Manifest
import android.graphics.Bitmap
import android.os.Bundle
import android.os.SystemClock
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.basictest.databinding.ActivityMainBinding
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
        val modelID = R.raw.abcd_mbv3
        return resources.openRawResource(modelID).readBytes()
    }

    /**
     * 配置
     */
    private fun toolCofig() {
        RxPermissions(this)
                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe { aBoolean ->
                    if (aBoolean) {
                        binding!!.CustomSignatureViewMainActivityCanvas
                                .tooSetTextColor(R.color.cardview_dark_background) //设置签名字体颜色
                                .toolSetCanvasColor(R.color.cardview_light_background) //设置签名背景颜色
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
                    if (binding!!.CustomSignatureViewMainActivityCanvas.isDrawingCacheEmpty) {
                        Toast.makeText(this@MainActivity, "还没有画轨迹", Toast.LENGTH_SHORT).show()
                        return@Action1
                    }
                    val ortSession = ortEnv?.createSession(readModel())
                    val analyzer = ORTAnalyzer(ortSession, object :(Result)-> Unit{
                        override fun invoke(p1: Result) {

                        }
                    })
                    analyzer.analyze(binding!!.CustomSignatureViewMainActivityCanvas.currBitmap){
                        binding!!.result.text = when(it){
                            0 -> A
                            1 -> B
                            2 -> C
                            3 -> D
                            else -> "Unknown"
                        }
                    }
                })
        /**
         * 清除签名
         */
        RxView.clicks(binding!!.TextViewMainActivityClear)
                .subscribe { binding!!.CustomSignatureViewMainActivityCanvas.toolClearCanvas() }
    }
}