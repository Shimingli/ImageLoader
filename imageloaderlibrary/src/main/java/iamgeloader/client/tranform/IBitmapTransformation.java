package iamgeloader.client.tranform;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;


/**
 * Created by shiming on 2016/10/26.
 * 摒弃掉glide中使用的方法处理图片的方法 ，自定义transform的方法转化bitmap
 */

public interface IBitmapTransformation {
    public Bitmap transform(Bitmap source, ImageView imageView);
    //context 需要被使用的地方持有
    public Context getContext();
}
