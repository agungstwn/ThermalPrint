package id.agung.android.thermalprintlib.helper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by agung on 29/07/18.
 */

public class ImageHalper {
    public static Bitmap resize(Bitmap bitmap){
        if (bitmap.getWidth() > 400){
            float devider = 400 / bitmap.getWidth();
            return Bitmap.createScaledBitmap(bitmap, (int) (bitmap.getWidth()*devider),
                    (int) (bitmap.getHeight()*devider), false);
        }else {
            return bitmap;
        }
    }
}
