package code.shiming.com.imageloader471.tranform;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.widget.ImageView;

/**
 * Created by shiming on 2016/10/26.
 */

public class RoundBitmapTransformation implements IBitmapTransformation {
    private int radius;
    private Context context;


    public RoundBitmapTransformation(Context context, int radius) {
        this.context = context;
        this.radius = radius;
    }

    @Override
    public Bitmap transform(Bitmap source, ImageView imageView) {

        int bw = source.getWidth();
        int bh = source.getHeight();


        Bitmap target = Bitmap.createBitmap(bw, bh, Bitmap.Config.ARGB_8888);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setAntiAlias(true);
        RectF f = new RectF(0, 0, bw, bh);
        Canvas canvas = new Canvas(target);
        canvas.drawRoundRect(f, radius, radius, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(source, 0, 0, paint);
        context = null;
        return target;
    }

    @Override
    public Context getContext() {
        return context;
    }
}
