package code.shiming.com.imageloader471;

import android.content.Context;
import android.support.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.AppGlideModule;

import java.io.InputStream;

import code.shiming.com.imageloader471.okhttp.ProgressManager;


@GlideModule
public class AppGlideModuleProgress extends AppGlideModule {
    /**
     OkHttp 是一个底层网络库(相较于 Cronet 或 Volley 而言)，尽管它也包含了 SPDY 的支持。
     OkHttp 与 Glide 一起使用可以提供可靠的性能，并且在加载图片时通常比 Volley 产生的垃圾要少。
     对于那些想要使用比 Android 提供的 HttpUrlConnection 更 nice 的 API，
      或者想确保网络层代码不依赖于 app 安装的设备上 Android OS 版本的应用，OkHttp 是一个合理的选择。
      如果你已经在 app 中某个地方使用了 OkHttp ，这也是选择继续为 Glide 使用 OkHttp 的一个很好的理由，就像选择其他网络库一样。
     添加 OkHttp 集成库的 Gradle 依赖将使 Glide 自动开始使用 OkHttp 来加载所有来自 http 和 https URL 的图片
     * @param context
     * @param glide
     * @param registry
     */
    @Override
    public void registerComponents(Context context, Glide glide, Registry registry) {
        super.registerComponents(context, glide, registry);
        registry.replace(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(ProgressManager.getOkHttpClient()));
    }

    /**
     * 默认情况下，Glide使用 LruResourceCache ，
     * 这是 MemoryCache 接口的一个缺省实现，使用固定大小的内存和 LRU 算法。
     * LruResourceCache 的大小由 Glide 的 MemorySizeCalculator 类来决定，这个类主要关注设备的内存类型，
     * 设备 RAM 大小，以及屏幕分辨率。应用程序可以自定义 MemoryCache 的大小，
     * 具体是在它们的 AppGlideModule 中使用applyOptions(Context, GlideBuilder) 方法配置 MemorySizeCalculator
     *
     * 建议使用glide 默认的情况
     * @param context
     * @param builder
     */

    //Glide会自动合理分配内存缓存，但是也可以自己手动分配。
//    @Override
//    public void applyOptions(@NonNull Context context, @NonNull GlideBuilder builder) {
//        super.applyOptions(context, builder);
//        //1、setMemoryCacheScreens设置MemoryCache应该能够容纳的像素值的设备屏幕数，
//        // 说白了就是缓存多少屏图片，默认值是2。
//        //todo 建议不要设置，使用glide 默认
////        MemorySizeCalculator calculator = new MemorySizeCalculator.Builder(context)
////                .setMemoryCacheScreens(2)
////                .build();
////        builder.setMemoryCache(new LruResourceCache(calculator.getMemoryCacheSize()));
//
//        //2。也可以直接覆写缓存大小：todo 建议不要设置，使用glide 默认
////        int memoryCacheSizeBytes = 1024 * 1024 * 20; // 20mb
////        builder.setMemoryCache(new LruResourceCache(memoryCacheSizeBytes));
////        3.甚至可以提供自己的 MemoryCache 实现：
////        builder.setMemoryCache(new YourAppMemoryCacheImpl());
//    }
//

    /**
     * Bitmap 池
     Glide 使用 LruBitmapPool 作为默认的 BitmapPool 。
     LruBitmapPool 是一个内存中的固定大小的 BitmapPool，使用 LRU 算法清理。
     默认大小基于设备的分辨率和密度，同时也考虑内存类和 isLowRamDevice 的返回值。
     具体的计算通过 Glide 的 MemorySizeCalculator 来完成，与 Glide 的 MemoryCache 的大小检测方法相似。
     * @param context
     * @param builder
     */
//    @Override
//    public void applyOptions(@NonNull Context context, @NonNull GlideBuilder builder) {
//        super.applyOptions(context, builder);
//    //1 .应用可以在它们的 AppGlideModule 中定制 [BitmapPool] 的尺寸，
//        // 使用 applyOptions(Context, GlideBuilder) 方法并配置 MemorySizeCalculator
////        MemorySizeCalculator calculator = new MemorySizeCalculator.Builder(context)
////                .setBitmapPoolScreens(3)
////                .build();
////        builder.setBitmapPool(new LruBitmapPool(calculator.getBitmapPoolSize()));
//
//        //2、应用也可以直接复写这个池的大小：
//
////        int bitmapPoolSizeBytes = 1024 * 1024 * 30; // 30mb
////        builder.setBitmapPool(new LruBitmapPool(bitmapPoolSizeBytes));
//
//
//        //3、甚至可以提供 BitmapPool 的完全自定义实现：
//
////        builder.setBitmapPool(new YourAppBitmapPoolImpl());
//    }

    /**
     * 磁盘缓存
     Glide 使用 DiskLruCacheWrapper 作为默认的 磁盘缓存 。
     DiskLruCacheWrapper 是一个使用 LRU 算法的固定大小的磁盘缓存。默认磁盘大小为 250 MB ，
     位置是在应用的 缓存文件夹 中的一个 特定目录 。

     * @param context
     * @param builder
     */
    @Override
    public void applyOptions(@NonNull Context context, @NonNull GlideBuilder builder) {
        super.applyOptions(context, builder);
        //假如应用程序展示的媒体内容是公开的（从无授权机制的网站上加载，或搜索引擎等），
        //那么应用可以将这个缓存位置改到外部存储：
//        builder.setDiskCache(new ExternalDiskCacheFactory(context));


         //2、无论使用内部或外部磁盘缓存，应用程序都可以改变磁盘缓存的大小：
//        int diskCacheSizeBytes = 1024 *1024 *100;  //100 MB
//        builder.setDiskCache(new InternalDiskCacheFactory(context, diskCacheSizeBytes));


        //3、应用程序还可以改变缓存文件夹在外存或内存上的名字：
//        int diskCacheSizeBytes = 1024 * 1024 *100; // 100 MB
//        builder.setDiskCache(new InternalDiskCacheFactory(context, cacheFolderName, diskCacheSizeBytes));


        //4、应用程序还可以自行选择 DiskCache 接口的实现，并提供自己的 DiskCache.Factory 来创建缓存。
        // Glide 使用一个工厂接口来在后台线程中打开 磁盘缓存 ，
        // 这样方便缓存做诸如检查路径存在性等的IO操作而不用触发 严格模式 。
//        builder.setDiskCache(new DiskCache.Factory() {
//            @Override
//            public DiskCache build() {
//                return new YourAppCustomDiskCache();
//            }
//        });

    }

    /**
     * 为了维持对 Glide v3 的 GlideModules 的向后兼容性，
     * Glide 仍然会解析应用程序和所有被包含的库中的 AndroidManifest.xml 文件，
     * 并包含在这些清单中列出的旧 GlideModules 模块类。
     如果你已经迁移到 Glide v4 的 AppGlideModule 和 LibraryGlideModule ，你可以完全禁用清单解析。
     这样可以改善 Glide 的初始启动时间，并避免尝试解析元数据时的一些潜在问题。要禁用清单解析，
     请在你的 AppGlideModule 实现中复写 isManifestParsingEnabled() 方法：
     * @return
     */
    @Override
    public boolean isManifestParsingEnabled() {
        return false;
    }
}
