package com.example.seetaface2demo

import android.app.Activity
import android.app.ProgressDialog
import android.graphics.*
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureMimeType
import com.seeta.sdk.SeetaImageData
import com.seeta.sdk.SeetaRect
import com.seeta.sdk.util.SeetaHelper
import com.seeta.sdk.util.SeetaUtil
import kotlinx.android.synthetic.main.activity_facerecognizer2.*
import android.content.Intent
import android.widget.Toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

//人脸检测、关键点定位、两张人脸相似度
class FaceRecognizer2Activity : AppCompatActivity() , View.OnClickListener{


    lateinit var paint:Paint
    lateinit var bitmap: Bitmap
    lateinit var bitmap2: Bitmap
    lateinit var seetaImageData: SeetaImageData
    lateinit var seetaRects:Array<SeetaRect>
    var hasFace = false
    val similarity = mutableListOf<Float>()
    lateinit var pd:ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_facerecognizer2)
        bitmap = BitmapFactory.decodeResource(resources, R.mipmap.lyf2)
        bitmap2 = BitmapFactory.decodeResource(resources, R.mipmap.lyf)
        initData()
        pd = ProgressDialog(this)
        pd.setTitle("提示")
        pd.setMessage("正在检测中...")
        pd.setCanceledOnTouchOutside(false)
    }

    private fun initData() {
        bitmap = BitmapFactory.decodeResource(resources, R.mipmap.lyf2)
        detectFace()
        paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 3.0f
        paint.color = Color.RED
        paint.textSize = 36.0f
    }

    private fun detectFace(){
        Thread{
            seetaImageData = SeetaUtil.ConvertToSeetaImageData(bitmap)
            seetaRects = SeetaHelper.getInstance().faceDetector2.Detect(seetaImageData)
            if (seetaRects.isEmpty()) {
                Log.i("detectFace","没有检测到人脸")
                hasFace = false
            }
            hasFace = true
        }.start()
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.btn1 -> {
                PictureSelector.create(this)
                    .openGallery(PictureMimeType.ofImage())
                    .maxSelectNum(1)
                    .forResult(1);
            }
            R.id.btn2 -> {
                if (!hasFace){
                    Toast.makeText(this, "没有检测到人脸", Toast.LENGTH_SHORT).show()
                    return
                }
                val out =bitmap.copy(Bitmap.Config.ARGB_8888, true)
                val canvas = Canvas(out)
                //绘制出所有的检测出来的人脸的区域
                for (i in seetaRects.indices){
                    val rect = seetaRects[i]
                    canvas.drawRect(Rect(rect.x, rect.y, rect.x+rect.width, rect.y+rect.height), paint)
                    canvas.drawText(i.toString(), rect.x + rect.width/2.0f, rect.y-15.0f, paint)
                }
                img1.setImageBitmap(out)
            }
            R.id.btn3 -> {
                if (!hasFace){
                    Toast.makeText(this, "没有检测到人脸", Toast.LENGTH_SHORT).show()
                    return
                }
                val out = bitmap.copy(Bitmap.Config.ARGB_8888, true)
                val canvas = Canvas(out)
                for (i in seetaRects.indices) {
                    val rect = seetaRects[i]
                    canvas.drawText(i.toString(), rect.x + rect.width/2.0f, rect.y-15.0f, paint)
                    //根据检测到的人脸进行关键点定位
                    val seetaPoints = SeetaHelper.getInstance().pointDetector2.Detect(seetaImageData, rect)
                    if (seetaPoints.isNotEmpty()) {
                        paint.color = Color.RED
                        for (seetaPoint in seetaPoints) { //绘制面部关键点
                            canvas.drawCircle(
                                seetaPoint.x.toFloat(),
                                seetaPoint.y.toFloat(),
                                5.0f,
                                paint
                            )
                        }
                    }
                }
                img1.setImageBitmap(out)
            }
            R.id.btn4 -> {
                PictureSelector.create(this)
                    .openGallery(PictureMimeType.ofImage())
                    .maxSelectNum(1)
                    .forResult(2);
            }
            R.id.btn5 -> {
                compareTwo()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                1 -> {
                    // 图片、视频、音频选择结果回调
                    val selectList = PictureSelector.obtainMultipleResult(data)
                    bitmap = BitmapFactory.decodeFile(selectList[0].path)
                    img1.setImageBitmap(bitmap)
                    detectFace()
                }
                2 -> {
                    val selectList = PictureSelector.obtainMultipleResult(data)
                    bitmap2 = BitmapFactory.decodeFile(selectList[0].path)
                    img2.setImageBitmap(bitmap2)
                }
            }
        }
    }


    private fun compareTwo() {
        txt.text = "相似度（与第一张图片第0张人脸）："
        if (seetaRects.isEmpty()){
            Toast.makeText(this@FaceRecognizer2Activity, "第一张图片没有检测到人脸", Toast.LENGTH_SHORT).show()
            return
        }
        similarity.clear()
        val seetaImageData2 = SeetaUtil.ConvertToSeetaImageData(bitmap2)
        val seetaRects2 = SeetaHelper.getInstance().faceDetector2.Detect(seetaImageData2)
        if (seetaRects2.isEmpty()){
            Toast.makeText(this@FaceRecognizer2Activity, "第二张图片没有检测到人脸", Toast.LENGTH_SHORT).show()
            return
        }
        showDia()
        val out = bitmap2.copy(Bitmap.Config.ARGB_8888, true)
        val result = StringBuffer()
        val compare = GlobalScope.async (Dispatchers.IO) {
            val canvas = Canvas(out)
            //绘制出所有的检测出来的人脸的区域
            for (i in seetaRects2.indices) {
                val rect = seetaRects2[i]
                canvas.drawRect(Rect(rect.x, rect.y, rect.x + rect.width, rect.y + rect.height), paint)
                canvas.drawText(i.toString(), rect.x + rect.width/2.0f, rect.y-15.0f, paint)
                //根据检测到的人脸进行面部关键点定位
                val seetaPoints = SeetaHelper.getInstance().pointDetector2.Detect(seetaImageData, seetaRects[0])//第一张人脸
                val seetaPoints2 = SeetaHelper.getInstance().pointDetector2.Detect(seetaImageData2, rect)
                val sim = SeetaHelper.getInstance().faceRecognizer2.Compare(
                    seetaImageData,
                    seetaPoints,
                    seetaImageData2,
                    seetaPoints2
                )
                Log.i("FaceRecognizer2", "similarity:${sim}")
                similarity.add(sim)
            }
            for (i in similarity.indices) {
                if (!result.isNullOrBlank())
                    result.append(";\n")
                result.append("第$i 个:${similarity[i]}")
                Log.i("FaceRecognizer2", "第$i 个similarity:${similarity[i]}")
            }
        }
        GlobalScope.launch (Dispatchers.Main){
            compare.await()
            txt.text = "相似度（与第一张图片第0张人脸）：\n $result"
            img2.setImageBitmap(out)
            cancelDia()
        }
    }

    private fun showDia(){
        if (pd!=null && !pd.isShowing){
            pd.show()
        }
    }

    private fun cancelDia(){
        if (pd!=null && pd.isShowing){
            pd.dismiss()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cancelDia()
    }
}