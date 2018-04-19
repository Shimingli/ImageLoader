package com.shiming.compresspic;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.media.ExifInterface;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * 图片处理工具类
 * Created by DDY-03 on 2017/11/17.
 * @version V1.0.0
 */

public class BitmapUtil {
    //默认压缩率
    private final static int DEFAULT_QUALITY = 95;

    /**
     * JNI基本压缩
     * @param bit bitmap对象
     * @param fileName 指定保存目录名
     * @param optimize 是否采用哈弗曼表数据计算 品质相差5-10倍
     * @author XiaoSai
     * @since  2016年3月23日 下午6:32:49
     */
    public static String compressBitmap(Bitmap bit, String fileName, boolean optimize) {
        return CompressCore.saveBitmap(bit, DEFAULT_QUALITY, fileName, optimize);
    }

    /**
     * 通过JNI图片压缩把Bitmap保存到指定目录
     * @param image bitmap对象
     * @param filePath 要保存的指定目录
     * @param maxSize 最大图片大小 150KB
     * @author XiaoSai
     * @since  2016年3月23日 下午6:28:15
     */
    public static String compressBitmap(Bitmap image, String filePath, int maxSize) {
        // 获取尺寸压缩倍数
        int ratio = getRatioSize(image.getWidth(),image.getHeight());
        // 压缩Bitmap到对应尺寸
        Bitmap result = Bitmap.createBitmap(image.getWidth() / ratio,image.getHeight() / ratio, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        Rect rect = new Rect(0, 0, image.getWidth() / ratio, image.getHeight() / ratio);
        canvas.drawBitmap(image,null,rect,null);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        result.compress(Bitmap.CompressFormat.JPEG, options, baos);
        // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
        while (baos.toByteArray().length / 1024 > maxSize) {
            // 重置baos即清空baos
            baos.reset();
            // 每次都减少10
            options -= 10;
            // 这里压缩options%，把压缩后的数据存放到baos中
            result.compress(Bitmap.CompressFormat.JPEG, options, baos);
        }
        // JNI保存图片到SD卡 这个关键
        String resultStr = CompressCore.saveBitmap(result, options, filePath, true);
        // 释放Bitmap
        if (!result.isRecycled()) {
            result.recycle();
        }
        return resultStr;
    }

    /**
     * 通过JNI图片压缩把Bitmap保存到指定目录
     * @param curFilePath 当前图片文件地址
     * @param targetFilePath 要保存的图片文件地址
     * @param maxSize 最大图片大小
     * @author XiaoSai
     * @since  2016年9月28日 下午17:43:15
     */
    public static String compressBitmap(String curFilePath, String targetFilePath, int maxSize) {
        //根据地址获取bitmap
        Bitmap result = getBitmapFromFile(curFilePath);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int quality = 100;
        result.compress(Bitmap.CompressFormat.JPEG, quality, baos);
        // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
        while (baos.toByteArray().length / 1024 > maxSize) {
            // 重置baos即清空baos
            baos.reset();
            // 每次都减少10
            quality -= 10;
            // 这里压缩quality，把压缩后的数据存放到baos中
            result.compress(Bitmap.CompressFormat.JPEG, quality, baos);
        }
        // JNI保存图片到SD卡 这个关键
        String resultStr = CompressCore.saveBitmap(result, quality, targetFilePath, true);
        // 释放Bitmap
        if (!result.isRecycled()) {
            result.recycle();
        }
        return resultStr;
    }

    /**
     * 计算缩放比
     * @param bitWidth 当前图片宽度
     * @param bitHeight 当前图片高度
     * @return int 缩放比
     * @author XiaoSai
     * @since  2016年3月21日 下午3:03:38
     */
    private static int getRatioSize(int bitWidth, int bitHeight) {
        // 图片最大分辨率
        int imageHeight = 1280;
        int imageWidth = 960;
        // 缩放比
        int ratio = 1;
        // 缩放比,由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        if (bitWidth > bitHeight && bitWidth > imageWidth) {
            // 如果图片宽度比高度大,以宽度为基准
            ratio = bitWidth / imageWidth;
        } else if (bitWidth < bitHeight && bitHeight > imageHeight) {
            // 如果图片高度比宽度大，以高度为基准
            ratio = bitHeight / imageHeight;
        }
        // 最小比率为1
        if (ratio <= 0)
            ratio = 1;
        return ratio;
    }

    /**
     * 通过文件路径读获取Bitmap防止OOM以及解决图片旋转问题
     * @param filePath 路径
     * @return bitmap
     */
    private static Bitmap getBitmapFromFile(String filePath){
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = true;//只读边,不读内容
        BitmapFactory.decodeFile(filePath, newOpts);
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        // 获取尺寸压缩倍数
        newOpts.inSampleSize = getRatioSize(w,h);
        newOpts.inJustDecodeBounds = false;//读取所有内容
        newOpts.inDither = false;
        newOpts.inPurgeable=true;
        newOpts.inInputShareable=true;
        newOpts.inTempStorage = new byte[32 * 1024];
        Bitmap bitmap = null;
        File file = new File(filePath);
        FileInputStream fs = null;
        try {
            fs = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            if(fs!=null){
                bitmap = BitmapFactory.decodeFileDescriptor(fs.getFD(),null,newOpts);
                //旋转图片
                int photoDegree = readPictureDegree(filePath);
                if(photoDegree != 0){
                    Matrix matrix = new Matrix();
                    matrix.postRotate(photoDegree);
                    // 创建新的图片
                    bitmap = Bitmap.createBitmap(bitmap, 0, 0,
                            bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            if(fs!=null) {
                try {
                    fs.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bitmap;
    }

    /**
     * 读取图片属性：旋转的角度
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */

    private static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }
}
