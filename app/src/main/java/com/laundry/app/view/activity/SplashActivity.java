package com.laundry.app.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;

import com.laundry.app.R;
import com.laundry.app.constant.Constant;
import com.laundry.app.databinding.SplashActivityBinding;
import com.laundry.app.dto.AddressInfo;
import com.laundry.app.dto.Role;
import com.laundry.app.dto.UserInfo;
import com.laundry.app.utils.SharePreferenceManager;
import com.laundry.base.BaseActivity;

public class SplashActivity extends BaseActivity<SplashActivityBinding> {
    @Override
    protected int getLayoutResource() {
        return R.layout.splash_activity;
    }

    @Override
    public void onInitView() {

        // Init address
        AddressInfo.getInstance().init(this);

        Handler handler = new Handler();
        handler.postDelayed(() -> {
            String mode = SharePreferenceManager.getMode(this);
            if (TextUtils.isEmpty(mode)) {
                navigateTo(SplashActivity.this, SwitchModeActivity.class);
            } else {
                if (TextUtils.equals(Role.CUSTOMER.role(), mode)) {
                    navigateTo(SplashActivity.this, HomeActivity.class);
                } else {
                    if (UserInfo.getInstance().isLogin(this)) {
                        navigateTo(SplashActivity.this, HomeActivity.class);
                    } else {
                        Intent intent = new Intent(SplashActivity.this, LoginOrRegisterActivity.class);
                        intent.putExtra(Constant.ROLE_SWITCH, mode);
                        startActivity(intent);
                        overridePendingTransition(com.laundry.base.R.anim.slide_pop_enter_right_to_left, com.laundry.base.R.anim.slide_pop_exit_left_to_right);
                    }
                }
            }
            finish();


        }, 3000);
    }

    @Override
    public void onViewClick() {
    }
}
