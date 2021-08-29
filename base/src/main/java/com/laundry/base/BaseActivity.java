package com.laundry.base;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.view.InflateException;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

public abstract class BaseActivity<DB extends ViewDataBinding> extends AppCompatActivity implements BaseView {

    private static final int REQUEST_PERMISSION = 10000;
    private DB mBinding;
    private ConfigPermission mConfigPermission;


    public DB getBinding() {
        return mBinding;
    }

    protected abstract int getLayoutResource();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onPreInitView();
        try {
            if (isFixSingleTask()) {
                if (!isTaskRoot()) {
                    finishAffinity();
                    return;
                }
            }
            mBinding = DataBindingUtil.setContentView(this, getLayoutResource());
            mBinding.setLifecycleOwner(this);
            onInitView();
            onViewClick();
        } catch (InflateException | Resources.NotFoundException e) {
            e.printStackTrace();
        }

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.green_8cc63e));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (checkPermission(permissions)) {
            mConfigPermission.onAllow();
        } else {
            mConfigPermission.onDenied();
            ConfigUtils.openAppSetting(this, REQUEST_PERMISSION);
        }
    }

    public boolean isFixSingleTask() {
        return false;
    }

    public void navigateTo(Context context, Class<? extends BaseActivity<?>> destination) {
        Intent intent = new Intent(context, destination);
        startActivity(intent);
//        overridePendingTransition(R.animator.nav_default_enter_anim, R.animator.nav_default_exit_anim);
    }

    public void navigateTo(BaseActivity<?> source, Class<BaseActivity<?>> destination, Bundle bundle) {
        Intent intent = new Intent(source, destination);
        startActivity(intent, bundle);
//        overridePendingTransition(R.animator.nav_default_enter_anim, R.animator.nav_default_exit_anim);
    }

    public void doRequestPermission(String[] permissionList, ConfigPermission callback) {
        this.mConfigPermission = callback;
        if (checkPermission(permissionList)) {
            callback.onAllow();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(permissionList, REQUEST_PERMISSION);
            }
        }
    }

    private boolean checkPermission(String[] permissionList) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (String s : permissionList) {
                if (checkSelfPermission(s) == PackageManager.PERMISSION_GRANTED) {
                    return true;
                }
            }
        }
        return false;
    }

    private void setPortraitScreen() {

    }

    public interface ConfigPermission {
        void onAllow();

        void onDenied();

        default void onNeverAskAgain() {
        }
    }
}
