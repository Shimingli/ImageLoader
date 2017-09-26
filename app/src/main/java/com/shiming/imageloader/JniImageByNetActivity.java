package com.shiming.imageloader;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.shiming.imageloader.jnitest.JniUtils;

/**
 * @author shiming
 * @version v1.0 create at 2017/9/26
 * @des 通过jni实现的高斯模糊图片
 */
public class JniImageByNetActivity extends AppCompatActivity {

    private ImageView mImageView_1;
    private String mUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jni_image);
        findView();
        mUrl = "http://imgsrc.baidu.com/imgad/pic/item/267f9e2f07082838b5168c32b299a9014c08f1f9.jpg";
        initData();

    }

    private void initData() {
        JniUtils.loadBlurImg(this,mUrl,mImageView_1);
    }

    private void findView() {
        mImageView_1 = (ImageView) findViewById(R.id.image_view_1);
    }
}
