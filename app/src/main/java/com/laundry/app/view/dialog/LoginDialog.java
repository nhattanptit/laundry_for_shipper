package com.laundry.app.view.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
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
import com.laundry.base.BaseDialog;

import org.json.JSONException;

import java.util.Arrays;

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
    private CallbackManager mCallbackManager;

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

        mCallbackManager = CallbackManager.Factory.create();
        // If using in a fragment
        binding.loginWithFacebook.setFragment(this);


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

        binding.loginWithFacebook.setOnClickListener(v -> {
            signInFacebook();
            isSocialLogin = true;
        });
    }

    @Override
    protected boolean dismissByTouchOutside() {
        return false;
    }

    /**
     * onClick signin facebook
     */
    private void signInFacebook() {

        beforeCallApi();

        LoginManager.getInstance().logInWithReadPermissions(
                this,
                Arrays.asList("user_friends", "email", "public_profile"));
        // Callback registration
        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                setFacebookData(loginResult);
            }

            @Override
            public void onCancel() {
                // App code
                LoginManager.getInstance().logOut();
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Toast.makeText(getMyActivity(), "Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Set facebook data
     *
     * @param loginResult
     */
    private void setFacebookData(final LoginResult loginResult) {
        final ProfileTracker[] mProfileTracker = new ProfileTracker[1];
        GraphRequest request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(),
                (object, response) -> {
                    // Application code
                    try {
                        Log.i("Response", response.toString());

                        String email = response.getJSONObject().getString("email");
                        String firstName = response.getJSONObject().getString("first_name");
                        String lastName = response.getJSONObject().getString("last_name");
                        SharePreferenceManager.setUsername(getMyActivity(), firstName + lastName);

                        Profile profile = Profile.getCurrentProfile();

                        if (profile == null) {
                            mProfileTracker[0] = new ProfileTracker() {
                                @Override
                                protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                                    Log.v("facebook - profile", currentProfile.getFirstName());
                                    mProfileTracker[0].stopTracking();
                                }
                            };
                            // no need to call startTracking() on mProfileTracker
                            // because it is called by its constructor, internally.
                        } else {
                            profile = Profile.getCurrentProfile();
                        }

                        profileImage = profile.getLinkUri();
                        if (Profile.getCurrentProfile() != null) {
                            Log.i("Login", "ProfilePic" + Profile.getCurrentProfile().getProfilePictureUri(200, 200));
                        }

                        Log.i("Login" + "Email", email);
                        Log.i("Login" + "FirstName", firstName);
                        Log.i("Login" + "LastName", lastName);

                        callSocialLogin(firstName, email);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,email,first_name,last_name");
        request.setParameters(parameters);
        request.executeAsync();
    }

    /**
     * Call api login
     */
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
            if (isSocialLogin && profileImage != null) {
                SharePreferenceManager.setUserAvatarSocialLogin(getMyActivity(), profileImage.toString());
            }
            this.dismiss();
        } else if (TextUtils.equals(APIConstant.STATUS_CODE_EMAIL_NOT_EXIST, body.statusCd)) {
            this.dismiss();

            AddAddressDialog addAddressDialog = AddAddressDialog.newInstance(body.data.socialName, body.data.email, AddAddressDialog.TRANSITION_NO_SOCIAL_LOGIN, currentTab);
            addAddressDialog.showDialog(getMyActivity().getSupportFragmentManager(), AddAddressDialog.class.getSimpleName());
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
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

            Log.d(TAG, "handleAfterLogin: " + personEmail + personName);
            callSocialLogin(personName, personEmail);
        }
    }

    /**
     * Call social login api
     *
     * @param name
     * @param email
     */
    private void callSocialLogin(String name, String email) {
        SocialLoginRequestLite socialLoginRequestLite = new SocialLoginRequestLite();
        socialLoginRequestLite.name = name;
        socialLoginRequestLite.email = email;
        mDataController.socialLogin(socialLoginRequestLite, this);
    }
}


