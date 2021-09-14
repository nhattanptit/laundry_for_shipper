package com.laundry.app.view.adapter;

import androidx.databinding.ViewDataBinding;

import com.laundry.app.R;
import com.laundry.app.databinding.CustomerOrderListItemBinding;
import com.laundry.app.dto.orderlistcustomer.OrderListCustomerDto;
import com.laundry.base.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

public class OrderListCustomerAdapter extends BaseAdapter {

    private ISOrderListCallBack mIsOrderListCallBack;

    public void setIsOrderListCallBack(ISOrderListCallBack isOrderListCallBack) {
        this.mIsOrderListCallBack = isOrderListCallBack;
    }

    @Override
    protected int getLayoutResource(int viewType) {
        return R.layout.customer_order_list_item;
    }

    @Override
    protected Object getDataInPosition(int position) {
        return dataList.get(position);
    }

    @Override
    protected BaseVH<?> onCreateVH(int viewType, ViewDataBinding viewDataBinding) {
        return new OrderListCustomerVH(viewDataBinding);
    }

    @Override
    public void submitList(List<?> list) {
        List<OrderListCustomerDisplay> displayList = new ArrayList<>();
        for (Object obj : list) {
            OrderListCustomerDisplay display = new OrderListCustomerDisplay();
            display.data = (OrderListCustomerDto) obj;
            displayList.add(display);
        }
        resetDataList(displayList);
    }

    private class OrderListCustomerVH extends BaseVH<OrderListCustomerDisplay> {

        private CustomerOrderListItemBinding binding;

        public OrderListCustomerVH(ViewDataBinding viewDataBinding) {
            super(viewDataBinding);
            binding = (CustomerOrderListItemBinding) viewDataBinding;
            binding.getRoot().setOnClickListener(view -> {
                OrderListCustomerDisplay display = (OrderListCustomerDisplay) getDataInPosition(getAbsoluteAdapterPosition());
                OrderListCustomerDto dto = display.data;
                mIsOrderListCallBack.onClickItem(dto.id);
            });
        }

        @Override
        public void bind(OrderListCustomerDisplay item) {
            super.bind(item);
            binding.nameReceiver.setText(item.data.shippingPersonName);
            binding.phoneReceiver.setText(item.data.shippingPersonPhoneNumber);
            binding.statusOrder.setText(item.data.getStatusContent());
            binding.createDate.setText(item.data.createdDate);
            binding.icon.setImageResource(item.getIcon());
        }
    }

    public interface ISOrderListCallBack {
        void onClickItem(int id);
    }
}
