package code.shiming.com.imageloader471;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;


import java.io.File;

import code.shiming.com.imageloader471.listener.IGetBitmapListener;
import code.shiming.com.imageloader471.listener.IGetDrawableListener;
import code.shiming.com.imageloader471.listener.IImageLoaderListener;
import code.shiming.com.imageloader471.listener.ImageSize;
import code.shiming.com.imageloader471.okhttp.OnGlideImageViewListener;
import code.shiming.com.imageloader471.okhttp.OnProgressListener;
import code.shiming.com.imageloader471.okhttp.ProgressManager;
import code.shiming.com.imageloader471.tranform.BlurBitmapTranformation;
import code.shiming.com.imageloader471.tranform.GlideCircleTransformation;
import code.shiming.com.imageloader471.tranform.RoundBitmapTranformation;


/**
 * Created by shiming on 2016/10/26.
 * des:
 * with(Context context). 使用Application上下文，Glide请求将不受Activity/Fragment生命周期控制。
   with(Activity activity).使用Activity作为上下文，Glide的请求会受到Activity生命周期控制。
   with(FragmentActivity activity).Glide的请求会受到FragmentActivity生命周期控制。
   with(android.app.Fragment fragment).Glide的请求会受到Fragment 生命周期控制。
   with(android.support.v4.app.Fragment fragment).Glide的请求会受到Fragment生命周期控制。
 */

public class GlideImageLoaderClient implements IImageLoaderClient {

    private static final String TAG ="GlideImageLoaderClient";

    @Override
    public void init(Context context) {
    }
    @Override
    public void destroy(Context context) {
        clearMemoryCache(context);
    }
    @Override
    public File getCacheDir(Context context) {
        return Glide.getPhotoCacheDir(context);
    }

    /**
     * 使用ui线程
     * @param context
     */
    @UiThread
    @Override
    public void clearMemoryCache(Context context) {
        GlideApp.get(context).clearMemory();
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public void clearDiskCache(final Context context) {
        new AsyncTask<Void, Void, Void> (){
            @Override
            protected Void doInBackground(Void... params) {
                //必须在子线程中  This method must be called on a background thread.
                Glide.get(context).clearDiskCache();
                return null;
            }
        };
    }

    @Override
    public Bitmap getBitmapFromCache(Context context, String url) {
        throw new UnsupportedOperationException("glide 不支持同步 获取缓存中 bitmap");
    }
    /**
     * 获取缓存中的图片
     * @param context
     * @param url
     * @param listener
     */
    @Override
    public void getBitmapFromCache(Context context, String url, final IGetBitmapListener listener) {
       GlideApp.with(context).asBitmap().load(url).into(new SimpleTarget<Bitmap>() {
           @Override
           public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
               if (listener != null) {
                   listener.onBitmap(resource);
               }
           }
       });
    }

