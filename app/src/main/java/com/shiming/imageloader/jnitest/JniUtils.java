package com.shiming.imageloader.jnitest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.shiming.imageloader.MyApp;

import java.util.ArrayList;

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
    //此处命名要和com_shiming_imageloader_jnitest_JniUtils.cpp 中的一样才可以 出错一步没得商量，要不断的检查
    public static native void blurIntArray(int[] pImg, int w, int h, int r);

    /**
     * 参照：MediaPlayer的写法
      static {
      System.loadLibrary("media_jni");
      native_init();
       }
     */
    static {
        /**
         * #编译生成的文件的类库叫什么名字
         LOCAL_MODULE    := ShimingImageBlur
         必须和Android.mk中的一样，生成的so库虽然会加上libShimingImageBlur.so 但是调用得到
         */
        System.loadLibrary("ShimingImageBlur");

    }

    /**
     * 已设置图片的view模糊  可设置其他view的background
     *
     * @param view
     */
    public static void applyBlur(final View view, final boolean flag) {
        // Register a callback to be invoked when the view tree is about to be drawn
        //翻译注册一个回调，view将要drawn，意思还没有drawn上去
        view.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            /**
             * 再绘图前加上
             * @return
             */
            @Override
            public boolean onPreDraw() {
                view.getViewTreeObserver().removeOnPreDrawListener(this);
                //buildDrawingCache(false);
                //如果绘制无效，则强制构建绘图缓存，
                view.buildDrawingCache();

                Bitmap bmp = view.getDrawingCache();
                blur(bmp, view,flag);
                //通常cache会占用一定量的内存，所以必须销毁
                view.destroyDrawingCache();
                //返回 true 继续绘制，返回false取消。 如果返回为false的，页面就不会绘制了
                return true;
            }
        });
    }

    /**
     * 模糊图片
     *  @param blurFactor 模糊因子
     * @param blurRadius 模糊半径
     * @param bitmap     图片
     * @param view       view
     * @param flag 是否需要jni里面的算法
     */
    public static void blur(float blurFactor, float blurRadius, Bitmap bitmap, final View view, boolean flag) {

        Bitmap overlay = Bitmap.createBitmap((int) (view.getMeasuredWidth() / blurFactor),
                (int) (view.getMeasuredHeight() / blurFactor), Bitmap.Config.ARGB_8888);
         overlay.getHeight();
        overlay.getWidth();
        Canvas canvas = new Canvas(overlay);
        //如果我们需要放大1倍，即 scale(2, 2);缩放的中心点默认也是canvas的左上角，所以先要进行坐标平移，才能去缩放
        //平移，将画布的坐标原点向左右方向移动x，向上下方向移动y.canvas的默认位置是在（0,0）
        canvas.translate(-view.getLeft() / blurFactor, -view.getTop() / blurFactor);
        canvas.scale(1 / blurFactor, 1 / blurFactor);
        Paint paint = new Paint();
        //抗锯齿
        paint.setFlags(Paint.FILTER_BITMAP_FLAG);
        canvas.drawBitmap(bitmap, 0, 0, paint);
        //这里的overlay已经包含了信息
        //是否需要jni的计算
        if (flag) {
            overlay = doBlurJniArray(overlay, (int) blurRadius, true);
        }
        //可以简单地理解为 Bitmap 储存的是 像素信息，Drawable 储存的是 对 Canvas 的一系列操作。
        //而 BitmapDrawable 储存的是「把 Bitmap 渲染到 Canvas 上」这个操作。
        BitmapDrawable blurDrawable = new BitmapDrawable(MyApp.getInstance().getApplicationContext()
                .getResources(), overlay);
        if (view instanceof ImageView) {
            ((ImageView)view).setImageDrawable(blurDrawable);
            System.out.println("shiming  blurDrawable" +blurDrawable);
        } else {
            view.setBackgroundDrawable(blurDrawable);
        }
    }

    /**
     * 模糊图片
     * @param bitmap
     * @param view
     * @param flag 是否需要jni来模糊
     */
    public static void blur(Bitmap bitmap, View view,boolean flag) {
        blur(DEFAULT_BLUR_FACTOR, DEFAULT_BLUR_RADIUS, bitmap, view,flag);
    }


    /**
     * 模糊网络图片
     *
     * @param context
     * @param url
     * @param imageView
     */
    public static void loadBlurImg(Context context, String url, final ImageView imageView, final boolean flag) {
        ImageLoader.getInstance().getBitmapFromCache(context, url, new IGetBitmapListener() {
            @Override
            public void onBitmap(Bitmap bitmap) {
                imageView.setImageBitmap(bitmap);
                applyBlur(imageView,flag);
            }
        });
    }

    /**
     *
     * @param sentBitmap
     * @param radius
     * @param canReuseInBitmap
     * @return
     *
    参数 :http://ranlic.iteye.com/blog/1313735
    pixels      接收位图颜色值的数组
    offset      写入到pixels[]中的第一个像素索引值
    stride      pixels[]中的行间距个数值(必须大于等于位图宽度)。可以为负数
    x          　从位图中读取的第一个像素的x坐标值。
    y           从位图中读取的第一个像素的y坐标值
    width    　　从每一行中读取的像素宽度
    height 　　　读取的行数
            异常
    IilegalArgumentExcepiton       如果x，y，width，height越界或stride的绝对值小于位图宽度时将被抛出。
    ArrayIndexOutOfBoundsException          如果像素数组太小而无法接收指定书目的像素值时将被抛出。
    */
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
        //pix数组，所有的关键的逻辑都是这个pix的操作，这里我们去交给了so.库去处理了，所以这里才是关键
        JniUtils.blurIntArray(pix, w, h, radius);
        bitmap.setPixels(pix, 0, w, 0, 0, w, h);
        return (bitmap);
    }

    /**
     *  Copy on write array. This array is not thread safe, and only one loop can
     * iterate over this array at any given time. This class avoids allocations
     * until a concurrent modification happens.
     *
     * 复制一个集合，线程不安全。只能有一个线程能够loop便利，但是这类能够避免并发修改的发生，如果出现了
     * 并发修改的了，马上抛出异常
     */
    public static void test(){
        final CopyOnWriteArray<String> listeners =
                new CopyOnWriteArray<>();
        listeners.add("1");
        listeners.add("2");
        listeners.add("3");
        listeners.add("4");
        listeners.add("5");
        if (listeners != null && listeners.size() > 0) {
            CopyOnWriteArray.Access<String> access = listeners.start();
            try {
                int count = access.size();
                for (int i = 0; i < count; i++) {
                    String s = access.get(i);
                    System.out.println("shiming==="+s);
                }
            } finally {
                listeners.end();
            }
        }
    }

    static class CopyOnWriteArray<T> {
        private ArrayList<T> mData = new ArrayList<T>();
        private ArrayList<T> mDataCopy;

        private final Access<T> mAccess = new Access<T>();

        private boolean mStart;

        static class Access<T> {
            private ArrayList<T> mData;
            private int mSize;

            T get(int index) {
                return mData.get(index);
            }

            int size() {
                return mSize;
            }
        }

        CopyOnWriteArray() {
        }

        private ArrayList<T> getArray() {
            if (mStart) {
                if (mDataCopy == null) mDataCopy = new ArrayList<T>(mData);
                return mDataCopy;
            }
            return mData;
        }

        Access<T> start() {
            if (mStart) throw new IllegalStateException("Iteration already started");
            mStart = true;
            mDataCopy = null;
            mAccess.mData = mData;
            mAccess.mSize = mData.size();
            return mAccess;
        }

        void end() {
            if (!mStart) throw new IllegalStateException("Iteration not started");
            mStart = false;
            if (mDataCopy != null) {
                mData = mDataCopy;
                mAccess.mData.clear();
                mAccess.mSize = 0;
            }
            mDataCopy = null;
        }

        int size() {
            return getArray().size();
        }

        void add(T item) {
            getArray().add(item);
        }

        void addAll(CopyOnWriteArray<T> array) {
            getArray().addAll(array.mData);
        }

        void remove(T item) {
            getArray().remove(item);
        }

        void clear() {
            getArray().clear();
        }
    }
}