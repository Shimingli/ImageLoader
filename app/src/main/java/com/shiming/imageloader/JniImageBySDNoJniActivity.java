package com.shiming.imageloader;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.widget.ImageView;

import com.shiming.imageloader.jnitest.JniUtils;

import iamgeloader.client.ImageLoader;

/**
 * @author shiming
 * @version v1.0 create at 2017/9/27
 * @des  我是没有使用jni里面算法实现的高斯模糊,通过本地获取的图片"

 */
public class JniImageBySDNoJniActivity extends FragmentActivity {

    private ImageView mImageView;
    private ImageView mImageView2;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jni_image_by_sd_no_jni);
        mImageView = (ImageView) findViewById(R.id.image_view_1);
        mImageView2 = (ImageView) findViewById(R.id.image_view_2);
        mImageView.setImageResource(R.mipmap.icon);
        JniUtils.applyBlur(mImageView,false);
        ImageLoader.getInstance().displayImage(this,R.mipmap.icon,mImageView2);

    }
}
