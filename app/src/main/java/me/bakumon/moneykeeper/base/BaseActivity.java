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

package me.bakumon.moneykeeper.base;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;

import io.reactivex.disposables.CompositeDisposable;
import me.bakumon.moneykeeper.utill.StatusBarUtil;

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDisposable.clear();
    }

}