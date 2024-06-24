package com.example.basictest

import android.content.Context
import android.graphics.Bitmap
import android.os.Environment
import android.util.TypedValue
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

class Utils {
    companion object{
        val IMG_PARENT_FOLDER_PATH = Environment.getExternalStorageDirectory().toString() +
                File.separator + "handwritting_sampler" + File.separator + "images"
        val TRACES_PARENT_FOLDER_PATH = Environment.getExternalStorageDirectory().toString() +
                File.separator + "handwritting_sampler" + File.separator + "traces"

        /**
         * dp转px
         */
        fun dp2px(context: Context, dpVal: Float): Int {
            return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    dpVal, context.resources.displayMetrics).toInt()
        }

        /**
         * 保存签名文件
         */
        fun saveImgFile(mBitmap: Bitmap, fullPath : String): Boolean {
            val mFile = File(fullPath)
            if (mBitmap != null) {
                var out: FileOutputStream? = null
                try {
                    if (!mFile.exists()) {
                        if (!File(mFile.parent).exists()) {
                            if (File(mFile.parent).mkdirs()) {
                                if (!mFile.exists()) {
                                    if (mFile.createNewFile()) {
                                        out = FileOutputStream(mFile)
                                    }
                                }
                            }
                        } else {
                            if (!mFile.exists()) {
                                if (mFile.createNewFile()) {
                                    out = FileOutputStream(mFile)
                                }
                            }
                        }
                    } else {
                        out = FileOutputStream(mFile)
                    }
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                    return false
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                try {
                    if (null != out) {
                        mBitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
                        out.flush()
                        out.close()
                        return true
                    }
                } catch (e: IOException) {
                    return false
                }
            }
            return false
        }

        fun calculateImgFolderCount(folderPath : String) : Int{
            val folder = File(folderPath)
            return if (folder.exists() && folder.isDirectory) {
                val files = folder.listFiles()
                var imgCount = 0
                files?.forEach {
                    if (it.isFile && it.extension == "png") {
                        imgCount++
                    }
                }
                imgCount
            } else {
                0
            }
        }
    }

}