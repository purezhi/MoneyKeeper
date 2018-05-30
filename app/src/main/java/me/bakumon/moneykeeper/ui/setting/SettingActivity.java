package me.bakumon.moneykeeper.ui.setting;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.customtabs.CustomTabsIntent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;

import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.bakumon.moneykeeper.R;
import me.bakumon.moneykeeper.Router;
import me.bakumon.moneykeeper.base.BaseActivity;
import me.bakumon.moneykeeper.databinding.ActivitySettingBinding;
import me.bakumon.moneykeeper.utill.AlipayZeroSdk;
import me.bakumon.moneykeeper.utill.ToastUtils;
import me.drakeet.floo.Floo;

/**
 * 设置
 *
 * @author Bakumon https://bakumon.me
 */
public class SettingActivity extends BaseActivity {
    private static final String TAG = SettingActivity.class.getSimpleName();
    private ActivitySettingBinding mBinding;
    private SettingViewModel mViewModel;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void onInit(@Nullable Bundle savedInstanceState) {
        mBinding = getDataBinding();
        mViewModel = ViewModelProviders.of(this).get(SettingViewModel.class);

        initView();
    }

    private void initView() {
        mBinding.titleBar.ibtClose.setOnClickListener(v -> finish());
        mBinding.titleBar.setTitle(getString(R.string.text_title_setting));

        mBinding.rvSetting.setLayoutManager(new LinearLayoutManager(this));
        SettingAdapter adapter = new SettingAdapter(null);

        List<SettingSectionEntity> list = new ArrayList<>();

        list.add(new SettingSectionEntity("记账"));
        list.add(new SettingSectionEntity(new SettingSectionEntity.Item("极速记账", "打开应用立即记账", true)));
        list.add(new SettingSectionEntity(new SettingSectionEntity.Item("收支类型管理", null, false)));

        list.add(new SettingSectionEntity("备份"));
        list.add(new SettingSectionEntity(new SettingSectionEntity.Item("立即备份", "备份文件将保存到/sdcard/backup_moneykeeper", false)));
        list.add(new SettingSectionEntity(new SettingSectionEntity.Item("恢复备份", "备份文件将从/sdcard/backup_moneykeeper读取", false)));
        list.add(new SettingSectionEntity(new SettingSectionEntity.Item("自动备份", "数据有改变自动备份（建议开启）", true)));

        list.add(new SettingSectionEntity("关于|帮助"));
        list.add(new SettingSectionEntity(new SettingSectionEntity.Item("关于", "了解我们的设计理念", false)));
        list.add(new SettingSectionEntity(new SettingSectionEntity.Item("评分", "给个好评呗\uD83D\uDE18", false)));
        list.add(new SettingSectionEntity(new SettingSectionEntity.Item("捐赠作者", "", false)));
        list.add(new SettingSectionEntity(new SettingSectionEntity.Item(null, "隐私政策", false)));
        list.add(new SettingSectionEntity(new SettingSectionEntity.Item(null, "开源许可证", false)));
        list.add(new SettingSectionEntity(new SettingSectionEntity.Item(null, "帮助", false)));

        adapter.setNewData(list);

        adapter.setOnItemClickListener((adapter1, view, position) -> {
            switch (position) {
                case 0:
                    break;
                case 1:
                    break;
                case 2:
                    goTypeManage();
                    break;
                case 3:
                    break;
                case 4:
                    showBackupDialog();
                    break;
                case 5:
                    showRestoreDialog();
                    break;
                case 6:
                    break;
                case 7:
                    break;
                case 8:
                    break;
                case 9:
                    market();
                    break;
                case 10:
                    alipay();
                    break;
                case 11:
                    openWeb();
                    break;
                case 12:
                    break;
                default:
                    break;
            }
        });
        mBinding.rvSetting.setAdapter(adapter);
    }

    private void showBackupDialog() {
        new AlertDialog.Builder(this)
                .setTitle("立即备份")
                .setMessage("备份文件将保存到/sdcard/backup_moneykeeper/MoneyKeeperBackupUser.db")
                .setNegativeButton(R.string.text_button_cancel, null)
                .setPositiveButton(R.string.text_affirm, (dialog, which) -> {
                    AndPermission.with(this)
                            .runtime()
                            .permission(Permission.Group.STORAGE)
                            .onGranted(permissions -> {
                                backupDB();
                            })
                            .onDenied(permissions -> {
                                ToastUtils.show("备份数据需要开启读写权限");
                            })
                            .start();
                })
                .create()
                .show();
    }

    private void backupDB() {
        mDisposable.add(mViewModel.backupDB()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> ToastUtils.show("备份成功"),
                        throwable -> {
                            ToastUtils.show("备份失败");
                            Log.e(TAG, "备份失败", throwable);
                        }));
    }

    private void showRestoreDialog() {
        AndPermission.with(this)
                .runtime()
                .permission(Permission.Group.STORAGE)
                .onGranted(permissions -> {
                    mDisposable.add(mViewModel.getBackupFiles()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(backupBeans -> {
                                        BackupFliesDialog dialog = new BackupFliesDialog(this, backupBeans);
                                        dialog.setOnItemClickListener(file -> restoreDB(file.getPath()));
                                        dialog.show();
                                    },
                                    throwable -> {
                                        ToastUtils.show(R.string.toast_backup_list_fail);
                                        Log.e(TAG, "备份文件列表获取失败", throwable);
                                    }));
                })
                .onDenied(permissions -> {
                    ToastUtils.show("备份数据需要开启读写权限");
                })
                .start();
    }

    private void restoreDB(String restoreFile) {
        mDisposable.add(mViewModel.restoreDB(restoreFile)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> Floo.stack(this)
                                .target(Router.IndexKey.INDEX_KEY_HOME)
                                .result("refresh")
                                .start(),
                        throwable -> {
                            ToastUtils.show(R.string.toast_restore_fail);
                            Log.e(TAG, "恢复备份失败", throwable);
                        }));
    }

    private void goTypeManage() {
        Floo.navigation(this, Router.Url.URL_TYPE_MANAGE)
                .start();
    }

    private void market() {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("market://details?id=" + getPackageName()));
            startActivity(intent);
        } catch (Exception e) {
            ToastUtils.show(R.string.toast_not_install_market);
            e.printStackTrace();
        }
    }

    private void alipay() {
        // https://fama.alipay.com/qrcode/qrcodelist.htm?qrCodeType=P  二维码地址
        // http://cli.im/deqr/ 解析二维码
        // aex01251c8foqaprudcp503
        if (AlipayZeroSdk.hasInstalledAlipayClient(this)) {
            AlipayZeroSdk.startAlipayClient(this, "aex01251c8foqaprudcp503");
        } else {
            ToastUtils.show(R.string.toast_not_install_alipay);
        }
    }

    private void openWeb() {
        String url = "https://github.com/Bakumon/MoneyKeeper/blob/master/PrivacyPolicy.md";
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        // github 黑色把
        builder.setToolbarColor(Color.parseColor("#ff24292d"));
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(this, Uri.parse(url));
    }
}
