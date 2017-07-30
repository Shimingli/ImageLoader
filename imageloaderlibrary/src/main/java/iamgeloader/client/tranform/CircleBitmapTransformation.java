package iamgeloader.client.tranform;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.widget.ImageView;


/**
 * Created by shiming on 2016/10/26.
 */

public class CircleBitmapTransformation implements IBitmapTransformation {

    private Context context;

    public CircleBitmapTransformation(Context context) {
        this.context = context;
    }

    @Override
    public Bitmap transform(Bitmap source, ImageView imageView) {
        int size = Math.min(source.getWidth(), source.getHeight());

        int width = (source.getWidth() - size) / 2;
        int height = (source.getHeight() - size) / 2;


        Bitmap target = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(target);
        Paint paint = new Paint();

        BitmapShader shader = new BitmapShader(source, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
        if (width != 0 || height != 0) {
            // source isn't square, move viewport to center
            Matrix matrix = new Matrix();
            matrix.setTranslate(-width, -height);
            shader.setLocalMatrix(matrix);
        }
        paint.setShader(shader);
        paint.setAntiAlias(true);

        float r = size / 2f;
        canvas.drawCircle(r, r, r, paint);
        context = null;
        return target;
    }

    @Override
    public Context getContext() {
        return context;
    }
}
