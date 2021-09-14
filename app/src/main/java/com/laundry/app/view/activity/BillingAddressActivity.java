package com.laundry.app.view.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.laundry.app.R;
import com.laundry.app.control.ApiServiceOperator;
import com.laundry.app.control.DataController;
import com.laundry.app.databinding.BillingAddressActivityBinding;
import com.laundry.app.dto.AddressInfo;
import com.laundry.app.dto.addressall.AddressListResponse;
import com.laundry.app.dto.addressall.AddressListlDto;
import com.laundry.app.dto.addressdelete.AddressDeleteResponse;
import com.laundry.app.utils.SingleTapListener;
import com.laundry.app.view.adapter.AddressAdapter;
import com.laundry.app.view.dialog.AddAddressDialog;
import com.laundry.app.view.dialog.UpdateAddressDialog;
import com.laundry.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import static com.laundry.app.view.dialog.UpdateAddressDialog.KEY_ADDRESS_POSITION;
import static com.laundry.app.view.dialog.UpdateAddressDialog.KEY_ADDRESS_UPDATE;

public class BillingAddressActivity extends BaseActivity<BillingAddressActivityBinding> implements AddressAdapter.ISAddressCallBack {

    private static final String TAG = "BillingAddressActivity";
    private final DataController mDataController = new DataController();
    public final AddressAdapter addressAdapter = new AddressAdapter();
    private final List<Object> addressListlDtos = new ArrayList<>();
    private AddressListlDto addressDto;
    public static final String RESULT_CODE_ADDRESS = "RESULT_CODE_ADDRESS";
    public static final String KEY_ADDRESS_SELECTED = "KEY_ADDRESS_SELECTED";
    public int positionAddresses;

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
        getAddress();
        binding.toolbar.setTitle(getString(R.string.billing_address));
        binding.toolbar.setToolbarListener(view -> onBackPressed());
        addressAdapter.setSelectAddressCallBack(this);
        addressDto = (AddressListlDto) getIntent().getSerializableExtra(KEY_ADDRESS_SELECTED);
    }

    @Override
    public void onViewClick() {

        binding.addAddressButton.setOnClickListener(new SingleTapListener(view -> {
            AddAddressDialog addAddressDialog = AddAddressDialog.newInstance(AddAddressDialog.TRANSITION_NO_BILLING_ADDRESSS);
            addAddressDialog.show(getSupportFragmentManager(), AddAddressDialog.class.getSimpleName());
        }));

        binding.doneButton.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.putExtra(RESULT_CODE_ADDRESS, addressDto);
            setResult(RESULT_OK, intent);
            finish();
        });
    }

    @Override
    public void onClickItemAdd(int position, AddressListlDto item) {
        Log.d(TAG, "onClickItem: " + item.id);
        addressDto = item;
    }

    @Override
    public void onClickItemUpdate(int position, AddressListlDto item) {
        UpdateAddressDialog updateAddressDialog = new UpdateAddressDialog();
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_ADDRESS_UPDATE, item);
        bundle.putInt(KEY_ADDRESS_POSITION, position);
        updateAddressDialog.setArguments(bundle);
        updateAddressDialog.show(getSupportFragmentManager(), UpdateAddressDialog.class.getSimpleName());
        Log.d(TAG, "onClickItemUpdate: " + item.user.name);
    }

    @Override
    public void onClickItemDelete(int position, AddressListlDto item) {
        positionAddresses = position;
        addressDto = item;
        mDataController.deleteAddress(this, item.id, new DeleteAddressCallBack());
    }

    /** Call api get all address */
    public void getAddress() {
        beforeCallApi();
        addressListlDtos.clear();
        mDataController.getAddress(this, new AddressCallBack());
    }

    /** Call back get all address */
    private class AddressCallBack implements ApiServiceOperator.OnResponseListener<AddressListResponse> {
        @SuppressLint("SetTextI18n")
        @Override
        public void onSuccess(AddressListResponse body) {
            if (body != null && body.addressRegisters != null && body.addressRegisters.size() > 0) {
                addressListlDtos.addAll(body.addressRegisters);
            }
            addressAdapter.setDataList(addressListlDtos);
            afterCallApi();
            addressAdapter.setAddressSelected(addressDto);
        }

        @Override
        public void onFailure(Throwable t) {
            Log.e(TAG, "onFailure: ");
            afterCallApi();
        }
    }

    public void beforeCallApi() {
        binding.progressBar.maskviewLayout.setVisibility(View.VISIBLE);
    }

    public void afterCallApi() {
        binding.progressBar.maskviewLayout.setVisibility(View.GONE);
    }

    private class DeleteAddressCallBack implements ApiServiceOperator.OnResponseListener<AddressDeleteResponse> {
        @Override
        public void onSuccess(AddressDeleteResponse body) {
            Log.d(TAG, "onSuccess: " + body.toString());
            addressAdapter.delete(addressDto, positionAddresses);
        }

        @Override
        public void onFailure(Throwable t) {
            Log.e(TAG, "onFailure: " + t.getMessage());

        }
    }
}
