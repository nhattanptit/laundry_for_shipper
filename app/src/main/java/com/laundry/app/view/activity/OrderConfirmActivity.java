package com.laundry.app.view.activity;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.View;

import com.laundry.app.R;
import com.laundry.app.control.ApiServiceOperator;
import com.laundry.app.control.DataController;
import com.laundry.app.databinding.OrderConfirmActivityBinding;
import com.laundry.app.dto.addressaccount.AddressRegisteredDto;
import com.laundry.app.dto.addressaccount.AddressRegisteredResponse;
import com.laundry.app.dto.addressaccount.User;
import com.laundry.app.dto.sevicedetail.ServiceDetailDto;
import com.laundry.app.view.adapter.ServicesOrderAdapter;
import com.laundry.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class OrderConfirmActivity extends BaseActivity<OrderConfirmActivityBinding> {

    private static final String TAG = "OrderConfirmActivity";
    private final ServicesOrderAdapter mServicesOrderAdapter = new ServicesOrderAdapter();
    private List<ServiceDetailDto> mServiceDetails = new ArrayList<>();
    private ServiceDetailDto mOrder = new ServiceDetailDto();
    private DataController mDataController = new DataController();
    private Double subTotal;

    @Override
    protected int getLayoutResource() {
        return R.layout.order_confirm_activity;
    }

    @Override
    public void onPreInitView() {
        mServiceDetails = (List<ServiceDetailDto>) getIntent().getSerializableExtra("DTO");
        beforeCallApi();
        mDataController.getAddress(this, new AddressCallBack());
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

    }

    private class AddressCallBack implements ApiServiceOperator.OnResponseListener<AddressRegisteredResponse> {
        @SuppressLint("SetTextI18n")
        @Override
        public void onSuccess(AddressRegisteredResponse body) {
            Log.d(TAG, "onSuccess: " + body.addressRegisters.size());
            if (body != null && body.addressRegisters != null && body.addressRegisters.size() > 0) {
                binding.nameShippingText.setText(body.addressRegisters.get(0).user.name);
                binding.phoneShippingText.setText(" - " + body.addressRegisters.get(0).user.phoneNumber);
                binding.nestedLayout.setVisibility(View.VISIBLE);
            }
            afterCallApi();
        }

        @Override
        public void onFailure(Throwable t) {
            Log.e(TAG, "onFailure: ");
            afterCallApi();
        }
    }

    private void beforeCallApi() {
        binding.progressBar.maskviewLayout.setVisibility(View.VISIBLE);
    }

    private void afterCallApi() {
        binding.progressBar.maskviewLayout.setVisibility(View.GONE);
    }
}
