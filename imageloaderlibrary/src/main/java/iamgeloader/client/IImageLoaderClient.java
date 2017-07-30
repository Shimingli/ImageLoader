package iamgeloader.client;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.widget.ImageView;

import java.io.File;

import iamgeloader.client.listener.IGetBitmapListener;
import iamgeloader.client.listener.IGetDrawableListener;
import iamgeloader.client.listener.IImageLoaderListener;
import iamgeloader.client.listener.ImageSize;
import iamgeloader.client.tranform.IBitmapTransformation;

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

    public void displayImage(Context context, String url, ImageView imageView, int defRes, IBitmapTransformation... transformations);

    public void displayImage(Fragment fragment, String url, ImageView imageView, int defRes, IBitmapTransformation... transformations);

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

    public void displayImageInResource(Context context, int resId,  ImageView imageView);

    public void displayImageInResource(Fragment fragment, int resId, ImageView imageView);

    public void displayImageInResource(Context context, int resId,  ImageView imageView, IBitmapTransformation... transformations);

    public void displayImageInResource(Fragment fragment, int resId, ImageView imageView, IBitmapTransformation... transformations);

    public void displayImageInResource(Context context, int resId,  ImageView imageView, int defRes);

    public void displayImageInResource(Fragment fragment, int resId,  ImageView imageView, int defRes);

    public void displayImageInResource(Context context, int resId,  ImageView imageView, int defRes, IBitmapTransformation... transformations);

    public void displayImageInResource(Fragment fragment, int resId,  ImageView imageView, int defRes, IBitmapTransformation... transformations);
}
