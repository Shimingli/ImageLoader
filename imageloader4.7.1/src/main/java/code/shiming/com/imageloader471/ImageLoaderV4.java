package code.shiming.com.imageloader471;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.widget.ImageView;

import java.io.File;

import code.shiming.com.imageloader471.listener.IGetBitmapListener;
import code.shiming.com.imageloader471.listener.IGetDrawableListener;
import code.shiming.com.imageloader471.listener.IImageLoaderListener;
import code.shiming.com.imageloader471.listener.ImageSize;
import code.shiming.com.imageloader471.tranform.IBitmapTransformation;


/**
 * Created by shiming on 2016/10/26.
 */
public class ImageLoaderV4 implements IImageLoaderClient {

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
    public void displayImage(Context context, String url, ImageView imageView, int defRes, IBitmapTransformation... transformations) {
        if (client != null) {
            client.displayImage(context, url, imageView, defRes, transformations);
        }
    }

    @Override
    public void displayImage(Fragment fragment, String url, ImageView imageView, int defRes, IBitmapTransformation... transformations) {
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
    public void displayImageInResource(Context context, int resId, ImageView imageView, IBitmapTransformation... transformations) {
        if (client != null) {
            client.displayImageInResource(context, resId, imageView, transformations);
        }
    }

    @Override
    public void displayImageInResource(Fragment fragment, int resId,  ImageView imageView, IBitmapTransformation... transformations) {
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
    public void displayImageInResource(Context context, int resId,  ImageView imageView, int defRes, IBitmapTransformation... transformations) {
        if (client != null) {
            client.displayImageInResource(context, resId, imageView, defRes, transformations);
        }
    }

    @Override
    public void displayImageInResource(Fragment fragment, int resId,  ImageView imageView, int defRes, IBitmapTransformation... transformations) {
        if (client != null) {
            client.displayImageInResource(fragment, resId,  imageView, defRes, transformations);
        }
    }


}
