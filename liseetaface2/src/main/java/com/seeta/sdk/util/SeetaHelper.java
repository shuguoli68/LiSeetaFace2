package com.seeta.sdk.util;


import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.seeta.sdk.FaceDetector2;
import com.seeta.sdk.FaceRecognizer2;
import com.seeta.sdk.PointDetector2;

import java.io.File;

public class SeetaHelper {

    public float SIMILARITY_VALUE = 0.55f;//相似度临界值
    //人脸识别引擎文件
    public static String ROOT_CACHE = Environment.getExternalStorageDirectory() + File.separator+"seetaface";
    public static String ROOT_ASSETS = "seetaface";//assets目录中要拷贝的文件名
    private static final String model1 = ROOT_CACHE+ File.separator + "SeetaFaceDetector2.0.ats";
    private static final String model2 = ROOT_CACHE+ File.separator + "SeetaPointDetector2.0.pts5.ats";
    private static final String model3 = ROOT_CACHE+ File.separator + "SeetaFaceRecognizer2.0.ats";
    //人脸检测、关键点定位、特征提取与对比
    public FaceDetector2 faceDetector2;
    public PointDetector2 pointDetector2;
    public FaceRecognizer2 faceRecognizer2;

    private static SeetaHelper instance;

    /**
     * 初始化引擎
     * 耗时8s左右，建议放在子线程
     */
    private SeetaHelper(){
        Log.i("Seetaface", "开始引擎初始化");
        if (faceDetector2!=null && pointDetector2!=null && faceRecognizer2!=null) {
            Log.i("Seetaface", "引擎已初始化");
            return;
        }
        //人脸检测模块
        faceDetector2 = new FaceDetector2(model1);
        //面部关键点定位模块
        pointDetector2 = new PointDetector2(model2);
        //人脸特征提取与比对模块
        faceRecognizer2 = new FaceRecognizer2(model3);
        Log.i("Seetaface", "引擎初始化完成");
    }


    public void init(){
        //检测引擎文件是否存在
        if (!new File(ROOT_CACHE).exists() || !new File(model1).exists() || !new File(model2).exists() || !new File(model3).exists()) {
            Log.i("Seetaface", "初始化失败，SD卡不存在引擎文件");
        }else {
            Log.i("Seetaface", "初始化成功");
        }
    }

    public static SeetaHelper getInstance(){
        if (instance == null){
            synchronized (SeetaHelper.class){
                if (instance == null){
                    instance = new SeetaHelper();
                }
            }
        }
        return instance;
    }


    /**
     * 从assets中复制ats模型文件到SD卡
     */
    public static Boolean copyAts(final Context mContext){
        try {
            //检测引擎文件是否存在
            if (!new File(model1).exists() || !new File(model2).exists() || !new File(model3).exists()) {
                Log.i("Seetaface", "开始复制引擎到SD卡...");
                SeetaUtil.copyAssetsToDst(mContext, ROOT_ASSETS, ROOT_CACHE);
                Log.i("Seetaface", "已成功复制引擎到SD卡");
                return true;
            }else {
                Log.i("Seetaface", "引擎文件已在SD卡");
                return true;
            }
        }catch (Exception e){
            Log.i("Seetaface", "复制引擎文件异常："+e.getMessage());
        }
        return false;
    }

    /**
     * 引擎销毁
     */
    public void destroy(){
        if (faceDetector2!=null) {
            faceDetector2.dispose();
            faceDetector2 = null;
        }
        if (pointDetector2!=null) {
            pointDetector2.dispose();
            pointDetector2 = null;
        }
        if (faceRecognizer2!=null) {
            faceRecognizer2.dispose();
            faceRecognizer2 = null;
        }
    }

}
