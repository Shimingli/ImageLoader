package iamgeloader.client.tranform;


import android.content.Context;
import android.graphics.Bitmap;
import android.os.Looper;
import android.util.Log;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;

/**
 * Created by shiming on 2016/10/26.
 */

public class GlideTransformD extends com.bumptech.glide.load.resource.bitmap.BitmapTransformation {

    private IBitmapTransformationD transformation;

    /**
     *
     * @param context
     * @param transformation 自定义图片装换接口，在构造方法中传入
     */
    public GlideTransformD(Context context, IBitmapTransformationD transformation) {
        super(context);
        this.transformation = transformation;
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        if (toTransform == null) {
            return null;
        }
        Log.d(getClass().getCanonicalName(), "transform in child thread : " + String.valueOf(Looper.getMainLooper() != Looper.myLooper()));
        return transformation.transform(toTransform, null);
    }

    @Override
    public String getId() {
        return getClass().getName() + transformation.hashCode();
    }
}
