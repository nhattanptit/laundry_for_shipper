package com.laundry.app.view.dialog;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.laundry.app.R;
import com.laundry.app.constant.Constant;
import com.laundry.app.control.ApiServiceOperator;
import com.laundry.app.control.DataController;
import com.laundry.app.databinding.RegisterAccountDialogBinding;
import com.laundry.app.dto.AddressInfo;
import com.laundry.app.dto.address.CityResponseDto;
import com.laundry.app.dto.address.DistrictResponseDto;
import com.laundry.app.dto.address.WardResponseDto;
import com.laundry.app.dto.authentication.RegisterRequest;
import com.laundry.app.dto.authentication.RegisterResponse;
import com.laundry.app.utils.SingleTapListener;
import com.laundry.base.BaseDialog;

import java.util.ArrayList;

public class RegisterAccountDialog extends BaseDialog<RegisterAccountDialogBinding>
        implements ApiServiceOperator.OnResponseListener<RegisterResponse> {

    private static final String TAG = RegisterAccountDialog.class.getSimpleName();
    private final DataController controller = new DataController();
    private String currentTab;

    private RegisterRequest mRegisterRequest = new RegisterRequest();

    public static RegisterAccountDialog newInstance(String currentTab) {
        RegisterAccountDialog registerAccountDialog = new RegisterAccountDialog();
        Bundle bundle = new Bundle();
        bundle.putString(Constant.CURRENT_TAB, currentTab);
        registerAccountDialog.setArguments(bundle);

        return registerAccountDialog;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.register_account_dialog;
    }

    @Override
    public void onInitView() {
        if (getArguments() != null) {
            currentTab = getArguments().getString(Constant.CURRENT_TAB);
        }

        loadCityList();
    }

    @Override
    public void onViewClick() {
        binding.registerButton.setOnClickListener(new SingleTapListener(view -> {
            registerAccount();
        }));

        binding.backToLogin.setOnClickListener(new SingleTapListener(view -> {
            LoginDialog loginDialog;
            if (!TextUtils.isEmpty(currentTab)) {
                loginDialog = LoginDialog.newInstance(currentTab);
            } else {
                loginDialog = new LoginDialog();
            }
            loginDialog.show(getMyActivity().getSupportFragmentManager(), LoginDialog.class.getSimpleName());
            this.dismiss();
        }));
    }

    @Override
    protected boolean dismissByTouchOutside() {
        return false;
    }

    /** Success register account */
    @Override
    public void onSuccess(RegisterResponse body) {
        int returnCd = Integer.parseInt(body.statusCd);
        if (returnCd == 200) {
            LoginDialog loginDialog;
            if (!TextUtils.isEmpty(currentTab)) {
                loginDialog = LoginDialog.newInstance(currentTab);
            } else {
                loginDialog = new LoginDialog();
            }
            loginDialog.show(getMyActivity().getSupportFragmentManager(), LoginDialog.class.getSimpleName());
            this.dismiss();
        } else {

        }
        afterCallApi();
    }

    @Override
    public void onFailure(Throwable t) {
        afterCallApi();
    }

    /**
     * Call api register
     */
    private void registerAccount() {
        if (validate()) {
            beforeCallApi();
            controller.register(mRegisterRequest, this);
        }
    }

    private void beforeCallApi() {
        binding.progressBar.maskviewLayout.setVisibility(View.VISIBLE);
    }

    private void afterCallApi() {
        binding.progressBar.maskviewLayout.setVisibility(View.GONE);
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
                    mRegisterRequest.city = "";
                    return;
                } else {
                    mRegisterRequest.city = cityList.get(position - 1).level1Id;
                    loadDistrictList(mRegisterRequest.city);
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
                    mRegisterRequest.district = "";
                    return;
                } else {
                    mRegisterRequest.district = districtList.get(position - 1).level2Id;
                    loadWardList(mRegisterRequest.district, cityId);
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
                    mRegisterRequest.ward = "";
                    return;
                } else {
                    mRegisterRequest.ward = wardList.get(position - 1).level3Id;
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


    /**
     * Validation data input
     *
     * @return
     */
    private boolean validate() {
        boolean isValid = true;

        // Validate full name
        mRegisterRequest.name = binding.accountName.getText().toString();
        if (TextUtils.isEmpty(mRegisterRequest.name)) {
            isValid = false;
            binding.accountNameLayout.setError(Constant.FULL_NAME_WRONG);
        } else {
            binding.accountNameLayout.setError(null);
            binding.accountNameLayout.setErrorEnabled(false);
        }
        // Validate username
        mRegisterRequest.username = binding.accountUsername.getText().toString();
        if (TextUtils.isEmpty(mRegisterRequest.username)) {
            isValid = false;
            binding.accountUsernameLayout.setError(Constant.USERNAME_WRONG);
        } else if (mRegisterRequest.username.length() < 3) {
            isValid = false;
            binding.accountUsernameLayout.setError(Constant.USERNAME_LENGTH_WRONG);
        } else {
            binding.accountUsernameLayout.setError(null);
            binding.accountUsernameLayout.setErrorEnabled(false);
        }

        // Validate mail
        mRegisterRequest.email = binding.accountMail.getText().toString();
        if (TextUtils.isEmpty(mRegisterRequest.email)) {
            isValid = false;
            binding.accountMailLayout.setError(Constant.MAIL_WRONG);
        } else if (!mRegisterRequest.email.matches(Patterns.EMAIL_ADDRESS.pattern())) {
            isValid = false;
            binding.accountMailLayout.setError(Constant.MAIL_INVALID_WRONG);
        } else {
            binding.accountMailLayout.setError(null);
            binding.accountMailLayout.setErrorEnabled(false);
        }

        // Validate phone number
        mRegisterRequest.phoneNumber = binding.accountPhone.getText().toString();
        if (TextUtils.isEmpty(mRegisterRequest.phoneNumber)) {
            isValid = false;
            binding.accountPhoneLayout.setError(Constant.PHONE_WRONG);
        } else if (mRegisterRequest.phoneNumber.length() < 10) {
            isValid = false;
            binding.accountPhoneLayout.setError(Constant.PHONE_LESS_WRONG);
        } else if (mRegisterRequest.phoneNumber.length() > 12) {
            isValid = false;
            binding.accountPhoneLayout.setError(Constant.PHONE_MORE_WRONG);
        } else if (!mRegisterRequest.phoneNumber.matches(Patterns.PHONE.pattern())) {
            isValid = false;
            binding.accountPhoneLayout.setError(Constant.PHONE_INVALID_WRONG);
        } else {
            binding.accountPhoneLayout.setError(null);
            binding.accountPhoneLayout.setErrorEnabled(false);
        }

        // Validate password
        mRegisterRequest.password = binding.accountPassword.getText().toString();
        if (TextUtils.isEmpty(mRegisterRequest.password)) {
            isValid = false;
            binding.accountPasswordLayout.setError(Constant.PASSWORD_WRONG);
        } else if (mRegisterRequest.password.length() < 8 || mRegisterRequest.password.length() > 15) {
            isValid = false;
            binding.accountPasswordLayout.setError(Constant.PASSWORD_LENGTH_WRONG);
        } else {
            binding.accountPasswordLayout.setError(null);
            binding.accountPasswordLayout.setErrorEnabled(false);
        }

        // Validate confirm password
        String confirmPassword = binding.accountConfirmPassword.getText().toString();
        if (TextUtils.isEmpty(confirmPassword)) {
            isValid = false;
            binding.accountConfirmPasswordLayout.setError(Constant.PASSWORD_CONFIRM_WRONG);
        } else if (confirmPassword.length() < 8 || confirmPassword.length() > 15) {
            isValid = false;
            binding.accountConfirmPasswordLayout.setError(Constant.PASSWORD_LENGTH_WRONG);
        } else if (!TextUtils.isEmpty(mRegisterRequest.password) && !TextUtils.isEmpty(confirmPassword)
                && !TextUtils.equals(mRegisterRequest.password, confirmPassword)) {
            isValid = false;
            binding.accountConfirmPasswordLayout.setError(Constant.PASSWORD_NOT_MATCH_WRONG);
        } else {
            binding.accountConfirmPasswordLayout.setError(null);
            binding.accountConfirmPasswordLayout.setErrorEnabled(false);
        }

        if (TextUtils.isEmpty(mRegisterRequest.city)) {
            isValid = false;
            binding.accountCityError.setText(Constant.CITY_WRONG);
            binding.accountCityError.setVisibility(View.VISIBLE);
        } else {
            binding.accountCityError.setVisibility(View.GONE);
        }

        if (TextUtils.isEmpty(mRegisterRequest.district)) {
            isValid = false;
            binding.accountDistrictError.setText(Constant.DISTRICT_WRONG);
            binding.accountDistrictError.setVisibility(View.VISIBLE);
        } else {
            binding.accountDistrictError.setVisibility(View.GONE);
        }

        if (TextUtils.isEmpty(mRegisterRequest.ward)) {
            isValid = false;
            binding.accountWardError.setText(Constant.WARD_WRONG);
            binding.accountWardError.setVisibility(View.VISIBLE);
        } else {
            binding.accountWardError.setVisibility(View.GONE);
        }

        // Validate address
        mRegisterRequest.address = binding.accountAddress.getText().toString();
        if (TextUtils.isEmpty(mRegisterRequest.address)) {
            isValid = false;
            binding.accountAddressLayout.setError(Constant.ADDRESS_WRONG);
        } else {
            binding.accountAddressLayout.setError(null);
            binding.accountAddressLayout.setErrorEnabled(false);
        }

        return isValid;
    }
}
