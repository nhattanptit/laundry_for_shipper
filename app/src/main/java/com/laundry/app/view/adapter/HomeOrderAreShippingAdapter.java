package com.laundry.app.view.adapter;

import android.view.View;

import androidx.databinding.ViewDataBinding;

import com.laundry.app.R;
import com.laundry.app.databinding.HomeOrderAreShippingListItemBinding;
import com.laundry.app.dto.order.OrderItem;
import com.laundry.base.BaseAdapter;

import java.util.List;

public class HomeOrderAreShippingAdapter extends BaseAdapter {

    private View.OnClickListener mOnClickListener;

    public HomeOrderAreShippingAdapter(View.OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }


    @Override
    protected int getLayoutResource(int viewType) {
        return R.layout.home_order_are_shipping_list_item;
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

        private final HomeOrderAreShippingListItemBinding binding;

        public OrderVH(ViewDataBinding viewDataBinding) {
            super(viewDataBinding);
            binding = (HomeOrderAreShippingListItemBinding) viewDataBinding;
        }

        @Override
        public void bind(OrderItem item) {
            binding.orderAreShippingDeliveryAddressContent.setText(item.getDeliveryAddress());
            binding.orderAreShippingPhonenumberContent.setText(item.getPhoneNumber());
            binding.homeStaffDeriveringOrderCallButton.setOnClickListener(mOnClickListener);
            binding.homeStaffDeriveringOrderDoneButton.setOnClickListener(mOnClickListener);
            binding.homeStaffDeriveringOrderCallButton.setTag(item);
            binding.homeStaffDeriveringOrderDoneButton.setTag(item);
        }
    }
}
