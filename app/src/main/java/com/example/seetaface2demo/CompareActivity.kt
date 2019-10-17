package com.example.seetaface2demo

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.seeta.sdk.SeetaImageData
import com.seeta.sdk.SeetaPointF
import com.seeta.sdk.SeetaRect
import com.seeta.sdk.util.SeetaHelper
import com.seeta.sdk.util.SeetaUtil
import kotlinx.android.synthetic.main.activity_compare.*
import kotlinx.coroutines.*
import java.lang.ref.WeakReference

class CompareActivity : AppCompatActivity(){

    private lateinit var data: List<FaceBean>
    private lateinit var adapter: CompareAdapter

    lateinit var seetaImageData: SeetaImageData
    lateinit var seetaPoints: Array<SeetaPointF>

    private val mHandler = MyHandler(this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compare)
        initData()
        initTarget()
        initListener()
    }

    private fun initData() {
        data = arrayListOf<FaceBean>(
            FaceBean(R.mipmap.zyl0,0.0f),
            FaceBean(R.mipmap.zyl1,0.0f),
            FaceBean(R.mipmap.zyl2,0.0f),
            FaceBean(R.mipmap.zyl3,0.0f),
            FaceBean(R.mipmap.zyl4,0.0f),
            FaceBean(R.mipmap.zyl5,0.0f),
            FaceBean(R.mipmap.zyl6,0.0f),
            FaceBean(R.mipmap.lyf,0.0f),
            FaceBean(R.mipmap.lzy,0.0f),
            FaceBean(R.mipmap.lyf2,0.0f)
        )
        adapter = CompareAdapter(this, data)
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = adapter
    }

    private fun initTarget() {
        val bitmap = BitmapFactory.decodeResource(resources, R.mipmap.zyl0)
        seetaImageData = SeetaUtil.ConvertToSeetaImageData(bitmap)
        val seetaRects = SeetaHelper.getInstance().faceDetector2.Detect(seetaImageData)
        seetaPoints = SeetaHelper.getInstance().pointDetector2.Detect(seetaImageData, seetaRects[0])
        bitmap.recycle()
//        SeetaHelper.faceRecognizer2.Clear()
//        val targetId = SeetaHelper.faceRecognizer2.Register(seetaImageData, seetaPoints)
//        Log.i("compare", "targetId:$targetId")
    }

    private fun initListener() {
        btn1.setOnClickListener {
            Toast.makeText(this,"开始获取两张图片中人脸相似度", Toast.LENGTH_SHORT).show()
            btn1.isEnabled = false
            Thread{
                compareTwo()
            }.start()
        }
    }

    private fun compareTwo() {
        var bitmap:Bitmap
        var seetaImageData2:SeetaImageData
        var seetaRects:Array<SeetaRect>
        var seetaPoints2:Array<SeetaPointF>
        for (position in data.indices) {
            bitmap = BitmapFactory.decodeResource(resources, data[position].resId)
            seetaImageData2 = SeetaUtil.ConvertToSeetaImageData(bitmap)
            seetaRects = SeetaHelper.getInstance().faceDetector2.Detect(seetaImageData2)
            seetaPoints2 = SeetaHelper.getInstance().pointDetector2.Detect(seetaImageData2, seetaRects[0])
            val value = SeetaHelper.getInstance().faceRecognizer2.Compare(seetaImageData,seetaPoints,seetaImageData2,seetaPoints2)
            data[position].value = value
            Log.i("compare", "value:$value")
            mHandler.sendMessage(mHandler.obtainMessage(1,position))
        }
    }

    private inner class MyHandler(comAct:CompareActivity):Handler(){
        var act:WeakReference<CompareActivity> = WeakReference(comAct)
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            val main = act.get()
            if (msg?.what == 1){
                main?.adapter?.notifyItemChanged(msg?.obj as Int)
                if (main?.data?.size?.minus(1) ?: 0 == msg?.obj as Int)
                    btn1.isEnabled = true
            }
        }
    }
}