package com.laundry.app.view.dialog;

import android.util.Log;

import com.laundry.app.R;
import com.laundry.app.control.ControllerListener;
import com.laundry.app.control.DataController;
import com.laundry.app.databinding.RegisterAccountDialogBinding;
import com.laundry.app.dto.authentication.RegisterResponse;
import com.laundry.base.BaseDialog;

public class RegisterAccountDialog extends BaseDialog<RegisterAccountDialogBinding> {

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
                "nguyenvana",
                "thang123",
                "nguyen van a",
                "thanglv11@gmail.com",
                "0912345678",
                "tp ha noi",
                new ControllerListener<RegisterResponse>() {
            @Override
            public void onSuccess(RegisterResponse registerResponse) {

            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });


    }


    @Override
    protected boolean dismissByTouchOutside() {
        return false;
    }

}
