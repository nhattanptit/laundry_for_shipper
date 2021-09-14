package com.laundry.app.view.fragment.customer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.laundry.app.R;
import com.laundry.app.constant.Constant;
import com.laundry.app.control.ApiServiceOperator;
import com.laundry.app.control.DataController;
import com.laundry.app.data.APIConstant;
import com.laundry.app.databinding.CustomerInfoFragmentBinding;
import com.laundry.app.dto.AddressInfo;
import com.laundry.app.dto.Role;
import com.laundry.app.dto.UserInfo;
import com.laundry.app.dto.user.PersonalInfoDto;
import com.laundry.app.dto.user.PersonalInfoResponse;
import com.laundry.app.utils.ErrorDialog;
import com.laundry.app.utils.SharePreferenceManager;
import com.laundry.app.utils.SingleTapListener;
import com.laundry.app.view.activity.HomeActivity;
import com.laundry.app.view.dialog.LoginDialog;
import com.laundry.app.view.dialog.RegisterAccountDialog;
import com.laundry.base.BaseFragment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class CustomerInfoFragment extends BaseFragment<CustomerInfoFragmentBinding> {

    private static final String IMAGE_DIRECTORY = "/sdcard";
    private static final int GALLERY = 1, CAMERA = 2;
    private ISCustomerInfoCallBack mIsCustomerInfoCallBack;

    private OnClickAccountInfomationListener mOnClickAccountInfomation;

    private PersonalInfoDto mPersonalInfoDto;

    private DataController mDataController = new DataController();


    @Override
    protected int getLayoutResource() {
        return R.layout.customer_info_fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mIsCustomerInfoCallBack = (ISCustomerInfoCallBack) context;
        this.mOnClickAccountInfomation = (OnClickAccountInfomationListener) context;
    }

    @Override
    public void onInitView() {
        if (UserInfo.getInstance().isLogin(getMyActivity())) {
            binding.registerLoginLayout.setVisibility(View.GONE);
            binding.accountInformationLayout.setVisibility(View.VISIBLE);
            if (UserInfo.getInstance().getUrlAvatar(getMyActivity()) != null) {
                binding.accountInfomationAvatar.setImageBitmap(decodeToBase64(UserInfo.getInstance().getUrlAvatar(getActivity())));
            }
        } else {
            binding.registerLoginLayout.setVisibility(View.VISIBLE);
            binding.accountInformationLayout.setVisibility(View.GONE);
        }

        String mMode = SharePreferenceManager.getMode(getMyActivity());
        if (TextUtils.equals(Role.CUSTOMER.role(), mMode)) {
            binding.registerLoginFragment.signUp.setVisibility(View.VISIBLE);
        }
        String imageProfile = SharePreferenceManager.getUserAvatarSocialLogin(getMyActivity());
        if (!TextUtils.isEmpty(imageProfile)) {
            Uri imageUri = Uri.parse(imageProfile);
            Glide.with(binding.getRoot().getContext())
                    .load(imageUri)
                    .centerCrop()
                    .placeholder(R.drawable.user_placeholder)
                    .into(binding.accountInfomationAvatar);
        }

        if (UserInfo.getInstance().isLogin(getMyActivity())) {
            beforeCallApi();
            callPersonalInfoApi();
        }
    }

    /**
     * Bind view personal info
     */
    private void bindViewPersonalInfo() {
        mPersonalInfoDto = SharePreferenceManager.getAccountInfomation(getMyActivity());
        if (mPersonalInfoDto != null) {
            binding.accountInfomation.setVisibility(View.VISIBLE);
            binding.accountInfomationUsername.setText(mPersonalInfoDto.username);
            binding.name.setText(mPersonalInfoDto.name);
            binding.phoneNumber.setText(mPersonalInfoDto.phoneNumber);
            binding.email.setText(mPersonalInfoDto.email);
            binding.address.setText(AddressInfo.getInstance().getAddressStr(getMyActivity()
                    , mPersonalInfoDto.address
                    , mPersonalInfoDto.city
                    , mPersonalInfoDto.district
                    , mPersonalInfoDto.ward));
        } else {
            binding.accountInfomation.setVisibility(View.GONE);
        }
    }

    @Override
    public void onViewClick() {
        binding.registerLoginFragment.login.setOnClickListener(v -> {
            LoginDialog loginDialog = LoginDialog.newInstance(CustomerInfoFragment.class.getSimpleName());
            loginDialog.show(getMyActivity().getSupportFragmentManager(), LoginDialog.class.getSimpleName());
        });

        binding.registerLoginFragment.signUp.setOnClickListener(v -> {
            RegisterAccountDialog registerAccountDialog = RegisterAccountDialog.newInstance(CustomerOderHistoryListFragment.class.getSimpleName());
            registerAccountDialog.show(getMyActivity().getSupportFragmentManager(), RegisterAccountDialog.class.getSimpleName());
        });

        binding.accountInfomationAvatar.setOnClickListener(view -> {
            showPictureDialog();
        });

        binding.accountInfomationLogout.setOnClickListener(view -> {
            UserInfo.getInstance().init(getActivity());
            logout();
        });


        binding.accountInfomationHistory.setOnClickListener(new SingleTapListener(v -> {
            mOnClickAccountInfomation.onMoveTab();
        }));
    }

    private void beforeCallApi() {
        binding.progressBar.maskviewLayout.setVisibility(View.VISIBLE);
    }

    private void afterCallApi() {
        binding.progressBar.maskviewLayout.setVisibility(View.GONE);
    }

    private void showPictureDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(getActivity());
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {"Select photo from gallery", "Capture photo from camera"};
        pictureDialog.setItems(pictureDialogItems,
                (dialog, which) -> {
                    switch (which) {
                        case 0:
                            choosePhotoFromGallary();
                            break;
                        case 1:
                            mIsCustomerInfoCallBack.setPermission();
                            takePhotoFromCamera();
                            break;
                    }
                });
        pictureDialog.show();
    }

    private void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, GALLERY);
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), contentURI);
                    binding.accountInfomationAvatar.setImageBitmap(bitmap);
                    UserInfo.getInstance().setUrlAvatar(getMyActivity(), encodeToBase64(bitmap));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } else if (requestCode == CAMERA) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            binding.accountInfomationAvatar.setImageBitmap(thumbnail);
            UserInfo.getInstance().setUrlAvatar(getMyActivity(), encodeToBase64(thumbnail));
            saveImage(thumbnail);
        }
    }

    private String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
        if (!wallpaperDirectory.exists()) {  // have the object build the directory structure, if needed.
            wallpaperDirectory.mkdirs();
        }
        try {
            File f = new File(wallpaperDirectory, Calendar.getInstance().getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(getActivity(),
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }

    public static String encodeToBase64(Bitmap bitmap) {
        Bitmap bit = bitmap;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bit.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        return imageEncoded;
    }

    public static Bitmap decodeToBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    /**
     * Logout -> move to home screen
     */
    private void logout() {
        Intent intent = new Intent(getMyActivity(), HomeActivity.class);
        intent.putExtra(Constant.ROLE_SWITCH, Role.CUSTOMER.role());
        startActivity(intent);
        getMyActivity().finish();
    }

    public interface ISCustomerInfoCallBack {
        void setPermission();
    }


    public interface OnClickAccountInfomationListener {
        void onMoveTab();
    }



    /**
     * Call personal info api
     */
    private void callPersonalInfoApi() {
        mDataController.getAccountInfomation(getMyActivity(), new AccountInfomationCallback());
    }

    /**
     * Account infomation callback
     */
    private class AccountInfomationCallback implements ApiServiceOperator.OnResponseListener<PersonalInfoResponse> {
        @Override
        public void onSuccess(PersonalInfoResponse body) {
            if (TextUtils.equals(APIConstant.STATUS_CODE_SUCCESS, body.statusCd)) {
                SharePreferenceManager.setAccountInfomation(getMyActivity(), body.personalInfoDto);
                bindViewPersonalInfo();
            } else {
                androidx.appcompat.app.AlertDialog alertDialog = ErrorDialog.buildPopupOnlyPositive(getMyActivity(),
                        getString(R.string.cannot_get_account_infomation), R.string.ok, (dialogInterface, i) -> {
                        });
                alertDialog.show();
            }
            afterCallApi();
        }

        @Override
        public void onFailure(Throwable t) {
            androidx.appcompat.app.AlertDialog alertDialog = ErrorDialog.buildPopupOnlyPositive(getMyActivity(),
                    getString(R.string.cannot_get_account_infomation), R.string.ok, (dialogInterface, i) -> {
                    });
            alertDialog.show();
            afterCallApi();
        }
    }

}