package code.shiming.com.imageloader471;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.widget.ImageView;

import com.bumptech.glide.TransitionOptions;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import java.io.File;

import code.shiming.com.imageloader471.listener.IGetBitmapListener;
import code.shiming.com.imageloader471.listener.IGetDrawableListener;
import code.shiming.com.imageloader471.listener.IImageLoaderListener;
import code.shiming.com.imageloader471.listener.ImageSize;
import code.shiming.com.imageloader471.okhttp.OnGlideImageViewListener;
import code.shiming.com.imageloader471.okhttp.OnProgressListener;


/**
 * Created by shiming on 2016/10/26.
 */
public class ImageLoaderV4 implements IImageLoaderClient {
    /**
     * volatile 关键字：我个人理解的是：使用volatile关键字的程序在并发时能够正确执行。
     * 但是它不能够代替synchronized关键字。在网上找到这么一句话：
     * 观察加入volatile关键字和没有加入volatile关键字时所生成的汇编代码发现，
     * 加入volatile关键字时，会多出一个lock前缀指令lock前缀指令实际上相当于一个内存屏障（也成内存栅栏），
     * 内存屏障会提供3个功能：1）它确保指令重排序时不会把其后面的指令排到内存屏障之前的位置，
     * 也不会把前面的指令排到内存屏障的后面；即在执行到内存屏障这句指令时，在它前面的操作已经全部完成；
     * 2）它会强制将对缓存的修改操作立即写入主存；3）如果是写操作，它会导致其他CPU中对应的缓存行无效。
     */
    private volatile static ImageLoaderV4 instance;
    private IImageLoaderClient client;

    private ImageLoaderV4() {
        client = new GlideImageLoaderClient();
    }

    /***
     * 设置 图片加载库客户端
     */

    public void setImageLoaderClient(Context context, IImageLoaderClient client) {
        if (this.client != null) {
            this.client.clearMemoryCache(context);
        }

        if (this.client != client) {
            this.client = client;
            if (this.client != null) {
                this.client.init(context);
            }
        }

    }
    /**
     * 因为每次调用实例都需要判断同步锁，很多项目包括很多人都是用这种的
     * 双重判断校验的方法，这种的方法看似很完美的解决了效率的问题，但是它
     * 在并发量不多，安全性不太高的情况下能完美的运行，但是，
     * 在jvm编译的过程中会出现指令重排的优化过程，这就会导致singleton实际上
     * 没有被初始化，就分配了内存空间，也就是说singleton！=null但是又没有被初始化，
     * 这就会导致返回的singletonthird返回的是不完整的
     * 参考：http://www.360doc.com/content/11/0810/12/1542811_139352888.shtml
     * 建议使用内部类实现单利模式
     */
    public static ImageLoaderV4 getInstance() {
        if (instance == null) {
            synchronized (ImageLoaderV4.class) {
                if (instance == null) {
                    instance = new ImageLoaderV4();
                }
            }
        }
        return instance;
    }

    @Override
    public void init(Context context) {

    }

    @Override
    public void destroy(Context context) {
        if (client != null) {
            client.destroy(context);
            client = null;
        }

        instance = null;
    }

    @Override
    public File getCacheDir(Context context) {
        if (client != null) {
            return client.getCacheDir(context);
        }
        return null;
    }

    @Override
    public void clearMemoryCache(Context context) {
        if (client != null) {
            client.clearMemoryCache(context);
        }
    }

    @Override
    public void clearDiskCache(Context context) {
        if (client != null) {
            client.clearDiskCache(context);
        }
    }

    @Override
    public Bitmap getBitmapFromCache(Context context, String url) {
        if (client != null) {
            return client.getBitmapFromCache(context, url);
        }
        return null;
    }

    /**
     * 不是
     * @param context
     * @param url
     * @param listener
     */
    @Override
    public void getBitmapFromCache(Context context, String url, IGetBitmapListener listener) {
        if (client != null) {
            client.getBitmapFromCache(context, url, listener);
        }
    }

