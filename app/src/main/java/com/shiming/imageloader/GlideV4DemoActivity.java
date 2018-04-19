package com.shiming.imageloader;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import code.shiming.com.imageloader471.ImageLoaderV4;
import code.shiming.com.imageloader471.tranform.BlurBitmapTransformation;
import code.shiming.com.imageloader471.tranform.CircleBitmapTransformation;
import code.shiming.com.imageloader471.tranform.RoundBitmapTransformation;

public class GlideV4DemoActivity extends AppCompatActivity {
    private ImageView mImageView_1;
    private ImageView mImageView_2;
    private ImageView mImageView_3;
    private ImageView mImageView_4;
    private ImageView mImageView_5;
    private ImageView mImageView_6;
    private ImageView mImageView_7;
    private ImageView mImageView_8;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glide_v4_demo);
        findView();
        //        //圆形图片
        ImageLoaderV4.getInstance().displayCircleImage(this, "http://imgsrc.baidu.com/imgad/pic/item/267f9e2f07082838b5168c32b299a9014c08f1f9.jpg", mImageView_1, R.mipmap.ic_launcher_round);
        //圆角图片
        ImageLoaderV4.getInstance().displayRoundImage(this, "http://imgsrc.baidu.com/imgad/pic/item/267f9e2f07082838b5168c32b299a9014c08f1f9.jpg", mImageView_2, R.mipmap.ic_launcher_round, 40);
        //模糊图片
        ImageLoaderV4.getInstance().displayBlurImage(this, "http://imgsrc.baidu.com/imgad/pic/item/267f9e2f07082838b5168c32b299a9014c08f1f9.jpg", mImageView_3, R.mipmap.ic_launcher_round, 100);

        //本地图片 不做处理
        ImageLoaderV4.getInstance().displayImageInResource(this, R.mipmap.test, mImageView_4);
        //本地图片，模糊处理
        ImageLoaderV4.getInstance().displayImageInResource(this, R.mipmap.test, mImageView_5, new BlurBitmapTransformation(this, 200));
        //本地图片，裁圆角处理
//        ImageLoaderV4.getInstance().displayImageInResource(this, R.mipmap.test, mImageView_6, new CircleBitmapTransformation(this));
        //四周倒角处理
        ImageLoaderV4.getInstance().displayImageInResource(this, R.mipmap.test, mImageView_7, new RoundBitmapTransformation(this, 40));
    }

    private void findView() {
        mImageView_1 = (ImageView) findViewById(R.id.image_view_1);
        mImageView_2 = (ImageView) findViewById(R.id.image_view_2);
        mImageView_3 = (ImageView) findViewById(R.id.image_view_3);
        mImageView_4 = (ImageView) findViewById(R.id.image_view_4);
        mImageView_5 = (ImageView) findViewById(R.id.image_view_5);
        mImageView_6 = (ImageView) findViewById(R.id.image_view_6);
        mImageView_7 = (ImageView) findViewById(R.id.image_view_7);
        mImageView_8 = (ImageView) findViewById(R.id.image_view_8);
    }
}
