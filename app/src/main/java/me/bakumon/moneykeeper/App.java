package me.bakumon.moneykeeper;

import android.app.Application;

/**
 * @author Bakumon https://bakumon.me
 */
public class App extends Application {
    private static App INSTANCE;

    public static App getINSTANCE() {
        return INSTANCE;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
    }
}
