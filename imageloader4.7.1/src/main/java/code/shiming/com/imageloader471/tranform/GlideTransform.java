package code.shiming.com.imageloader471.tranform;


import android.content.Context;
import android.graphics.Bitmap;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.Log;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;

import java.security.MessageDigest;

/**
 * Created by shiming on 2016/10/26.
 */

public class GlideTransform extends com.bumptech.glide.load.resource.bitmap.BitmapTransformation {

    private IBitmapTransformation transformation;

    /**
     *
     * @param context
     * @param transformation 自定义图片装换接口，在构造方法中传入
     */
    public GlideTransform(Context context, IBitmapTransformation transformation) {

        this.transformation = transformation;
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        if (toTransform == null) {
            return null;
        }
        return transformation.transform(toTransform, null);
    }


    public String getId() {
        return getClass().getName() + transformation.hashCode();
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {

    }
}
