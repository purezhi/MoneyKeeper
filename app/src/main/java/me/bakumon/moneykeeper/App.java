package me.bakumon.moneykeeper;

import android.app.Application;

import java.util.HashMap;
import java.util.Map;

import me.drakeet.floo.Floo;
import me.drakeet.floo.Target;
import me.drakeet.floo.extensions.LogInterceptor;
import me.drakeet.floo.extensions.OpenDirectlyHandler;

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
        Map<String, Target> mappings = new HashMap<>(4);
        mappings.put(Router.HOME, new Target("mk://bakumon.me/home"));
        mappings.put(Router.ADD_RECORD, new Target("mk://bakumon.me/addRecord"));
        mappings.put(Router.TYPE_MANAGE, new Target("mk://bakumon.me/typeManage"));
        mappings.put(Router.TYPE_SORT, new Target("mk://bakumon.me/typeSort"));
        mappings.put(Router.ADD_TYPE, new Target("mk://bakumon.me/addType"));

        Floo.configuration()
                .setDebugEnabled(BuildConfig.DEBUG)
                .addRequestInterceptor(new LogInterceptor("Request"))
                .addTargetInterceptor(new LogInterceptor("Target"))
                .addTargetNotFoundHandler(new OpenDirectlyHandler());

        Floo.apply(mappings);
    }
}
