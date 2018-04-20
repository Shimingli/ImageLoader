package code.shiming.com.glide471demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import code.shiming.com.imageloader471.GlideApp;
import code.shiming.com.imageloader471.ImageLoaderV4;
import code.shiming.com.imageloader471.okhttp.OnGlideImageViewListener;
import code.shiming.com.imageloader471.tranform.BlurBitmapTranformation;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class TransitionOptionsActivity extends AppCompatActivity {
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
    private TextView mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transition_options);
        findView();

        String url="https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=1243170106,2389135470&fm=27&gp=0.jpg";
        String ur11="https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1524215494107&di=d7649711ff443126d049915204cc2415&imgtype=0&src=http%3A%2F%2Fhimg2.huanqiu.com%2Fattachment2010%2F2017%2F0303%2F20170303101721677.jpg";


        GlideApp.with(this)
                .load(ur11)
                .transition(withCrossFade())
                .into(mImageView_1);

        GlideApp.with(this)
                .load(ur11)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(mImageView_6);
        /**
         * 过渡选项
         TransitionOptions 用于决定你的加载完成时会发生什么。
         使用 TransitionOption 可以应用以下变换：
         View淡入
         与占位符交叉淡入
         或者什么都不发生
         从白色 慢慢变透明 5s的间隔
         */
        GlideApp.with(this)
                .load(ur11)
                .transition(withCrossFade(5000))
                .into(mImageView_7);
        /**
         * 加载缩略图 (Thumbnail) 请求
         * 只要你的 thumbnailUrl 指向的图片比你的主 url 的分辨率更低，它将会很好地工作
         */
        GlideApp.with(this)
                .load(url)
                .thumbnail(Glide.with(this)
                        .load(ur11))
                .into(mImageView_3);

        //如果你仅仅想加载一个本地图像，或者你只有一个单独的远程 URL， 你仍然可以从缩略图 API 受益。
        // API 对本地和远程图片都适用，尤其是当低分辨率缩略图存在于 Glide 的磁盘缓存时，它们将很快被加载出来
        //
        String urlnoData="https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1524215871555&di=fcd8f0e14b57279d697ab7078ac36a88&imgtype=0&src=http%3A%2F%2Fimg5.duitang.com%2Fuploads%2Fitem%2F201610%2F11%2F20161011180349_tzeQC.jpeg";
        int thumbnailSize = 10;//越小，图片越小，低网络的情况，图片越小
        GlideApp.with(this)
                .load(urlnoData)
                .diskCacheStrategy(DiskCacheStrategy.NONE)//为了测试不缓存
                .thumbnail(GlideApp.with(this)
                        .load(urlnoData)
                        .override(thumbnailSize))// API 来强制 Glide 在缩略图请求中加载一个低分辨率图像
                .into(mImageView_4);
        /**
         * thumbnail 方法有一个简化版本，它只需要一个 sizeMultiplier 参数。
         * 如果你只是想为你的加载相同的图片，但尺寸为 View 或 Target 的某个百分比的话特别有用：
         */
        GlideApp.with(this)
                .load(urlnoData)
                .thumbnail(/*sizeMultiplier=*/ 0.25f)
                .into(mImageView_5);
        //以在主请求失败时开始一次新的加载。例如，在请求 primaryUrl 失败后加载 fallbackUrl：
//        Glide.with(this)
//                .load(primaryUrl)
//                .error(Glide.with(fragment)
//                        .load(fallbackUrl))
//                .into(imageView);

//        GlideApp.with(this)
//                .load(url)
//                .apply(option(MyCustomModelLoader.TIMEOUT_MS, 1000L))
//                .into(mImageView_6);
//
//        RequestOptions options = new RequestOptions()
//                .set(MyCustomModelLoader.TIMEOUT_MS, 1000L);

//        Glide.with(this)
//                .applyDefaultRequestOptions(
//                        new RequestOptions()
//                                .format(DecodeFormat.PREFER_ARGB_8888)
//                                .disallowHardwareBitmaps());

        ImageLoaderV4.getInstance().displayImageInResource(this, R.mipmap.test, mImageView_1, new BlurBitmapTranformation( 200));

        GlideApp.with(this).load( R.mipmap.test).disallowHardwareConfig().diskCacheStrategy(DiskCacheStrategy.NONE).transform(new BlurBitmapTranformation( 200)).into(mImageView_2);




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

        mProgress = findViewById(R.id.tv_progress);


    }
}
