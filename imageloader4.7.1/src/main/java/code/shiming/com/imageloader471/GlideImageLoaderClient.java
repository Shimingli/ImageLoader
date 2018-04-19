package code.shiming.com.imageloader471;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestFutureTarget;
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
import code.shiming.com.imageloader471.tranform.BlurBitmapTransformation;
import code.shiming.com.imageloader471.tranform.CircleBitmapTransformation;
import code.shiming.com.imageloader471.tranform.GlideTransform;
import code.shiming.com.imageloader471.tranform.IBitmapTransformation;
import code.shiming.com.imageloader471.tranform.RoundBitmapTransformation;

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

    @Override
    public void clearMemoryCache(Context context) {
        Glide.get(context).clearMemory();
    }

    @Override
    public void clearDiskCache(Context context) {
        Glide.get(context).clearDiskCache();
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
        GlideApp.with(context).load(resId).diskCacheStrategy(DiskCacheStrategy.DATA).into(imageView);
    }

    /**
     *
     * @param context
     * @param url url
     * @param imageView in
     */
    @Override
    public void displayImage(Context context, String url, ImageView imageView) {
        GlideApp.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.DATA).into(imageView);
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
        GlideApp.with(context).load(url).skipMemoryCache(isCache).diskCacheStrategy(isCache ? DiskCacheStrategy.DATA : DiskCacheStrategy.NONE).into(imageView);
    }

    /**
     *
     * @param fragment 绑定生命周期
     * @param url
     * @param imageView
     */
    @Override
    public void displayImage(Fragment fragment, String url, ImageView imageView) {
        GlideApp.with(fragment).load(url).diskCacheStrategy(DiskCacheStrategy.DATA).into(imageView);
    }

    /**
     * 使用.placeholder()方法在某些情况下会导致图片显示的时候出现图片变形的情况
     * 这是因为Glide默认开启的crossFade动画导致的TransitionDrawable绘制异常
     * @param context
     * @param url
     * @param imageView
     * @param defRes
     */
    //默认为2000  时间有点长  ，工程中要修改下，设置一个加载失败和加载中的动画过渡，V4.0的使用的方法
    @Override
    public void displayImage(Context context, String url, ImageView imageView, int defRes) {
        GlideApp.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.DATA).transition(new DrawableTransitionOptions().crossFade(200)).placeholder(defRes).error(defRes).into(imageView);
