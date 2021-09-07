package com.laundry.app.view.dialog;

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
import com.laundry.app.databinding.AddressAddDialogBinding;
import com.laundry.app.dto.AddressInfo;
import com.laundry.app.dto.address.CityResponseDto;
import com.laundry.app.dto.address.DistrictResponseDto;
import com.laundry.app.dto.address.WardResponseDto;
import com.laundry.app.dto.addressnew.AddressAddRequest;
import com.laundry.app.dto.addressnew.AddressAddResponse;
import com.laundry.base.BaseDialog;

import java.util.ArrayList;

public class AddAddressDialog extends BaseDialog<AddressAddDialogBinding> {

    private static final String TAG = "AddAddressDialog";
    private AddressAddRequest addressAddRequest = new AddressAddRequest();
    private DataController mDataController = new DataController();

    @Override
    protected int getLayoutResource() {
        return R.layout.address_add_dialog;
    }

    @Override
    public void onInitView() {
        Log.d(TAG, "onInitView: ");
        loadCityList();
    }

    @Override
    public void onViewClick() {
        binding.doneButton.setOnClickListener(view -> {
            addAddressNew();
            dismissDialog();
            getMyActivity().recreate();
        });

        binding.cancelButton.setOnClickListener(view -> {
            dismissDialog();
        });
    }

    /**
     * Call api register
     */
    private void addAddressNew() {
        if (validate()) {
            mDataController.addAddress(getMyActivity(), addressAddRequest, new AddressAddCallBack());
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
                    addressAddRequest.city = "";
                    return;
                } else {
                    addressAddRequest.city = cityList.get(position - 1).name;
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
                    addressAddRequest.district = "";
                    return;
                } else {
                    addressAddRequest.district = districtList.get(position - 1).name;
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
                    addressAddRequest.ward = "";
                    return;
                } else {
                    addressAddRequest.ward = wardList.get(position - 1).name;
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

        // Validate full name
        addressAddRequest.receiverName = binding.name.getText().toString();
        if (TextUtils.isEmpty(addressAddRequest.receiverName)) {
            isValid = false;
            binding.nameLayout.setError(Constant.FULL_NAME_WRONG);
        } else {
            binding.nameLayout.setError(null);
            binding.nameLayout.setErrorEnabled(false);
        }

        // Validate phone number
        addressAddRequest.receiverPhoneNumber = binding.accountPhone.getText().toString();
        if (TextUtils.isEmpty(addressAddRequest.receiverPhoneNumber)) {
            isValid = false;
            binding.accountPhoneLayout.setError(Constant.PHONE_WRONG);
        } else if (addressAddRequest.receiverPhoneNumber.length() < 10) {
            isValid = false;
            binding.accountPhoneLayout.setError(Constant.PHONE_LESS_WRONG);
        } else if (addressAddRequest.receiverPhoneNumber.length() > 12) {
            isValid = false;
            binding.accountPhoneLayout.setError(Constant.PHONE_MORE_WRONG);
        } else if (!addressAddRequest.receiverPhoneNumber.matches(Patterns.PHONE.pattern())) {
            isValid = false;
            binding.accountPhoneLayout.setError(Constant.PHONE_INVALID_WRONG);
        } else {
            binding.accountPhoneLayout.setError(null);
            binding.accountPhoneLayout.setErrorEnabled(false);
        }

        if (TextUtils.isEmpty(addressAddRequest.city)) {
            isValid = false;
            binding.accountCityError.setText(Constant.CITY_WRONG);
            binding.accountCityError.setVisibility(View.VISIBLE);
        } else {
            binding.accountCityError.setVisibility(View.GONE);
        }

        if (TextUtils.isEmpty(addressAddRequest.district)) {
            isValid = false;
            binding.accountDistrictError.setText(Constant.DISTRICT_WRONG);
            binding.accountDistrictError.setVisibility(View.VISIBLE);
        } else {
            binding.accountDistrictError.setVisibility(View.GONE);
        }

        if (TextUtils.isEmpty(addressAddRequest.ward)) {
            isValid = false;
            binding.accountWardError.setText(Constant.WARD_WRONG);
            binding.accountWardError.setVisibility(View.VISIBLE);
        } else {
            binding.accountWardError.setVisibility(View.GONE);
        }

        // Validate address
        addressAddRequest.address = binding.accountAddress.getText().toString();
        if (TextUtils.isEmpty(addressAddRequest.address)) {
            isValid = false;
            binding.accountAddressLayout.setError(Constant.ADDRESS_WRONG);
        } else {
            binding.accountAddressLayout.setError(null);
            binding.accountAddressLayout.setErrorEnabled(false);
        }
        return isValid;
    }
}
