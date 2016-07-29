package myapp.util;

import android.app.Application;

/**
 * Created by Diogo on 29/07/2016.
 */
public class MyApp extends Application {

    private static MyApp instance;
    public static MyApp get() { return instance; }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}
