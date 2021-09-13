package com.laundry.app.view.fragment.shipper;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import com.laundry.app.R;
import com.laundry.app.constant.Constant;
import com.laundry.app.databinding.ShipperFragmentInfoBinding;
import com.laundry.app.dto.Role;
import com.laundry.app.dto.UserInfo;
import com.laundry.app.utils.SharePreferenceManager;
import com.laundry.app.view.activity.LoginOrRegisterActivity;
import com.laundry.app.view.dialog.LoginDialog;
import com.laundry.app.view.dialog.RegisterAccountDialog;
import com.laundry.app.view.fragment.customer.CustomerInfoFragment;
import com.laundry.base.BaseFragment;

public class ShipperInfoFragment extends BaseFragment<ShipperFragmentInfoBinding> {

    @Override
    protected int getLayoutResource() {
        return R.layout.shipper_fragment_info;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onInitView() {
        if (UserInfo.getInstance().isLogin(getMyActivity())) {
            binding.registerLoginLayout.setVisibility(View.GONE);
            binding.accountInfomationLayout.setVisibility(View.VISIBLE);
            setDataForUser();
        } else {
            binding.registerLoginLayout.setVisibility(View.VISIBLE);
            binding.accountInfomationLayout.setVisibility(View.GONE);
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

        binding.accountInfomationFragment.accountInfomationLogout.setOnClickListener(v -> {
            UserInfo.getInstance().init(getMyActivity());
            logout();
        });
    }

    /**
     * Set data for user
     */
    private void setDataForUser() {
        binding.accountInfomationFragment.accountInfomationUsername.setText(UserInfo.getInstance().getUsername(getMyActivity()));
    }

    /**
     * Logout -> move to home screen
     */
    private void logout() {
        Intent intent = new Intent(getMyActivity(), LoginOrRegisterActivity.class);
        intent.putExtra(Constant.ROLE_SWITCH, Role.SHIPPER.role());
        startActivity(intent);
        getMyActivity().finish();
    }
}