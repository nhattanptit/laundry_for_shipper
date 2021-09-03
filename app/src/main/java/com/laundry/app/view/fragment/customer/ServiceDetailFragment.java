package com.laundry.app.view.fragment.customer;

import android.annotation.SuppressLint;
import android.util.Log;

import com.laundry.app.R;
import com.laundry.app.control.ApiServiceOperator;
import com.laundry.app.control.DataController;
import com.laundry.app.databinding.ServicesDetailsFragmentBinding;
import com.laundry.app.dto.ordercreate.OrderResponse;
import com.laundry.app.dto.ordercreate.OrderServiceDetailForm;
import com.laundry.app.dto.servicelist.ServiceListDto;
import com.laundry.app.dto.sevicedetail.ServiceDetailDto;
import com.laundry.app.dto.sevicedetail.ServicesDetailResponse;
import com.laundry.app.view.adapter.ServiceDetailAdapter;
import com.laundry.app.view.fragment.LaundryFragment;

import java.util.ArrayList;
import java.util.List;

public class ServiceDetailFragment extends LaundryFragment<ServicesDetailsFragmentBinding> implements ServiceDetailAdapter.IServiceDetailCallback {

    private static final String TAG = "ServiceDetailFragment";
    public static final String KEY_SEND_DATA = "KEY_SEND_DATA";
    private final DataController mDataController = new DataController();
    private ServiceListDto mServiceListDto;
    private List<ServiceDetailDto> mServiceDetails = new ArrayList<>();
    private final ServiceDetailAdapter mServiceDetailAdapter = new ServiceDetailAdapter();

    @Override
    protected int getLayoutResource() {
        return R.layout.services_details_fragment;
    }

    @Override
    public void onPreInitView() {
        mServiceListDto = (ServiceListDto) getArguments().getSerializable(KEY_SEND_DATA);
    }

    @Override
    public void onInitView() {
        binding.servicesDetailRecycle.setAdapter(mServiceDetailAdapter);
        if (mServiceListDto != null) {
            mDataController.getServicesDetail(mServiceListDto.id, new ApiServiceOperator.OnResponseListener<ServicesDetailResponse>() {
                @Override
                public void onSuccess(ServicesDetailResponse body) {
                    mServiceDetailAdapter.submitList(body.servicesDetails);
                    mServiceDetails = body.servicesDetails;
                }

                @Override
                public void onFailure(Throwable t) {

                }
            });
        }
        mServiceDetailAdapter.setCallback(this);
    }

    @Override
    public void onViewClick() {
        binding.bookButton.setOnClickListener(view -> {
            List<OrderServiceDetailForm> list = new ArrayList<>();

            list.add(new OrderServiceDetailForm(3, 2));

            mDataController.createOrder(11, list, 1, "Ha Noi", new ApiServiceOperator.OnResponseListener<OrderResponse>() {
                @Override
                public void onSuccess(OrderResponse body) {
                    Log.d(TAG, "onSuccess: " + body.toString());
                }

                @Override
                public void onFailure(Throwable t) {
                    Log.e(TAG, "onFailure: " + t.getMessage());
                }
            });
        });
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onClickItem(int position, ServiceDetailDto item) {
        binding.money.setText(grandTotal(mServiceDetails) +"$");
    }

    private Double grandTotal(List<ServiceDetailDto> list) {
        Double totalPrice = 0.0;
        for (int i = 0; i < list.size(); i++) {
            totalPrice += list.get(i).totalPrice;
        }
        return totalPrice;
    }
}