    @Override
    public void displayImage(Context context, int resId, ImageView imageView) {
        if (client != null) {
            client.displayImage(context, resId, imageView);
        }
    }

    @Override
    public void displayImage(Context context, String url, ImageView imageView) {
        if (client != null) {
            client.displayImage(context, url, imageView);
        }
    }

    @Override
    public void displayImage(Context context, String url, ImageView imageView, boolean isCache) {
        if (client != null) {
            client.displayImage(context, url, imageView,isCache);
        }
    }

    @Override
    public void displayImage(Fragment fragment, String url, ImageView imageView) {
        if (client != null) {
            client.displayImage(fragment, url, imageView);
        }
    }

    @Override
    public void displayImage(Context context, String url, ImageView imageView, int defRes) {
        if (client != null) {
            client.displayImage(context, url, imageView, defRes);
        }
    }

    @Override
    public void displayImage(Fragment fragment, String url, ImageView imageView, int defRes) {
        if (client != null) {
            client.displayImage(fragment, url, imageView, defRes);
        }
    }

    @Override
    public void displayImage(Context context, String url, ImageView imageView, int defRes,BitmapTransformation transformations) {
        if (client != null) {
            client.displayImage(context, url, imageView, defRes, transformations);
        }
    }

    @Override
    public void displayImage(Fragment fragment, String url, ImageView imageView, int defRes, BitmapTransformation transformations) {
        if (client != null) {
            client.displayImage(fragment, url, imageView, defRes, transformations);
        }
    }

    @Override
    public void displayImage(Context context, String url, ImageView imageView, int defRes, ImageSize size) {
        if (client != null) {
            client.displayImage(context, url, imageView, defRes, size);
        }
    }

    @Override
    public void displayImage(Fragment fragment, String url, ImageView imageView, int defRes, ImageSize size) {
        if (client != null) {
            client.displayImage(fragment, url, imageView, defRes, size);
        }
    }

    @Override
    public void displayImage(Context context, String url, ImageView imageView, int defRes, boolean cacheInMemory) {
        if (client != null) {
            client.displayImage(context, url, imageView, defRes, cacheInMemory);
        }
    }

    @Override
    public void displayImage(Fragment fragment, String url, ImageView imageView, int defRes, boolean cacheInMemory) {
        if (client != null) {
            client.displayImage(fragment, url, imageView, defRes, cacheInMemory);
        }
    }

    @Override
    public void displayImage(Context context, String url, ImageView imageView, IImageLoaderListener listener) {
        if (client != null) {
            client.displayImage(context, url, imageView, listener);
        }
    }

    @Override
    public void displayImage(Fragment fragment, String url, ImageView imageView, IImageLoaderListener listener) {
        if (client != null) {
            client.displayImage(fragment, url, imageView, listener);
        }
    }

    @Override
    public void displayImage(Context context, String url, ImageView imageView, int defRes, IImageLoaderListener listener) {
        if (client != null) {
            client.displayImage(context, url, imageView, defRes, listener);
        }
    }

    @Override
    public void displayImage(Fragment fragment, String url, ImageView imageView, int defRes, IImageLoaderListener listener) {
        if (client != null) {
            client.displayImage(fragment, url, imageView, defRes, listener);
        }
    }

    @Override
    public void displayCircleImage(Context context, String url, ImageView imageView, int defRes) {
        if (client != null) {
            client.displayCircleImage(context, url, imageView, defRes);
        }
    }

    @Override
    public void displayCircleImage(Fragment fragment, String url, ImageView imageView, int defRes) {
        if (client != null) {
            client.displayCircleImage(fragment, url, imageView, defRes);
        }
    }

    @Override
    public void displayRoundImage(Context context, String url, ImageView imageView, int defRes, int radius) {
        if (client != null) {
            client.displayRoundImage(context, url, imageView, defRes, radius);
        }
    }

