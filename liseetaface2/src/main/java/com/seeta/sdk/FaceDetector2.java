package com.seeta.sdk;


public class FaceDetector2
{
	static {
		System.loadLibrary("FaceDetectorJni");
	}

	public long impl = 0;

	private native void construct(String model);
	public native void dispose();

	/**
	 * \brief construct an facedetector
	 * \param model model path
	 * facerecognizer的构造方法
	 * 参数：模型路径
	 */
	public FaceDetector2(String model) {
		this.construct(model);
	}


	protected void finalize() throws Throwable {
        super.finalize();
		this.dispose();
    }

	/**
	 * \brief detect faces
	 * \param image [in]
	 * \return an array of rectangle, contains each faces location
	 * \note return 0 length array if no face detected
	 * \note faces were sorted by width * length
	 * 检测面孔
	 * 参数：img
	 * 返回矩形数组，包含每个面的位置
	 * 如果未检测到脸部，则返回0长度数组
	 * 注意：面的位置按宽度*长度排序
	 */
	public native SeetaRect[] Detect(SeetaImageData img);
	
}
