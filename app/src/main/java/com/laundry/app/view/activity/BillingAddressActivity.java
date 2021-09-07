package com.laundry.app.view.activity;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.View;

import com.laundry.app.R;
import com.laundry.app.control.ApiServiceOperator;
import com.laundry.app.control.DataController;
import com.laundry.app.databinding.BillingAddressActivityBinding;
import com.laundry.app.dto.AddressInfo;
import com.laundry.app.dto.addressall.AddressListResponse;
import com.laundry.app.dto.addressall.AddressListlDto;
import com.laundry.app.dto.sevicedetail.ServiceDetailDto;
import com.laundry.app.utils.SingleTapListener;
import com.laundry.app.view.adapter.AddressAdapter;
import com.laundry.app.view.dialog.AddAddressDialog;
import com.laundry.app.view.dialog.LoginDialog;
import com.laundry.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class BillingAddressActivity extends BaseActivity<BillingAddressActivityBinding> implements AddressAdapter.ISAddressCallBack {

    private static final String TAG = "BillingAddressActivity";
    private final DataController mDataController = new DataController();
    private final AddressAdapter addressAdapter = new AddressAdapter();
    private final List<Object> addressListlDtos = new ArrayList<>();

    @Override
    protected int getLayoutResource() {
        return R.layout.billing_address_activity;
    }

    @Override
    public void onInitView() {
        // Init address
        AddressInfo.getInstance().init(this);
        beforeCallApi();
        binding.addressListRecycle.setAdapter(addressAdapter);
        mDataController.getAddress(this, new AddressCallBack());
        binding.toolbar.setTitle(getString(R.string.billing_address));
        binding.toolbar.setToolbarListener(view -> onBackPressed());
        addressAdapter.setSelectAddressCallBack(this);
    }

    @Override
    public void onViewClick() {
        binding.addAddressButton.setOnClickListener(new SingleTapListener(view -> {
            AddAddressDialog addAddressDialog = new AddAddressDialog();
            addAddressDialog.show(getSupportFragmentManager(), LoginDialog.class.getSimpleName());
        }));
    }

    @Override
    public void onClickItem(int position, AddressListlDto item) {
        Log.d(TAG, "onClickItem: " + item);
    }

    private class AddressCallBack implements ApiServiceOperator.OnResponseListener<AddressListResponse> {
        @SuppressLint("SetTextI18n")
        @Override
        public void onSuccess(AddressListResponse body) {
            Log.d(TAG, "onSuccess: " + body.addressRegisters.size());
            if (body != null && body.addressRegisters != null && body.addressRegisters.size() > 0) {
                addressListlDtos.addAll(body.addressRegisters);
            }
            addressAdapter.setDataList(addressListlDtos);
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