    @Override
    public void displayBlurImage(Context context, String url, int blurRadius, final IGetDrawableListener listener) {
        if (client != null) {
            client.displayBlurImage(context, url, blurRadius, listener);
        }
    }

    @Override
    public void displayRoundImage(Fragment fragment, String url, ImageView imageView, int defRes, int radius) {
        if (client != null) {
            client.displayRoundImage(fragment, url, imageView, defRes, radius);
        }
    }

    @Override
    public void displayBlurImage(Context context, String url, ImageView imageView, int defRes, int blurRadius) {
        if (client != null) {
            client.displayBlurImage(context, url, imageView, defRes, blurRadius);
        }
    }

    @Override
    public void displayBlurImage(Context context, int resId, ImageView imageView, int blurRadius) {
        if (client != null) {
            client.displayBlurImage(context, resId, imageView, blurRadius);
        }
    }

    @Override
    public void displayBlurImage(Fragment fragment, String url, ImageView imageView, int defRes, int blurRadius) {
        if (client != null) {
            client.displayBlurImage(fragment, url, imageView, defRes, blurRadius);
        }
    }

    @Override
    public void displayImageInResource(Context context, int resId,  ImageView imageView) {
        if (client != null) {
            client.displayImageInResource(context, resId,  imageView);
        }
    }

    @Override
    public void displayImageInResource(Fragment fragment, int resId,  ImageView imageView) {
        if (client != null) {
            client.displayImageInResource(fragment, resId,  imageView);
        }
    }

    @Override
    public void displayImageInResource(Context context, int resId, ImageView imageView, BitmapTransformation transformations) {
        if (client != null) {
            client.displayImageInResource(context, resId, imageView, transformations);
        }
    }

    @Override
    public void displayImageInResource(Fragment fragment, int resId,  ImageView imageView, BitmapTransformation transformations) {
        if (client != null) {
            client.displayImageInResource(fragment, resId, imageView, transformations);
        }
    }

    @Override
    public void displayImageInResource(Context context, int resId,  ImageView imageView, int defRes) {
        if (client != null) {
            client.displayImageInResource(context, resId, imageView, defRes);
        }
    }

    @Override
    public void displayImageInResource(Fragment fragment, int resId,  ImageView imageView, int defRes) {
        if (client != null) {
            client.displayImageInResource(fragment, resId, imageView, defRes);
        }
    }

    @Override
    public void displayImageInResource(Context context, int resId,  ImageView imageView, int defRes, BitmapTransformation transformations) {
        if (client != null) {
            client.displayImageInResource(context, resId, imageView, defRes, transformations);
        }
    }

    @Override
    public void displayImageInResource(Fragment fragment, int resId,  ImageView imageView, int defRes, BitmapTransformation transformations) {
        if (client != null) {
            client.displayImageInResource(fragment, resId,  imageView, defRes, transformations);
        }
    }

    @Override
    public void displayImageInResourceTransform(Activity activity, int resId, ImageView imageView, Transformation transformation, int errorResId) {
        if (client != null) {
            client.displayImageInResourceTransform(activity, resId,  imageView, transformation, errorResId);
        }
    }

    @Override
    public void displayImageInResourceTransform(Context context, int resId, ImageView imageView, Transformation transformation, int errorResId) {
        if (client != null) {
            client.displayImageInResourceTransform(context, resId,  imageView, transformation, errorResId);
        }
    }

    @Override
    public void displayImageInResourceTransform(Fragment fragment, int resId, ImageView imageView, Transformation transformation, int errorResId) {
        if (client != null) {
            client.displayImageInResourceTransform(fragment, resId,  imageView, transformation, errorResId);
        }
    }

    @Override
    public void displayImageByNet(Context context, String url, ImageView imageView, int defRes, Transformation transformation) {
        if (client != null) {
            client.displayImageByNet(context, url,  imageView, defRes, transformation);
        }
    }

