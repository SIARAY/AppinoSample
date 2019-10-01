package ir.ayantech.appinosample;

import android.content.Context;
import android.graphics.Typeface;

public class Utils {
    public static Typeface getTypeface(Context context) {
        Typeface typeface;
        String fontName= "yekan";
        try {
            typeface = Typeface.createFromAsset(context.getAssets(),  fontName + ".ttf");
        } catch (Exception e) {
            typeface = Typeface.DEFAULT;
        }
        return typeface;
    }
}
