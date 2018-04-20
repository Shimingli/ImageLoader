package com.shiming.imageloader;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.widget.ImageView;

import com.shiming.imageloader.jnitest.JniUtils;

import iamgeloader.client.ImageLoader;


/**
 * @author shiming
 * @version v1.0 create at 2017/9/26
 * @des  获取本地了图片，使用了jni里面的算法
 */
public class JniImageBySDAndJniActivity extends FragmentActivity {

    private ImageView mImageView;
    private ImageView mImageView2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jni_image_by_sd);
        mImageView = (ImageView) findViewById(R.id.image_view_1);
        mImageView2 = (ImageView) findViewById(R.id.image_view_2);
        mImageView.setImageResource(R.mipmap.icon);
        JniUtils.applyBlur(mImageView,true);
        ImageLoader.getInstance().displayImage(this,R.mipmap.icon,mImageView2);

    }
}
