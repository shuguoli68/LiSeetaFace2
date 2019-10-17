package com.seeta.sdk;

public class PointDetector2
{
	static {
		System.loadLibrary("PointDetectorJni");
	}
	public long impl = 0;

	private native void construct(String model);
	public native void dispose();

	/**
	 * \brief construct an pointdetector
	 * \param model model path
	 * pointdetector的构造方法
	 * 参数：模型路径
	 */
	public PointDetector2(String model) {
		this.construct(model);
	}

	protected void finalize() throws Throwable {
        super.finalize();
		this.dispose();
    }

	/**
	 * \brief get number of landmarks can be detected
	 * \return number of landmarks can be detected
	 * 获取可检测到的面部关键点
	 * 返回可以检测到面部关键点的数量
	 */
	public native int LandmarkNum();

	/**
	 * \brief detect points on face
	 * \param image [in] the orignal image
	 * \param face [in] detected face location
	 * \return an array of points, contains each point location, with length of LandmarkNum
	 * \sa LandmarkNum
	 * 检测面部关键点
	 * 参数image：原始图像
	 * 参数face：检测到的脸部位置
	 * 返回关键点的数组，包含每个关键点的位置，其长度为LandmarkNum
	 * sa LandmarkNum
	 */
	public native SeetaPointF[] Detect(SeetaImageData image, SeetaRect face);

}
