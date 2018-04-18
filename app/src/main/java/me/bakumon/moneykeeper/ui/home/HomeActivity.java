package me.bakumon.moneykeeper.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import me.bakumon.moneykeeper.R;
import me.bakumon.moneykeeper.base.BaseActivity;
import me.bakumon.moneykeeper.bean.HomeBean;
import me.bakumon.moneykeeper.databinding.ActivityHomeBinding;
import me.bakumon.moneykeeper.ui.add.AddActivity;

/**
 * HomeActivity
 *
 * @author bakumon https://bakumon.me
 * @date 2018/4/9
 */
public class HomeActivity extends BaseActivity implements View.OnClickListener {

    private ActivityHomeBinding binding;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_home;
    }

    @Override
    protected void onInit(@Nullable Bundle savedInstanceState) {
        binding = getDataBinding();
        binding.rvHome.setLayoutManager(new LinearLayoutManager(this));

        List<HomeBean> data = new ArrayList<>();
        HomeBean bean = new HomeBean();
        bean.createDate = "201803";

        HomeBean bean1 = new HomeBean();
        bean1.createDate = "201804";

        data.add(bean);
        data.add(bean);
        data.add(bean1);
        data.add(bean1);
        data.add(bean1);
        data.add(bean1);
        data.add(bean1);
        data.add(bean1);
        HomeAdapter adapter = new HomeAdapter(data);

        binding.rvHome.setAdapter(adapter);

        binding.fabHomeRandom.setOnClickListener(this);
        binding.itSetting.setOnClickListener(this);
        binding.itStatistics.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.it_setting:
                Toast.makeText(this, "设置", Toast.LENGTH_SHORT).show();
                break;
            case R.id.it_statistics:
                Toast.makeText(this, "统计", Toast.LENGTH_SHORT).show();
                break;
            case R.id.fab_home_random:
                Intent intent = new Intent();
                intent.setClass(this, AddActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
