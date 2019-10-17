package com.seeta.sdk;


public class FaceRecognizer2
{
    static {
        System.loadLibrary("FaceRecognizerJni");
    }

	public long impl = 0;

	private native void construct(String model);
	public native void dispose();

    public FaceRecognizer2() {
        this("");
    }

	/**
	 * \brief construct an facerecognizer
	 * \param model model path
	 * facerecognizer的构造方法
	 * 参数：模型路径
	 */
	public FaceRecognizer2(String model) {
		this.construct(model);
	}

	protected void finalize() throws Throwable {
        super.finalize();
		this.dispose();
    }

	/**
	 * \brief return similarity of two faces
	 * \param image1 [in] the original image of face1
	 * \param landmarks1 [in] the detected landmarks of face1
	 * \param image2 [in] the original image of face2
	 * \param landmarks2 [in] the detected landmarks of face2
	 * \return return similarity of face1 and face2
	 * \note the detected landmarks  must have number of PointDetector2::LandmarkNum
	 * \note return 0 if failed
	 * 返回两张脸的相似度
	 * 参数image1：face1的原始图像
	 * 参数landmarks1：face1的特征点
	 * 参数image2：face2的原始图像
	 * 参数landmarks2：face2的特征点
	 * 返回face1和face2的相似度
	 * 请注意，检测到的关键点必须具有PointDetector2 :: LandmarkNum的数量
	 * 注意如果失败则返回0
	 */
	public native float Compare(SeetaImageData image1, SeetaPointF[] landmarks1, 
								SeetaImageData image2, SeetaPointF[] landmarks2);

	/**
	 * \brief register faces to database, return the index of registered face
	 * \param image [in] the original image of face
	 * \param landmarks [in] the detected landmarks of face
	 * \return index of registered face
	 * \note the detected landmarks  must have number of PointDetector2::LandmarkNum
	 * \note return -1 if faild.
	 * 向数据库注册面孔，返回已注册面孔的索引
	 * 参数image：人脸的原始图像
	 * 参数landmarks：检测到的面部关键点
	 * 返回已注册面孔的索引
	 * 请注意，检测到的面部关键点必须具有PointDetector2 :: LandmarkNum的数量
	 * 如果失败，请返回-1。
	 */
	public native int Register(SeetaImageData image, SeetaPointF[] landmarks);

	/**
	 * \brief clear all registered face
	 * 清除所有已注册的面孔
	 */
	public native void Clear();

	/**
	 * \brief get the number of registerd faces
	 * \return the number of registerd faces
	 * 获取已注册面孔的数量
	 * 返回已注册面孔的数量
	 */
	public native int MaxRegisterIndex();

	/**
	 * \brief recognize the given face from database, return the most similar face's index
	 * \param image [in] the original image of face
	 * \param landmarks [in] the detected landmarks of face
	 * \param similarity [out] the most similarity , note similarity must be one length array of float
	 * \return the most similar face's index
	 * \note return -1 if faild.
	 * \sa Register
	 * 从数据库中识别出给定的面孔，返回最相似的面孔的索引
	 * 参数image：人脸的原始图像
	 * 参数landmarks：检测到的面部地标中
	 * 参数similarity：最相似值，请注意相似度必须是float的长度为1的数组
	 * 返回最相似的面孔的索引
	 * 如果失败，请返回-1。
	 * sa注册
	 */
	public native int Recognize(SeetaImageData image, SeetaPointF[] landmarks, float[] similarity);

	/**
	 * \brief recognize the given face from database, return each similarity of database
	 * \param image [in] the original image of face
	 * \param landmarks [in] the detected landmarks of face
	 * \return an array of similarity, with length MaxRegisterIndex
	 * \note using index returned by Register to get exact face's similarity
	 * \sa Register
	 * \sa MaxRegisterIndex
	 * 从数据库中识别出给定的面孔，返回数据库的每个相似性
	 * 参数image：人脸的原始图像
	 * 参数landmarks：检测到的面部地标中
	 * 返回长度为MaxRegisterIndex的相似度数组
	 * 注意使用Register返回的索引来获得确切的面孔相似度
	 * sa 注册
	 * sa MaxRegisterIndex
	 */
	public native float[] RecognizeEx(SeetaImageData image, SeetaPointF[] landmarks);

}
