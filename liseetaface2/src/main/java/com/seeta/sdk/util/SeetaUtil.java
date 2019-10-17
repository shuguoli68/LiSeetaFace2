package com.seeta.sdk.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;

import com.seeta.sdk.SeetaImageData;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class SeetaUtil {

    /**
     *
     * @param context 上下文对象
     * @param assPath assets目录中要拷贝的文件名
     * @param sdPath 放到SD卡中的文件名
     */
    public static void copyAssetsToDst(Context context, String assPath, String sdPath) {
        try {
            String fileNames[] = context.getAssets().list(assPath);
            if (fileNames.length > 0) {
                File file = new File(/*Environment.getExternalStorageDirectory(), */sdPath);
                if (!file.exists()) file.mkdirs();
                for (String fileName : fileNames) {
                    if (!assPath.equals("")) { // assets 文件夹下的目录
                        copyAssetsToDst(context, assPath + File.separator + fileName, sdPath + File.separator + fileName);
                    } else { // assets 文件夹
                        copyAssetsToDst(context, fileName, sdPath + File.separator + fileName);
                    }
                }
            } else {
                File outFile = new File(/*Environment.getExternalStorageDirectory(), */sdPath);
                InputStream is = context.getAssets().open(assPath);
                FileOutputStream fos = new FileOutputStream(outFile);
                byte[] buffer = new byte[1024];
                int byteCount;
                while ((byteCount = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, byteCount);
                }
                fos.flush();
                is.close();
                fos.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            context = null;
        }
    }

    /**
     * 转换生成SeetaImageData
     * @param bitmap
     * @return
     */
    public static SeetaImageData ConvertToSeetaImageData(Bitmap bitmap) {
        Bitmap bmp_src = bitmap.copy(Bitmap.Config.ARGB_8888, true); // true is RGBA
        //SeetaImageData大小与原图像一致，但是通道数为3个通道即BGR
        SeetaImageData imageData = new SeetaImageData(bmp_src.getWidth(), bmp_src.getHeight(), 3);
        imageData.data = getPixelsBGR(bmp_src);
        return imageData;
    }

    /**
     * 提取图像中的BGR像素
     * @param image
     * @return
     */
    public static byte[] getPixelsBGR(Bitmap image) {
        // calculate how many bytes our image consists of
        int bytes = image.getByteCount();

        ByteBuffer buffer = ByteBuffer.allocate(bytes); // Create a new buffer
        image.copyPixelsToBuffer(buffer); // Move the byte data to the buffer

        byte[] temp = buffer.array(); // Get the underlying array containing the data.

        byte[] pixels = new byte[(temp.length/4) * 3]; // Allocate for BGR

        // Copy pixels into place
        for (int i = 0; i < temp.length/4; i++) {

            pixels[i * 3] = temp[i * 4 + 2];        //B
            pixels[i * 3 + 1] = temp[i * 4 + 1];    //G
            pixels[i * 3 + 2] = temp[i * 4 ];       //R

        }

        return pixels;
    }
}
