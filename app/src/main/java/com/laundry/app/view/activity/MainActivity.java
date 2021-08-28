package com.laundry.app.view.activity;

import android.Manifest;
import android.util.Log;

import com.laundry.app.R;
import com.laundry.app.databinding.MainActivityBinding;
import com.laundry.base.BaseActivity;

public class MainActivity extends BaseActivity<MainActivityBinding> {

    private static final String TAG = "MainActivity";
    private final String[] permissionList = {
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    @Override
    protected int getLayoutResource() {
        return R.layout.main_activity;
    }

    @Override
    public void onPreInitView() {

    }

    @Override
    public void onInitView() {
        doRequestPermission(permissionList, new ConfigPermission() {
            @Override
            public void onAllow() {
                Log.d(TAG, "onAllow: ");
            }

            @Override
            public void onDenied() {
                Log.d(TAG, "onDenied: ");
            }
        });
    }

    @Override
    public void onViewClick() {

    }
}