    @Override
    public void displayImageByNet(Fragment fragment, String url, ImageView imageView, int defRes, Transformation transformation) {
        if (client != null) {
            client.displayImageByNet(fragment, url,  imageView, defRes, transformation);
        }
    }

    @Override
    public void displayImageByNet(Activity activity, String url, ImageView imageView, int defRes, Transformation transformation) {
        if (client != null) {
            client.displayImageByNet(activity, url,  imageView, defRes, transformation);
        }
    }
    /**尽管及时取消不必要的加载是很好的实践，但这并不是必须的操作。
     * 实际上，当 Glide.with() 中传入的 Activity 或 Fragment 实例销毁时，
     * Glide 会自动取消加载并回收资源。这里我隐藏了api的调用
     * {@hide}
     */
    @Override
    public void clear(Activity activity, ImageView imageView) {
        if (client != null) {
            client.clear(activity, imageView);
        }
    }
    /**尽管及时取消不必要的加载是很好的实践，但这并不是必须的操作。
     * 实际上，当 Glide.with() 中传入的 Activity 或 Fragment 实例销毁时，
     * Glide 会自动取消加载并回收资源。这里我隐藏了api的调用
     * {@hide}
     */
    @Override
    public void clear(Context context, ImageView imageView) {
        if (client != null) {
            client.clear(context, imageView);
        }
    }
    /**尽管及时取消不必要的加载是很好的实践，但这并不是必须的操作。
     * 实际上，当 Glide.with() 中传入的 Activity 或 Fragment 实例销毁时，
     * Glide 会自动取消加载并回收资源。这里我隐藏了api的调用
     * {@hide}
     */
    @Override
    public void clear(Fragment fragment, ImageView imageView) {
        if (client != null) {
            client.clear(fragment, imageView);
        }
    }

    /**
     * 指定选择哪种缓存的策略
     * @param fragment
     * @param url
     * @param diskCacheStrategy
     * @param imageView
     */
    @Override
    public void displayImageByDiskCacheStrategy(Fragment fragment, String url, DiskCacheStrategy diskCacheStrategy, ImageView imageView) {
        if (client != null) {
            client.displayImageByDiskCacheStrategy(fragment, url,  diskCacheStrategy, imageView);
        }
    }

    @Override
    public void displayImageByDiskCacheStrategy(Activity activity, String url, DiskCacheStrategy diskCacheStrategy, ImageView imageView) {
        if (client != null) {
            client.displayImageByDiskCacheStrategy(activity, url,  diskCacheStrategy, imageView);
        }
    }

    @Override
    public void displayImageByDiskCacheStrategy(Context context, String url, DiskCacheStrategy diskCacheStrategy, ImageView imageView) {
        if (client != null) {
            client.displayImageByDiskCacheStrategy(context, url,  diskCacheStrategy, imageView);
        }
    }

    @Override
    public void disPlayImageOnlyRetrieveFromCache(Fragment fragment, String url, ImageView imageView) {
        if (client != null) {
            client.disPlayImageOnlyRetrieveFromCache(fragment, url, imageView);
        }
    }

    @Override
    public void disPlayImageOnlyRetrieveFromCache(Activity activity, String url, ImageView imageView) {
        if (client != null) {
            client.disPlayImageOnlyRetrieveFromCache(activity, url, imageView);
        }
    }

    @Override
    public void disPlayImageOnlyRetrieveFromCache(Context context, String url, ImageView imageView) {
        if (client != null) {
            client.disPlayImageOnlyRetrieveFromCache(context, url, imageView);
        }
    }

    @Override
    public void disPlayImageSkipMemoryCache(Fragment fragment, String url, ImageView imageView, boolean skipflag, boolean diskCacheStratey) {
        if (client != null) {
            client.disPlayImageSkipMemoryCache(fragment, url, imageView,skipflag,diskCacheStratey);
        }
    }

