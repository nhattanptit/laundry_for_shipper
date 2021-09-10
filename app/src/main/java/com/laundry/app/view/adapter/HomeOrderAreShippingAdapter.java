package com.laundry.app.view.adapter;

import android.text.TextUtils;
import android.view.View;

import androidx.databinding.ViewDataBinding;

import com.laundry.app.R;
import com.laundry.app.constant.Constant;
import com.laundry.app.databinding.HomeOrderAreShippingListItemBinding;
import com.laundry.app.dto.orderlistshipper.OrderListShipperDto;
import com.laundry.app.utils.SingleTapListener;
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
    class OrderVH extends BaseVH<OrderListShipperDto> {

        private final HomeOrderAreShippingListItemBinding binding;

        public OrderVH(ViewDataBinding viewDataBinding) {
            super(viewDataBinding);
            binding = (HomeOrderAreShippingListItemBinding) viewDataBinding;
        }

        @Override
        public void bind(OrderListShipperDto item) {
            binding.orderAreShippingDeliveryAddressContent.setText(item.deliverAddress);
            binding.orderAreShippingPhonenumberContent.setText(item.shippingPhoneNumber);
            binding.statusIcon.setBackgroundResource(item.getIconByStatus());
            binding.statusContent.setText(item.getStatusContent());

            binding.homeStaffDeriveringOrderCallButton.setOnClickListener(mOnClickListener);
            binding.homeStaffDeriveringOrderDoneButton.setOnClickListener(mOnClickListener);
            binding.homeStaffDeriveringOrderCallButton.setTag(item);
            binding.homeStaffDeriveringOrderDoneButton.setTag(item);

            binding.orderAreShippingItem.setOnClickListener(new SingleTapListener(mOnClickListener));
            binding.orderAreShippingItem.setTag(item);

            if (TextUtils.equals(Constant.SHIPPER_DELIVER_ORDER, item.status)
                    || TextUtils.equals(Constant.SHIPPER_RECEIVED_ORDER, item.status)) {
                binding.homeStaffDeriveringOrderDoneButton.setEnabled(true);
                binding.homeStaffDeriveringOrderDoneButton.setBackground(binding.homeStaffDeriveringOrderDoneButton.getContext().getResources().getDrawable(R.drawable.shaper_button_green_big));
            } else {
                binding.homeStaffDeriveringOrderDoneButton.setEnabled(false);
                binding.homeStaffDeriveringOrderDoneButton.setBackground(binding.homeStaffDeriveringOrderDoneButton.getContext().getResources().getDrawable(R.drawable.shaper_button_green_big_disable));
            }
        }
    }
}
