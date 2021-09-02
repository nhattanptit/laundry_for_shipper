package com.laundry.app.view.activity;

import android.content.Intent;
import android.text.TextUtils;

import com.facebook.FacebookSdk;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.laundry.app.R;
import com.laundry.app.constant.Constant;
import com.laundry.app.databinding.HomeBinding;
import com.laundry.app.dto.Role;
import com.laundry.app.dto.UserInfo;
import com.laundry.app.utils.SharePreferenceManager;
import com.laundry.base.BaseActivity;

import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

public class HomeActivity extends BaseActivity<HomeBinding> {

    private String mMode;

    @Override
    protected int getLayoutResource() {
        return R.layout.home;
    }

    @Override
    public void onInitView() {
        Intent intent = getIntent();
        mMode = intent.getStringExtra(Constant.ROLE_SWITCH);
        if (TextUtils.isEmpty(mMode)) {
            mMode = SharePreferenceManager.getMode(this);
        }

        boolean isCustomer = TextUtils.equals(Role.CUSTOMER.role(), mMode);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.inflateMenu(isCustomer ? R.menu.customer_bottom_nav_menu : R.menu.shipper_bottom_nav_menu);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        navController.setGraph(isCustomer ? R.navigation.customer_navigation : R.navigation.shipper_navigation);
        NavigationUI.setupWithNavController(navView, navController);
        FacebookSdk.sdkInitialize(this);

        if (TextUtils.equals(Role.CUSTOMER.role(), mMode)) {
            if (!UserInfo.getInstance().isLogin(this)) {
                navController.navigate(R.id.navigation_register_or_login, null, new NavOptions.Builder()
                        .setPopUpTo(R.id.navigation_customer_user, true)
                        .build());
            }
        }

    }

    @Override
    public void onViewClick() {

    }
}