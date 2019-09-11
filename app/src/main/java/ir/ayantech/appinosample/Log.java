package ir.ayantech.appinosample;

public class Log {
    public static <T> void print(T output) {
        android.util.Log.i("AppinoSample", "" + output);
    }
}
