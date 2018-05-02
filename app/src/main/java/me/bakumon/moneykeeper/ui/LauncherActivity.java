package me.bakumon.moneykeeper.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import me.bakumon.moneykeeper.ui.home.HomeActivity;

/**
 * LauncherActivity
 *
 * @author bakumon https://bakumon.me
 * @date 2018/5/2
 */
public class LauncherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }
}
