package com.example.seetaface2demo

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.text.style.BackgroundColorSpan
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import com.seeta.sdk.util.SeetaHelper
import com.seeta.sdk.util.SeetaUtil
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import java.io.File
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity(),View.OnClickListener {

    //权限
    val pers = arrayOf<String>(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initPer()
    }

    private fun initPer() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //判断是否具有权限
            for (per in pers) {
                if (ContextCompat.checkSelfPermission(this, per) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, arrayOf(per), 1)
                }else{
                    initData()
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, @NonNull permissions: Array<String>, @NonNull grantResults: IntArray) {
        if (requestCode == 1) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initData()
            } else {
                Toast.makeText(this,"需要访问SD卡权限，否则无法使用",Toast.LENGTH_SHORT).show()
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    private fun initData() {
//        SeetaHelper.ROOT_CACHE = Environment.getExternalStorageDirectory().toString() + File.separator + "seetaface"
//        SeetaHelper.ROOT_ASSETS = "seetaface"
        Thread{
            if (SeetaHelper.copyAts(this))
                SeetaHelper.getInstance().init()
        }.start()

    }

    override fun onClick(p0: View?) {
        var clz:Class<*> = FaceRecognizer2Activity::class.java
        when(p0?.id){
            R.id.main_txt2 -> {
                clz = CompareActivity::class.java
            }
        }
        startActivity(Intent(this@MainActivity, clz))
    }

    override fun onDestroy() {
        super.onDestroy()
        SeetaHelper.getInstance().destroy()
    }
}