    /**
     *
     默认的策略是DiskCacheStrategy.AUTOMATIC
     DiskCacheStrategy.ALL 使用DATA和RESOURCE缓存远程数据，仅使用RESOURCE来缓存本地数据。
     DiskCacheStrategy.NONE 不使用磁盘缓存
     DiskCacheStrategy.DATA 在资源解码前就将原始数据写入磁盘缓存
     DiskCacheStrategy.RESOURCE 在资源解码后将数据写入磁盘缓存，即经过缩放等转换后的图片资源。
     DiskCacheStrategy.AUTOMATIC 根据原始图片数据和资源编码策略来自动选择磁盘缓存策略。
     * @param context  上下文
     * @param resId  id
     * @param imageView into
      */
    //DiskCacheStrategy.SOURCE：缓存原始数据 DiskCacheStrategy.DATA对应Glide 3中的DiskCacheStrategy.SOURCE
    @Override
    public void displayImage(Context context, int resId, ImageView imageView) {
        //设置缓存策略缓存原始数据  Saves just the original data to cache
        GlideApp.with(context).load(resId).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).into(imageView);
    }

    /**
     *
     * @param context
     * @param url url
     * @param imageView in
     */
    @Override
    public void displayImage(Context context, String url, ImageView imageView) {
        GlideApp.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).into(imageView);
    }

    /**
     *
     * @param context
     * @param url
     * @param imageView
     * @param isCache 是否是缓存 如果是：缓存策略缓存原始数据  不是的话 ：缓存策略DiskCacheStrategy.NONE：什么都不缓存
     */
    @Override
    public void displayImage(Context context, String url, ImageView imageView, boolean isCache) {
        GlideApp.with(context).load(url).skipMemoryCache(isCache).diskCacheStrategy(isCache ? DiskCacheStrategy.AUTOMATIC : DiskCacheStrategy.NONE).into(imageView);
    }

    /**
     *
     * @param fragment 绑定生命周期
     * @param url
     * @param imageView
     */
    @Override
    public void displayImage(Fragment fragment, String url, ImageView imageView) {
        GlideApp.with(fragment).load(url).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).into(imageView);
    }

    /**
     * 使用.placeholder()方法在某些情况下会导致图片显示的时候出现图片变形的情况
     * 这是因为Glide默认开启的crossFade动画导致的TransitionDrawable绘制异常
     * @param context
     * @param url
     * @param imageView
     * @param defRes defRes 可以是个new ColorDrawable(Color.BLACK) 也可以是张图片
     */
    //默认为200  时间有点长  ，工程中要修改下，设置一个加载失败和加载中的动画过渡，V4.0的使用的方法
    @Override
    public void displayImage(Context context, String url, ImageView imageView, int defRes) {
        GlideApp.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).transition(new DrawableTransitionOptions().crossFade(200)).placeholder(defRes).error(defRes).into(imageView);
    }

    /**
     * 默认时间为200 需
     * @param fragment
     * @param url
     * @param imageView
     * @param defRes
     */
    @Override
    public void displayImage(Fragment fragment, String url, ImageView imageView, int defRes) {
        GlideApp.with(fragment).load(url).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).transition(new DrawableTransitionOptions().crossFade(200)).placeholder(defRes).error(defRes).into(imageView);
    }

    /**
     *
     * @param context
     * @param url
     * @param imageView
     * @param defRes
     * @param transformations bitmapTransform 方法设置图片转换
     */

    @Override
    public void displayImage(Context context, String url, ImageView imageView, int defRes, final BitmapTransformation transformations) {
        GlideApp.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).apply(requestOptionsTransformation(defRes,defRes,transformations)).into(imageView);
    }

    @Override
    public void displayImage(Fragment fragment, String url, ImageView imageView, int defRes, BitmapTransformation transformations) {
        GlideApp.with(fragment).load(url).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).apply(requestOptionsTransformation(defRes,defRes,transformations)).into(imageView);
    }


    /**
     * 加载原圆形图片
     * @param context
     * @param url
     * @param imageView
     * @param defRes
     */
    public void displayImageCircle(Context context, String url, ImageView imageView, int defRes) {
        GlideApp.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).apply(circleRequestOptions(defRes,defRes)).into(imageView);

    }
    public void displayImageCircle(Fragment context, String url, ImageView imageView, int defRes) {
        GlideApp.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.DATA).apply(circleRequestOptions(defRes,defRes)).into(imageView);
    }
    public RequestOptions requestOptions(int placeholderResId, int errorResId) {
        return new RequestOptions()
                .placeholder(placeholderResId)
                .error(errorResId);
    }
    public RequestOptions requestOptionsTransformation(int placeholderResId, int errorResId,BitmapTransformation bitmapTransformation) {
        return requestOptions(placeholderResId,errorResId)
                .transform(bitmapTransformation);
    }
    /**
     * 加载原图
     * @param placeholderResId
     * @param errorResId
     * @return
     */
    public RequestOptions circleRequestOptions(int placeholderResId, int errorResId) {
        return requestOptions(placeholderResId, errorResId)
                .transform(new GlideCircleTransformation());
    }

    public RequestOptions roundRequestOptions(int placeholderResId, int errorResId,int radius) {
        return requestOptions(placeholderResId, errorResId)
                .transform(new RoundBitmapTranformation(radius));
    }
    /**
     *
     * @param context
     * @param url
     * @param imageView
     * @param defRes placeholder(int resourceId). 设置资源加载过程中的占位Drawable  error(int resourceId).设置load失败时显示的Drawable
     * @param size override(int width, int height). 重新设置Target的宽高值
     */
    @Override
    public void displayImage(Context context, String url, ImageView imageView, int defRes, ImageSize size) {
        GlideApp.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).placeholder(defRes).error(defRes).override(size.getWidth(), size.getHeight()).into(imageView);
    }

    @Override
    public void displayImage(Fragment fragment, String url, ImageView imageView, int defRes, ImageSize size) {
        GlideApp.with(fragment).load(url).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).placeholder(defRes).error(defRes).override(size.getWidth(), size.getHeight()).into(imageView);
    }

    /**
     * .skipMemoryCache( true )去特意告诉Glide跳过内存缓存  是否跳过内存，还是不跳过
     * @param context
     * @param url
     * @param imageView
     * @param defRes
     * @param cacheInMemory
     */
    @Override
    public void displayImage(Context context, String url, ImageView imageView, int defRes, boolean cacheInMemory) {
        GlideApp.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).placeholder(defRes).error(defRes).skipMemoryCache(cacheInMemory).into(imageView);
    }

    @Override
    public void displayImage(Fragment fragment, String url, ImageView imageView, int defRes, boolean cacheInMemory) {
        GlideApp.with(fragment).load(url).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).placeholder(defRes).error(defRes).skipMemoryCache(cacheInMemory).into(imageView);
    }

    /**
     * 只在需要的地方进行监听 listener 通过自定义的接口回调参数
     * @param context
     * @param url
     * @param imageView
     * @param listener  监听资源加载的请求状态 但不要每次请求都使用新的监听器，要避免不必要的内存申请，
     *                  可以使用单例进行统一的异常监听和处理
     */
    @Override
    public void displayImage(Context context, final String url, final ImageView imageView, final IImageLoaderListener listener) {
        GlideApp.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                listener.onLoadingFailed(url, imageView, e);
                Log.e(TAG, "Load failed", e);//如果关系的话，关系如何失败
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                listener.onLoadingComplete(url, imageView);
                return false;
            }
        }).into(imageView);
    }

    @Override
    public void displayImage(Fragment fragment, final String url, final ImageView imageView, final IImageLoaderListener listener) {
        GlideApp.with(fragment).load(url).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                listener.onLoadingFailed(url, imageView, e);
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                listener.onLoadingComplete(url, imageView);
                return false;
            }
        }).into(imageView);
    }

    @Override
    public void displayImage(Context context, final String url, final ImageView imageView, int defRes, final IImageLoaderListener listener) {
        GlideApp.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).placeholder(defRes).error(defRes).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                listener.onLoadingFailed(url, imageView, e);
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                listener.onLoadingComplete(url, imageView);
                return false;
            }
        }).into(imageView);
    }

    @Override
    public void displayImage(Fragment fragment, final String url, final ImageView imageView, int defRes, final IImageLoaderListener listener) {
        GlideApp.with(fragment).load(url).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                listener.onLoadingFailed(url, imageView, e);
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                listener.onLoadingComplete(url, imageView);
                return false;
            }
        }).into(imageView);
    }

    /**
     * 圆形图片的裁剪
     * @param context
     * @param url
     * @param imageView
     * @param defRes
     */
    @Override
    public void displayCircleImage(Context context, String url, ImageView imageView, int defRes) {
        GlideApp.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).apply(circleRequestOptions(defRes,defRes)).into(imageView);
    }

    @Override
    public void displayCircleImage(Fragment fragment, String url, ImageView imageView, int defRes) {
        GlideApp.with(fragment).load(url).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).apply(circleRequestOptions(defRes,defRes)).into(imageView);
    }


    public void displayCircleImage(Activity fragment, String url, ImageView imageView, int defRes) {
        GlideApp.with(fragment).load(url).diskCacheStrategy(DiskCacheStrategy.DATA).apply(circleRequestOptions(defRes,defRes)).into(imageView);
    }
    /**
     *
     * @param context
     * @param url
     * @param imageView
     * @param defRes
     * @param radius 倒圆角的图片 需要传入需要radius  越大的话，倒角越明显
     */
    @Override
    public void displayRoundImage(Context context, String url, ImageView imageView, int defRes, int radius) {
        GlideApp.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).apply(roundRequestOptions(defRes,defRes,radius)).into(imageView);
    }

    @Override
    public void displayRoundImage(Fragment fragment, String url, ImageView imageView, int defRes, int radius) {
        GlideApp.with(fragment).load(url).diskCacheStrategy(DiskCacheStrategy.DATA).apply(roundRequestOptions(defRes,defRes,radius)).into(imageView);
    }
    /**
     *
     * @param context
     * @param url
     * @param blurRadius 模糊的程度 ，数字越大越模糊
     * @param listener 接口回调需要拿到drawable
     */
    @Override
    public void displayBlurImage(Context context, String url, int blurRadius, final IGetDrawableListener listener) {
        GlideApp.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                if ( listener != null ) {
                    listener.onDrawable( resource );
                }
            }
        });
    }

    /**
     * 不需要关系此模糊图的drawable
     * @param context
     * @param url
     * @param imageView
     * @param defRes
     * @param blurRadius
     */
    @Override
    public void displayBlurImage(Context context, String url, ImageView imageView, int defRes, int blurRadius) {
        GlideApp.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).apply(blurRequestOptions(defRes,defRes,blurRadius)).into(imageView);
    }

    private RequestOptions blurRequestOptions(int defRes, int defRes1, int blurRadius) {
        return requestOptions(defRes, defRes1)
                .transform(new BlurBitmapTranformation(blurRadius));
    }

    @Override
    public void displayBlurImage(Context context, int resId, ImageView imageView, int blurRadius) {
        GlideApp.with(context).load(resId).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).apply(blurRequestOptions(resId,resId,blurRadius)).into(imageView);
    }

    @Override
    public void displayBlurImage(Fragment fragment, String url, ImageView imageView, int defRes, int blurRadius) {
        GlideApp.with(fragment).load(url).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).apply(blurRequestOptions(defRes,defRes,blurRadius)).into(imageView);
    }

    /**
     *  加载资源文件
     * @param context
     * @param resId
     * @param imageView
     */
    @Override
    public void displayImageInResource(Context context, int resId, ImageView imageView) {
        GlideApp.with(context).load(resId).diskCacheStrategy(DiskCacheStrategy.NONE).into(imageView);
    }

    @Override
    public void displayImageInResource(Fragment fragment, int resId, ImageView imageView) {
        GlideApp.with(fragment).load(resId).diskCacheStrategy(DiskCacheStrategy.NONE).into(imageView);
    }

    public void displayImageInResource(Activity fragment, int resId, ImageView imageView) {
        GlideApp.with(fragment).load(resId).diskCacheStrategy(DiskCacheStrategy.NONE).into(imageView);
    }

    /**
     *
     * @param fragment
     * @param resId
     * @param imageView
     * @param transformation 需要变换那种图像
     * @param errorResId
     */
    @Override
    public void displayImageInResourceTransform(Activity fragment, int resId, ImageView imageView,Transformation transformation,int errorResId) {
        GlideApp.with(fragment).load(resId).diskCacheStrategy(DiskCacheStrategy.NONE).apply(requestOptionsTransform(errorResId,errorResId,transformation)).into(imageView);
    }

    @Override
    public void displayImageInResourceTransform(Context context, int resId, ImageView imageView, Transformation transformation, int errorResId) {
        GlideApp.with(context).load(resId).diskCacheStrategy(DiskCacheStrategy.NONE).apply(requestOptionsTransform(errorResId,errorResId,transformation)).into(imageView);
    }

    @Override
    public void displayImageInResourceTransform(Fragment fragment, int resId, ImageView imageView, Transformation transformation, int errorResId) {
        GlideApp.with(fragment).load(resId).diskCacheStrategy(DiskCacheStrategy.NONE).apply(requestOptionsTransform(errorResId,errorResId,transformation)).into(imageView);
    }

    @Override
    public void displayImageByNet(Context context, String url, ImageView imageView, int defRes, Transformation transformation) {
        GlideApp.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).apply(requestOptionsTransform(defRes,defRes,transformation)).into(imageView);
    }

    @Override
    public void displayImageByNet(Fragment fragment, String url, ImageView imageView, int defRes, Transformation transformation) {
        GlideApp.with(fragment).load(url).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).apply(requestOptionsTransform(defRes,defRes,transformation)).into(imageView);
    }

    @Override
    public void displayImageByNet(Activity activity, String url, ImageView imageView, int defRes, Transformation transformation) {
        GlideApp.with(activity).load(url).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).apply(requestOptionsTransform(defRes,defRes,transformation)).into(imageView);
    }

    @Override
    public void clear(Activity activity, ImageView imageView) {
        GlideApp.with(activity).clear(imageView);
    }

    @Override
    public void clear(Context context, ImageView imageView) {
        GlideApp.with(context).clear(imageView);
    }

    @Override
    public void clear(Fragment fragment, ImageView imageView) {
        GlideApp.with(fragment).clear(imageView);
    }
