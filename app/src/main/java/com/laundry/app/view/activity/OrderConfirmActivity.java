package com.laundry.app.view.activity;

import android.annotation.SuppressLint;

import androidx.fragment.app.FragmentManager;

import com.laundry.app.R;
import com.laundry.app.databinding.OrderConfirmActivityBinding;
import com.laundry.app.dto.sevicedetail.ServiceDetailDto;
import com.laundry.app.utils.SingleTapListener;
import com.laundry.app.view.adapter.ServicesOrderAdapter;
import com.laundry.app.view.dialog.AddAddressDialog;
import com.laundry.app.view.dialog.LoginDialog;
import com.laundry.app.view.dialog.RegisterAccountDialog;
import com.laundry.app.view.fragment.customer.HomeFragment;
import com.laundry.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class OrderConfirmActivity extends BaseActivity<OrderConfirmActivityBinding> {

    private static final String TAG = "OrderConfirmActivity";
    private final ServicesOrderAdapter mServicesOrderAdapter = new ServicesOrderAdapter();
    private List<ServiceDetailDto> mServiceDetails = new ArrayList<>();
    private Double subTotal;

    @Override
    protected int getLayoutResource() {
        return R.layout.order_confirm_activity;
    }

    @Override
    public void onPreInitView() {
        mServiceDetails = (List<ServiceDetailDto>) getIntent().getSerializableExtra("DTO");
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
}

