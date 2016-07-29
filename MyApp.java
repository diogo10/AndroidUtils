package ie.ienquire.fff.dreamteam.util;

import android.app.Application;

/**
 * Created by barry on 29/07/2016.
 * DreamTeam
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
