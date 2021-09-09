package com.laundry.app.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
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

    private ISAddressCallBack mIsAddressCallBack;
    private List<AddressListlDto> mSelectedList = new ArrayList<>();

    public void setSelectAddressCallBack(ISAddressCallBack addressCallBack) {
        this.mIsAddressCallBack = addressCallBack;
    }

    private Context context;

    public AddressAdapter(Context context) {
        this.context = context;
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
//            billingAddressActivity.setAddressSelectedCallBack(new BillingAddressActivity.ISAddressSelected() {
//                @Override
//                public void itemSelectedBefore(AddressListlDto dto) {
//                    selectedItem(dto);
//                }
//            });


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
            binding.informationText.setText(String.format(String.valueOf(context.getResources().getString(R.string.name_and_phone_format)),
                    item.receiverName,
                    item.receiverPhoneNumber));
            binding.addressDetail.setText(String.format(String.valueOf(context.getResources().getString(R.string.address_format)),
                    item.address,
                    AddressInfo.getInstance().getWardNameById(item.city, item.district, item.ward),
                    AddressInfo.getInstance().getDistrictNameById(item.city, item.district),
                    AddressInfo.getInstance().getCityNameById(item.city)));

            if (isItemSelected(item)) {
                binding.selectedIcon.setVisibility(View.VISIBLE);
            } else {
                binding.selectedIcon.setVisibility(View.GONE);
            }r
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
        void onClickItemAdd(int position, AddressListlDto item);

        void onClickItemUpdate(int position, AddressListlDto item);

        void onClickItemDelete(int position, AddressListlDto item);

    }
}
