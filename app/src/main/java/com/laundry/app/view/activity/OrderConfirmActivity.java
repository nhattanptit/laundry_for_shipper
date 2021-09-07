package com.laundry.app.view.activity;

import android.annotation.SuppressLint;
import android.content.Intent;

import androidx.annotation.Nullable;

import com.laundry.app.R;
import com.laundry.app.databinding.OrderConfirmActivityBinding;
import com.laundry.app.dto.addressall.AddressListlDto;
import com.laundry.app.dto.sevicedetail.ServiceDetailDto;
import com.laundry.app.view.adapter.ServicesOrderAdapter;
import com.laundry.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class OrderConfirmActivity extends BaseActivity<OrderConfirmActivityBinding> {

    private static final String TAG = "OrderConfirmActivity";
    private final ServicesOrderAdapter mServicesOrderAdapter = new ServicesOrderAdapter();
    private List<ServiceDetailDto> mServiceDetails = new ArrayList<>();
    private AddressListlDto addressDto;
    private Double subTotal;
    public static final int REQUEST_CODE_ADDRESS_SELECT = 1;

    @Override
    protected int getLayoutResource() {
        return R.layout.order_confirm_activity;
    }

    @Override
    public void onPreInitView() {
        mServiceDetails = (List<ServiceDetailDto>) getIntent().getSerializableExtra("DTO");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ADDRESS_SELECT) {
            if (resultCode == RESULT_OK) {
                addressDto = (AddressListlDto) data.getSerializableExtra(BillingAddressActivity.RESULT_CODE_ADDRESS);
                updateView();
            }
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onInitView() {
        subTotal = 0.0;
        mServicesOrderAdapter.typeService = ServicesOrderAdapter.SERVICES_DETAIL_VIEW_TYPE.ORDER;
        mServicesOrderAdapter.submitList(mServiceDetails);
        binding.orderedList.setAdapter(mServicesOrderAdapter);
        for (int i = 0; i < mServiceDetails.size(); i++) {
            subTotal += mServiceDetails.get(i).totalPrice;
        }
        binding.subTotalFee.setText(subTotal + "$");
        binding.toolbar.setTitle(getString(R.string.order_confirm));
        binding.toolbar.setToolbarListener(view -> {
            onBackPressed();
        });
    }

    @Override
    public void onViewClick() {
        binding.shippingDetailIcon.setOnClickListener(view -> {
            Intent intent = new Intent(this, BillingAddressActivity.class);
            startActivityForResult(intent, REQUEST_CODE_ADDRESS_SELECT);
        });
    }

    private void updateView() {
        if (addressDto != null && addressDto.user != null) {
            binding.nameShippingText.setText(addressDto.user.name);
            binding.phoneShippingText.setText(addressDto.user.phoneNumber);
        }
    }
}

