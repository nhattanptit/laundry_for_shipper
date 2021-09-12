package com.laundry.app.view.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.laundry.app.R;
import com.laundry.app.constant.Constant;
import com.laundry.app.control.ApiServiceOperator;
import com.laundry.app.control.DataController;
import com.laundry.app.data.APIConstant;
import com.laundry.app.databinding.LoginDialogBinding;
import com.laundry.app.dto.UserInfo;
import com.laundry.app.dto.authentication.LoginRequest;
import com.laundry.app.dto.authentication.LoginResponseDto;
import com.laundry.app.dto.authentication.SocialLoginRequest;
import com.laundry.app.dto.authentication.SocialLoginRequestLite;
import com.laundry.app.utils.ErrorDialog;
import com.laundry.app.utils.SharePreferenceManager;
import com.laundry.app.view.activity.OrderConfirmActivity;
import com.laundry.base.BaseDialog;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

public class LoginDialog extends BaseDialog<LoginDialogBinding> implements ApiServiceOperator.OnResponseListener<LoginResponseDto> {

    private static final String TAG = LoginDialog.class.getSimpleName();

    private LoginRequest mLoginRequest = new LoginRequest();
    private final DataController mDataController = new DataController();
    private LoginListener mLoginListener;
    private String currentTab;
    private String mMode;
    private GoogleSignInClient mGoogleSignInClient;
    private SocialLoginRequest mSocialLoginRequest = new SocialLoginRequest();
    private boolean isSocialLogin;
    private Uri profileImage;

    public static LoginDialog newInstance(String currentTab) {
        LoginDialog loginDialog = new LoginDialog();
        Bundle bundle = new Bundle();
        bundle.putString(Constant.CURRENT_TAB, currentTab);
        loginDialog.setArguments(bundle);

        return loginDialog;
    }


    public interface LoginListener {
        void onLoginSuccess();

        void onLoginSuccess(String currentTab);
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            this.mLoginListener = (LoginListener) context;
        } catch (ClassCastException e) {
            throw e;
        }
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.login_dialog;
    }

    @Override
    public void onInitView() {
        if (getArguments() != null) {
            currentTab = getArguments().getString(Constant.CURRENT_TAB);
            mMode = getArguments().getString(Constant.ROLE_SWITCH);
            if (!TextUtils.isEmpty(mMode)) {
                SharePreferenceManager.setMode(getMyActivity(), mMode);
            }
        }


        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(getMyActivity(), gso);

        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getMyActivity());

    }

    @Override
    public void onViewClick() {
        binding.login.setOnClickListener(view -> {
            login();
            isSocialLogin = false;
        });

        binding.loginWithGoogle.setOnClickListener(v -> {
            signInGoogle();
            isSocialLogin = true;
        });
    }

    @Override
    protected boolean dismissByTouchOutside() {
        return false;
    }

    private void login() {
        if (validate()) {
            beforeCallApi();
            mDataController.login(getMyActivity(), mLoginRequest, this);
        }
    }

    private void beforeCallApi() {
        binding.progressBar.maskviewLayout.setVisibility(View.VISIBLE);
    }

    private void afterCallApi() {
        binding.progressBar.maskviewLayout.setVisibility(View.GONE);
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
        if (TextUtils.equals(APIConstant.STATUS_CODE_SUCCESS, body.statusCd)) {
            UserInfo userInfo = UserInfo.getInstance();
            userInfo.setToken(getMyActivity(), String.format(Constant.TOKEN_FORMAT, body.data.type, body.data.token));
            userInfo.setUsername(getMyActivity(), body.data.username);

            if (mLoginListener != null) {
                mLoginListener.onLoginSuccess();
                mLoginListener.onLoginSuccess(currentTab);

            }
            if (isSocialLogin) {
                SharePreferenceManager.setUserAvatarSocialLogin(getMyActivity(), profileImage.toString());
            }
            this.dismiss();
        } else if (TextUtils.equals(APIConstant.STATUS_CODE_EMAIL_NOT_EXIST, body.statusCd)) {
            this.dismiss();
            AddAddressDialog addAddressDialog = AddAddressDialog.newInstance(body.data.socialName, body.data.email, AddAddressDialog.TRANSITION_NO_SOCIAL_LOGIN, currentTab);
            addAddressDialog.show(getMyActivity().getSupportFragmentManager(), AddAddressDialog.class.getSimpleName());
        } else if (TextUtils.equals(APIConstant.STATUS_CODE_EMAIL_EXIST, body.statusCd)) {
            AlertDialog alertDialog = ErrorDialog.buildPopupOnlyPositive(getMyActivity(),
                    body.message, R.string.ok, (dialogInterface, i) -> {

                    });
            alertDialog.show();
        }
        afterCallApi();
    }

    @Override
    public void onFailure(Throwable t) {
        afterCallApi();
    }

    private void signInGoogle() {
        beforeCallApi();
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();

        someActivityResultLauncher.launch(signInIntent);
    }

    private final ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    // The Task returned from this call is always completed, no need to attach
                    // a listener.
                    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                    handleSignInResult(task);

                }
            });

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        if (completedTask.isSuccessful()) {
            try {
                GoogleSignInAccount account = completedTask.getResult(ApiException.class);
                handleAfterLogin(account);
                // Signed in successfully, show authenticated UI.
                Log.d("TAG", "handleSignInResult: ");
            } catch (ApiException e) {
                // The ApiException status code indicates the detailed failure reason.
                // Please refer to the GoogleSignInStatusCodes class reference for more information.
            }
        }
    }

    private void handleAfterLogin(GoogleSignInAccount account) {
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getMyActivity());
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personEmail = acct.getEmail();
            profileImage = acct.getPhotoUrl();

            Log.d(TAG, "handleAfterLogin: "+ personEmail + personName);
            SocialLoginRequestLite socialLoginRequestLite = new SocialLoginRequestLite();
            socialLoginRequestLite.name = personName;
            socialLoginRequestLite.email = personEmail;
            mDataController.socialLogin(socialLoginRequestLite, this);
        }
    }
}


