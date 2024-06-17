package com.huitongzhiyuan.testapplication

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Environment
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

object HandWriteBitmap {


    fun saveBitmapToDisk(bitmap: Bitmap?, fileName: String): Boolean {
        if (bitmap == null) {
            return false
        }

        // 获取外部存储目录
        val storageDir = Environment.getExternalStorageDirectory().toString()

        // 创建文件对象，指定文件路径和文件名
        val file = File(storageDir, fileName)

        var fos: FileOutputStream? = null
        try {
            // 创建文件输出流对象
            fos = FileOutputStream(file)

            // 将Bitmap压缩并写入输出流中，格式为PNG，质量为100%
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)

            // 确保所有数据都写入输出流中
            fos.flush()

            return true
        } catch (e: IOException) {
            e.printStackTrace()
            return false
        } finally {
            try {
                // 关闭输出流
                fos?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }


    fun cropTransparentArea(bitmap: Bitmap?,targetWidth:Int,ImagePadding:Int): Bitmap? {
        if (bitmap == null) return null

        val width = bitmap.width
        val height = bitmap.height

        var minX = width
        var minY = height
        var maxX = -1
        var maxY = -1

        // 遍历所有像素，找到非透明像素的边界
        for (y in 0 until height) {
            for (x in 0 until width) {
                if (bitmap.getPixel(x, y) != Color.TRANSPARENT) {
                    if (x < minX) minX = x
                    if (y < minY) minY = y
                    if (x > maxX) maxX = x
                    if (y > maxY) maxY = y
                }
            }
        }

        // 检查是否找到有效区域
        if (minX <= maxX && minY <= maxY) {
            val newW = maxX - minX + 1
            val newH = maxY - minY + 1

            // 取最大值，确保新Bitmap是正方形
            var sideLength = Math.max(newW, newH)
           val newImagePadding = sideLength.toFloat()/targetWidth.toFloat() * ImagePadding.toFloat()
            sideLength = (sideLength + newImagePadding.toInt() + newImagePadding.toInt())

            // 创建一个新的空白Bitmap，大小为sideLength x sideLength，背景为透明或白色（根据需求）
            val squareBitmap = Bitmap.createBitmap(sideLength, sideLength, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(squareBitmap)
            canvas.drawColor(Color.WHITE) // 设置背景为透明，如果需要白色背景，可以使用Color.WHITE

            // 计算偏移量，以保持裁剪后的Bitmap居中
            val offsetX = (sideLength - newW) / 2
            val offsetY = (sideLength - newH) / 2

            // 绘制裁剪后的Bitmap到新的空白Bitmap上，并进行居中处理
            canvas.drawBitmap(bitmap, android.graphics.Rect(minX, minY, maxX + 1, maxY + 1), android.graphics.Rect(offsetX, offsetY, offsetX + newW, offsetY + newH), null)


            return Bitmap.createScaledBitmap(squareBitmap, targetWidth, targetWidth, true)
        }

        // 如果没有找到有效区域，则返回原始Bitmap
        return bitmap
    }




    fun cropTransparentArea2(bitmap: Bitmap?, targetWidth: Int, targetHeight: Int, padding: Int): Bitmap? {
        val new_bitmap = cropTransparentArea(bitmap)
        if (new_bitmap == null) return null

        // 创建一个新的空白Bitmap，大小为targetWidth x targetHeight
        val newBitmap = Bitmap.createBitmap(targetWidth, targetHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(newBitmap)
        canvas.drawColor(Color.WHITE)
//        canvas.drawColor(Color.TRANSPARENT) // 设置背景为透明

        // 计算缩放比例和偏移量，以保持裁剪后的Bitmap居中并留出边框
        val scaleFactor = Math.min((targetWidth - 2 * padding) / new_bitmap.width.toFloat(), (targetHeight - 2 * padding) / new_bitmap.height.toFloat())
        val scaledWidth = (new_bitmap.width * scaleFactor).toInt()
        val scaledHeight = (new_bitmap.height * scaleFactor).toInt()

        val left = (targetWidth - scaledWidth) / 2
        val top = (targetHeight - scaledHeight) / 2

        // 绘制裁剪后的Bitmap到新的空白Bitmap上，并进行缩放和居中处理
        val paint = Paint()
        paint.isFilterBitmap = true // 启用抗锯齿
        canvas.drawBitmap(new_bitmap, null, android.graphics.Rect(left, top, left + scaledWidth, top + scaledHeight), paint)

        return newBitmap
    }



    fun cropTransparentArea(bitmap: Bitmap?): Bitmap? {
        if (bitmap == null) return null
        val width = bitmap.width
        val height = bitmap.height

        var minX = width
        var minY = height
        var maxX = -1
        var maxY = -1

        // 遍历所有像素，找到非透明像素的边界
        for (y in 0 until height) {
            for (x in 0 until width) {
                if (bitmap.getPixel(x, y) != Color.TRANSPARENT) {
                    if (x < minX) minX = x
                    if (y < minY) minY = y
                    if (x > maxX) maxX = x
                    if (y > maxY) maxY = y
                }
            }
        }
        // 检查是否找到有效区域
        if (minX <= maxX && minY <= maxY) {
            // 裁剪Bitmap，保留有效区域
            return Bitmap.createBitmap(bitmap, minX, minY, maxX - minX + 1, maxY - minY + 1)
        }
        // 如果没有找到有效区域，则返回原始Bitmap
        return bitmap
    }
}