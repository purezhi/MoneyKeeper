package me.bakumon.moneykeeper.base;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;

import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;

import io.reactivex.disposables.CompositeDisposable;
import me.bakumon.moneykeeper.ConfigManager;
import me.bakumon.moneykeeper.R;
import me.bakumon.moneykeeper.utill.StatusBarUtil;
import me.bakumon.moneykeeper.utill.ToastUtils;

/**
 * 1.沉浸式状态栏
 * 2.ViewDataBinding 封装
 *
 * @author Bakumon
 * @date 18-1-17
 */
public abstract class BaseActivity extends AppCompatActivity {

    private ViewDataBinding dataBinding;
    protected final CompositeDisposable mDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataBinding = DataBindingUtil.setContentView(this, getLayoutId());
        onInit(savedInstanceState);
        checkPermissionForBackup();
    }

    /**
     * 子类必须实现，用于创建 view
     *
     * @return 布局文件 Id
     */
    @LayoutRes
    protected abstract int getLayoutId();

    /**
     * 获取 ViewDataBinding
     *
     * @param <T> BaseActivity#getLayoutId() 布局创建的 ViewDataBinding
     *            如 R.layout.activity_demo 会创建出 ActivityDemoBinding.java
     * @return T
     */
    protected <T extends ViewDataBinding> T getDataBinding() {
        return (T) dataBinding;
    }

    /**
     * 设置沉浸式状态栏
     */
    private void setImmersiveStatus() {
        View[] views = setImmersiveView();
        if (views == null || views.length < 1) {
            return;
        }
        StatusBarUtil.immersive(this);
        for (View view : views) {
            StatusBarUtil.setPaddingSmart(this, view);
        }
    }

    /**
     * 子类重写该方法设置沉浸式状态栏
     *
     * @return null 或 view[]大小为0,则不启用沉浸式
     */
    protected View[] setImmersiveView() {
        ViewGroup rootView = (ViewGroup) dataBinding.getRoot();
        return new View[]{rootView.getChildAt(0)};
    }

    /**
     * 是否已经设置了沉浸式状态栏
     */
    private boolean isSetupImmersive;

    @Override
    protected void onResume() {
        super.onResume();
        if (!isSetupImmersive) {
            setImmersiveStatus();
            isSetupImmersive = true;
        }
    }

    /**
     * 开始的方法
     *
     * @param savedInstanceState 保存的 Bundle
     */
    protected abstract void onInit(@Nullable Bundle savedInstanceState);

    /**
     * inflate view root：null，attachToRoot：false
     *
     * @param resource 布局 id
     * @return view
     */
    protected View inflate(@LayoutRes int resource) {
        return getLayoutInflater().inflate(resource, null, false);
    }

    @Override
    public Resources getResources() {
        // 固定字体大小，不随系统字体大小改变
        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
        return res;
    }

    ///////////////////////////////////////
    ///////// 自动备份打开时，检查是否有权限
    ///////////////////////////////////////

    /**
     * 防止在 onResume 中请求权限
     * 权限被拒绝，方法会走两次
     */
    private boolean isRunningPermission;

    private void checkPermissionForBackup() {
        if (isRunningPermission) {
            return;
        }
        if (!ConfigManager.isAutoBackup()) {
            return;
        }
        if (AndPermission.hasPermissions(this, Permission.Group.STORAGE)) {
            return;
        }
        // 当自动备份打开，并且没有存储权限，提示用户需要申请权限
        isRunningPermission = true;
        new AlertDialog.Builder(this)
                .setCancelable(false)
                .setTitle(R.string.text_storage)
                .setMessage(R.string.text_storage_content)
                .setPositiveButton(R.string.text_affirm, (dialog, which) ->
                        AndPermission.with(this)
                                .runtime()
                                .permission(Permission.Group.STORAGE)
                                .onGranted(data -> {
                                    updateConfig(true);
                                    isRunningPermission = false;
                                })
                                .onDenied(data -> {
                                    isRunningPermission = false;
                                    if (AndPermission.hasAlwaysDeniedPermission(this, data)) {
                                        goSettingForAutoBackup();
                                    } else {
                                        updateConfig(false);
                                    }
                                })
                                .start())
                .show();
    }

    public void updateConfig(boolean isAutoBackup) {
        if (isAutoBackup) {
            ConfigManager.setIsAutoBackup(true);
        } else {
            if (ConfigManager.setIsAutoBackup(false)) {
                ToastUtils.show(R.string.toast_open_auto_backup);
            }
        }
    }

    private void goSettingForAutoBackup() {
        // 这里使用一个Dialog展示没有这些权限应用程序无法继续运行，询问用户是否去设置中授权。
        new AlertDialog.Builder(this)
                .setCancelable(false)
                .setTitle(R.string.text_open_permissions)
                .setMessage(R.string.text_storage_permission_tip)
                .setNegativeButton(R.string.text_button_cancel, (dialog, which) -> {
                    updateConfig(false);
                })
                .setPositiveButton(R.string.text_affirm, (dialog, which) ->
                        AndPermission.with(this)
                                .runtime()
                                .setting()
                                .onComeback(() -> {
                                    boolean hasStorage = AndPermission.hasPermissions(this, Permission.Group.STORAGE);
                                    updateConfig(hasStorage);
                                })
                                .start())
                .create()
                .show();
    }

    ///////////////////////////////////////
    ///////// 为子类提供打开设置的方法
    ///////////////////////////////////////

    public void goSetting(String tip) {
        goSetting(-1, tip);
    }

    /**
     * 提供子类调用
     *
     * @param requestCode 区分不同来源
     */
    public void goSetting(int requestCode, String tip) {
        // 这里使用一个Dialog展示没有这些权限应用程序无法继续运行，询问用户是否去设置中授权。
        new AlertDialog.Builder(this)
                .setCancelable(false)
                .setTitle(R.string.text_open_permissions)
                .setMessage(tip)
                .setNegativeButton(R.string.text_button_cancel, (dialog, which) -> onGoSettingNegativeButtonClick(requestCode))
                .setPositiveButton(R.string.text_affirm, (dialog, which) ->
                        AndPermission.with(this)
                                .runtime()
                                .setting()
                                .onComeback(() -> onComeBackFromSetting(requestCode))
                                .start())
                .create()
                .show();
    }

    /**
     * 去设置界面，点击了取消
     * 子类复写
     */
    public void onGoSettingNegativeButtonClick(int requestCode) {

    }

    /**
     * 用户从设置回来
     * 子类复写
     */
    public void onComeBackFromSetting(int requestCode) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDisposable.clear();
    }

}