package ir.ayantech.appinosample;

import android.app.Application;
import android.content.Context;

import ir.ayantech.appino.Appino;

public class AppController extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        initializeAppino(getApplicationContext());
    }

    private void initializeAppino(Context context) {
        Appino.getInstance(context)
                .setDebugEnabled(true)
                .build();
    }
}
