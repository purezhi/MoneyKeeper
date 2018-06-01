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
