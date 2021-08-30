package com.laundry.app.view.dialog;

import android.util.Log;

import com.laundry.app.R;
import com.laundry.app.control.ApiServiceOperator;
import com.laundry.app.control.DataController;
import com.laundry.app.databinding.RegisterAccountDialogBinding;
import com.laundry.app.dto.authentication.RegisterResponse;
import com.laundry.base.BaseDialog;

public class RegisterAccountDialog extends BaseDialog<RegisterAccountDialogBinding>
        implements ApiServiceOperator.OnResponseListener<RegisterResponse> {

    private static final String TAG = "RegisterAccountDialog";
    private DataController controller = new DataController();

    @Override
    protected int getLayoutResource() {
        return R.layout.register_account_dialog;
    }

    @Override
    public void onInitView() {

    }

    @Override
    public void onViewClick() {
        binding.registerButton.setOnClickListener(view -> {
            Log.d(TAG, "onViewClick: ");
            registerAccount();
        });
    }

    private void registerAccount() {
        controller.register(
                "tannv11",
                "tan@12345",
                "nguyen van a",
                "tannv11@gmail.com",
                "0111111111",
                "tp ha noi",
                this);
    }


    @Override
    protected boolean dismissByTouchOutside() {
        return false;
    }

    @Override
    public void onSuccess(RegisterResponse body) {
        Log.d(TAG, "onSuccess: " + body);
    }

    @Override
    public void onFailure(Throwable t) {

    }
}
