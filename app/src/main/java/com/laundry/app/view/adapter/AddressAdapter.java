package com.laundry.app.view.adapter;

import android.annotation.SuppressLint;

import androidx.databinding.ViewDataBinding;

import com.laundry.app.R;
import com.laundry.app.databinding.BillingAddressItemBinding;
import com.laundry.app.dto.addressall.AddressListlDto;
import com.laundry.app.utils.SingleTapListener;
import com.laundry.base.BaseAdapter;

import java.util.List;

public class AddressAdapter extends BaseAdapter {

    private ISAddressCallBack mIsAddressCallBack;

    public void setSelectAddressCallBack(ISAddressCallBack addressCallBack) {
        this.mIsAddressCallBack = addressCallBack;
    }

    @Override
    protected int getLayoutResource(int viewType) {
        return R.layout.billing_address_item;
    }

    @Override
    protected Object getDataInPosition(int position) {
        return dataList.get(position);
    }

    @Override
    protected BaseVH<?> onCreateVH(int viewType, ViewDataBinding viewDataBinding) {
        return new AddressVH(viewDataBinding);
    }

    @Override
    public void submitList(List<?> list) {

    }

    private class AddressVH extends BaseVH<AddressListlDto> {
        private BillingAddressItemBinding binding;

        public AddressVH(ViewDataBinding viewDataBinding) {
            super(viewDataBinding);
            binding = (BillingAddressItemBinding) viewDataBinding;
            binding.getRoot().setOnClickListener(new SingleTapListener(v -> {
                AddressListlDto item = (AddressListlDto) dataList.get(getAbsoluteAdapterPosition());
                mIsAddressCallBack.onClickItem(getAbsoluteAdapterPosition(), item);
            }));
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void bind(AddressListlDto item) {
            super.bind(item);
            binding.infomationText.setText(item.receiverName + " -- " + item.receiverPhoneNumber);
            binding.addressDetail.setText(item.address + " - " + item.ward + " - " + item.district + " - " + item.city);
        }
    }

    public interface ISAddressCallBack {
        void onClickItem(int position, AddressListlDto item);
    }
}
