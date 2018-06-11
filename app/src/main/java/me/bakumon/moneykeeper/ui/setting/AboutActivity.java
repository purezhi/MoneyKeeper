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
