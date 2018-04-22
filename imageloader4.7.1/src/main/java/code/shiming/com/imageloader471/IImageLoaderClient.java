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

public interface IImageLoaderClient {
    public void init(Context context);

    public void destroy(Context context);

    public File getCacheDir(Context context);

    public void clearMemoryCache(Context context);

    public void clearDiskCache(Context context);

    public Bitmap getBitmapFromCache(Context context, String url);

    public void getBitmapFromCache(Context context, String url, IGetBitmapListener listener);

    public void displayImage(Context context, int resId, ImageView imageView);

    public void displayImage(Context context, String url, ImageView imageView);

    public void displayImage(Context context, String url, ImageView imageView, boolean isCache);

    public void displayImage(Fragment fragment, String url, ImageView imageView);

    public void displayImage(Context context, String url, ImageView imageView, int defRes);

    public void displayImage(Fragment fragment, String url, ImageView imageView, int defRes);

    public void displayImage(Context context, String url, ImageView imageView, int defRes, BitmapTransformation transformations);

    public void displayImage(Fragment fragment, String url, ImageView imageView, int defRes, BitmapTransformation transformations);

    public void displayImage(Context context, String url, ImageView imageView, int defRes, ImageSize size);

    public void displayImage(Fragment fragment, String url, ImageView imageView, int defRes, ImageSize size);

    public void displayImage(Context context, String url, ImageView imageView, int defRes, boolean cacheInMemory);

    public void displayImage(Fragment fragment, String url, ImageView imageView, int defRes, boolean cacheInMemory);


    public void displayImage(Context context, String url, ImageView imageView, IImageLoaderListener listener);

    public void displayImage(Fragment fragment, String url, ImageView imageView, IImageLoaderListener listener);

    public void displayImage(Context context, String url, ImageView imageView, int defRes, IImageLoaderListener listener);

    public void displayImage(Fragment fragment, String url, ImageView imageView, int defRes, IImageLoaderListener listener);


    public void displayCircleImage(Context context, String url, ImageView imageView, int defRes);

    public void displayCircleImage(Fragment fragment, String url, ImageView imageView, int defRes);

    public void displayRoundImage(Context context, String url, ImageView imageView, int defRes, int radius);

    public void displayRoundImage(Fragment fragment, String url, ImageView imageView, int defRes, int radius);

    public void displayBlurImage(Context context, String url, int blurRadius, IGetDrawableListener listener);

    public void displayBlurImage(Context context, String url, ImageView imageView, int defRes, int blurRadius);

    public void displayBlurImage(Context context, int resId, ImageView imageView, int blurRadius);

    public void displayBlurImage(Fragment fragment, String url, ImageView imageView, int defRes, int blurRadius);

    public void displayImageInResource(Context context, int resId, ImageView imageView);

    public void displayImageInResource(Fragment fragment, int resId, ImageView imageView);

    public void displayImageInResource(Context context, int resId, ImageView imageView, BitmapTransformation transformations);

    public void displayImageInResource(Fragment fragment, int resId, ImageView imageView, BitmapTransformation transformations);

    public void displayImageInResource(Context context, int resId, ImageView imageView, int defRes);

    public void displayImageInResource(Fragment fragment, int resId, ImageView imageView, int defRes);

    public void displayImageInResource(Context context, int resId, ImageView imageView, int defRes, BitmapTransformation transformations);

    public void displayImageInResource(Fragment fragment, int resId, ImageView imageView, int defRes, BitmapTransformation transformations);


    //add shiming   2018.4.20 transformation 需要装换的那种图像的风格，错误图片，或者是，正在加载中的错误图
    public void displayImageInResourceTransform(Activity activity, int resId, ImageView imageView, Transformation transformation, int errorResId);
    public void displayImageInResourceTransform(Context context, int resId, ImageView imageView, Transformation transformation, int errorResId);
    public void displayImageInResourceTransform(Fragment fragment, int resId, ImageView imageView, Transformation transformation, int errorResId);

    //这是对网络图片，进行的图片操作，使用的glide中的方法
    public void displayImageByNet(Context context, String url, ImageView imageView, int defRes,Transformation transformation);
    public void displayImageByNet(Fragment fragment, String url, ImageView imageView, int defRes,Transformation transformation);
    public void displayImageByNet(Activity activity, String url, ImageView imageView, int defRes,Transformation transformation);


