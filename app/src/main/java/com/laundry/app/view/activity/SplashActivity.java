package com.laundry.app.view.activity;

import android.os.Handler;

import com.laundry.app.R;
import com.laundry.app.databinding.SplashActivityBinding;
import com.laundry.base.BaseActivity;

public class SplashActivity extends BaseActivity<SplashActivityBinding> {
    @Override
    protected int getLayoutResource() {
        return R.layout.splash_activity;
    }

    @Override
    public void onInitView() {
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            navigateTo(SplashActivity.this, SwitchModeActivity.class);
            finish();
        }, 3000);
    }

    @Override
    public void onViewClick() {
    }
}
