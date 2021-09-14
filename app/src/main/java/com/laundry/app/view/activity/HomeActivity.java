package com.laundry.app.view.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.facebook.FacebookSdk;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.laundry.app.R;
import com.laundry.app.constant.Constant;
import com.laundry.app.control.ApiServiceOperator;
import com.laundry.app.control.DataController;
import com.laundry.app.data.APIConstant;
import com.laundry.app.databinding.HomeBinding;
import com.laundry.app.dto.Role;
import com.laundry.app.dto.UserInfo;
import com.laundry.app.dto.authentication.RegisterRequest;
import com.laundry.app.dto.user.PersonalInfoResponse;
import com.laundry.app.utils.ErrorDialog;
import com.laundry.app.utils.SharePreferenceManager;
import com.laundry.app.view.dialog.LoginDialog;
import com.laundry.app.view.fragment.customer.CustomerInfoFragment;
import com.laundry.app.view.fragment.customer.CustomerOderHistoryListFragment;
import com.laundry.app.view.fragment.customer.HomeFragment;
import com.laundry.app.view.fragment.shipper.ShipperHomeFragment;
import com.laundry.app.view.fragment.shipper.ShipperInfoFragment;
import com.laundry.base.BaseActivity;

import java.util.ArrayList;

public class HomeActivity extends BaseActivity<HomeBinding> implements LoginDialog.LoginListener,
        ShipperHomeFragment.OnClickCallPhone, BaseActivity.ConfigPermission, CustomerInfoFragment.ISCustomerInfoCallBack, CustomerInfoFragment.OnClickAccountInfomationListener {

    private static final String TAG = HomeActivity.class.getSimpleName();
    private String mMode;
    private NavController navController;

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
        } else {
            SharePreferenceManager.setMode(this, mMode);
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
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        navController.setGraph(isCustomer ? R.navigation.customer_navigation : R.navigation.shipper_navigation);
        NavigationUI.setupWithNavController(navView, navController);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        FacebookSdk.sdkInitialize(this);

        // Add permission
        listPermission.add(Manifest.permission.CALL_PHONE);


        SharePreferenceManager.hasVisitedHome(this, true);

    }

    @Override
    public void onViewClick() {

    }


    @Override
    public void onLoginSuccess() {

    }

    @Override
    public void onLoginSuccess(String currentTab) {
        if (TextUtils.equals(Role.CUSTOMER.role(), mMode)) {
            if (TextUtils.equals(HomeFragment.class.getSimpleName(), currentTab)) {
                navController.navigate(R.id.navigation_customer_home);
            } else if (TextUtils.equals(CustomerOderHistoryListFragment.class.getSimpleName(), currentTab)) {
                navController.navigate(R.id.navigation_customer_order_list);
            } else {
                navController.navigate(R.id.navigation_customer_user);
            }
        } else {
            navController.navigate(R.id.navigation_shipper_home);
            navController.navigate(R.id.navigation_shipper_info);
        }

    }

    /**
     * Call phone
     *
     * @param phoneNumber Phone number
     */
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
        // Request permission
        doRequestPermission(simpleArray, this);
    }

    @Override
    public void onAllow() {
        this.callNow(mPhoneNumber);
    }

    @Override
    public void onDenied() {

    }

    public void showMenu() {
        binding.navView.setVisibility(View.VISIBLE);
    }

    public void hideMenu() {
        binding.navView.setVisibility(View.GONE);
    }

    @Override
    public void setPermission() {
        String[] simpleArray = {Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE};
        doRequestPermission(simpleArray, this);
    }

    @Override
    public void onMoveTab() {
        if (TextUtils.equals(Role.CUSTOMER.role(), mMode)) {
            navController.navigate(R.id.navigation_customer_order_list);
        }
    }
}