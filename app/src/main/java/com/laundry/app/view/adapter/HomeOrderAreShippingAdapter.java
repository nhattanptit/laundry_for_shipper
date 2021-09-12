package com.laundry.app.view.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;

import androidx.core.content.ContextCompat;
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
    private Context mContext;

    public HomeOrderAreShippingAdapter(Context context, View.OnClickListener onClickListener) {
        this.mContext = context;
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
            binding.homeStaffDeriveringOrderCancelButton.setOnClickListener(mOnClickListener);
            binding.homeStaffDeriveringOrderCallButton.setTag(item);
            binding.homeStaffDeriveringOrderDoneButton.setTag(item);
            binding.homeStaffDeriveringOrderCancelButton.setTag(item);

            binding.orderAreShippingItem.setOnClickListener(new SingleTapListener(mOnClickListener));
            binding.orderAreShippingItem.setTag(item);

            if (TextUtils.equals(Constant.SHIPPER_ACCEPTED_ORDER, item.status)) {
                binding.homeStaffDeriveringOrderCancelButton.setVisibility(View.VISIBLE);
                binding.homeStaffDeriveringOrderDoneButton.setVisibility(View.VISIBLE);
                binding.homeStaffDeriveringOrderCancelButton.setEnabled(true);
                binding.homeStaffDeriveringOrderDoneButton.setEnabled(true);

                binding.homeStaffDeriveringOrderDoneButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f);
                binding.homeStaffDeriveringOrderDoneButton.setBackground(ContextCompat.getDrawable(mContext,
                        R.drawable.shaper_button_green_big));
                binding.homeStaffDeriveringOrderDoneButton.setText(R.string.order_receivered);
            } else if (TextUtils.equals(Constant.SHIPPER_RECEIVED_ORDER, item.status)) {
                binding.homeStaffDeriveringOrderCancelButton.setVisibility(View.GONE);

                binding.homeStaffDeriveringOrderDoneButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f);
                binding.homeStaffDeriveringOrderDoneButton.setText(R.string.order_delivered);
                binding.homeStaffDeriveringOrderDoneButton.setEnabled(false);
                binding.homeStaffDeriveringOrderDoneButton.setBackground(ContextCompat.getDrawable(mContext,
                        R.drawable.shaper_button_green_disable));
            } else if (TextUtils.equals(Constant.STORE_RECEIVED_ORDER, item.status)
                    || TextUtils.equals(Constant.STORE_DONE_ORDER, item.status)) {
                binding.homeStaffDeriveringOrderCancelButton.setVisibility(View.GONE);
                binding.homeStaffDeriveringOrderDoneButton.setEnabled(true);
                binding.homeStaffDeriveringOrderDoneButton.setBackground(ContextCompat.getDrawable(mContext,
                        R.drawable.shaper_button_green_big));

                binding.homeStaffDeriveringOrderDoneButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f);
                binding.homeStaffDeriveringOrderDoneButton.setText(R.string.order_delivered);
            } else if (TextUtils.equals(Constant.SHIPPER_DELIVER_ORDER, item.status)) {
                binding.homeStaffDeriveringOrderCancelButton.setVisibility(View.GONE);

                binding.homeStaffDeriveringOrderDoneButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f);
                binding.homeStaffDeriveringOrderDoneButton.setText(R.string.order_complete);
            } else {
                binding.homeStaffDeriveringOrderCancelButton.setVisibility(View.GONE);
            }
        }
    }
}
