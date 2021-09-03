package com.laundry.app.view.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.laundry.app.R;
import com.laundry.app.constant.Constant;
import com.laundry.app.control.ApiServiceOperator;
import com.laundry.app.control.DataController;
import com.laundry.app.databinding.LoginDialogBinding;
import com.laundry.app.dto.UserInfo;
import com.laundry.app.dto.authentication.LoginRequest;
import com.laundry.app.dto.authentication.LoginResponseDto;
import com.laundry.app.dto.authentication.RegisterResponse;
import com.laundry.base.BaseDialog;

import androidx.annotation.NonNull;

public class LoginDialog extends BaseDialog<LoginDialogBinding> implements ApiServiceOperator.OnResponseListener<LoginResponseDto> {

    private static final String TAG = LoginDialog.class.getSimpleName();

    private LoginRequest mLoginRequest = new LoginRequest();
    private final DataController controller = new DataController();
    private LoginListener mLoginListener;

    public interface LoginListener {
        void onLoginSuccess();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.mLoginListener = (LoginListener) context;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.login_dialog;
    }

    @Override
    public void onInitView() {
    }

    @Override
    public void onViewClick() {
        binding.login.setOnClickListener(view -> {
            login();
        });
    }

    @Override
    protected boolean dismissByTouchOutside() {
        return false;
    }

    private void login() {
        if (validate()) {
            beforeCallApi();
            controller.login(mLoginRequest, this);
        }
    }

    private void beforeCallApi() {
        binding.maskviewLayout.setVisibility(View.VISIBLE);
    }

    private void afterCallApi() {
        binding.maskviewLayout.setVisibility(View.GONE);
    }

    /**
     * Validation data input
     *
     * @return
     */
    private boolean validate() {
        boolean isValid = true;

        // Validate username
        mLoginRequest.loginId = binding.accountUsername.getText().toString();
        if (TextUtils.isEmpty(mLoginRequest.loginId)) {
            isValid = false;
            binding.accountUsernameLayout.setError(Constant.USERNAME_WRONG);
        } else if (mLoginRequest.loginId.length() < 3) {
            isValid = false;
            binding.accountUsernameLayout.setError(Constant.USERNAME_LENGTH_WRONG);
        } else {
            binding.accountUsernameLayout.setError(null);
            binding.accountUsernameLayout.setErrorEnabled(false);
        }

        // Validate password
        mLoginRequest.password = binding.accountPassword.getText().toString();
        if (TextUtils.isEmpty(mLoginRequest.password)) {
            isValid = false;
            binding.accountPasswordLayout.setError(Constant.PASSWORD_WRONG);
        } else if (mLoginRequest.password.length() < 8 || mLoginRequest.password.length() > 15) {
            isValid = false;
            binding.accountPasswordLayout.setError(Constant.PASSWORD_LENGTH_WRONG);
        } else {
            binding.accountPasswordLayout.setError(null);
            binding.accountPasswordLayout.setErrorEnabled(false);
        }
        return isValid;
    }


    @Override
    public void onSuccess(LoginResponseDto body) {
        int returnCd = Integer.parseInt(body.status);
        if (returnCd == 200) {
            UserInfo userInfo = UserInfo.getInstance();
            userInfo.setToken(getMyActivity(), String.format(Constant.TOKEN_FORMAT, body.data.type, body.data.token));
            userInfo.setUsername(getMyActivity(), body.data.username);
            if (mLoginListener != null) {
                mLoginListener.onLoginSuccess();
            }
            this.dismiss();
        }
        afterCallApi();
    }

    @Override
    public void onFailure(Throwable t) {
        afterCallApi();
    }
}


