package com.laundry.app.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.laundry.app.R;
import com.laundry.app.constant.Constant;
import com.laundry.app.databinding.ActivityLoginOrRegisterBinding;
import com.laundry.app.dto.Role;
import com.laundry.app.utils.SingleTapListener;
import com.laundry.app.view.dialog.LoginDialog;
import com.laundry.app.view.dialog.RegisterAccountDialog;
import com.laundry.base.BaseActivity;

public class LoginOrRegisterActivity extends BaseActivity<ActivityLoginOrRegisterBinding> implements LoginDialog.LoginListener {

    private static final String TAG = LoginOrRegisterActivity.class.getSimpleName();
    private String mMode;
    private String mCurrentTab;


    @Override
    protected int getLayoutResource() {
        return R.layout.activity_login_or_register;
    }

    @Override
    public void onInitView() {
        mMode = getIntent().getStringExtra(Constant.ROLE_SWITCH);
        mCurrentTab = getIntent().getStringExtra(Constant.CURRENT_TAB);
        if (TextUtils.equals(Role.SHIPPER.role(), mMode)) {
            binding.registerLoginLayout.signUp.setVisibility(View.GONE);
            binding.registerLoginLayout.goToHomeScreen.setVisibility(View.GONE);
        } else if (TextUtils.equals(Role.CUSTOMER.role(), mMode)) {
            binding.registerLoginLayout.goToHomeScreen.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onViewClick() {
        binding.registerLoginLayout.goToHomeScreen.setOnClickListener(new SingleTapListener(view -> {
            gotoHome();
        }));
        binding.registerLoginLayout.login.setOnClickListener(v -> {
            LoginDialog loginDialog = new LoginDialog();
            Bundle bundle = new Bundle();
            bundle.putString(Constant.ROLE_SWITCH, mMode);
            loginDialog.setArguments(bundle);
            loginDialog.show(getSupportFragmentManager(), LoginDialog.class.getSimpleName());
        });

        binding.registerLoginLayout.signUp.setOnClickListener(v -> {
            RegisterAccountDialog registerAccountDialog = new RegisterAccountDialog();
            registerAccountDialog.show(getSupportFragmentManager(), RegisterAccountDialog.class.getSimpleName());
        });

    }

    @Override
    public void onLoginSuccess() {
        gotoHome();
    }

    @Override
    public void onLoginSuccess(String currentTab) {
        Log.d(TAG, "onLoginSuccess: " + currentTab);
    }

    private void gotoHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(Constant.ROLE_SWITCH, mMode);
        startActivity(intent);
        finish();
    }
}