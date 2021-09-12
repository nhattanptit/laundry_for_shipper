package com.laundry.app.view.fragment.customer;

import android.text.TextUtils;
import android.view.View;

import com.laundry.app.R;
import com.laundry.app.control.ApiServiceOperator;
import com.laundry.app.control.DataController;
import com.laundry.app.databinding.CustomerHistoryOrderFragmentBinding;
import com.laundry.app.dto.Role;
import com.laundry.app.dto.UserInfo;
import com.laundry.app.dto.orderlistcustomer.OrderListCustomerResponse;
import com.laundry.app.utils.SharePreferenceManager;
import com.laundry.app.view.activity.OrderDetailCustomerActivity;
import com.laundry.app.view.adapter.OrderListCustomerAdapter;
import com.laundry.app.view.dialog.LoginDialog;
import com.laundry.app.view.dialog.RegisterAccountDialog;
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
        if (UserInfo.getInstance().isLogin(getMyActivity())) {
            binding.registerLoginLayout.setVisibility(View.GONE);
            binding.orderListLayout.setVisibility(View.VISIBLE);
            updateView();
        } else {
            binding.registerLoginLayout.setVisibility(View.VISIBLE);
            binding.orderListLayout.setVisibility(View.GONE);
        }

        String mMode = SharePreferenceManager.getMode(getMyActivity());
        if (TextUtils.equals(Role.CUSTOMER.role(), mMode)) {
            binding.registerLoginFragment.signUp.setVisibility(View.VISIBLE);
        }
    }

    private void updateView() {
        beforeCallApi();
        mDataController.getOrderListCustomer(getMyActivity(), 0, 9999, new OrderListCustomerCallBack());
        mOrderListCustomerAdapter.setIsOrderListCallBack(this);
    }

    @Override
    public void onViewClick() {
        binding.registerLoginFragment.login.setOnClickListener(v -> {
            LoginDialog loginDialog = LoginDialog.newInstance(CustomerOderHistoryListFragment.class.getSimpleName());
            loginDialog.show(getMyActivity().getSupportFragmentManager(), LoginDialog.class.getSimpleName());
        });

        binding.registerLoginFragment.signUp.setOnClickListener(v -> {
            RegisterAccountDialog registerAccountDialog = RegisterAccountDialog.newInstance(CustomerOderHistoryListFragment.class.getSimpleName());
            registerAccountDialog.show(getMyActivity().getSupportFragmentManager(), RegisterAccountDialog.class.getSimpleName());
        });
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