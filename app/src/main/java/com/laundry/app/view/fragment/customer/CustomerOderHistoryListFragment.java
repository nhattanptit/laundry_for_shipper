package com.laundry.app.view.fragment.customer;

import android.view.View;

import com.laundry.app.R;
import com.laundry.app.control.ApiServiceOperator;
import com.laundry.app.control.DataController;
import com.laundry.app.databinding.CustomerHistoryOrderFragmentBinding;
import com.laundry.app.dto.orderlistcustomer.OrderListCustomerResponse;
import com.laundry.app.view.activity.OrderDetailCustomerActivity;
import com.laundry.app.view.adapter.OrderListCustomerAdapter;
import com.laundry.base.BaseFragment;

public class CustomerOderHistoryListFragment extends BaseFragment<CustomerHistoryOrderFragmentBinding> implements OrderListCustomerAdapter.ISOrderListCallBack {

    private final DataController mDataController = new DataController();
    private final OrderListCustomerAdapter mOrderListCustomerAdapter = new OrderListCustomerAdapter();

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.customer_history_order_fragment;
    }

    @Override
    public void onInitView() {
        binding.toolbar.setTitle(getString(R.string.order_history_uppercase));
        binding.toolbar.setHideButtonBack();
        beforeCallApi();
        mDataController.getOrderListCustomer(getMyActivity(), 0, 9999, new OrderListCustomerCallBack());
        mOrderListCustomerAdapter.setIsOrderListCallBack(this);
    }

    @Override
    public void onViewClick() {
    }

    @Override
    public void onClickItem(int id) {
        startActivity(OrderDetailCustomerActivity.getNewActivityStartIntent(getMyActivity(), id));
    }

    private class OrderListCustomerCallBack implements ApiServiceOperator.OnResponseListener<OrderListCustomerResponse> {
        @Override
        public void onSuccess(OrderListCustomerResponse body) {
            if (body != null && body.orderListCustomerDtoes != null) {
                mOrderListCustomerAdapter.submitList(body.orderListCustomerDtoes);
                binding.orderList.setAdapter(mOrderListCustomerAdapter);
            }
            afterCallApi();
        }

        @Override
        public void onFailure(Throwable t) {
            afterCallApi();
        }
    }

    private void beforeCallApi() {
        binding.progressBar.maskviewLayout.setVisibility(View.VISIBLE);
    }

    private void afterCallApi() {
        binding.progressBar.maskviewLayout.setVisibility(View.GONE);
    }
}