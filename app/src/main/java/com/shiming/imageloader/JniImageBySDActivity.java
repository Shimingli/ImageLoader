package com.shiming.imageloader;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.widget.ImageView;

import com.shiming.imageloader.jnitest.JniUtils;

/**
 * @author shiming
 * @version v1.0 create at 2017/9/26
 * @des
 */
public class JniImageBySDActivity extends FragmentActivity {

    private ImageView mImageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jni_image_by_sd);
        mImageView = (ImageView) findViewById(R.id.image_view_1);
        mImageView.setImageResource(R.mipmap.banner_me_def_bg);
        JniUtils.applyBlur(mImageView);
    }
}
