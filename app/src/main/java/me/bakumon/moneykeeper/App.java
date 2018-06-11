/*
 * Copyright 2018 Bakumon. https://github.com/Bakumon
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package me.bakumon.moneykeeper;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

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
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
        // Normal app init code...
        INSTANCE = this;
        Map<String, Target> mappings = new HashMap<>(8);
        mappings.put(Router.Url.URL_HOME, new Target("mk://bakumon.me/home"));
        mappings.put(Router.Url.URL_ADD_RECORD, new Target("mk://bakumon.me/addRecord"));
        mappings.put(Router.Url.URL_TYPE_MANAGE, new Target("mk://bakumon.me/typeManage"));
        mappings.put(Router.Url.URL_TYPE_SORT, new Target("mk://bakumon.me/typeSort"));
        mappings.put(Router.Url.URL_ADD_TYPE, new Target("mk://bakumon.me/addType"));
        mappings.put(Router.Url.URL_STATISTICS, new Target("mk://bakumon.me/statistics"));
        mappings.put(Router.Url.URL_TYPE_RECORDS, new Target("mk://bakumon.me/typeRecords"));
        mappings.put(Router.Url.URL_SETTING, new Target("mk://bakumon.me/setting"));
        mappings.put(Router.Url.URL_OPEN_SOURCE, new Target("mk://bakumon.me/openSource"));
        mappings.put(Router.Url.URL_ABOUT, new Target("mk://bakumon.me/about"));

        Floo.configuration()
                .setDebugEnabled(BuildConfig.DEBUG)
                .addRequestInterceptor(new PureSchemeInterceptor(getString(R.string.scheme)))
                .addRequestInterceptor(new LogInterceptor("Request"))
                .addTargetInterceptor(new PureSchemeInterceptor(getString(R.string.scheme)))
                .addTargetInterceptor(new LogInterceptor("Target"))
                .addTargetNotFoundHandler(new OpenDirectlyHandler());

        Floo.apply(mappings);
    }
}
