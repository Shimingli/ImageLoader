package code.shiming.com.imageloader471.listener;

import android.graphics.drawable.Drawable;

/**
 * Created by shiming on 2016/10/26.
 * 设置此皆苦是为了业务需要，一般不需要关心网络请求回来的drawable，但是业务需要切换的地方的话，需要拿到网络请求回来的drawable
 */

public interface IGetDrawableListener {
    void onDrawable(Drawable drawable);
}
