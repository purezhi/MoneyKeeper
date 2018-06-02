package me.bakumon.moneykeeper.datasource;

import me.bakumon.moneykeeper.App;
import me.bakumon.moneykeeper.R;

/**
 * 备份失败异常
 *
 * @author Bakumon https://bakumon.me
 */
public class BackupFailException extends Exception {
    public BackupFailException() {
        super(App.getINSTANCE().getString(R.string.text_tip_backup_fail));
    }
}