    @Override
    public void disPlayImageSkipMemoryCache(Activity activity, String url, ImageView imageView, boolean skipflag, boolean diskCacheStratey) {
        if (client != null) {
            client.disPlayImageSkipMemoryCache(activity, url, imageView,skipflag,diskCacheStratey);
        }
    }

    @Override
    public void disPlayImageSkipMemoryCache(Context context, String url, ImageView imageView, boolean skipflag, boolean diskCacheStratey) {
        if (client != null) {
            client.disPlayImageSkipMemoryCache(context, url, imageView,skipflag,diskCacheStratey);
        }
    }

    @Override
    public void disPlayImageErrorReload(Fragment fragment, String url, String fallbackUrl, ImageView imageView) {
        if (client != null) {
            client.disPlayImageErrorReload(fragment, url,fallbackUrl, imageView);
        }
    }

    @Override
    public void disPlayImageErrorReload(Activity activity, String url, String fallbackUrl, ImageView imageView) {
        if (client != null) {
            client.disPlayImageErrorReload(activity, url,fallbackUrl, imageView);
        }
    }

    @Override
    public void disPlayImageErrorReload(Context context, String url, String fallbackUrl, ImageView imageView) {
        if (client != null) {
            client.disPlayImageErrorReload(context, url,fallbackUrl, imageView);
        }
    }

    @Override
    public void disPlayImagedisallowHardwareConfig(Fragment fragment, String url, ImageView imageView) {
        if (client != null) {
            client.disPlayImagedisallowHardwareConfig(fragment, url,imageView);
        }
    }

    @Override
    public void disPlayImagedisallowHardwareConfig(Activity activity, String url, ImageView imageView) {
        if (client != null) {
            client.disPlayImagedisallowHardwareConfig(activity, url,imageView);
        }
    }

    @Override
    public void disPlayImagedisallowHardwareConfig(Context context, String url, ImageView imageView) {
        if (client != null) {
            client.disPlayImagedisallowHardwareConfig(context, url,imageView);
        }
    }

    @Override
    public void disPlayImageProgress(Context context, String url, ImageView imageView, int placeholderResId, int errorResId, OnGlideImageViewListener listener) {
        if (client != null) {
            client.disPlayImageProgress(context, url,imageView,placeholderResId,errorResId,listener);
        }
    }

    @Override
    public void disPlayImageProgress(Activity activity, String url, ImageView imageView, int placeholderResId, int errorResId, OnGlideImageViewListener listener) {
        if (client != null) {
            client.disPlayImageProgress(activity, url,imageView,placeholderResId,errorResId,listener);
        }
    }

    @Override
    public void disPlayImageProgress(Fragment fragment, String url, ImageView imageView, int placeholderResId, int errorResId, OnGlideImageViewListener listener) {
        if (client != null) {
            client.disPlayImageProgress(fragment, url,imageView,placeholderResId,errorResId,listener);
        }
    }

    @Override
    public void disPlayImageProgressByOnProgressListener(Context context, String url, ImageView imageView, int placeholderResId, int errorResId, OnProgressListener onProgressListener) {
        if (client != null) {
            client.disPlayImageProgressByOnProgressListener(context, url,imageView,placeholderResId,errorResId,onProgressListener);
        }
    }

    @Override
    public void disPlayImageProgressByOnProgressListener(Activity activity, String url, ImageView imageView, int placeholderResId, int errorResId, OnProgressListener onProgressListener) {
        if (client != null) {
            client.disPlayImageProgressByOnProgressListener(activity, url,imageView,placeholderResId,errorResId,onProgressListener);
        }
    }

