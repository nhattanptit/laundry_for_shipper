package com.laundry.app.view.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import com.laundry.app.R;
import com.laundry.app.constant.Constant;
import com.laundry.app.control.ApiServiceOperator;
import com.laundry.app.control.DataController;
import com.laundry.app.databinding.ServicesDetailsActivityBinding;
import com.laundry.app.dto.UserInfo;
import com.laundry.app.dto.order.OrderConfirmResponseDto;
import com.laundry.app.dto.ordercreate.OrderServiceDetailForm;
import com.laundry.app.dto.servicelist.ServiceListDto;
import com.laundry.app.dto.sevicedetail.ServiceDetailDto;
import com.laundry.app.dto.sevicedetail.ServicesDetailResponse;
import com.laundry.app.utils.ErrorDialog;
import com.laundry.app.view.adapter.ServicesOrderAdapter;
import com.laundry.app.view.dialog.LoginDialog;
import com.laundry.app.view.dialog.RegisterOrLoginFragment;
import com.laundry.base.BaseActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.appcompat.app.AlertDialog;

public class ServicesDetailsActivity extends BaseActivity<ServicesDetailsActivityBinding>
        implements ServicesOrderAdapter.IServiceDetailCallback, LoginDialog.LoginListener {

    private static final String TAG = "ServiceDetailFragment";
    public static final String KEY_SEND_DATA = "KEY_SEND_DATA";
    private final DataController mDataController = new DataController();
    private ServiceListDto mServiceListDto;
    private List<ServiceDetailDto> mServiceDetails = new ArrayList<>();
    private final ServicesOrderAdapter mServicesOrderAdapter = new ServicesOrderAdapter();
    private Map<Integer, OrderServiceDetailForm> mListItemSelected = new HashMap<>();

    @Override
    protected int getLayoutResource() {
        return R.layout.services_details_activity;
    }

    @Override
    public void onPreInitView() {
        mServiceListDto = (ServiceListDto) getIntent().getSerializableExtra(KEY_SEND_DATA);
    }

    @Override
    public void onInitView() {
        beforeCallApi();
        binding.servicesDetailRecycle.setAdapter(mServicesOrderAdapter);
        if (mServiceListDto != null) {
            binding.toolbar.setTitle(mServiceListDto.name);
            mDataController.getServicesDetail(mServiceListDto.id, new ServiceDetailCallBack());
        }

        binding.toolbar.setToolbarListener(view -> {
            onBackPressed();
        });

        mServicesOrderAdapter.setCallback(this);
    }

    /** Handle tap button book when login or yet login */
    @Override
    public void onViewClick() {
        binding.bookButton.setOnClickListener(view -> {
            if (getProductList().size() == 0) {
                Toast.makeText(ServicesDetailsActivity.this, getResources().getString(R.string.please_select_service), Toast.LENGTH_LONG).show();
            } else {
                if (UserInfo.getInstance().isLogin(this)) {

                    mDataController.oderConfirm(this, getProductList(), new ApiServiceOperator.OnResponseListener<OrderConfirmResponseDto>() {
                        @Override
                        public void onSuccess(OrderConfirmResponseDto body) {
                            body.data.serviceParentId = mServiceListDto.id;
                            Intent intent = new Intent(ServicesDetailsActivity.this, OrderConfirmActivity.class);
                            intent.putExtra(Constant.KEY_ORDER_CONFIRM_DTO, (Serializable) body.data);
                            startActivity(intent);
                        }

                        @Override
                        public void onFailure(Throwable t) {

                        }
                    });
                } else {
                    AlertDialog alertDialog = ErrorDialog.buildPopupOnlyPositive(this, getString(R.string.please_login_or_register),
                            R.string.ok, (dialogInterface, i) -> {
                                RegisterOrLoginFragment registerOrLoginFragment = new RegisterOrLoginFragment();
                                registerOrLoginFragment.show(getSupportFragmentManager(), RegisterOrLoginFragment.class.getSimpleName());
                            });
                    alertDialog.show();
                }
            }

        });

    }

    /** Tap button add and minus item */
    @SuppressLint("SetTextI18n")
    @Override
    public void onClickItem(int position, ServiceDetailDto item) {
        binding.money.setText(grandTotal(mServiceDetails) + "$");
        mListItemSelected.put(position, new OrderServiceDetailForm(item.serviceDetailId, item.quantity));
    }

    @Override
    public void onLoginSuccess() {

    }

    @Override
    public void onLoginSuccess(String currentTab) {

    }


    private List<OrderServiceDetailForm> getProductList() {
        List<OrderServiceDetailForm> list = new ArrayList<>();

        for (Map.Entry<Integer, OrderServiceDetailForm> entry : mListItemSelected.entrySet()) {
            if (entry.getValue().quantity > 0) {
                list.add(entry.getValue());
            }
        }
        return list;
    }

    private Double grandTotal(List<ServiceDetailDto> list) {
        Double totalPrice = 0.0;
        for (int i = 0; i < list.size(); i++) {
            totalPrice += list.get(i).totalPrice;
        }
        return totalPrice;
    }

    private void beforeCallApi() {
        binding.progressBar.maskviewLayout.setVisibility(View.VISIBLE);
    }

    private void afterCallApi() {
        binding.priceLayout.setVisibility(View.VISIBLE);
        binding.progressBar.maskviewLayout.setVisibility(View.GONE);
    }

    /** Call back call api services detail */
    private class ServiceDetailCallBack implements ApiServiceOperator.OnResponseListener<ServicesDetailResponse> {
        @Override
        public void onSuccess(ServicesDetailResponse body) {
            mServicesOrderAdapter.submitList(body.servicesDetails);
            mServiceDetails = body.servicesDetails;
            afterCallApi();
        }

        @Override
        public void onFailure(Throwable t) {
            afterCallApi();
        }
    }
}