//    默认的策略是DiskCacheStrategy.AUTOMATIC
//    DiskCacheStrategy.ALL 使用DATA和RESOURCE缓存远程数据，仅使用RESOURCE来缓存本地数据。
//    DiskCacheStrategy.NONE 不使用磁盘缓存
//    DiskCacheStrategy.DATA 在资源解码前就将原始数据写入磁盘缓存
//    DiskCacheStrategy.RESOURCE 在资源解码后将数据写入磁盘缓存，即经过缩放等转换后的图片资源。
//    DiskCacheStrategy.AUTOMATIC 根据原始图片数据和资源编码策略来自动选择磁盘缓存策略。
    @Override
    public void displayImageByDiskCacheStrategy(Fragment fragment, String url, DiskCacheStrategy diskCacheStrategy,ImageView imageView) {
        GlideApp.with(fragment).load(url).diskCacheStrategy(diskCacheStrategy).into(imageView);
    }
//    DiskCacheStrategy.NONE： 表示不缓存任何内容。
//    DiskCacheStrategy.DATA： 表示只缓存原始图片。
//    DiskCacheStrategy.RESOURCE： 表示只缓存转换过后的图片。
//    DiskCacheStrategy.ALL ： 表示既缓存原始图片，也缓存转换过后的图片。
//    DiskCacheStrategy.AUTOMATIC： 表示让Glide根据图片资源智能地选择使用哪一种缓存策略（默认选项）。
    @Override
    public void displayImageByDiskCacheStrategy(Activity activity, String url, DiskCacheStrategy diskCacheStrategy,ImageView imageView) {
        GlideApp.with(activity).load(url).diskCacheStrategy(diskCacheStrategy).into(imageView);
    }

    @Override
    public void displayImageByDiskCacheStrategy(Context context, String url, DiskCacheStrategy diskCacheStrategy,ImageView imageView) {
        GlideApp.with(context).load(url).diskCacheStrategy(diskCacheStrategy).into(imageView);
    }

    @Override
    public void disPlayImageOnlyRetrieveFromCache(Fragment fragment, String url, ImageView imageView) {
        GlideApp.with(fragment)
                .load(url)
                .onlyRetrieveFromCache(true)
                .into(imageView);
    }

    @Override
    public void disPlayImageOnlyRetrieveFromCache(Activity activity, String url, ImageView imageView) {
        GlideApp.with(activity)
                .load(url)
                .onlyRetrieveFromCache(true)
                .into(imageView);
    }

    @Override
    public void disPlayImageOnlyRetrieveFromCache(Context context, String url, ImageView imageView) {
        GlideApp.with(context)
                .load(url)
                .onlyRetrieveFromCache(true)
                .into(imageView);
    }
    /**
     *如果你想确保一个特定的请求跳过磁盘和/或内存缓存（比如，图片验证码 –）
     * @param fragment
     * @param url
     * @param imageView
     * @param skipflag  是否跳过内存缓存
     * @param diskCacheStratey  是否跳过磁盘缓存
     */
    @Override
    public void disPlayImageSkipMemoryCache(Fragment fragment, String url, ImageView imageView, boolean skipflag, boolean diskCacheStratey) {
        GlideApp.with(fragment)
                .load(url)
                .diskCacheStrategy(diskCacheStratey?DiskCacheStrategy.NONE:DiskCacheStrategy.AUTOMATIC)
                .skipMemoryCache(skipflag)
                .into(imageView);
    }

    @Override
    public void disPlayImageSkipMemoryCache(Activity activity, String url, ImageView imageView, boolean skipflag, boolean diskCacheStratey) {
        GlideApp.with(activity)
                .load(url)
                .diskCacheStrategy(diskCacheStratey?DiskCacheStrategy.NONE:DiskCacheStrategy.AUTOMATIC)
                .skipMemoryCache(skipflag)
                .into(imageView);
    }

    @Override
    public void disPlayImageSkipMemoryCache(Context context, String url, ImageView imageView, boolean skipflag, boolean diskCacheStratey) {
        GlideApp.with(context)
                .load(url)
                .diskCacheStrategy(diskCacheStratey?DiskCacheStrategy.NONE:DiskCacheStrategy.AUTOMATIC)
                .skipMemoryCache(skipflag)
                .into(imageView);
    }

    @Override
    public void disPlayImageErrorReload(Fragment fragment, String url, String fallbackUrl, ImageView imageView) {
        GlideApp.with(fragment)
                .load(url)
                .error(GlideApp.with(fragment)
                        .load(fallbackUrl))
                .into(imageView);
    }

    @Override
    public void disPlayImageErrorReload(Activity activity, String url, String fallbackUrl, ImageView imageView) {
        GlideApp.with(activity)
                .load(url)
                .error(GlideApp.with(activity)
                        .load(fallbackUrl))
                .into(imageView);
    }

    @Override
    public void disPlayImageErrorReload(Context context, String url, String fallbackUrl, ImageView imageView) {
        GlideApp.with(context)
                .load(url)
                .error(GlideApp.with(context)
                        .load(fallbackUrl))
                .into(imageView);
    }

    @Override
    public void disPlayImagedisallowHardwareConfig(Fragment fragment, String url, ImageView imageView) {
        GlideApp.with(fragment)
                .load(url)
                .disallowHardwareConfig()
                .into(imageView);
        //第二种方法
//        RequestOptions options = new RequestOptions().disallowHardwareConfig();
//        Glide.with(fragment)
//                .load(url)
//                .apply(options)
//                .into(imageView);
    }

    @Override
    public void disPlayImagedisallowHardwareConfig(Activity activity, String url, ImageView imageView) {
        GlideApp.with(activity)
                .load(url)
                .disallowHardwareConfig()
                .into(imageView);
    }

    @Override
    public void disPlayImagedisallowHardwareConfig(Context context, String url, ImageView imageView) {
        GlideApp.with(context)
                .load(url)
                .disallowHardwareConfig()
                .into(imageView);
    }
    //监听进度
    @Override
    public void disPlayImageProgress(Context context, final String url, ImageView imageView, int placeholderResId, int errorResId, OnGlideImageViewListener listener) {
        GlideApp.with(context)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.NONE)//todo 我是为了测试，看到进度条，才把缓存策略设置成这样的，项目中一定不要这样做
                .apply(new RequestOptions()
                        .placeholder(placeholderResId)
                        .error(errorResId))
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        mainThreadCallback(url,mLastBytesRead, mTotalBytes, true, e);
                        ProgressManager.removeProgressListener(internalProgressListener);
                        return false;
                    }
                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        mainThreadCallback(url,mLastBytesRead, mTotalBytes, true, null);
                        ProgressManager.removeProgressListener(internalProgressListener);
                        return false;
                    }
                }).into(imageView);

        //赋值 上去
        onGlideImageViewListener=listener;
        mMainThreadHandler = new Handler(Looper.getMainLooper());
        internalProgressListener = new OnProgressListener() {
            @Override
            public void onProgress(String imageUrl, final long bytesRead, final long totalBytes, final boolean isDone, final GlideException exception) {
                if (totalBytes == 0) return;
                if (mLastBytesRead == bytesRead && mLastStatus == isDone) return;

                mLastBytesRead = bytesRead;
                mTotalBytes = totalBytes;
                mLastStatus = isDone;
                mainThreadCallback(imageUrl,bytesRead, totalBytes, isDone, exception);

                if (isDone) {
                    ProgressManager.removeProgressListener(this);
                }
            }
        };
        ProgressManager.addProgressListener(internalProgressListener);



    }

    @Override
    public void disPlayImageProgressByOnProgressListener(Context context, final String url, ImageView imageView, int placeholderResId, int errorResId, OnProgressListener onProgressListener) {
        GlideApp.with(context)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.NONE)//todo 我是为了测试，看到进度条，才把缓存策略设置成这样的，项目中一定不要这样做
                .apply(new RequestOptions()
                        .placeholder(placeholderResId)
                        .error(errorResId))
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        mainThreadCallback(url,mLastBytesRead, mTotalBytes, true, e);
                        ProgressManager.removeProgressListener(internalProgressListener);
                        return false;
                    }
                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        mainThreadCallback(url,mLastBytesRead, mTotalBytes, true, null);
                        ProgressManager.removeProgressListener(internalProgressListener);
                        return false;
                    }
                }).into(imageView);

        //赋值 上去
        this.onProgressListener = onProgressListener;
        mMainThreadHandler = new Handler(Looper.getMainLooper());
        internalProgressListener = new OnProgressListener() {
            @Override
            public void onProgress(String imageUrl, final long bytesRead, final long totalBytes, final boolean isDone, final GlideException exception) {
                if (totalBytes == 0) return;
                if (mLastBytesRead == bytesRead && mLastStatus == isDone) return;

                mLastBytesRead = bytesRead;
                mTotalBytes = totalBytes;
                mLastStatus = isDone;
                mainThreadCallback(imageUrl,bytesRead, totalBytes, isDone, exception);

                if (isDone) {
                    ProgressManager.removeProgressListener(this);
                }
            }
        };
        ProgressManager.addProgressListener(internalProgressListener);
    }

    private Handler mMainThreadHandler;
    private long mTotalBytes = 0;
    private long mLastBytesRead = 0;
    private void mainThreadCallback(final String url, final long bytesRead, final long totalBytes, final boolean isDone, final GlideException exception) {
        mMainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                // TODO: 2018/4/20 放慢效果  实际的项目中给我去掉
                SystemClock.sleep(100);
                final int percent = (int) ((bytesRead * 1.0f / totalBytes) * 100.0f);
                if (onProgressListener != null) {
                    onProgressListener.onProgress(url, bytesRead, totalBytes, isDone, exception);
                }

                if (onGlideImageViewListener != null) {
                    onGlideImageViewListener.onProgress(percent, isDone, exception);
                }
            }
        });
    }
    private boolean mLastStatus = false;
    private OnProgressListener internalProgressListener;
    private OnGlideImageViewListener onGlideImageViewListener;
    private OnProgressListener onProgressListener;
    private void addProgressListener() {

    }

    /**
     * 指定传入的那种图片的变形
     * @param placeholderResId
     * @param errorResId
     * @param transformation
     * @return
     */
    public RequestOptions requestOptionsTransform(int placeholderResId, int errorResId,Transformation transformation) {
        return new RequestOptions()
                .placeholder(placeholderResId)
                .error(errorResId).transform(transformation);
    }
    /**
     * 加载资源文件的同时，对图片进行处理
     * @param context
     * @param resId
     * @param imageView
     * @param transformations
     */
    @Override
    public void displayImageInResource(Context context, int resId, ImageView imageView, BitmapTransformation transformations) {
        GlideApp.with(context).load(resId).diskCacheStrategy(DiskCacheStrategy.NONE).transform(transformations).into(imageView);
    }

    @Override
    public void displayImageInResource(Fragment fragment, int resId,  ImageView imageView, BitmapTransformation transformations) {
        GlideApp.with(fragment).load(resId).diskCacheStrategy(DiskCacheStrategy.NONE).transform(transformations).into(imageView);
    }

    /**
     * 当传入到其他的东西的时候，我要保证图片不变形
     * @param placeholderResId
     * @param errorResId
     * @param transformation
     * @return
     */
    public RequestOptions requestOptionsNoTransform(int placeholderResId, int errorResId,Transformation transformation) {
        return new RequestOptions()
                .placeholder(placeholderResId)
                .error(errorResId).transform(transformation);
    }
    /**
     * 加载资源文件失败了，加载中的默认图和失败的图片
     * @param context
     * @param resId
     * @param imageView
     * @param defRes
     */
    @Override
    public void displayImageInResource(Context context, int resId,  ImageView imageView, int defRes) {
        GlideApp.with(context).load(resId).diskCacheStrategy(DiskCacheStrategy.NONE).placeholder(defRes).error(defRes).into(imageView);
    }

    @Override
    public void displayImageInResource(Fragment fragment, int resId, ImageView imageView, int defRes) {
        GlideApp.with(fragment).load(resId).diskCacheStrategy(DiskCacheStrategy.NONE).placeholder(defRes).error(defRes).into(imageView);
    }
    //关心context
    @Override
    public void displayImageInResource(Context context, int resId,  ImageView imageView, int defRes, BitmapTransformation transformations) {
        GlideApp.with(context).load(resId).diskCacheStrategy(DiskCacheStrategy.NONE).placeholder(defRes).error(defRes).transform(transformations).into(imageView);
    }
    //关心fragment
    @Override
    public void displayImageInResource(Fragment fragment, int resId,  ImageView imageView, int defRes, BitmapTransformation transformations) {
        GlideApp.with(fragment).load(resId).diskCacheStrategy(DiskCacheStrategy.NONE).placeholder(defRes).error(defRes).transform(transformations).into(imageView);
    }


}
