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

package me.bakumon.moneykeeper.utill;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import me.bakumon.moneykeeper.App;
import me.bakumon.moneykeeper.database.AppDatabase;

/**
 * SharedPreferences 工具类
 *
 * @author Bakumon https://bakumon.me
 */
public class SPUtils {
    private static volatile SPUtils INSTANCE;
    private SharedPreferences sp;

    public static SPUtils getInstance(String spName) {
        if (TextUtils.isEmpty(spName)) {
            spName = "spUtils";
        }
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = new SPUtils(spName);
                }
            }
        }
        return INSTANCE;
    }

    private SPUtils(final String spName) {
        sp = App.getINSTANCE().getSharedPreferences(spName, Context.MODE_PRIVATE);
    }

    public boolean put(@NonNull final String key, final String value) {
        return sp.edit().putString(key, value).commit();
    }

    public String getString(@NonNull final String key) {
        return getString(key, "");
    }

    public String getString(@NonNull final String key, final String defaultValue) {
        return sp.getString(key, defaultValue);
    }

    public boolean put(@NonNull final String key, final int value) {
        return sp.edit().putInt(key, value).commit();
    }

    public int getInt(@NonNull final String key) {
        return getInt(key, -1);
    }

    public int getInt(@NonNull final String key, final int defaultValue) {
        return sp.getInt(key, defaultValue);
    }

    public boolean put(@NonNull final String key, final boolean value) {
        return sp.edit().putBoolean(key, value).commit();
    }

    public boolean getBoolean(@NonNull final String key) {
        return getBoolean(key, false);
    }

    public boolean getBoolean(@NonNull final String key, final boolean defaultValue) {
        return sp.getBoolean(key, defaultValue);
    }

}