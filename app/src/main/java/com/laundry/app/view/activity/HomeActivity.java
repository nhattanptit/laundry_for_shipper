package com.laundry.app.view.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.laundry.app.R;
import com.laundry.app.constant.Constant;
import com.laundry.app.databinding.HomeBinding;
import com.laundry.app.dto.Role;
import com.laundry.app.dto.UserInfo;
import com.laundry.app.utils.SharePreferenceManager;
import com.laundry.app.view.dialog.LoginDialog;
import com.laundry.app.view.fragment.shipper.ShipperHomeFragment;
import com.laundry.base.BaseActivity;

import java.util.ArrayList;

import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class HomeActivity extends BaseActivity<HomeBinding> implements LoginDialog.LoginListener,
        ShipperHomeFragment.OnClickCallPhone, BaseActivity.ConfigPermission {


    private static final String TAG = HomeActivity.class.getSimpleName();
    private String mMode;

    @Override
    protected int getLayoutResource() {
        return R.layout.home;
    }

    private String mPhoneNumber;
    private ArrayList<String> listPermission = new ArrayList<>();

    @Override
    public void onInitView() {
        Intent intent = getIntent();
        mMode = intent.getStringExtra(Constant.ROLE_SWITCH);
        if (TextUtils.isEmpty(mMode)) {
            mMode = SharePreferenceManager.getMode(this);
        }

        boolean isCustomer = TextUtils.equals(Role.CUSTOMER.role(), mMode);
        AppBarConfiguration appBarConfiguration;
        if (isCustomer) {
            appBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.navigation_customer_home,
                    R.id.navigation_customer_order_list,
                    R.id.navigation_customer_user)
                    .build();
        } else {
            appBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.navigation_shipper_home,
                    R.id.navigation_shipper_info)
                    .build();
        }

        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.inflateMenu(isCustomer ? R.menu.customer_bottom_nav_menu : R.menu.shipper_bottom_nav_menu);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        navController.setGraph(isCustomer ? R.navigation.customer_navigation : R.navigation.shipper_navigation);
        NavigationUI.setupWithNavController(navView, navController);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        FacebookSdk.sdkInitialize(this);

        // Add permission
        listPermission.add(Manifest.permission.CALL_PHONE);


        navView.setOnItemSelectedListener(item -> {
            if (item.getItemId() != binding.navView.getSelectedItemId())
                NavigationUI.onNavDestinationSelected(item, navController);
            return false;
        });

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

    @Override
    public void onLoginSuccess() {
        Log.d(TAG, "onLoginSuccess: ");
    }

    @SuppressLint("MissingPermission")
    private void callNow(String phoneNumber) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + phoneNumber));
        try {
            this.startActivity(callIntent);
        } catch (Exception ex) {
            Toast.makeText(this, "Your call failed... " + ex.getMessage(),
                    Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    @Override
    public void onCall(String phoneNumber) {
        this.mPhoneNumber = phoneNumber;
        String[] simpleArray = new String[listPermission.size()];
        listPermission.toArray(simpleArray);
        doRequestPermission(simpleArray, this);
    }

    @Override
    public void onAllow() {
        this.callNow(mPhoneNumber);
    }

    @Override
    public void onDenied() {

    }
}