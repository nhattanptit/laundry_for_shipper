package com.laundry.app.view.adapter;

import android.annotation.SuppressLint;
import android.view.View;

import androidx.databinding.ViewDataBinding;

import com.laundry.app.R;
import com.laundry.app.databinding.BillingAddressItemBinding;
import com.laundry.app.dto.addressall.AddressListlDto;
import com.laundry.app.utils.SingleTapListener;
import com.laundry.base.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

public class AddressAdapter extends BaseAdapter {

    private ISAddressCallBack mIsAddressCallBack;
    private List<AddressListlDto> mSelectedList = new ArrayList<>();

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
                selectedItem(item);
            }));
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void bind(AddressListlDto item) {
            super.bind(item);
            binding.informationText.setText(item.receiverName + " -- " + item.receiverPhoneNumber);
            binding.addressDetail.setText(item.address + " - " + item.ward + " - " + item.district + " - " + item.city);

            if (isItemSelected(item)) {
                binding.selectedIcon.setVisibility(View.VISIBLE);
            } else {
                binding.selectedIcon.setVisibility(View.GONE);
            }
        }

        private boolean isItemSelected(AddressListlDto item) {
            boolean isSelected = false;
            for (AddressListlDto v : mSelectedList) {
                if (item == v) {
                    isSelected = true;
                    break;
                }
            }
            return isSelected;
        }

        private void selectedItem(AddressListlDto item) {
            if (mSelectedList != null) {
                if (mSelectedList.size() > 0) {
                    mSelectedList.clear();
                }
                mSelectedList.add(item);
                notifyDataSetChanged();
            } else {
                throw new NullPointerException("create instance of list");
            }
        }
    }

    public interface ISAddressCallBack {
        void onClickItem(int position, AddressListlDto item);
    }
}
