package code.shiming.com.imageloader471.tranform;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

/**
 * Created by shiming on 2016/10/26.
 * 处理模糊图片，但是不能达到高斯模糊的效果
 */


public class BlurBitmapTransformation implements IBitmapTransformation {

    private int radius;
    private Context context;

    public BlurBitmapTransformation(Context context, int radius) {
        this.radius = radius;
        this.context = context;
    }

    @Override
    public Bitmap transform(Bitmap source, ImageView imageView) {

        if (source == null) {
            return null;
        }

        if (radius <= 0) {
            radius = 25;
        }

        Bitmap target;

//        if(Build.VERSION.SDK_INT >= 17){
//            target = Bitmap.createBitmap(source.getWidth(),source.getHeight(),Bitmap.Config.ARGB_8888);
//            final RenderScript rs = RenderScript.create(context.getApplicationContext());
//            final Allocation input = Allocation.createFromBitmap(rs, source);
//            final Allocation output = Allocation.createFromBitmap(rs, target);
//            final ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
//            script.setRadius(radius);
//            script.setInput(input);
//            script.forEach(output);
//            output.copyTo(target);
//            rs.destroy();
//            context = null;
//            return target;
//        }

        target = blur(source, radius);
        context = null;
        return target;

    }

    @Override
    public Context getContext() {
        return context;
    }

    /**
     * 当设置过大的radius 容易内存溢出
     * at iamgeloader.client.tranform.BlurBitmapTransformation.blur(BlurBitmapTransformation.java:80)
     * @param source
     * @param radius
     * @return
     */
    private Bitmap blur(Bitmap source, int radius) {
        Bitmap bitmap = source.copy(source.getConfig(), true);
        int w = source.getWidth();
        int h = source.getHeight();
        int[] pix = new int[w * h];
//        像素颜色写入位图
//        要跳过的像素[ [] ]中的条目数
//*行（必须是=位图的宽度）。
//        从每行读取的像素数
//        要读取的行数。
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);

        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;

        int r[] = new int[wh];
        int g[] = new int[wh];
        int b[] = new int[wh];
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
        int vmin[] = new int[Math.max(w, h)];

        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int dv[] = new int[256 * divsum];
        for (i = 0; i < 256 * divsum; i++) {
            dv[i] = (i / divsum);
        }

        yw = yi = 0;

        int[][] stack = new int[div][3];
        int stackPointer;
        int stackStart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routSum, goutSum, boutSum;
        int rinSum, ginSum, binSum;

        for (y = 0; y < h; y++) {
            rinSum = ginSum = binSum = routSum = goutSum = boutSum = rsum = gsum = bsum = 0;
            for (i = -radius; i <= radius; i++) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
//    12&5 的值是多少？答：12转成二进制数是1100（前四位省略了），
// 5转成二进制数是0101，则运算后的结果为0100即4  这是两侧为数值时；
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;
                if (i > 0) {
                    rinSum += sir[0];
                    ginSum += sir[1];
                    binSum += sir[2];
                } else {
                    routSum += sir[0];
                    goutSum += sir[1];
                    boutSum += sir[2];
                }
            }
            stackPointer = radius;

            for (x = 0; x < w; x++) {

                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];

                rsum -= routSum;
                gsum -= goutSum;
                bsum -= boutSum;

                stackStart = stackPointer - radius + div;
                sir = stack[stackStart % div];

                routSum -= sir[0];
                goutSum -= sir[1];
                boutSum -= sir[2];

                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm);
                }
                p = pix[yw + vmin[x]];

                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);

                rinSum += sir[0];
                ginSum += sir[1];
                binSum += sir[2];

                rsum += rinSum;
                gsum += ginSum;
                bsum += binSum;

                stackPointer = (stackPointer + 1) % div;
                sir = stack[(stackPointer) % div];

                routSum += sir[0];
                goutSum += sir[1];
                boutSum += sir[2];

                rinSum -= sir[0];
                ginSum -= sir[1];
                binSum -= sir[2];

                yi++;
            }
            yw += w;
        }
        for (x = 0; x < w; x++) {
            rinSum = ginSum = binSum = routSum = goutSum = boutSum = rsum = gsum = bsum = 0;
            yp = -radius * w;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;

                sir = stack[i + radius];

                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];

                rbs = r1 - Math.abs(i);

                rsum += r[yi] * rbs;
                gsum += g[yi] * rbs;
                bsum += b[yi] * rbs;

                if (i > 0) {
                    rinSum += sir[0];
                    ginSum += sir[1];
                    binSum += sir[2];
                } else {
                    routSum += sir[0];
                    goutSum += sir[1];
                    boutSum += sir[2];
                }

                if (i < hm) {
                    yp += w;
                }
            }
            yi = x;
            stackPointer = radius;
            for (y = 0; y < h; y++) {
                // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                //在该位置点出现一个萌白的点
                pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16) | (dv[gsum] << 8) | dv[bsum];

                rsum -= routSum;
                gsum -= goutSum;
                bsum -= boutSum;

                stackStart = stackPointer - radius + div;
                sir = stack[stackStart % div];

                routSum -= sir[0];
                goutSum -= sir[1];
                boutSum -= sir[2];

                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vmin[y];

                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];

                rinSum += sir[0];
                ginSum += sir[1];
                binSum += sir[2];

                rsum += rinSum;
                gsum += ginSum;
                bsum += binSum;

                stackPointer = (stackPointer + 1) % div;
                sir = stack[stackPointer];

                routSum += sir[0];
                goutSum += sir[1];
                boutSum += sir[2];

                rinSum -= sir[0];
                ginSum -= sir[1];
                binSum -= sir[2];

                yi += w;
            }
        }

        bitmap.setPixels(pix, 0, w, 0, 0, w, h);

        return bitmap;
    }
}
