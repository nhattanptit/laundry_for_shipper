package com.laundry.app.view.adapter;

import android.annotation.SuppressLint;
import android.view.View;

import androidx.databinding.ViewDataBinding;

import com.laundry.app.R;
import com.laundry.app.databinding.BillingAddressItemBinding;
import com.laundry.app.dto.AddressInfo;
import com.laundry.app.dto.addressall.AddressListlDto;
import com.laundry.app.utils.SingleTapListener;
import com.laundry.base.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

public class AddressAdapter extends BaseAdapter {

    private static final String TAG = "AddressAdapter";
    private ISAddressCallBack mIsAddressCallBack;
    public List<AddressListlDto> mSelectedList = new ArrayList<>();

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

    public void setAddressSelected(AddressListlDto item) {
        if (item != null) {
            int position = getIndexItemSelectedBefore(item);
            if (position >= 0) {
                selectedItem((AddressListlDto) dataList.get(position));
            }
        }
    }

    private int getIndexItemSelectedBefore(AddressListlDto item) {
        for (int i = 0; i < dataList.size(); i++) {
            AddressListlDto dto = (AddressListlDto) dataList.get(i);
            if (dto.id == item.id) {
                return i;
            }
        }
        return -1;
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

    private class AddressVH extends BaseVH<AddressListlDto> {

        private BillingAddressItemBinding binding;

        public AddressVH(ViewDataBinding viewDataBinding) {
            super(viewDataBinding);
            binding = (BillingAddressItemBinding) viewDataBinding;
            binding.deleteIcon.setOnClickListener(view -> {
                AddressListlDto item = (AddressListlDto) dataList.get(getAbsoluteAdapterPosition());
                mIsAddressCallBack.onClickItemDelete(getAbsoluteAdapterPosition(), item);
            });

            binding.updateIcon.setOnClickListener(view -> {
                AddressListlDto item = (AddressListlDto) dataList.get(getAbsoluteAdapterPosition());
                mIsAddressCallBack.onClickItemUpdate(getAbsoluteAdapterPosition(), item);
            });

            binding.getRoot().setOnClickListener(new SingleTapListener(v -> {
                AddressListlDto item = (AddressListlDto) dataList.get(getAbsoluteAdapterPosition());
                mIsAddressCallBack.onClickItemAdd(getAbsoluteAdapterPosition(), item);
                selectedItem(item);
            }));
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void bind(AddressListlDto item) {
            super.bind(item);
            binding.informationText.setText(String.format(String.valueOf(context.getString(R.string.name_and_phone_format)),
                    item.receiverName,
                    item.receiverPhoneNumber));
            binding.addressDetail.setText(String.format(String.valueOf(context.getString(R.string.address_format)),
                    item.address,
                    AddressInfo.getInstance().getWardNameById(item.city, item.district, item.ward),
                    AddressInfo.getInstance().getDistrictNameById(item.city, item.district),
                    AddressInfo.getInstance().getCityNameById(item.city)));

            if (dataList.get(0).equals(item)) {
                binding.deleteIcon.setVisibility(View.GONE);
            }

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

    }

    public interface ISAddressCallBack {

        void onClickItemAdd(int position, AddressListlDto item);

        void onClickItemUpdate(int position, AddressListlDto item);

        void onClickItemDelete(int position, AddressListlDto item);

    }
}