    /**
     *  停止图片的加载，对某一个的Activity
     * @hide
     */
    public void clear(Activity activity,ImageView imageView);
    /**
     * 停止图片的加载，context
     * {@hide}
     */
    public void clear(Context context,ImageView imageView);
    /**
     * 停止图片的加载，fragment
     * {@hide}
     */
    public void clear(Fragment fragment,ImageView imageView);


    //如果需要的话，需要指定加载中，或者是失败的图片
    public void displayImageByDiskCacheStrategy(Fragment fragment, String url, DiskCacheStrategy diskCacheStrategy,ImageView imageView);
    public void displayImageByDiskCacheStrategy(Activity activity, String url, DiskCacheStrategy diskCacheStrategy,ImageView imageView);
    public void displayImageByDiskCacheStrategy(Context context, String url, DiskCacheStrategy diskCacheStrategy,ImageView imageView);
    //某些情形下，你可能希望只要图片不在缓存中则加载直接失败（比如省流量模式）
    public void disPlayImageOnlyRetrieveFromCache(Fragment fragment,String url,ImageView imageView);
    public void disPlayImageOnlyRetrieveFromCache(Activity activity,String url,ImageView imageView);
    public void disPlayImageOnlyRetrieveFromCache(Context context,String url,ImageView imageView);



    /**
     *如果你想确保一个特定的请求跳过磁盘和/或内存缓存（比如，图片验证码 –）
     * @param fragment
     * @param url
     * @param imageView
     * @param skipflag  是否跳过内存缓存
     * @param diskCacheStratey  是否跳过磁盘缓存
     */
    public void disPlayImageSkipMemoryCache(Fragment fragment,String url,ImageView imageView,boolean skipflag,boolean diskCacheStratey);
    public void disPlayImageSkipMemoryCache(Activity activity,String url,ImageView imageView,boolean skipflag,boolean diskCacheStratey);
    public void disPlayImageSkipMemoryCache(Context context,String url,ImageView imageView,boolean skipflag,boolean diskCacheStratey);

    /**
     * 知道这个图片会加载失败，那么的话，我们可以重新加载
     * @param fragment
     * @param url
     * @param fallbackUrl
     * @param imageView
     */
    //从 Glide 4.3.0 开始，你可以很轻松地使用 .error() 方法。这个方法接受一个任意的 RequestBuilder,它会且只会在主请求失败时开始一个新的请求：
    public void disPlayImageErrorReload(Fragment fragment,String url,String fallbackUrl,ImageView imageView);
    public void disPlayImageErrorReload(Activity activity,String url,String fallbackUrl,ImageView imageView);
    public void disPlayImageErrorReload(Context context,String url,String fallbackUrl,ImageView imageView);


    /**
     未来 Glide 将默认加载硬件位图而不需要额外的启用配置，只保留禁用的选项 现在已经默认开启了这个配置，但是在有些情况下需要关闭
     所以提供了以下的方法，禁用硬件位图 disallowHardwareConfig
     * @param fragment
     * @param url
     * @param imageView
     */
//    哪些情况不能使用硬件位图?
//    在显存中存储像素数据意味着这些数据不容易访问到，在某些情况下可能会发生异常。已知的情形列举如下：
//    在 Java 中读写像素数据，包括：
//    Bitmap#getPixel
//    Bitmap#getPixels
//    Bitmap#copyPixelsToBuffer
//    Bitmap#copyPixelsFromBuffer
//    在本地 (native) 代码中读写像素数据
//    使用软件画布 (software Canvas) 渲染硬件位图:
//    Canvas canvas = new Canvas(normalBitmap)
//canvas.drawBitmap(hardwareBitmap, 0, 0, new Paint());
//    在绘制位图的 View 上使用软件层 (software layer type) （例如，绘制阴影）
//    ImageView imageView = …
//            imageView.setImageBitmap(hardwareBitmap);
//imageView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
//    打开过多的文件描述符 . 每个硬件位图会消耗一个文件描述符。
// 这里存在一个每个进程的文件描述符限制 ( Android O 及更早版本一般为 1024，在某些 O-MR1 和更高的构建上是 32K)。
// Glide 将尝试限制分配的硬件位图以保持在这个限制以内，但如果你已经分配了大量的文件描述符，这可能是一个问题。
//    需要ARGB_8888 Bitmaps 作为前置条件
//    在代码中触发截屏操作，它会尝试使用 Canvas 来绘制视图层级。
//    作为一个替代方案，在 Android O 以上版本你可以使用 PixelCopy.
//   共享元素过渡 (shared element transition)(OMR1已修复)
    public void disPlayImagedisallowHardwareConfig(Fragment fragment,String url,ImageView imageView);
    public void disPlayImagedisallowHardwareConfig(Activity activity,String url,ImageView imageView);
    public void disPlayImagedisallowHardwareConfig(Context context,String url,ImageView imageView);

