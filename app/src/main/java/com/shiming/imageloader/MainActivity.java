package com.shiming.imageloader;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import iamgeloader.client.ImageLoader;
import iamgeloader.client.tranform.BlurBitmapTransformation;
import iamgeloader.client.tranform.CircleBitmapTransformation;
import iamgeloader.client.tranform.RoundBitmapTransformation;

public class MainActivity extends AppCompatActivity {

    private ImageView mImageView_1;
    private ImageView mImageView_2;
    private ImageView mImageView_3;
    private ImageView mImageView_4;
    private ImageView mImageView_5;
    private ImageView mImageView_6;
    private ImageView mImageView_7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findView();
        loadData();
    }

    private void loadData() {
        //圆形图片
        ImageLoader.getInstance().displayCircleImage(this,"http://imgsrc.baidu.com/imgad/pic/item/267f9e2f07082838b5168c32b299a9014c08f1f9.jpg",mImageView_1,R.mipmap.ic_launcher_round);
        //圆角图片
        ImageLoader.getInstance().displayRoundImage(this,"http://imgsrc.baidu.com/imgad/pic/item/267f9e2f07082838b5168c32b299a9014c08f1f9.jpg",mImageView_2,R.mipmap.ic_launcher_round,20);
        //模糊图片
        ImageLoader.getInstance().displayBlurImage(this,"http://imgsrc.baidu.com/imgad/pic/item/267f9e2f07082838b5168c32b299a9014c08f1f9.jpg",mImageView_3,R.mipmap.ic_launcher_round,20);

        //本地图片
        ImageLoader.getInstance().displayImageInResource(this,R.mipmap.test,mImageView_4);
        ImageLoader.getInstance().displayImageInResource(this,R.mipmap.test,mImageView_5,new BlurBitmapTransformation(this,40));
        ImageLoader.getInstance().displayImageInResource(this,R.mipmap.test,mImageView_6,new CircleBitmapTransformation(this));
        ImageLoader.getInstance().displayImageInResource(this,R.mipmap.test,mImageView_6,new RoundBitmapTransformation(this,40));


    }

    private void findView() {
        mImageView_1 = (ImageView) findViewById(R.id.image_view_1);
        mImageView_2 = (ImageView) findViewById(R.id.image_view_2);
        mImageView_3 = (ImageView) findViewById(R.id.image_view_3);
        mImageView_4 = (ImageView) findViewById(R.id.image_view_4);
        mImageView_5 = (ImageView) findViewById(R.id.image_view_5);
        mImageView_6 = (ImageView) findViewById(R.id.image_view_6);
        mImageView_7 = (ImageView) findViewById(R.id.image_view_7);
    }
}
