package iamgeloader.client.listener;

import android.widget.ImageView;

/**
 * Created by shiming on 2016/10/26.
 * 监听图片下载进度
 */
public interface IImageLoaderListenerByCall {

    //监听图片下载错误
    void onLoadingFailed(String url, ImageView target, Exception exception);
   //监听图片加载成功
    void onLoadingComplete(String url, ImageView target);

}
