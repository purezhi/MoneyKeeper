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

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.reactivex.disposables.CompositeDisposable;


/**
 * 1.ViewDataBinding 封装
 * 2.数据懒加载：配合 ViewPager 时，需要 ViewPager#setOffscreenPageLimit 为最大
 * https://github.com/Lesincs/LazyInitFrag-Demo/
 * https://juejin.im/post/5a9398b56fb9a0634e6cb19a
 *
 * @author Bakumon https://bakumon.me
 * @date 2018/5/23
 */

public abstract class BaseFragment extends Fragment {

    /**
     * 标志位 判断数据是否初始化
     */
    private boolean isInitData = false;
    /**
     * 标志位 判断fragment是否可见
     */
    private boolean isVisibleToUser = false;
    /**
     * 标志位 判断view已经加载完成 避免空指针操作
     */
    private boolean isPrepareView = false;
    private ViewDataBinding dataBinding;
    protected final CompositeDisposable mDisposable = new CompositeDisposable();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        dataBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);
        View rootView = dataBinding.getRoot();
        onInit(savedInstanceState);
        return rootView;
    }

    /**
     * 子类必须实现，用于创建 view
     *
     * @return 布局文件 Id
     */
    @LayoutRes
    protected abstract int getLayoutId();

    /**
     * 开始的方法
     *
     * @param savedInstanceState 保存的 Bundle
     */
    protected abstract void onInit(@Nullable Bundle savedInstanceState);

    /**
     * 获取 ViewDataBinding
     *
     * @param <T> BaseFragment#getLayoutId() 布局创建的 ViewDataBinding
     *            如 R.layout.fragment_demo 会创建出 FragmentDemoBinding.java
     * @return T
     */
    protected <T extends ViewDataBinding> T getDataBinding() {
        return (T) dataBinding;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isPrepareView = true;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
        initData();
    }

    /**
     * 懒加载方法
     */
    private void initData() {
        if (!isInitData && isVisibleToUser && isPrepareView) {
            isInitData = true;
            lazyInitData();
        }
    }

    /**
     * 加载数据的方法,由子类实现
     */
    protected abstract void lazyInitData();

    protected View inflate(@LayoutRes int resource) {
        return getLayoutInflater().inflate(resource, null, false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDisposable.clear();
    }
}
