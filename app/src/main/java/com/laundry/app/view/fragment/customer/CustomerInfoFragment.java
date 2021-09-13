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

import com.laundry.app.R;
import com.laundry.app.constant.Constant;
import com.laundry.app.databinding.CustomerInfoFragmentBinding;
import com.laundry.app.dto.Role;
import com.laundry.app.dto.UserInfo;
import com.laundry.app.utils.SharePreferenceManager;
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

    @Override
    protected int getLayoutResource() {
        return R.layout.customer_info_fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mIsCustomerInfoCallBack = (ISCustomerInfoCallBack) context;
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

}