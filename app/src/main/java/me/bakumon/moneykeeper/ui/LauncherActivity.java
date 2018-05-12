package me.bakumon.moneykeeper.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import me.bakumon.moneykeeper.Router;
import me.drakeet.floo.Floo;

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
        Floo.navigation(this, Router.HOME).start();
        finish();
    }
}
