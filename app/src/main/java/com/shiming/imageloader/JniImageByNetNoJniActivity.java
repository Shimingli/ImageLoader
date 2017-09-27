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
 * @des 通过网络获取图片没有使用到jni里面的算法
 */
public class JniImageByNetNoJniActivity extends FragmentActivity{

    private ImageView mImageView_1;
    private ImageView mImageView_2;
    private String mUrl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jni_image_by_net_no_jni);
        findView();
        mUrl = "http://imgsrc.baidu.com/imgad/pic/item/267f9e2f07082838b5168c32b299a9014c08f1f9.jpg";
        initData();
        JniUtils.test();
    }

    private void initData() {
        JniUtils.loadBlurImg(this,mUrl,mImageView_1,false);
        ImageLoader.getInstance().displayImage(this,mUrl,mImageView_2);
    }

    private void findView() {
        mImageView_1 = (ImageView) findViewById(R.id.image_view_1);
        mImageView_2 = (ImageView) findViewById(R.id.image_view_2);
    }
}
