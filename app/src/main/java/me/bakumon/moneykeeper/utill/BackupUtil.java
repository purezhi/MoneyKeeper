package me.bakumon.moneykeeper.utill;

import com.snatik.storage.Storage;

import java.io.File;

import me.bakumon.moneykeeper.App;
import me.bakumon.moneykeeper.R;
import me.bakumon.moneykeeper.database.AppDatabase;

/**
 * 备份相关工具类
 *
 * @author Bakumon https:/bakumon.me
 */
public class BackupUtil {
    public static final String BACKUP_DIR = "backup_moneykeeper";
    public static final String BACKUP_DB_NAME = "moneykeeper_backup.db";

    public static boolean autoBackup() {
        Storage storage = new Storage(App.getINSTANCE());
        boolean isWritable = Storage.isExternalWritable();
        if (!isWritable) {
            ToastUtils.show(R.string.toast_is_not_writable_external);
            return false;
        }
        String path = storage.getExternalStorageDirectory() + File.separator + BACKUP_DIR;
        if (!storage.isDirectoryExists(path)) {
            storage.createDirectory(path);
        }
        return storage.copy(App.getINSTANCE().getDatabasePath(AppDatabase.DB_NAME).getPath(), path + File.separator + BACKUP_DB_NAME);
    }

    public static void restoreDB() {
        Storage storage = new Storage(App.getINSTANCE());
        String file = storage.getExternalStorageDirectory() + File.separator + BACKUP_DIR + File.separator + BACKUP_DB_NAME;
        if (storage.isFileExist(file)){
            storage.copy(file, App.getINSTANCE().getDatabasePath(AppDatabase.DB_NAME).getPath());

        }
    }
}
