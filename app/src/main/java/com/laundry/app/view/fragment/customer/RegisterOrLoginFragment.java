package com.laundry.app.view.fragment.customer;

import com.laundry.app.R;
import com.laundry.app.databinding.RegisterLoginFragmentBinding;
import com.laundry.app.view.fragment.LaundryTabFragment;
import com.laundry.base.BaseFragment;

public class RegisterOrLoginFragment extends LaundryTabFragment<RegisterLoginFragmentBinding> {
    @Override
    protected int getLayoutResource() {
        return R.layout.register_login_fragment;
    }

    @Override
    public void onInitView() {

    }

    @Override
    public void onViewClick() {
        binding.login.setOnClickListener(v -> {
            navigateTo(R.id.action_navigation_register_or_login_to_navigation_login_account_dialog);
        });

        binding.signUp.setOnClickListener(v -> {
            navigateTo(R.id.action_navigation_register_or_login_to_navigation_register_account_dialog);
        });
    }

}
