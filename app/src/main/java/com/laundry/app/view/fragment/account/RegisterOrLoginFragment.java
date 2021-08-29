package com.laundry.app.view.fragment.account;

import com.laundry.app.R;
import com.laundry.app.databinding.RegisterLoginFragmentBinding;
import com.laundry.base.BaseFragment;

public class RegisterOrLoginFragment extends BaseFragment<RegisterLoginFragmentBinding> {
    @Override
    protected int getLayoutResource() {
        return R.layout.register_login_fragment;
    }

    @Override
    public void onInitView() {

    }

    @Override
    public void onViewClick() {
        binding.login.setOnClickListener( v -> {
//            navigateTo(R.id.action_navigation_RegisterOrLoginFragment_to_navigation_login);
        });

        binding.signUp.setOnClickListener( v -> {
//            navigateTo(R.id.action_navigation_RegisterOrLoginFragment_to_navigation_register);
        });
    }
}
