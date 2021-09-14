package com.laundry.app.view.adapter;

import android.view.View;
import android.view.ViewGroup;

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
            if (item.getIconByStatus() == 0) {
                binding.getRoot().setVisibility(View.GONE);
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) itemView.getLayoutParams();
                params.width = 0;
                params.height = 0;
                params.setMargins(0, 0, 0, 0);
                itemView.requestLayout();
            } else {
                binding.nameReceiver.setText(item.shippingPersonName);
                binding.phoneReceiver.setText(item.shippingPersonPhoneNumber);
                binding.statusOrder.setText(item.getStatusContent());
                binding.createDate.setText(item.createdDate);
                binding.icon.setImageResource(item.getIconByStatus());
            }
        }
    }

    public interface ISOrderListCallBack {
        void onClickItem(int id);
    }

}
