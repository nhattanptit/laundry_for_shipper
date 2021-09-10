package com.laundry.app.view.adapter;

import android.view.View;

import androidx.databinding.ViewDataBinding;

import com.bumptech.glide.Glide;
import com.laundry.app.R;
import com.laundry.app.databinding.HomeOrderItemBinding;
import com.laundry.app.dto.order.OrderItem;
import com.laundry.app.dto.orderlistshipper.OrderListShipperDto;
import com.laundry.base.BaseAdapter;

import java.util.List;

public class HomeOrderAdapter extends BaseAdapter {

    private boolean mIsDisplayButtonAccept;
    private View.OnClickListener mOnClickListener;

    public void setOnClickListener(View.OnClickListener mOnClickListener) {
        this.mOnClickListener = mOnClickListener;
    }

    public HomeOrderAdapter(boolean isDisplayButtonAccept) {
        this.mIsDisplayButtonAccept = isDisplayButtonAccept;
    }

    @Override
    protected int getLayoutResource(int viewType) {
        return R.layout.home_order_item;
    }

    @Override
    protected Object getDataInPosition(int position) {
        return dataList.get(position);
    }

    @Override
    protected BaseVH<?> onCreateVH(int viewType, ViewDataBinding viewDataBinding) {
        return new OrderVH(viewDataBinding);
    }

    @Override
    public void submitList(List<?> list) {
        setDataList((List<Object>) list);
    }

    /**
     * Order viewholder
     */
    class OrderVH extends BaseVH<OrderListShipperDto> {

        private final HomeOrderItemBinding binding;

        public OrderVH(ViewDataBinding viewDataBinding) {
            super(viewDataBinding);
            binding = (HomeOrderItemBinding) viewDataBinding;
        }

        @Override
        public void bind(OrderListShipperDto item) {
            Glide.with(binding.getRoot().getContext())
                    .load("")
                    .centerCrop()
                    .placeholder(R.drawable.user_placeholder)
                    .into(binding.orderItemCustomerAvatar);
            binding.orderItemCustomerName.setText(item.shippingNamePerson);
            binding.homeOrderOrderName.setText(item.serviceName);
            binding.homeStaffItemAcceptButton.setVisibility(mIsDisplayButtonAccept ? View.VISIBLE : View.GONE);
            binding.homeOrderPickupDateTime.setText(item.pickUpDateTime);
            binding.homeOrderPickupAddress.setText(item.pickUpAddress);
            binding.homeOrderDeliveryDateTime.setText(item.deliveryDateTime);
            binding.homeOrderDeliveryAddress.setText(item.deliverAddress);
            binding.orderHomeItemLayout.setOnClickListener(mOnClickListener);
            binding.orderHomeItemLayout.setTag(item);
            binding.homeStaffItemAcceptButton.setOnClickListener(mOnClickListener);
            binding.homeStaffItemAcceptButton.setTag(item);
        }
    }
}
