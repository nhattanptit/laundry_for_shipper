package com.laundry.app.view.adapter;

import android.view.View;

import androidx.databinding.ViewDataBinding;

import com.laundry.app.R;
import com.laundry.app.databinding.CustomerOrderIncompleteItemBinding;
import com.laundry.app.dto.orderincompletelist.OrderListIncompleteCustomerDto;
import com.laundry.base.BaseAdapter;

import java.util.List;

public class OrderListIncompleteAdapter extends BaseAdapter {

    private ISOrderListCallBack mIsOrderListCallBack;

    public void setIsOrderListCallBack(ISOrderListCallBack isOrderListCallBack) {
        this.mIsOrderListCallBack = isOrderListCallBack;
    }

    @Override
    protected int getLayoutResource(int viewType) {
        return R.layout.customer_order_incomplete_item;
    }

    @Override
    protected Object getDataInPosition(int position) {
        return dataList.get(position);
    }

    @Override
    protected BaseVH<?> onCreateVH(int viewType, ViewDataBinding viewDataBinding) {
        return new OrderIncomplete(viewDataBinding);
    }

    @Override
    public void submitList(List<?> list) {

    }

    private class OrderIncomplete extends BaseVH<OrderListIncompleteCustomerDto> {

        private CustomerOrderIncompleteItemBinding binding;

        public OrderIncomplete(ViewDataBinding viewDataBinding) {
            super(viewDataBinding);
            binding = (CustomerOrderIncompleteItemBinding) viewDataBinding;

            binding.getRoot().setOnClickListener(view -> {
                OrderListIncompleteCustomerDto dto = (OrderListIncompleteCustomerDto) dataList.get(getAbsoluteAdapterPosition());
                mIsOrderListCallBack.onClickItem(dto.id);
            });
        }

        @Override
        public void bind(OrderListIncompleteCustomerDto item) {
            super.bind(item);
            if (item.getIconByStatus() != 0) {
                binding.orderStatusTitle.setText(context.getString(R.string.order_status));
                binding.dateCreateTitle.setText(context.getString(R.string.date_create));
                binding.statusOrder.setText(item.status);
                binding.createDate.setText(item.createdDate);
                binding.icon.setImageResource(item.getIconByStatus());
            } else {
                binding.statusOrder.setVisibility(View.GONE);
                binding.createDate.setVisibility(View.GONE);
                binding.icon.setVisibility(View.GONE);
                binding.getRoot().setVisibility(View.GONE);
            }
        }
    }

    public interface ISOrderListCallBack {
        void onClickItem(int id);
    }

}
