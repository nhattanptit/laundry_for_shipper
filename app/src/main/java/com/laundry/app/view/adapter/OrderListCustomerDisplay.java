package com.laundry.app.view.adapter;

import android.util.Log;

import com.laundry.app.R;
import com.laundry.app.dto.orderlistcustomer.OrderListCustomerDto;
import com.laundry.base.BaseDisplay;

public class OrderListCustomerDisplay extends BaseDisplay<OrderListCustomerDto> {
    private static final String TAG = OrderListCustomerDisplay.class.getSimpleName();

    @Override
    protected int getIcon() {
        Log.d(TAG, "getIcon: " + data.getOrderListIcon());
        switch (data.getOrderListIcon()) {
            case NEW:
                return R.drawable.new_order_icon;
            case SHIPPER_ACCEPTED_ORDER:
                return R.drawable.shipper_accepted_order_icon;
            case SHIPPER_RECEIVED_ORDER:
                return R.drawable.shipper_receiverd_order_icon;
            case STORE_RECEIVED_ORDER:
                return R.drawable.delivered_order_icon;
            case STORE_DONE_ORDER:
                return R.drawable.store_done_order_icon;
            case SHIPPER_DELIVER_ORDER:
                return R.drawable.shipper_delivering_order_icon;
            case COMPLETE_ORDER:
                return R.drawable.store_receivered_order_icon;
            case CANCEL:
                return R.drawable.order_cancelled_icon;
            default:
                return R.drawable.new_order_icon;
        }
    }
}
