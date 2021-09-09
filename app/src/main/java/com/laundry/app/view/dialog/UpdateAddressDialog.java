package com.laundry.app.view.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import com.laundry.app.R;
import com.laundry.app.constant.Constant;
import com.laundry.app.control.ApiServiceOperator;
import com.laundry.app.control.DataController;
import com.laundry.app.databinding.AddressUpdateDialogBinding;
import com.laundry.app.dto.AddressInfo;
import com.laundry.app.dto.address.CityResponseDto;
import com.laundry.app.dto.address.DistrictResponseDto;
import com.laundry.app.dto.address.WardResponseDto;
import com.laundry.app.dto.addressall.AddressListlDto;
import com.laundry.app.dto.addressupdate.AddressUpdateRequest;
import com.laundry.app.dto.addressupdate.AddressUpdateResponse;
import com.laundry.app.view.activity.BillingAddressActivity;
import com.laundry.base.BaseDialog;

import java.util.ArrayList;

public class UpdateAddressDialog extends BaseDialog<AddressUpdateDialogBinding> {

    private static final String TAG = UpdateAddressDialog.class.getSimpleName();
    public static final String KEY_ADDRESS_UPDATE = "KEY_ADDRESS_UPDATE";
    public static final String KEY_ADDRESS_POSITION = "KEY_ADDRESS_POSITION";
    private final AddressUpdateRequest updateRequest = new AddressUpdateRequest();
    private final DataController mDataController = new DataController();
    private BillingAddressActivity activity;
    private AddressListlDto addressDto = new AddressListlDto();
    private int positionUpdate;

    @Override
    protected int getLayoutResource() {
        return R.layout.address_update_dialog;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (BillingAddressActivity) context;
    }

    @Override
    public void onPreInitView() {
        if (getArguments() != null) {
            addressDto = (AddressListlDto) getArguments().getSerializable(KEY_ADDRESS_UPDATE);
            positionUpdate = getArguments().getInt(KEY_ADDRESS_POSITION);
        }
    }

    @Override
    public void onInitView() {
        if (addressDto != null && addressDto.user != null) {
            binding.name.setText(addressDto.receiverName);
            binding.accountPhone.setText(addressDto.receiverPhoneNumber);
        }
        loadCityList();
    }

    @Override
    public void onViewClick() {
        binding.updateButton.setOnClickListener(view -> {
            activity.beforeCallApi();
            updateAddress();
        });
    }

    /**
     * Call api update address
     */
    private void updateAddress() {
        if (validate() && addressDto != null) {
            activity.beforeCallApi();
            mDataController.updateAddress(getMyActivity(), addressDto.id, updateRequest, new UpdateAddressCallBack());
            dismissDialog();
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
                    updateRequest.city = "";
                    return;
                } else {
                    updateRequest.city = cityList.get(position - 1).level1Id;
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
                    updateRequest.district = "";
                    return;
                } else {
                    updateRequest.district = districtList.get(position - 1).level2Id;
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
                    updateRequest.ward = "";
                    return;
                } else {
                    updateRequest.ward = wardList.get(position - 1).level3Id;
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
        updateRequest.receiverName = binding.name.getText().toString();
        if (TextUtils.isEmpty(updateRequest.receiverName)) {
            isValid = false;
            binding.nameLayout.setError(Constant.FULL_NAME_WRONG);
        } else {
            binding.nameLayout.setError(null);
            binding.nameLayout.setErrorEnabled(false);
        }

        // Validate phone number
        updateRequest.receiverPhoneNumber = binding.accountPhone.getText().toString();
        if (TextUtils.isEmpty(updateRequest.receiverPhoneNumber)) {
            isValid = false;
            binding.accountPhoneLayout.setError(Constant.PHONE_WRONG);
        } else if (updateRequest.receiverPhoneNumber.length() < 10) {
            isValid = false;
            binding.accountPhoneLayout.setError(Constant.PHONE_LESS_WRONG);
        } else if (updateRequest.receiverPhoneNumber.length() > 12) {
            isValid = false;
            binding.accountPhoneLayout.setError(Constant.PHONE_MORE_WRONG);
        } else if (!updateRequest.receiverPhoneNumber.matches(Patterns.PHONE.pattern())) {
            isValid = false;
            binding.accountPhoneLayout.setError(Constant.PHONE_INVALID_WRONG);
        } else {
            binding.accountPhoneLayout.setError(null);
            binding.accountPhoneLayout.setErrorEnabled(false);
        }

        if (TextUtils.isEmpty(updateRequest.city)) {
            isValid = false;
            binding.accountCityError.setText(Constant.CITY_WRONG);
            binding.accountCityError.setVisibility(View.VISIBLE);
        } else {
            binding.accountCityError.setVisibility(View.GONE);
        }

        if (TextUtils.isEmpty(updateRequest.district)) {
            isValid = false;
            binding.accountDistrictError.setText(Constant.DISTRICT_WRONG);
            binding.accountDistrictError.setVisibility(View.VISIBLE);
        } else {
            binding.accountDistrictError.setVisibility(View.GONE);
        }

        if (TextUtils.isEmpty(updateRequest.ward)) {
            isValid = false;
            binding.accountWardError.setText(Constant.WARD_WRONG);
            binding.accountWardError.setVisibility(View.VISIBLE);
        } else {
            binding.accountWardError.setVisibility(View.GONE);
        }

        // Validate address
        updateRequest.address = binding.accountAddress.getText().toString();
        if (TextUtils.isEmpty(updateRequest.address)) {
            isValid = false;
            binding.accountAddressLayout.setError(Constant.ADDRESS_WRONG);
        } else {
            binding.accountAddressLayout.setError(null);
            binding.accountAddressLayout.setErrorEnabled(false);
        }
        return isValid;
    }

    private class UpdateAddressCallBack implements ApiServiceOperator.OnResponseListener<AddressUpdateResponse> {
        @Override
        public void onSuccess(AddressUpdateResponse body) {
            activity.getAddress();
        }

        @Override
        public void onFailure(Throwable t) {
            activity.afterCallApi();
        }
    }
}

