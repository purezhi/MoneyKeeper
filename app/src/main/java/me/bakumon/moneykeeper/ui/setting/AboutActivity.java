package me.bakumon.moneykeeper.ui.setting;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import me.bakumon.moneykeeper.BuildConfig;
import me.bakumon.moneykeeper.R;
import me.bakumon.moneykeeper.base.BaseActivity;
import me.bakumon.moneykeeper.databinding.ActivityAboutBinding;
import me.bakumon.moneykeeper.utill.AndroidUtil;
import me.bakumon.moneykeeper.utill.CustomTabsUtil;

/**
 * 关于
 *
 * @author Bakumon https://bakumon.me
 */
public class AboutActivity extends BaseActivity {
    private ActivityAboutBinding mBinding;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_about;
    }

    @Override
    protected void onInit(@Nullable Bundle savedInstanceState) {
        mBinding = getDataBinding();

        initView();
    }

    private void initView() {
        mBinding.titleBar.ibtClose.setOnClickListener(v -> finish());
        mBinding.titleBar.setTitle(getString(R.string.text_title_about));

        mBinding.tvVersion.setText(BuildConfig.VERSION_NAME);
    }

    public void goPrivacy(View view) {
        CustomTabsUtil.openWeb(this, "https://github.com/Bakumon/MoneyKeeper/blob/master/PrivacyPolicy.md");
    }

    public void share(View view) {
        AndroidUtil.share(this, getString(R.string.text_share_content));
    }

    public void goHelp(View view) {
        CustomTabsUtil.openWeb(this, "https://github.com/Bakumon/MoneyKeeper/blob/master/Help.md");
    }
}
