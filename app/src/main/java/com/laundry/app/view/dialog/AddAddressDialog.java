package com.laundry.app.view.dialog;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.facebook.login.LoginManager;
import com.laundry.app.R;
import com.laundry.app.constant.Constant;
import com.laundry.app.control.ApiServiceOperator;
import com.laundry.app.control.DataController;
import com.laundry.app.data.APIConstant;
import com.laundry.app.databinding.AddressAddDialogBinding;
import com.laundry.app.dto.AddressInfo;
import com.laundry.app.dto.UserInfo;
import com.laundry.app.dto.address.CityResponseDto;
import com.laundry.app.dto.address.DistrictResponseDto;
import com.laundry.app.dto.address.WardResponseDto;
import com.laundry.app.dto.addressnew.AddressAddRequest;
import com.laundry.app.dto.addressnew.AddressAddResponse;
import com.laundry.app.dto.authentication.LoginResponseDto;
import com.laundry.app.dto.authentication.SocialLoginRequest;
import com.laundry.app.utils.ErrorDialog;
import com.laundry.app.view.activity.BillingAddressActivity;
import com.laundry.base.BaseDialog;

import java.util.ArrayList;

public class AddAddressDialog extends BaseDialog<AddressAddDialogBinding> {

    private static final String TAG = "AddAddressDialog";
    private final AddressAddRequest addAddressRequest = new AddressAddRequest();
    private final SocialLoginRequest socialLoginRequest = new SocialLoginRequest();
    private final DataController mDataController = new DataController();
    private BillingAddressActivity activity;
    public static final int TRANSITION_NO_SOCIAL_LOGIN = 1;
    public static final int TRANSITION_NO_BILLING_ADDRESSS = 2;
    private String customerName;
    private LoginDialog.LoginListener mLoginListener;
    private int transitionNo;
    private String currentTab;

    public static AddAddressDialog newInstance(String customerName, String email, int transitionNo, String currentTab) {
        AddAddressDialog addressDialog = new AddAddressDialog();
        Bundle bundle = new Bundle();
        bundle.putString(Constant.KEY_BUNDLE_CUSTOMER_NAME, customerName);
        bundle.putString(Constant.KEY_BUNDLE_CUSTOMER_EMAIL, email);
        bundle.putInt(Constant.KEY_BUNDLE_TRANSITION_NO, transitionNo);
        bundle.putString(Constant.CURRENT_TAB, currentTab);
        addressDialog.setArguments(bundle);

        return addressDialog;
    }

