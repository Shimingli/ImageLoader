package iamgeloader.client;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;

import java.io.File;

import iamgeloader.client.listener.IGetBitmapListener;
import iamgeloader.client.listener.IGetDrawableListener;
import iamgeloader.client.listener.IImageLoaderListener;
import iamgeloader.client.listener.ImageSize;
import iamgeloader.client.tranform.BlurBitmapTransformation;
import iamgeloader.client.tranform.CircleBitmapTransformation;
import iamgeloader.client.tranform.GlideTransform;
import iamgeloader.client.tranform.IBitmapTransformation;
import iamgeloader.client.tranform.RoundBitmapTransformation;

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

    @Override
    public void getBitmapFromCache(Context context, String url, final IGetBitmapListener listener) {
        Glide.with(context).load(url).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                if (listener != null) {
                    listener.onBitmap(resource);
                }
            }
        });
    }

    /**
     *
     * @param context  上下文
     * @param resId  id
     * @param imageView into
      */
    @Override
    public void displayImage(Context context, int resId, ImageView imageView) {
        //设置缓存策略缓存原始数据
        Glide.with(context).load(resId).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imageView);
    }

    /**
     *
     * @param context
     * @param url url
     * @param imageView in
     */
    @Override
    public void displayImage(Context context, String url, ImageView imageView) {
        Glide.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imageView);
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
        Glide.with(context).load(url).skipMemoryCache(isCache).diskCacheStrategy(isCache ? DiskCacheStrategy.SOURCE : DiskCacheStrategy.NONE).into(imageView);
    }

    /**
     *
     * @param fragment 绑定生命周期
     * @param url
     * @param imageView
     */
    @Override
    public void displayImage(Fragment fragment, String url, ImageView imageView) {
        Glide.with(fragment).load(url).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imageView);
    }

    /**
     * 使用.placeholder()方法在某些情况下会导致图片显示的时候出现图片变形的情况
     * 这是因为Glide默认开启的crossFade动画导致的TransitionDrawable绘制异常
     * @param context
     * @param url
     * @param imageView
     * @param defRes
     */
    @Override
    public void displayImage(Context context, String url, ImageView imageView, int defRes) {
        Glide.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.SOURCE).crossFade().placeholder(defRes).error(defRes).into(imageView);
    }

    @Override
    public void displayImage(Fragment fragment, String url, ImageView imageView, int defRes) {
        Glide.with(fragment).load(url).diskCacheStrategy(DiskCacheStrategy.SOURCE).placeholder(defRes).error(defRes).into(imageView);
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
        Glide.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.SOURCE).placeholder(defRes).error(defRes).bitmapTransform(glideTransforms).into(imageView);
    }

    @Override
    public void displayImage(Fragment fragment, String url, ImageView imageView, int defRes, IBitmapTransformation... transformations) {
        int size = transformations.length;
        GlideTransform[] glideTransforms = new GlideTransform[size];

        for (int i = 0; i < size; i++) {
            glideTransforms[i] = new GlideTransform(transformations[i].getContext(), transformations[i]);
        }
        Glide.with(fragment).load(url).diskCacheStrategy(DiskCacheStrategy.SOURCE).placeholder(defRes).error(defRes).bitmapTransform(glideTransforms).into(imageView);
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
        Glide.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.SOURCE).placeholder(defRes).error(defRes).override(size.getWidth(), size.getHeight()).into(imageView);
    }

    @Override
    public void displayImage(Fragment fragment, String url, ImageView imageView, int defRes, ImageSize size) {
        Glide.with(fragment).load(url).diskCacheStrategy(DiskCacheStrategy.SOURCE).placeholder(defRes).error(defRes).override(size.getWidth(), size.getHeight()).into(imageView);
    }

    @Override
    public void displayImage(Context context, String url, ImageView imageView, int defRes, boolean cacheInMemory) {
        Glide.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.SOURCE).placeholder(defRes).error(defRes).skipMemoryCache(cacheInMemory).into(imageView);
    }

    @Override
    public void displayImage(Fragment fragment, String url, ImageView imageView, int defRes, boolean cacheInMemory) {
        Glide.with(fragment).load(url).diskCacheStrategy(DiskCacheStrategy.SOURCE).placeholder(defRes).error(defRes).skipMemoryCache(cacheInMemory).into(imageView);
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
        Glide.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.SOURCE).listener(new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                listener.onLoadingFailed(url, imageView, e);
                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                listener.onLoadingComplete(url, imageView);
                return false;
            }
        }).into(imageView);
    }

    @Override
    public void displayImage(Fragment fragment, final String url, final ImageView imageView, final IImageLoaderListener listener) {
        Glide.with(fragment).load(url).diskCacheStrategy(DiskCacheStrategy.SOURCE).listener(new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                listener.onLoadingFailed(url, imageView, e);
                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                listener.onLoadingComplete(url, imageView);
                return false;
            }
        }).into(imageView);
    }

    @Override
    public void displayImage(Context context, final String url, final ImageView imageView, int defRes, final IImageLoaderListener listener) {
        Glide.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.SOURCE).placeholder(defRes).error(defRes).listener(new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                listener.onLoadingFailed(url, imageView, e);
                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                listener.onLoadingComplete(url, imageView);
                return false;
            }
        }).into(imageView);
    }

    @Override
    public void displayImage(Fragment fragment, final String url, final ImageView imageView, int defRes, final IImageLoaderListener listener) {
        Glide.with(fragment).load(url).diskCacheStrategy(DiskCacheStrategy.SOURCE).listener(new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                listener.onLoadingFailed(url, imageView, e);
                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
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
        Glide.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.SOURCE).placeholder(defRes).error(defRes).bitmapTransform(new GlideTransform(context, new CircleBitmapTransformation(context))).into(imageView);
    }

    @Override
    public void displayCircleImage(Fragment fragment, String url, ImageView imageView, int defRes) {
        Glide.with(fragment).load(url).diskCacheStrategy(DiskCacheStrategy.SOURCE).placeholder(defRes).error(defRes).bitmapTransform(new GlideTransform(imageView.getContext(), new CircleBitmapTransformation(imageView.getContext()))).into(imageView);
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
        Glide.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.SOURCE).placeholder(defRes).error(defRes).bitmapTransform(new GlideTransform(context, new RoundBitmapTransformation(imageView.getContext(), radius))).into(imageView);
    }

    @Override
    public void displayRoundImage(Fragment fragment, String url, ImageView imageView, int defRes, int radius) {
        Glide.with(fragment).load(url).diskCacheStrategy(DiskCacheStrategy.SOURCE).placeholder(defRes).error(defRes).bitmapTransform(new GlideTransform(imageView.getContext(), new RoundBitmapTransformation(imageView.getContext(), radius))).into(imageView);
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
        Glide.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.SOURCE).
                bitmapTransform(new GlideTransform(context, new BlurBitmapTransformation(context, blurRadius))).
                into(new SimpleTarget<GlideDrawable>() {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
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
        Glide.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.SOURCE).placeholder(defRes).error(defRes).bitmapTransform(new GlideTransform(context, new BlurBitmapTransformation(context, blurRadius))).into(imageView);
    }

    @Override
    public void displayBlurImage(Context context, int resId, ImageView imageView, int blurRadius) {
        Glide.with(context).load(resId).diskCacheStrategy(DiskCacheStrategy.SOURCE).bitmapTransform(new GlideTransform(imageView.getContext(), new BlurBitmapTransformation(imageView.getContext(), blurRadius))).into(imageView);
    }

    @Override
    public void displayBlurImage(Fragment fragment, String url, ImageView imageView, int defRes, int blurRadius) {
        Glide.with(fragment).load(url).diskCacheStrategy(DiskCacheStrategy.SOURCE).placeholder(defRes).error(defRes).bitmapTransform(new GlideTransform(imageView.getContext(), new BlurBitmapTransformation(imageView.getContext(), blurRadius))).into(imageView);
    }

    /**
     *  加载资源文件
     * @param context
     * @param resId
     * @param imageView
     */
    @Override
    public void displayImageInResource(Context context, int resId, ImageView imageView) {
        Glide.with(context).load(resId).diskCacheStrategy(DiskCacheStrategy.NONE).into(imageView);
    }

    @Override
    public void displayImageInResource(Fragment fragment, int resId, ImageView imageView) {
        Glide.with(fragment).load(resId).diskCacheStrategy(DiskCacheStrategy.NONE).into(imageView);
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
        int size = transformations.length;
        GlideTransform[] glideTransforms = new GlideTransform[size];

        for (int i = 0; i < size; i++) {
            glideTransforms[i] = new GlideTransform(transformations[i].getContext(), transformations[i]);
        }

        Glide.with(context).load(resId).diskCacheStrategy(DiskCacheStrategy.NONE).transform(glideTransforms).into(imageView);
    }

    @Override
    public void displayImageInResource(Fragment fragment, int resId,  ImageView imageView, IBitmapTransformation... transformations) {
        int size = transformations.length;
        GlideTransform[] glideTransforms = new GlideTransform[size];

        for (int i = 0; i < size; i++) {
            glideTransforms[i] = new GlideTransform(transformations[i].getContext(), transformations[i]);
        }

        Glide.with(fragment).load(resId).diskCacheStrategy(DiskCacheStrategy.NONE).transform(glideTransforms).into(imageView);
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
        Glide.with(context).load(resId).diskCacheStrategy(DiskCacheStrategy.NONE).placeholder(defRes).error(defRes).into(imageView);
    }

    @Override
    public void displayImageInResource(Fragment fragment, int resId, ImageView imageView, int defRes) {
        Glide.with(fragment).load(resId).diskCacheStrategy(DiskCacheStrategy.NONE).placeholder(defRes).error(defRes).into(imageView);
    }

    @Override
    public void displayImageInResource(Context context, int resId,  ImageView imageView, int defRes, IBitmapTransformation... transformations) {
        int size = transformations.length;
        GlideTransform[] glideTransforms = new GlideTransform[size];

        for (int i = 0; i < size; i++) {
            glideTransforms[i] = new GlideTransform(transformations[i].getContext(), transformations[i]);
        }

        Glide.with(context).load(resId).diskCacheStrategy(DiskCacheStrategy.NONE).placeholder(defRes).error(defRes).transform(glideTransforms).into(imageView);
    }

    @Override
    public void displayImageInResource(Fragment fragment, int resId,  ImageView imageView, int defRes, IBitmapTransformation... transformations) {
        int size = transformations.length;
        GlideTransform[] glideTransforms = new GlideTransform[size];

        for (int i = 0; i < size; i++) {
            glideTransforms[i] = new GlideTransform(transformations[i].getContext(), transformations[i]);
        }

        Glide.with(fragment).load(resId).diskCacheStrategy(DiskCacheStrategy.NONE).placeholder(defRes).error(defRes).transform(glideTransforms).into(imageView);
    }


}