//        GlideApp.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.DATA).crossFade().placeholder(defRes).error(defRes).into(imageView);
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
        GlideApp.with(fragment).load(url).diskCacheStrategy(DiskCacheStrategy.DATA).transition(new DrawableTransitionOptions().crossFade(200)).placeholder(defRes).error(defRes).into(imageView);
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
    public void displayImage(Context context, String url, ImageView imageView, int defRes, final IBitmapTransformation... transformations) {
        int size = transformations.length;
        //由于这里bitmapTransform接受的为com.bumptech.glide.load.resource.bitmap.BitmapTransformation子类
        //所以需要我们传入构造函数去
        GlideTransform[] glideTransforms = new GlideTransform[size];
        for (int i = 0; i < size; i++) {
            glideTransforms[i] = new GlideTransform(transformations[i].getContext(), transformations[i]);
        }
//        GlideApp.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.DATA).placeholder(defRes).error(defRes).bitmapTransform(glideTransforms).into(imageView);
//        GlideApp.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.DATA).placeholder(defRes).error(defRes).requestOptions(defRes, defRes)
//                .transform(new GlideCircleTransformation()).into(imageView);

        GlideApp.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.DATA).apply(circleRequestOptions(defRes,defRes)).into(imageView);

    }

    @Override
    public void displayImage(Fragment fragment, String url, ImageView imageView, int defRes, IBitmapTransformation... transformations) {

    }

    /**
     * 加载原圆形图片
     * @param context
     * @param url
     * @param imageView
     * @param defRes
     */
    public void displayImageCircle(Context context, String url, ImageView imageView, int defRes) {
        GlideApp.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.DATA).apply(circleRequestOptions(defRes,defRes)).into(imageView);

    }
    public void displayImageCircle(Fragment context, String url, ImageView imageView, int defRes) {
        GlideApp.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.DATA).apply(circleRequestOptions(defRes,defRes)).into(imageView);

    }
    public RequestOptions requestOptions(int placeholderResId, int errorResId) {
        return new RequestOptions()
                .placeholder(placeholderResId)
                .error(errorResId);
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

    public RequestOptions roundRequestOptions(int placeholderResId, int errorResId) {
        return requestOptions(placeholderResId, errorResId)
                .transform(new RoundBitmapTranformation());
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
        GlideApp.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.DATA).placeholder(defRes).error(defRes).override(size.getWidth(), size.getHeight()).into(imageView);
    }

    @Override
    public void displayImage(Fragment fragment, String url, ImageView imageView, int defRes, ImageSize size) {
        GlideApp.with(fragment).load(url).diskCacheStrategy(DiskCacheStrategy.DATA).placeholder(defRes).error(defRes).override(size.getWidth(), size.getHeight()).into(imageView);
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
        GlideApp.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.DATA).placeholder(defRes).error(defRes).skipMemoryCache(cacheInMemory).into(imageView);
    }

    @Override
    public void displayImage(Fragment fragment, String url, ImageView imageView, int defRes, boolean cacheInMemory) {
        GlideApp.with(fragment).load(url).diskCacheStrategy(DiskCacheStrategy.DATA).placeholder(defRes).error(defRes).skipMemoryCache(cacheInMemory).into(imageView);
    }

    /**
     * 只在需要的地方进行监听 listener 通过自定义的接口回调参数
     * @param context
     * @param url
     * @param imageView
     * @param listener  监听资源加载的请求状态 但不要每次请求都使用新的监听器，要避免不必要的内存申请，可以使用单例进行统一的异常监听和处理
     */
    @Override
    public void displayImage(Context context, final String url, final ImageView imageView, final IImageLoaderListener listener) {
        GlideApp.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.DATA).listener(new RequestListener<Drawable>() {
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
    public void displayImage(Fragment fragment, final String url, final ImageView imageView, final IImageLoaderListener listener) {
        GlideApp.with(fragment).load(url).diskCacheStrategy(DiskCacheStrategy.DATA).listener(new RequestListener<Drawable>() {
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
        GlideApp.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.DATA).placeholder(defRes).error(defRes).listener(new RequestListener<Drawable>() {
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
        GlideApp.with(fragment).load(url).diskCacheStrategy(DiskCacheStrategy.DATA).listener(new RequestListener<Drawable>() {
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
        GlideApp.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.DATA).apply(circleRequestOptions(defRes,defRes)).into(imageView);
    }

    @Override
    public void displayCircleImage(Fragment fragment, String url, ImageView imageView, int defRes) {
        GlideApp.with(fragment).load(url).diskCacheStrategy(DiskCacheStrategy.DATA).apply(circleRequestOptions(defRes,defRes)).into(imageView);
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
     * @param radius 倒圆角的图片 需要传入需要radius
     */
    @Override
    public void displayRoundImage(Context context, String url, ImageView imageView, int defRes, int radius) {
        GlideApp.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.DATA).apply(circleRequestOptions(defRes,defRes)).into(imageView);
    }

    @Override
    public void displayRoundImage(Fragment fragment, String url, ImageView imageView, int defRes, int radius) {
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
        GlideApp.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.DATA).into(new SimpleTarget<Drawable>() {
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
    }

    @Override
    public void displayBlurImage(Context context, int resId, ImageView imageView, int blurRadius) {
    }

    @Override
    public void displayBlurImage(Fragment fragment, String url, ImageView imageView, int defRes, int blurRadius) {
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
     * 加载资源文件的同时，对图片进行处理
     * @param context
     * @param resId
     * @param imageView
     * @param transformations
     */
    @Override
    public void displayImageInResource(Context context, int resId,ImageView imageView, IBitmapTransformation... transformations) {

        GlideApp.with(context).load(resId).diskCacheStrategy(DiskCacheStrategy.NONE).transform(new CenterCrop()).into(imageView);
    }

    @Override
    public void displayImageInResource(Fragment fragment, int resId,  ImageView imageView, IBitmapTransformation... transformations) {

        GlideApp.with(fragment).load(resId).diskCacheStrategy(DiskCacheStrategy.NONE).transform(new GlideCircleTransformation()).into(imageView);
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
    public void displayImageInResource(Context context, int resId,  ImageView imageView, int defRes, IBitmapTransformation... transformations) {

        GlideApp.with(context).load(resId).diskCacheStrategy(DiskCacheStrategy.NONE).placeholder(defRes).error(defRes).transform(new GlideCircleTransformation()).into(imageView);
    }
    //关心fragment
    @Override
    public void displayImageInResource(Fragment fragment, int resId,  ImageView imageView, int defRes, IBitmapTransformation... transformations) {
        GlideApp.with(fragment).load(resId).diskCacheStrategy(DiskCacheStrategy.NONE).placeholder(defRes).error(defRes).transform(new GlideCircleTransformation()).into(imageView);
    }


}
