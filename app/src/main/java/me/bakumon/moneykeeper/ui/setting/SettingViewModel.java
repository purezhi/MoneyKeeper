package me.bakumon.moneykeeper.ui.setting;

import android.arch.lifecycle.ViewModel;

import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import me.bakumon.moneykeeper.utill.BackupUtil;

/**
 * 设置 ViewModel
 *
 * @author Bakumon https://bakumon.me
 */
public class SettingViewModel extends ViewModel {

    public Flowable<List<BackupBean>> getBackupFiles() {
        return Flowable.create(e -> {
            e.onNext(BackupUtil.getBackupFiles());
            e.onComplete();
        }, BackpressureStrategy.BUFFER);
    }

    public Completable backupDB() {
        return Completable.create(e -> {
            boolean result = BackupUtil.userBackup();
            if (result) {
                e.onComplete();
            } else {
                e.onError(new Exception());
            }
        });
    }

    public Completable restoreDB(String restoreFile) {
        return Completable.create(e -> {
            boolean result = BackupUtil.restoreDB(restoreFile);
            if (result) {
                e.onComplete();
            } else {
                e.onError(new Exception());
            }
        });
    }
}