    //监听图片的下载进度，是否完成，百分比 也可以加载本地图片，扩张一下
    public void disPlayImageProgress(Context context,String url,ImageView imageView,int placeholderResId,int errorResId,OnGlideImageViewListener listener);
    public void disPlayImageProgress(Activity activity,String url,ImageView imageView,int placeholderResId,int errorResId,OnGlideImageViewListener listener);
    public void disPlayImageProgress(Fragment fragment,String url,ImageView imageView,int placeholderResId,int errorResId,OnGlideImageViewListener listener);

    public void disPlayImageProgressByOnProgressListener(Context context,String url,ImageView imageView,int placeholderResId,int errorResId, OnProgressListener onProgressListener);
    public void disPlayImageProgressByOnProgressListener(Activity activity,String url,ImageView imageView,int placeholderResId,int errorResId, OnProgressListener onProgressListener);
    public void disPlayImageProgressByOnProgressListener(Fragment fragment,String url,ImageView imageView,int placeholderResId,int errorResId, OnProgressListener onProgressListener);



//    TransitionOptions 用于给一个特定的请求指定过渡。
//    每个请求可以使用 RequestBuilder 中的 transition()
//    方法来设定 TransitionOptions 。还可以通过使用
//    BitmapTransitionOptions 或 DrawableTransitionOptions
//    来指定类型特定的过渡动画。对于 Bitmap 和 Drawable
//    之外的资源类型，可以使用 GenericTransitionOptions。 Glide v4 将不会默认应用交叉淡入或任何其他的过渡效果。每个请求必须手动应用过渡。
    public void displayImageByTransition(Context context, String url, TransitionOptions transitionOptions, ImageView imageView);
    public void displayImageByTransition(Activity activity, String url, TransitionOptions transitionOptions, ImageView imageView);
    public void displayImageByTransition(Fragment fragment, String url, TransitionOptions transitionOptions, ImageView imageView);

    //失去焦点，建议实际的项目中少用，取消求情
    public void glidePauseRequests(Context context);
    public void glidePauseRequests(Activity activity);
    public void glidePauseRequests(Fragment fragment);

    //获取焦点，建议实际的项目中少用
    public void glideResumeRequests(Context context);
    public void glideResumeRequests(Activity activity);
    public void glideResumeRequests(Fragment fragment);
    //加载缩图图     int thumbnailSize = 10;//越小，图片越小，低网络的情况，图片越小
    //GlideApp.with(this).load(urlnoData).override(thumbnailSize))// API 来强制 Glide 在缩略图请求中加载一个低分辨率图像
    public void displayImageThumbnail(Context context,String url,String backUrl,int thumbnailSize,ImageView imageView);
    public void displayImageThumbnail(Activity activity,String url,String backUrl,int thumbnailSize,ImageView imageView);
    public void displayImageThumbnail(Fragment fragment,String url,String backUrl,int thumbnailSize,ImageView imageView);
    //如果没有两个url的话，也想，记载一个缩略图
    public void displayImageThumbnail(Fragment fragment,String url,float thumbnailSize,ImageView imageView);
    public void displayImageThumbnail(Activity activity,String url,float thumbnailSize,ImageView imageView);
    public void displayImageThumbnail(Context context,String url,float thumbnailSize,ImageView imageView);
}
