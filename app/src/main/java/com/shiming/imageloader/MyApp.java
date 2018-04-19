package com.shiming.imageloader;

import android.app.Application;
import android.content.Context;



/**
 * @author shiming
 * @version v1.0 create at 2017/9/26
 * @des
 */
public class MyApp extends Application {

    private static Context mInstance;

    public static Context getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance=this;
        //方法失效了 这个还没有成功尝试
//        CompressCore compressCore = new CompressCore();


    }
}
