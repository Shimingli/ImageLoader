package code.shiming.com.imageloader471.okhttp;

import com.bumptech.glide.load.engine.GlideException;


public interface OnGlideImageViewListener {
    /**
     *
     * @param percent 下载进度的百分比，不关心，大小
     * @param isDone 是否完成
     * @param exception 异常
     */
    void onProgress(int percent, boolean isDone, GlideException exception);
}
