# LiSeetaFace2
离线人脸识别升级版SeetaFace2

###### 效果截图
1. 人脸检测识别
<div align="center">
<img src="https://github.com/shuguoli68/SeetaFace2Demo/blob/master/Screenshots/1.png"  height="480" width="270">
<img src="https://github.com/shuguoli68/SeetaFace2Demo/blob/master/Screenshots/2.png"  height="480" width="270">
<img src="https://github.com/shuguoli68/SeetaFace2Demo/blob/master/Screenshots/3.png"  height="480" width="270">
</div>
2. 人脸相似度：
<div align="center">
<img src="https://github.com/shuguoli68/SeetaFace2Demo/blob/master/Screenshots/4.png"  height="480" width="270">
<img src="https://github.com/shuguoli68/SeetaFace2Demo/blob/master/Screenshots/5.png"  height="480" width="270">
</div>
<div align="center">
<img src="https://github.com/shuguoli68/SeetaFace2Demo/blob/master/Screenshots/6.png"  height="480" width="270">
<img src="https://github.com/shuguoli68/SeetaFace2Demo/blob/master/Screenshots/7.png"  height="480" width="270">
</div>


### 使用
+ 一、How to use ?

Step 1. Add it in your root build.gradle at the end of repositories:
```
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
Step 2. Add the dependency
```
dependencies {
	        implementation 'com.github.shuguoli68:SeetaFace2Demo:1.0'
	}
```


+ 二、添加so文件、将模型文件复制到SD卡

1. **so文件下载**:<a href="https://pan.baidu.com/s/1lG56H7GXuQDfC6ybhrXbNA" target="_blank">https://pan.baidu.com/s/1lG56H7GXuQDfC6ybhrXbNA</a>  
armeabi-v7a： libSeetaFaceDetector2.so 、 libSeetaFaceLandmarker2.so 、 libSeetaFaceRecognizer2.so 、 libseetanet2.so  
将SO文件放置在jniLibs下的armeabi-v7a的文件夹下，并且在主module下（一般为app）：
```
defaultConfig {
        ...
        ndk {
            abiFilters 'armeabi-v7a'
        }
    }
```
<div align="left">
<img src="https://github.com/shuguoli68/SeetaFace2Demo/blob/master/Screenshots/so.png">
</div>

2. **模型文件下载**: <a href="https://pan.baidu.com/s/1s4OWPnVFj3_xLNoQM1mdqA" target="_blank">https://pan.baidu.com/s/1s4OWPnVFj3_xLNoQM1mdqA</a>  
dat模型文件seetaface ：SeetaFaceDetector2.0.ats 、 SeetaFaceRecognizer2.0.ats 、 SeetaPointDetector2.0.pts5.ats  
将三个ats文件放置在SD卡，根目录下的seetaface目录下，可以放在assets资源目录下，然后复制到SD卡，或者去服务器下载
<div align="left">
<img src="https://github.com/shuguoli68/SeetaFace2Demo/blob/master/Screenshots/ats.png">
</div>

+ 二、初始化代码
```
Thread{
            if (SeetaHelper.copyAts(this))//将assets目录中的模型文件拷贝到SD卡
                SeetaHelper.getInstance().init()
        }.start()
```

+ 三、关键代码
```
val seetaImageData = SeetaUtil.ConvertToSeetaImageData(bitmap)//转化SeetaImageData
val seetaRects = SeetaHelper.getInstance().faceDetector2.Detect(seetaImageData)//人脸框的位置
val seetaPoints = SeetaHelper.getInstance().pointDetector2.Detect(seetaImageData, seetaRects[i])//脸部5个特征点
//得出两张人脸的相似度
val sim = SeetaHelper.getInstance().faceRecognizer2.Compare(
                    seetaImageData,
                    seetaPoints,
                    seetaImageData2,
                    seetaPoints2
                )
```
如有需求，也可以重新设置模型文件所在目录，必须初始化前设置。如：
```
//设置模型文件在SD卡的位置
SeetaHelper.ROOT_CACHE = Environment.getExternalStorageDirectory().toString() + File.separator + "seetaface"
//设置assets目录中的模型文件
SeetaHelper.ROOT_ASSETS = "seetaface"
```


> 参考：
>> https://github.com/seetafaceengine/SeetaFace2
>> https://github.com/seetaface/SeetaFaceEngine2/blob/master/example/android/README.md
