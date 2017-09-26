package com.shiming.imageloader.jnitest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.shiming.imageloader.MyApp;

import iamgeloader.client.ImageLoader;
import iamgeloader.client.listener.IGetBitmapListener;

/**
 * @author shiming
 * @version v1.0 create at 2017/9/26
 * @des
 */
public class JniUtils {

    private final static float DEFAULT_BLUR_FACTOR = 7f;          //模糊因子    越大越模糊

    private final static float DEFAULT_BLUR_RADIUS = 2f;         //模糊半径

    public static native void blurIntArray(int[] pImg, int w, int h, int r);

    static {
        System.loadLibrary("ShimingImageBlur");
    }

    /**
     * 已设置图片的view模糊  可设置其他view的background
     *
     * @param view
     */
    public static void applyBlur(final View view) {
        view.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                view.getViewTreeObserver().removeOnPreDrawListener(this);

                view.buildDrawingCache();

                Bitmap bmp = view.getDrawingCache();

                blur(bmp, view);

                view.destroyDrawingCache();

                return true;
            }
        });
    }

    /**
     * 模糊图片
     *
     * @param blurFactor 模糊因子
     * @param blurRadius 模糊半径
     * @param bitmap     图片
     * @param view       view
     */
    public static void blur(float blurFactor, float blurRadius, Bitmap bitmap, final View view) {
        Bitmap overlay = Bitmap.createBitmap((int) (view.getMeasuredWidth() / blurFactor),
                (int) (view.getMeasuredHeight() / blurFactor), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(overlay);
        canvas.translate(-view.getLeft() / blurFactor, -view.getTop() / blurFactor);
        canvas.scale(1 / blurFactor, 1 / blurFactor);
        Paint paint = new Paint();
        paint.setFlags(Paint.FILTER_BITMAP_FLAG);
        canvas.drawBitmap(bitmap, 0, 0, paint);
        overlay = doBlurJniArray(overlay, (int) blurRadius, true);
        Drawable blurDrawable = new BitmapDrawable(MyApp.getInstance().getApplicationContext()
                .getResources(), overlay);
        if (view instanceof ImageView) {
            ((ImageView) view).setImageDrawable(blurDrawable);
        } else {
            view.setBackgroundDrawable(blurDrawable);
        }

    }

    /**
     * 模糊图片
     *
     * @param bitmap
     * @param view
     */
    public static void blur(Bitmap bitmap, View view) {
        blur(DEFAULT_BLUR_FACTOR, DEFAULT_BLUR_RADIUS, bitmap, view);
    }


    /**
     * 模糊网络图片
     *
     * @param context
     * @param url
     * @param imageView
     */
    public static void loadBlurImg(Context context, String url, final ImageView imageView) {
        ImageLoader.getInstance().getBitmapFromCache(context, url, new IGetBitmapListener() {
            @Override
            public void onBitmap(Bitmap bitmap) {
                imageView.setImageBitmap(bitmap);
                applyBlur(imageView);
            }
        });

    }


    private static Bitmap doBlurJniArray(Bitmap sentBitmap, int radius, boolean canReuseInBitmap) {
        Bitmap bitmap;
        if (canReuseInBitmap) {
            bitmap = sentBitmap;
        } else {
            bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);
        }

        if (radius < 1) {
            return (null);
        }

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int[] pix = new int[w * h];
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);

        JniUtils.blurIntArray(pix, w, h, radius);
        bitmap.setPixels(pix, 0, w, 0, 0, w, h);
        return (bitmap);
    }
}