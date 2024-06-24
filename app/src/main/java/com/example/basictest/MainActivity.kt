package com.example.basictest

import android.Manifest
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import com.example.basictest.databinding.ActivityMainBinding
import com.example.basictest.databinding.ListItemViewBinding
import com.example.basictest.pen.pt.InkPen
import com.huitongzhiyuan.testapplication.HandWriteBitmap
import com.huitongzhiyuan.testapplication.pen.HandWriteView
import com.huitongzhiyuan.testapplication.pen.obj.NoteDataManager
import com.tbruyelle.rxpermissions.RxPermissions
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.EasyPermissions.hasPermissions
import com.vmadalin.easypermissions.EasyPermissions.onRequestPermissionsResult
import com.vmadalin.easypermissions.EasyPermissions.requestPermissions
import java.io.File

class MainActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {
    private var binding: ActivityMainBinding? = null
    val INIT_ITEM = "INIT_ITEM"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        requestPermission()
        penAndCanvasSetting()
        initSampleList()
        initView()
        initListener()
    }

    private var handWriteView: HandWriteView? = null
    private var currSelectedItemView : ListItemViewBinding? = null
    private var currSamplingItem : SampleBean? = null

    private fun initSampleList(){
        SampleUtil.sampleList.forEachIndexed { index, sampleBean ->
            val currDoneCount = Utils.calculateImgFolderCount(Utils.IMG_PARENT_FOLDER_PATH + File.separator + sampleBean.text)
            val itemViewBinding = ListItemViewBinding.bind(LayoutInflater.from(this).inflate(R.layout.list_item_view, null, false))
            if (index == 0){
                itemViewBinding.root.tag = INIT_ITEM
            }
            sampleBean.count = currDoneCount
            itemViewBinding.text.text = sampleBean.text
            itemViewBinding.timeCount.text = currDoneCount.toString()
            itemViewBinding.target.text = sampleBean.target.toString()
            itemViewBinding.root.setOnClickListener {
                this.currSelectedItemView = itemViewBinding
                this.currSamplingItem = sampleBean
                binding?.icon?.text = sampleBean.text
            }
            binding?.contentWrapper?.addView(itemViewBinding.root)
        }
    }

    private fun refreshSampleList(){
        binding?.contentWrapper?.children?.forEachIndexed { index, view ->
            val itemViewBinding = ListItemViewBinding.bind(view)
            val currDoneCount = Utils.calculateImgFolderCount(Utils.IMG_PARENT_FOLDER_PATH + File.separator + itemViewBinding.text.text)
            itemViewBinding.timeCount.text = currDoneCount.toString()
        }
    }

    private fun initView(){
        val initView = binding?.contentWrapper?.findViewWithTag<ViewGroup>(INIT_ITEM)
        initView?.performClick()
    }

    /**
     * 配置
     */
    private fun penAndCanvasSetting() {
        RxPermissions(this)
                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe { aBoolean ->
                    if (aBoolean) {
                        //添加手写区域
                        val baseWidth = 18f
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
        binding!!.save.setOnClickListener {
            if (handWriteView?.isDrawValidPath() == false) {
                Toast.makeText(this, "请先签名，再保存", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            var offscreenBitmap: Bitmap? = handWriteView?.offscreenBitmap
            val croppedBitmap: Bitmap? = HandWriteBitmap.cropTransparentArea(offscreenBitmap, 224, 20)
            val currCount = currSelectedItemView?.timeCount?.text.toString().toInt()
            val newCount = currCount + 1
            croppedBitmap?.let {
                Utils.saveImgFile(it, Utils.IMG_PARENT_FOLDER_PATH + File.separator + currSamplingItem?.text + File.separator + newCount + ".png")
            }
            handWriteView?.savePointList(Utils.TRACES_PARENT_FOLDER_PATH + File.separator, currSamplingItem?.text ?:"")
            currSelectedItemView?.timeCount?.text = newCount.toString()
            refreshSampleList()
            handWriteView?.clear()
        }

        binding!!.clear.setOnClickListener {
            binding!!.canvas.clear()
        }
    }

    companion object {
        @JvmField
        val PARENT_PATH = Environment.getExternalStorageDirectory().absolutePath

        @JvmField
        var root_folder = "handwriting_data"
    }
}