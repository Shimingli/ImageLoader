# ImageLoader
##不逼逼，先看效果
1、使用Glide裁剪圆形图片、圆角图片和处理模糊图片
![Paste_Image.png](http://upload-images.jianshu.io/upload_images/5363507-78e54509580bcd6a.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
2、对本地图片处理的三种效果
![Paste_Image.png](http://upload-images.jianshu.io/upload_images/5363507-548f00bd21d6d8fd.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

![](http://upload-images.jianshu.io/upload_images/5363507-5734d40402d6d8ed.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

##如何搭建图片加载框架？
1、肯定不能光自己爽，你要想到用到地方的可能性。
2、同时，代码要一目十行，一看就明白这个方法是做什么，如果别人想扩展，要很容易。
3、基于Glide图片加载框架，使用起来要务必方便，代码不臃肿，能够满足各种姿势，各种要求。如果不能满足，自定义起来需务必方便。

###创建依赖
1、这个不需多提，不要什么都放在app目录下，一定要用依赖的形式
![Paste_Image.png](http://upload-images.jianshu.io/upload_images/5363507-4d191c9737376677.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
2、加入依赖
` compile 'com.github.bumptech.glide:glide:3.6.1`
3、包名结构

![Paste_Image.png](http://upload-images.jianshu.io/upload_images/5363507-2becb7eaf8f40fe5.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
a、关键接口定义：需要根据业务逻辑不断的修改，明确几点即可，哪里需要用到图片加载？Fragment中使用，Activity中使用，加载需要监听的地方，需要给与加载中或者是加载失败的图片过度，需要加载本地图片，需要对图片进行处理（圆形，倒角，模糊图等效果），反之即可定义此接口，方便以后扩展更加复杂的图片处理。
```
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

```
b、需要根据业务定义自定义的接口回调,以下三个接口回调的用处是为了获取缓存中的bitmap、获取加载完成后的drawable，获取下载失败成功的回调
```
/**
 * Created by shiming on 2016/10/26.
 */

public interface IGetBitmapListener {
    void onBitmap(Bitmap bitmap);
}

/**
 * Created by shiming on 2016/10/26.
 * 设置此皆苦是为了业务需要，一般不需要关心网络请求回来的drawable，但是业务需要切换的地方的话，需要拿到网络请求回来的drawable
 */

public interface IGetDrawableListener {
    void onDrawable(Drawable drawable);
}

/**
 * Created by shiming on 2016/10/26.
 * 监听图片下载进度
 */
public interface IImageLoaderListener {

    //监听图片下载错误
    void onLoadingFailed(String url, ImageView target, Exception exception);
   //监听图片加载成功
    void onLoadingComplete(String url, ImageView target);

}
```
c、主要的实现类
```

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
    //关心context
    @Override
    public void displayImageInResource(Context context, int resId,  ImageView imageView, int defRes, IBitmapTransformation... transformations) {
        int size = transformations.length;
        GlideTransform[] glideTransforms = new GlideTransform[size];

        for (int i = 0; i < size; i++) {
            glideTransforms[i] = new GlideTransform(transformations[i].getContext(), transformations[i]);
        }
        Glide.with(context).load(resId).diskCacheStrategy(DiskCacheStrategy.NONE).placeholder(defRes).error(defRes).transform(glideTransforms).into(imageView);
    }
    //关心fragment
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

```
d、项目中调用的主体实现类:在此构建此类中需要注意两点（java并发编程）：
1、volatile 关键字：我个人理解的是：使用volatile关键字的程序在并发时能够正确执行。但是它不能够代替synchronized关键字。当初我在构建的此图片框架时，我领导说过，需加上此关键字，本人才疏学浅，能初步理解此用途，但是还是不够深入了解。但是在初期不加这个关键字，好像对图片加载并无什么影响。在网上找到这么一句话：观察加入volatile关键字和没有加入volatile关键字时所生成的汇编代码发现，加入volatile关键字时，会多出一个lock前缀指令lock前缀指令实际上相当于一个内存屏障（也成内存栅栏），内存屏障会提供3个功能：1）它确保指令重排序时不会把其后面的指令排到内存屏障之前的位置，也不会把前面的指令排到内存屏障的后面；即在执行到内存屏障这句指令时，在它前面的操作已经全部完成；2）它会强制将对缓存的修改操作立即写入主存；3）如果是写操作，它会导致其他CPU中对应的缓存行无效。
2、synchronized（记得双重的判断）关键字是防止多个线程同时执行一段代码，那么就会很影响程序执行效率，而volatile关键字在某些情况下性能要优于synchronized，至于那些情况我也不太明白，因为涉及到java内存模型的相关概念，抛砖引玉一下：http://www.importnew.com/18126.html
```

public class ImageLoader implements IImageLoaderClient {
    private volatile static ImageLoader instance;
    private IImageLoaderClient client;
    private ImageLoader() {
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

    public static ImageLoader getInstance() {
        if (instance == null) {
            synchronized (ImageLoader.class) {
                if (instance == null) {
                    instance = new ImageLoader();
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
```
##项目中的使用
使用的方式及其简单，一行带代码搞定
```
  //圆形图片
        ImageLoader.getInstance().displayCircleImage(this,"http://imgsrc.baidu.com/imgad/pic/item/267f9e2f07082838b5168c32b299a9014c08f1f9.jpg",mImageView_1,R.mipmap.ic_launcher_round);
        //圆角图片
        ImageLoader.getInstance().displayRoundImage(this,"http://imgsrc.baidu.com/imgad/pic/item/267f9e2f07082838b5168c32b299a9014c08f1f9.jpg",mImageView_2,R.mipmap.ic_launcher_round,20);
        //模糊图片
        ImageLoader.getInstance().displayBlurImage(this,"http://imgsrc.baidu.com/imgad/pic/item/267f9e2f07082838b5168c32b299a9014c08f1f9.jpg",mImageView_3,R.mipmap.ic_launcher_round,20);

        //本地图片
        ImageLoader.getInstance().displayImageInResource(this,R.mipmap.test,mImageView_4);
        ImageLoader.getInstance().displayImageInResource(this,R.mipmap.test,mImageView_5,new BlurBitmapTransformation(this,40));
        ImageLoader.getInstance().displayImageInResource(this,R.mipmap.test,mImageView_6,new CircleBitmapTransformation(this));
        ImageLoader.getInstance().displayImageInResource(this,R.mipmap.test,mImageView_6,new RoundBitmapTransformation(this,40));

```

##最后，代码已上传git，欢迎提意见
https://github.com/Shimingli/ImageLoader