package com.shiming.imageloader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.widget.ImageView;

import iamgeloader.client.ImageLoader;
import iamgeloader.client.tranform.BlurBitmapTransformationDD;
import iamgeloader.client.tranform.CircleBitmapTransformationDD;
import iamgeloader.client.tranform.RoundBitmapTransformationD;


/**
 * @author shiming
 * @version v1.0 create at 2017/9/26
 * @des  通过imageloader 处理图片
 */
public class ImageLoaderByNetActivity extends FragmentActivity {


    private ImageView mImageView_1;
    private ImageView mImageView_2;
    private ImageView mImageView_3;
    private ImageView mImageView_4;
    private ImageView mImageView_5;
    private ImageView mImageView_6;
    private ImageView mImageView_7;
    private ImageView mImageView_8;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_loader);
        findView();
        loadData();
    }

    private void loadData() {
        //圆形图片
        ImageLoader.getInstance().displayCircleImage(this, "http://imgsrc.baidu.com/imgad/pic/item/267f9e2f07082838b5168c32b299a9014c08f1f9.jpg", mImageView_1, R.mipmap.ic_launcher_round);
        //圆角图片
        ImageLoader.getInstance().displayRoundImage(this, "http://imgsrc.baidu.com/imgad/pic/item/267f9e2f07082838b5168c32b299a9014c08f1f9.jpg", mImageView_2, R.mipmap.ic_launcher_round, 40);
        //模糊图片
        ImageLoader.getInstance().displayBlurImage(this, "http://imgsrc.baidu.com/imgad/pic/item/267f9e2f07082838b5168c32b299a9014c08f1f9.jpg", mImageView_3, R.mipmap.ic_launcher_round, 100);

        //本地图片 不做处理
        ImageLoader.getInstance().displayImageInResource(this, R.mipmap.test, mImageView_4);
        //本地图片，模糊处理
        ImageLoader.getInstance().displayImageInResource(this, R.mipmap.test, mImageView_5, new BlurBitmapTransformationDD(this, 200));
        //本地图片，裁圆角处理
        ImageLoader.getInstance().displayImageInResource(this, R.mipmap.test, mImageView_6, new CircleBitmapTransformationDD(this));
        //四周倒角处理
        ImageLoader.getInstance().displayImageInResource(this, R.mipmap.test, mImageView_7, new RoundBitmapTransformationD(this, 40));
        //通过代码实现裁剪为圆形图片
        drawCicriBitmap();

    }

    private void drawCicriBitmap() {
        Bitmap source = BitmapFactory.decodeResource(getResources(), R.mipmap.icon);
        int size = Math.min(source.getWidth(), source.getHeight());
        int width = (source.getWidth() - size) / 2;
        int height = (source.getHeight() - size) / 2;
        Bitmap target = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(target);
        Paint paint = new Paint();
        //        Call this to create a new shader that will draw with a bitmap.
        BitmapShader shader = new BitmapShader(source, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
        if (width != 0 || height != 0) {
            Matrix matrix = new Matrix();
            matrix.setTranslate(-width, -height);
            shader.setLocalMatrix(matrix);
        }
        paint.setShader(shader);
        paint.setAntiAlias(true);
        float r = size / 2f;
        canvas.drawCircle(r, r, r, paint);
        mImageView_8.setImageBitmap(target);
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