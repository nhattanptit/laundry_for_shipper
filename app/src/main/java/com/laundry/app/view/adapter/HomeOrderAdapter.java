package com.laundry.app.view.adapter;

import android.view.View;

import androidx.databinding.ViewDataBinding;

import com.bumptech.glide.Glide;
import com.laundry.app.R;
import com.laundry.app.databinding.HomeOrderItemBinding;
import com.laundry.app.dto.order.OrderItem;
import com.laundry.base.BaseAdapter;

import java.util.List;

public class HomeOrderAdapter extends BaseAdapter {

    private boolean mIsDisplayButtonAccept;

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
    class OrderVH extends BaseVH<OrderItem> {

        private final HomeOrderItemBinding binding;

        public OrderVH(ViewDataBinding viewDataBinding) {
            super(viewDataBinding);
            binding = (HomeOrderItemBinding) viewDataBinding;
        }

        @Override
        public void bind(OrderItem item) {
            Glide.with(binding.getRoot().getContext())
                    .load(item.getCustomerImage())
                    .centerCrop()
                    .placeholder(R.drawable.user_placeholder)
                    .into(binding.orderItemCustomerAvatar);
            binding.orderItemCustomerName.setText(item.getCustomerName());
            binding.orderItemOrderCode.setText(item.getOrderCode());
            binding.homeOrderOrderName.setText(item.getOrderName());
            binding.homeStaffItemAcceptButton.setVisibility(mIsDisplayButtonAccept ? View.VISIBLE : View.GONE);
            binding.homeOrderOrderName.setText(item.getOrderName());
            binding.homeOrderPickupDateTime.setText(item.getPickupDateTime());
            binding.homeOrderPickupAddress.setText(item.getPickupAddress());
            binding.homeOrderDeliveryDateTime.setText(item.getDeliveryDateTime());
            binding.homeOrderDeliveryAddress.setText(item.getDeliveryAddress());
        }
    }
}
