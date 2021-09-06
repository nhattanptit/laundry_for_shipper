package com.laundry.app.view.dialog;

import com.laundry.app.R;
import com.laundry.app.databinding.RegisterLoginDialogBinding;
import com.laundry.app.view.fragment.customer.HomeFragment;
import com.laundry.base.BaseDialog;

public class RegisterOrLoginFragment extends BaseDialog<RegisterLoginDialogBinding> {

    @Override
    protected int getLayoutResource() {
        return R.layout.register_login_dialog;
    }

    @Override
    public void onInitView() {

    }

    @Override
    public void onViewClick() {
        binding.login.setOnClickListener(v -> {
            LoginDialog loginDialog = new LoginDialog();
            loginDialog.show(getMyActivity().getSupportFragmentManager(), LoginDialog.class.getSimpleName());
            this.dismiss();
        });

        binding.signUp.setOnClickListener(v -> {
            RegisterAccountDialog registerAccountDialog = new RegisterAccountDialog();
            registerAccountDialog.show(getMyActivity().getSupportFragmentManager(), RegisterAccountDialog.class.getSimpleName());
            this.dismiss();
        });
    }

}
