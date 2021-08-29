package com.laundry.app.view.activity;

import android.os.Handler;
import android.text.TextUtils;

import com.laundry.app.R;
import com.laundry.app.databinding.SplashActivityBinding;
import com.laundry.app.dto.Role;
import com.laundry.app.utils.SharePreferenceManager;
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
            String mode = SharePreferenceManager.getMode(this);
            if (TextUtils.isEmpty(mode)) {
                navigateTo(SplashActivity.this, SwitchModeActivity.class);
            } else {
                navigateTo(SplashActivity.this, HomeActivity.class);
            }
            finish();
        }, 3000);
    }

    @Override
    public void onViewClick() {
    }
}
