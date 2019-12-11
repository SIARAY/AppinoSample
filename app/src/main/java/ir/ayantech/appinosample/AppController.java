package ir.ayantech.appinosample;

import android.app.Application;
import android.content.Context;

import ir.ayantech.appino.Appino;

import static ir.ayantech.appinosample.Utils.getTypeface;

public class AppController extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        initializeAppino(getApplicationContext());
    }

    private void initializeAppino(Context context) {
        Appino.getInstance(context)
                .setDebugEnabled(true)
                .setTypeface(getTypeface(context),1.5f)
                .build();
    }
}
