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

import me.bakumon.moneykeeper.utill.SPUtils;

/**
 * 管理本地配置
 *
 * @author Bakumon https://bakumon.me
 */
public class ConfigManager {
    private static final String SP_NAME = "config";
    private static final String KEY_AUTO_BACKUP = "auto_backup";

    public static boolean setIsAutoBackup(boolean isAutoBackup) {
        return SPUtils.getInstance(SP_NAME).put(KEY_AUTO_BACKUP, isAutoBackup);
    }

    public static boolean isAutoBackup() {
        return SPUtils.getInstance(SP_NAME).getBoolean(KEY_AUTO_BACKUP, true);
    }

}
