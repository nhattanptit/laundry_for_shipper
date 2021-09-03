package com.laundry.app.view.fragment.customer;

import android.text.TextUtils;
import android.view.View;

import com.laundry.app.R;
import com.laundry.app.databinding.CustomerInfoFragmentBinding;
import com.laundry.app.dto.Role;
import com.laundry.app.dto.UserInfo;
import com.laundry.app.utils.SharePreferenceManager;
import com.laundry.app.view.dialog.LoginDialog;
import com.laundry.app.view.dialog.RegisterAccountDialog;
import com.laundry.app.view.fragment.LaundryTabFragment;
import com.laundry.base.BaseFragment;

public class CustomerInfoFragment extends LaundryTabFragment<CustomerInfoFragmentBinding> {

    @Override
    protected int getLayoutResource() {
        return R.layout.customer_info_fragment;
    }

    @Override
    public void onInitView() {

        if (UserInfo.getInstance().isLogin(getMyActivity())) {
            binding.registerLoginLayout.setVisibility(View.GONE);
            binding.textNotifications.setVisibility(View.VISIBLE);
        } else {
            binding.registerLoginLayout.setVisibility(View.VISIBLE);
            binding.textNotifications.setVisibility(View.GONE);
        }

        String mMode = SharePreferenceManager.getMode(getMyActivity());
        if (TextUtils.equals(Role.CUSTOMER.role(), mMode)) {
            binding.registerLoginFragment.signUp.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onViewClick() {
        binding.registerLoginFragment.login.setOnClickListener(v -> {
            LoginDialog loginDialog = LoginDialog.newInstance(CustomerInfoFragment.class.getSimpleName());
            loginDialog.show(getMyActivity().getSupportFragmentManager(), LoginDialog.class.getSimpleName());
        });

        binding.registerLoginFragment.signUp.setOnClickListener(v -> {
            RegisterAccountDialog registerAccountDialog = RegisterAccountDialog.newInstance(CustomerInfoFragment.class.getSimpleName());
            registerAccountDialog.show(getMyActivity().getSupportFragmentManager(), RegisterAccountDialog.class.getSimpleName());
        });
    }
}