    /**
     * 需要监听 总的字节数，和文件的大小，同时也可以扩展为，加载本地图片
     * @param fragment
     * @param url
     * @param imageView
     * @param placeholderResId
     * @param errorResId
     * @param onProgressListener
     */
    @Override
    public void disPlayImageProgressByOnProgressListener(Fragment fragment, String url, ImageView imageView, int placeholderResId, int errorResId, OnProgressListener onProgressListener) {
        if (client != null) {
            client.disPlayImageProgressByOnProgressListener(fragment, url,imageView,placeholderResId,errorResId,onProgressListener);
        }
    }
    /**
     * 过渡选项
     TransitionOptions 用于决定你的加载完成时会发生什么。
     使用 TransitionOption 可以应用以下变换：
     View淡入
     与占位符交叉淡入
     或者什么都不发生
     从白色 慢慢变透明 5s的间隔
     GlideApp.with(this)
     .load(ur11)
     .transition(withCrossFade(5000)) 可以传入过度的时间
     .into(mImageView_7);
     */

    @Override
    public void displayImageByTransition(Context context, String url, TransitionOptions transitionOptions, ImageView imageView) {
        if (client != null) {
            client.displayImageByTransition(context, url,transitionOptions,imageView);
        }

    }

    @Override
    public void displayImageByTransition(Activity activity, String url, TransitionOptions transitionOptions, ImageView imageView) {
        if (client != null) {
            client.displayImageByTransition(activity, url,transitionOptions,imageView);
        }
    }

    @Override
    public void displayImageByTransition(Fragment fragment, String url, TransitionOptions transitionOptions, ImageView imageView) {
        if (client != null) {
            client.displayImageByTransition(fragment, url,transitionOptions,imageView);
        }
    }

    @Override
    public void glidePauseRequests(Context context) {
        if (client != null) {
            client.glidePauseRequests(context);
        }
    }

    @Override
    public void glidePauseRequests(Activity activity) {
        if (client != null) {
            client.glidePauseRequests(activity);
        }
    }

    @Override
    public void glidePauseRequests(Fragment fragment) {
        if (client != null) {
            client.glidePauseRequests(fragment);
        }
    }

    @Override
    public void glideResumeRequests(Context context) {
        if (client != null) {
            client.glideResumeRequests(context);
        }
    }

    @Override
    public void glideResumeRequests(Activity activity) {
        if (client != null) {
            client.glideResumeRequests(activity);
        }
    }

    @Override
    public void glideResumeRequests(Fragment fragment) {
        if (client != null) {
            client.glideResumeRequests(fragment);
        }
    }

    @Override
    public void displayImageThumbnail(Context context, String url, String backUrl, int thumbnailSize, ImageView imageView) {
        if (client != null) {
            client.displayImageThumbnail(context,url,backUrl,thumbnailSize,imageView);
        }
    }

    @Override
    public void displayImageThumbnail(Activity activity, String url, String backUrl, int thumbnailSize, ImageView imageView) {
        if (client != null) {
            client.displayImageThumbnail(activity,url,backUrl,thumbnailSize,imageView);
        }
    }

    @Override
    public void displayImageThumbnail(Fragment fragment, String url, String backUrl, int thumbnailSize, ImageView imageView) {
        if (client != null) {
            client.displayImageThumbnail(fragment,url,backUrl,thumbnailSize,imageView);
        }
    }

    /**
     * 没有地址也需要指定缩略图
     * @param fragment
     * @param url
     * @param thumbnailSize
     * @param imageView
     */
    @Override
    public void displayImageThumbnail(Fragment fragment, String url, float thumbnailSize, ImageView imageView) {
        if (client != null) {
            client.displayImageThumbnail(fragment,url,thumbnailSize,imageView);
        }
    }

    @Override
    public void displayImageThumbnail(Activity activity, String url, float thumbnailSize, ImageView imageView) {
        if (client != null) {
            client.displayImageThumbnail(activity,url,thumbnailSize,imageView);
        }
    }

    @Override
    public void displayImageThumbnail(Context context, String url, float thumbnailSize, ImageView imageView) {
        if (client != null) {
            client.displayImageThumbnail(context,url,thumbnailSize,imageView);
        }
    }


}
