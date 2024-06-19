// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.example.basictest

import ai.onnxruntime.*
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.os.SystemClock
import android.util.Log
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.util.*


internal data class Result(
        var detectedIndices: List<Int> = emptyList(),
        var detectedScore: MutableList<Float> = mutableListOf<Float>(),
        var processTimeMs: Long = 0
) {}

internal class ORTAnalyzer() {
    val TAG = "ORTAnalyzer"
    var candidateList = mutableListOf<String>()
    private lateinit var context: Context
    private var ortSession: OrtSession? = null
    constructor(context: Context, ortSession: OrtSession?) : this() {
        this.context = context
        this.ortSession = ortSession
        init()
    }
    private fun init(){
        candidateList = readAssetFileToMutableList(context, "labels_cn.txt")
    }

    fun readAssetFileToMutableList(context: Context, fileName: String): MutableList<String> {
        val list = mutableListOf<String>() // 创建一个可变的字符串列表

        try {
            // 获取AssetManager实例
            val assetManager = context.assets
            // 使用AssetManager打开文件
            val inputStream: InputStream = assetManager.open(fileName)

            // 创建BufferedReader来读取文件
            val reader = BufferedReader(InputStreamReader(inputStream))

            var line: String?
            while (reader.readLine().also { line = it } != null) {
                // 将每一行添加到列表中
                list.add(line ?:"")
            }
            // 关闭BufferedReader
            reader.close()
            inputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return list
    }

    // Rotate the image of the input bitmap
    fun Bitmap.rotate(degrees: Float): Bitmap {
        val matrix = Matrix().apply { postRotate(degrees) }
        return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
    }

    fun analyze(image: Bitmap?, callBack: (Int, Double, String)-> Unit) {
        // Convert the input image to bitmap and resize to 224x224 for model input
        val rawBitmap = image?.let { Bitmap.createScaledBitmap(it, 32, 32, false) }
        val bitmap = rawBitmap?.rotate(0.0f)

        if (bitmap != null) {
            var result = Result()
            val imgData = preProcess(bitmap)
            val inputName = ortSession?.inputNames?.iterator()?.next()
            val shape = longArrayOf(1, 1, 32, 32)
            val env = OrtEnvironment.getEnvironment()
            env.use {
                val tensor = OnnxTensor.createTensor(env, imgData, shape)
                val startTime = SystemClock.uptimeMillis()
                val map = hashMapOf(inputName to tensor)
                map.put("input_lengths", OnnxTensor.createTensor(env, longArrayOf(bitmap.width.toLong())))
                tensor.use {
                    //开始推理
                    val output = ortSession?.run(map)
                    output.use {
                        result.processTimeMs = SystemClock.uptimeMillis() - startTime
                        @Suppress("UNCHECKED_CAST")
                        val probabilities = (output?.get(0)?.value as Array<Any>)?.get(0)
                        val softmaxedProbilityFromResultByLength = mutableListOf<Array<Double>>()
                        (probabilities as? Array<FloatArray>)?.forEachIndexed { index, value ->
                            val itemlist = value.map {
                                it.toDouble()
                            }
                            softmaxedProbilityFromResultByLength.add(Utils.softMax(itemlist))
                        }

                        val maxProbilityEveryLength = mutableListOf<Double>()
                        softmaxedProbilityFromResultByLength.forEach {
                            maxProbilityEveryLength.add(it.maxOrNull() ?:0.0)
                        }

                        //以预测结果最小值作为整个推理的概率
                        val probilityResult = maxProbilityEveryLength.minOrNull() ?:0.0

                        val argmaxResult = mutableListOf<Int>()
                        softmaxedProbilityFromResultByLength.forEach {
                            argmaxResult.add(Utils.argmax(it.toList()))
                        }

                        val resultStr = StringBuilder()
                        val maxIndex = candidateList.lastIndex
                        argmaxResult.forEachIndexed { index, i ->
                            if ( i < candidateList.size && i != maxIndex) {
                                resultStr.append(candidateList[i])
                            }
                        }
                        callBack.invoke(result.processTimeMs.toInt(), probilityResult, resultStr.toString())
                        Log.i(TAG, "Predicted label: ${argmaxResult.toIntArray().minOrNull() ?:0}, result:${resultStr}, probility = ${probilityResult}")
                    }
                }
            }
        }
    }

    // We can switch analyzer in the app, need to make sure the native resources are freed
    protected fun finalize() {
        ortSession?.close()
    }
}