package com.laundry.app.view.dialog;

import com.laundry.app.R;
import com.laundry.app.databinding.LoginDialogBinding;
import com.laundry.app.databinding.RegisterAccountDialogBinding;
import com.laundry.base.BaseDialog;

public class RegisterAccountDialog extends BaseDialog<RegisterAccountDialogBinding> {

    @Override
    protected int getLayoutResource() {
        return R.layout.register_account_dialog;
    }

    @Override
    public void onInitView() {

    }

    @Override
    public void onViewClick() {

    }

    @Override
    protected boolean dismissByTouchOutside() {
        return false;
    }

}
