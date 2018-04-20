package code.shiming.com.imageloader471.tranform;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import java.security.MessageDigest;

/**
 * author： Created by shiming on 2018/4/19 18:11
 * mailbox：lamshiming@sina.com
 */

public class RoundBitmapTranformation extends BitmapTransformation {
    private int radius;

    public RoundBitmapTranformation( int radius) {
        this.radius = radius;
    }
    @Override
    protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
        return roundCrop(pool, toTransform);
    }

    /**
     *Glide 使用 LruBitmapPool 作为默认的 BitmapPool 。
     * LruBitmapPool 是一个内存中的固定大小的 BitmapPool，
     * 使用 LRU 算法清理。默认大小基于设备的分辨率和密度，
     * 同时也考虑内存类和 isLowRamDevice 的返回值。
     * 具体的计算通过 Glide 的 MemorySizeCalculator 来完成，
     * 与 Glide 的 MemoryCache 的大小检测方法相似。
     * @param pool
     * @param toTransform
     * @return
     */
    private Bitmap roundCrop(BitmapPool pool, Bitmap toTransform) {
        int bw = toTransform.getWidth();
        int bh = toTransform.getHeight();


//        int width = toTransform.getWidth(); // 图片的宽
//        int height = toTransform.getHeight(); // 图片的高
//
//        if (width > height) {
//            // 如果图片的宽大于高，则以高为基准，以形状的宽高比重新设置宽度
//            width = (int) (height * (shapeWidth / shapeHeight));
//        } else {
//            // 如果图片的宽小于等于高，则以宽为基准，以形状的宽高比重新设置高度度
//            height = (int) (width * (shapeHeight / shapeWidth));
//        }

        //由于有这个对象，可以这样的获取尺寸，方便对图片的操作，和对垃圾的回收
        Bitmap target = pool.get(bw, bh, Bitmap.Config.ARGB_8888);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setAntiAlias(true);
        RectF f = new RectF(0, 0, bw, bh);
        Canvas canvas = new Canvas(target);
        canvas.drawRoundRect(f, radius, radius, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(toTransform, 0, 0, paint);
        return target;
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {

    }
}
