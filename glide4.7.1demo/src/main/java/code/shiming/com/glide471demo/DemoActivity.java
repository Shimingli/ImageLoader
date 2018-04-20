package code.shiming.com.glide471demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.load.resource.bitmap.CenterInside;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.FitCenter;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;

import code.shiming.com.imageloader471.GlideApp;
import code.shiming.com.imageloader471.ImageLoaderV4;
import code.shiming.com.imageloader471.tranform.BlurBitmapTranformation;
import code.shiming.com.imageloader471.tranform.GlideCircleTransformation;
import code.shiming.com.imageloader471.tranform.RoundBitmapTranformation;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class DemoActivity extends AppCompatActivity {

    private ImageView mImageView_1;
    private ImageView mImageView_2;
    private ImageView mImageView_3;
    private ImageView mImageView_4;
    private ImageView mImageView_5;
    private ImageView mImageView_6;
    private ImageView mImageView_7;
    private ImageView mImageView_8;

    private ImageView mImageView_9;
    private ImageView mImageView_10;
    private ImageView mImageView_11;
    private ImageView mImageView_12;
    private ImageView mImageView_13;
    private ImageView mImageView_14;
    private ImageView mImageView_15;
    private ImageView mImageView_16;
    private ImageView mImageView_17;
    private ImageView mImageView_18;
    private ImageView mImageView_19;
    private ImageView mImageView_20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        findView();
        String url="https://upload-images.jianshu.io/upload_images/5363507-476c8bb17b124d22.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240";
        //        //圆形图片
        ImageLoaderV4.getInstance().displayCircleImage(this, "http://imgsrc.baidu.com/imgad/pic/item/267f9e2f07082838b5168c32b299a9014c08f1f9.jpg", mImageView_1, R.mipmap.ic_launcher_round);
        //圆角图片
        ImageLoaderV4.getInstance().displayRoundImage(this, "http://imgsrc.baidu.com/imgad/pic/item/267f9e2f07082838b5168c32b299a9014c08f1f9.jpg", mImageView_2, R.mipmap.ic_launcher_round, 40);
        //模糊图片
        ImageLoaderV4.getInstance().displayBlurImage(this, "http://imgsrc.baidu.com/imgad/pic/item/267f9e2f07082838b5168c32b299a9014c08f1f9.jpg", mImageView_3, R.mipmap.ic_launcher_round, 10);

        //本地图片 不做处理
        ImageLoaderV4.getInstance().displayImageInResource(this, R.mipmap.test, mImageView_4);
        // TODO: 2018/4/20  以下的三种不介意使用，需要解决一些问题  start
        //本地图片，模糊处理
        ImageLoaderV4.getInstance().displayImageInResource(this, R.mipmap.test, mImageView_5, new BlurBitmapTranformation( 200));
        //本地图片，裁圆角处理
        ImageLoaderV4.getInstance().displayImageInResource(this, R.mipmap.test, mImageView_6, new GlideCircleTransformation());
        //四周倒角处理
        ImageLoaderV4.getInstance().displayImageInResource(this, R.mipmap.test, mImageView_7, new RoundBitmapTranformation( 40));
        // TODO: 2018/4/20  以上的三种不介意使用，需要解决一些问题  start


        //使用的是另外一种方法，指定传入的那种的方法 ,还可以不断的扩展，不断的扩展，这是Gilded提供的一些操作，牛逼 ，牛逼 这是对本地图片的操作
        ImageLoaderV4.getInstance().displayImageInResourceTransform(this, R.mipmap.test, mImageView_8, new CenterInside(), R.mipmap.test);
        ImageLoaderV4.getInstance().displayImageInResourceTransform(this, R.mipmap.test, mImageView_9, new CircleCrop(), R.mipmap.test);
        ImageLoaderV4.getInstance().displayImageInResourceTransform(this, R.mipmap.test, mImageView_10, new FitCenter(), R.mipmap.test);
        ImageLoaderV4.getInstance().displayImageInResourceTransform(this, R.mipmap.test, mImageView_11, new RoundedCorners(10), R.mipmap.test);

       //对网络图片的操作
        ImageLoaderV4.getInstance().displayImageByNet(this,url,mImageView_12, R.mipmap.test,new CenterInside());
        ImageLoaderV4.getInstance().displayImageByNet(this,url,mImageView_13, R.mipmap.test,new CircleCrop());
        ImageLoaderV4.getInstance().displayImageByNet(this,url,mImageView_14, R.mipmap.test,new FitCenter());
        ImageLoaderV4.getInstance().displayImageByNet(this,url,mImageView_15, R.mipmap.test,new RoundedCorners(10));


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
        mImageView_9 = (ImageView) findViewById(R.id.image_view_9);
        mImageView_10 = (ImageView) findViewById(R.id.image_view_10);
        mImageView_11 = (ImageView) findViewById(R.id.image_view_11);
        mImageView_12 = (ImageView) findViewById(R.id.image_view_12);
        mImageView_13 = (ImageView) findViewById(R.id.image_view_13);
        mImageView_14 = (ImageView) findViewById(R.id.image_view_14);
        mImageView_15 = (ImageView) findViewById(R.id.image_view_15);
        mImageView_16 = (ImageView) findViewById(R.id.image_view_16);
        mImageView_17 = (ImageView) findViewById(R.id.image_view_17);
        mImageView_18 = (ImageView) findViewById(R.id.image_view_18);
        mImageView_19 = (ImageView) findViewById(R.id.image_view_19);
        mImageView_20 = (ImageView) findViewById(R.id.image_view_20);


    }
}