    public static AddAddressDialog newInstance(int transitionNo) {
        AddAddressDialog addressDialog = new AddAddressDialog();
        Bundle bundle = new Bundle();
        bundle.putInt(Constant.KEY_BUNDLE_TRANSITION_NO, transitionNo);
        addressDialog.setArguments(bundle);

        return addressDialog;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.address_add_dialog;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof BillingAddressActivity) {
            activity = (BillingAddressActivity) context;
        } else {
            try {
                this.mLoginListener = (LoginDialog.LoginListener) context;
            } catch (ClassCastException e) {
                throw e;
            }
        }
    }

    @Override
    public void onInitView() {
        Log.d(TAG, "onInitView: ");
        if (getArguments() != null) {
            transitionNo = getArguments().getInt(Constant.KEY_BUNDLE_TRANSITION_NO);
            currentTab = getArguments().getString(Constant.CURRENT_TAB);
            socialLoginRequest.name = getArguments().getString(Constant.KEY_BUNDLE_CUSTOMER_NAME);
            socialLoginRequest.email = getArguments().getString(Constant.KEY_BUNDLE_CUSTOMER_EMAIL);
        }
        if (transitionNo == TRANSITION_NO_BILLING_ADDRESSS) {
            binding.nameLayout.setVisibility(View.VISIBLE);
        } else {
            binding.nameLayout.setVisibility(View.GONE);
        }
        loadCityList();
    }

    @Override
    public void onViewClick() {
        binding.doneButton.setOnClickListener(view -> {
            switch (transitionNo) {
                case TRANSITION_NO_BILLING_ADDRESSS:
                    addAddressNew();
                    break;
                case TRANSITION_NO_SOCIAL_LOGIN:
                    callSocialLoginApi();
                    break;
            }
        });

        binding.cancelButton.setOnClickListener(view -> {
            dismissDialog();
            if (transitionNo == TRANSITION_NO_SOCIAL_LOGIN) {
                LoginManager.getInstance().logOut();
            }
        });
    }

    /**
     * Call social login api
     */
    private void callSocialLoginApi() {
        if (validate()) {
            beforeCallApi();
            mDataController.socialLoginFirst(getMyActivity(), socialLoginRequest, new SocialLoginCallBack());
        }
    }

    /**
     * Call api add address
     */
    private void addAddressNew() {
        if (validate()) {
            dismissDialog();
            activity.beforeCallApi();
            mDataController.addAddress(getMyActivity(), addAddressRequest, new AddressAddCallBack());
        }
    }

    /**
     * Load city list to view
     */
    private void loadCityList() {
        ArrayList<CityResponseDto> cityList = AddressInfo.getInstance().getCityList();
        ArrayList<String> cityName = new ArrayList<>();
        cityName.add("Please select a city");
        for (CityResponseDto city : cityList) {
            cityName.add(city.name);
        }
        binding.spinnerCity.setAdapter(new ArrayAdapter<>(getMyActivity(), R.layout.spinner_item, cityName));
        binding.spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position == 0) {
                    Log.d(TAG, "onItemSelected: ");
                    addAddressRequest.city = "";
                    return;
                } else {
                    addAddressRequest.city = cityList.get(position - 1).level1Id;
                    socialLoginRequest.city = cityList.get(position - 1).level1Id;
                    loadDistrictList(cityList.get(position - 1).level1Id);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void loadDistrictList(String cityId) {
        ArrayList<DistrictResponseDto> districtList = new ArrayList<>();
        for (CityResponseDto dto : AddressInfo.getInstance().getCityList()) {
            if (TextUtils.equals(cityId, dto.level1Id)) {
                districtList.addAll(dto.level2s);
                break;
            }
        }
        ArrayList<String> districtNameList = new ArrayList<>();
        districtNameList.add("Please select a district");
        for (DistrictResponseDto district : districtList) {
            districtNameList.add(district.name);
        }
        binding.spinnerDistrict.setAdapter(new ArrayAdapter<>(getMyActivity(), R.layout.spinner_item, districtNameList));
        binding.spinnerDistrict.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position == 0) {
                    addAddressRequest.district = "";
                    return;
                } else {
                    addAddressRequest.district = districtList.get(position - 1).level2Id;
                    socialLoginRequest.district = districtList.get(position - 1).level2Id;
                    loadWardList(districtList.get(position - 1).level2Id, cityId);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void loadWardList(String districtId, String cityId) {
        ArrayList<DistrictResponseDto> districtList = getDistrictByCityId(cityId);
        ArrayList<WardResponseDto> wardList = new ArrayList<>();

        for (DistrictResponseDto dto : districtList) {
            if (TextUtils.equals(districtId, dto.level2Id)) {
                wardList.addAll(dto.level3s);
                break;
            }
        }
        ArrayList<String> wardNameList = new ArrayList<>();
        wardNameList.add("Please select a ward");
        for (WardResponseDto district : wardList) {
            wardNameList.add(district.name);
        }
        binding.spinnerWard.setAdapter(new ArrayAdapter<>(getMyActivity(), R.layout.spinner_item, wardNameList));
        binding.spinnerWard.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position == 0) {
                    addAddressRequest.ward = "";
                    return;
                } else {
                    addAddressRequest.ward = wardList.get(position - 1).level3Id;
                    socialLoginRequest.ward = wardList.get(position - 1).level3Id;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private ArrayList<DistrictResponseDto> getDistrictByCityId(String cityId) {
        ArrayList<DistrictResponseDto> districtList = new ArrayList<>();
        for (CityResponseDto dto : AddressInfo.getInstance().getCityList()) {
            if (TextUtils.equals(cityId, dto.level1Id)) {
                districtList.addAll(dto.level2s);
                break;
            }
        }
        return districtList;
    }

    private class AddressAddCallBack implements ApiServiceOperator.OnResponseListener<AddressAddResponse> {
        @Override
        public void onSuccess(AddressAddResponse body) {
            Log.d(TAG, "onSuccess: " + body.data);
            activity.getAddress();
        }

        @Override
        public void onFailure(Throwable t) {
            Log.e(TAG, "onFailure: ");
        }
    }

    /**
     * Validation data input
     *
     * @return
     */
    private boolean validate() {
        boolean isValid = true;


        if (transitionNo == TRANSITION_NO_BILLING_ADDRESSS) {
            // Validate full name
            addAddressRequest.receiverName = binding.name.getText().toString();
            if (TextUtils.isEmpty(addAddressRequest.receiverName)) {
                isValid = false;
                binding.nameLayout.setError(Constant.FULL_NAME_WRONG);
            } else {
                binding.nameLayout.setError(null);
                binding.nameLayout.setErrorEnabled(false);
            }
        }

        // Validate phone number
        addAddressRequest.receiverPhoneNumber = binding.accountPhone.getText().toString();
        socialLoginRequest.phoneNumber = binding.accountPhone.getText().toString();
        if (TextUtils.isEmpty(addAddressRequest.receiverPhoneNumber)) {
            isValid = false;
            binding.accountPhoneLayout.setError(Constant.PHONE_WRONG);
        } else if (addAddressRequest.receiverPhoneNumber.length() < 10) {
            isValid = false;
            binding.accountPhoneLayout.setError(Constant.PHONE_LESS_WRONG);
        } else if (addAddressRequest.receiverPhoneNumber.length() > 12) {
            isValid = false;
            binding.accountPhoneLayout.setError(Constant.PHONE_MORE_WRONG);
        } else if (!addAddressRequest.receiverPhoneNumber.matches(Patterns.PHONE.pattern())) {
            isValid = false;
            binding.accountPhoneLayout.setError(Constant.PHONE_INVALID_WRONG);
        } else {
            binding.accountPhoneLayout.setError(null);
            binding.accountPhoneLayout.setErrorEnabled(false);
        }

        if (TextUtils.isEmpty(addAddressRequest.city)) {
            isValid = false;
            binding.accountCityError.setText(Constant.CITY_WRONG);
            binding.accountCityError.setVisibility(View.VISIBLE);
        } else {
            binding.accountCityError.setVisibility(View.GONE);
        }

        if (TextUtils.isEmpty(addAddressRequest.district)) {
            isValid = false;
            binding.accountDistrictError.setText(Constant.DISTRICT_WRONG);
            binding.accountDistrictError.setVisibility(View.VISIBLE);
        } else {
            binding.accountDistrictError.setVisibility(View.GONE);
        }

        if (TextUtils.isEmpty(addAddressRequest.ward)) {
            isValid = false;
            binding.accountWardError.setText(Constant.WARD_WRONG);
            binding.accountWardError.setVisibility(View.VISIBLE);
        } else {
            binding.accountWardError.setVisibility(View.GONE);
        }

        // Validate address
        addAddressRequest.address = binding.accountAddress.getText().toString();
        socialLoginRequest.address = binding.accountAddress.getText().toString();
        if (TextUtils.isEmpty(addAddressRequest.address)) {
            isValid = false;
            binding.accountAddressLayout.setError(Constant.ADDRESS_WRONG);
        } else {
            binding.accountAddressLayout.setError(null);
            binding.accountAddressLayout.setErrorEnabled(false);
        }
        return isValid;
    }

    @Override
    protected boolean dismissByOnBackPress() {
        return transitionNo == TRANSITION_NO_BILLING_ADDRESSS;
    }

    private void beforeCallApi() {
        binding.progressBar.maskviewLayout.setVisibility(View.VISIBLE);
    }

    private void afterCallApi() {
        binding.progressBar.maskviewLayout.setVisibility(View.GONE);
    }

    private class SocialLoginCallBack implements ApiServiceOperator.OnResponseListener<LoginResponseDto> {

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

                dismissDialog();
            } else if (TextUtils.equals(APIConstant.STATUS_CODE_EMAIL_NOT_EXIST, body.statusCd)) {
                dismissDialog();
                AddAddressDialog addAddressDialog = AddAddressDialog.newInstance(AddAddressDialog.TRANSITION_NO_SOCIAL_LOGIN);
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
    }

}
