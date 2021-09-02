package com.laundry.app.view.fragment.customer;

import android.util.Log;

import com.laundry.app.R;
import com.laundry.app.control.ApiServiceOperator;
import com.laundry.app.control.DataController;
import com.laundry.app.databinding.ServicesDetailsFragmentBinding;
import com.laundry.app.dto.ordercreate.OrderResponse;
import com.laundry.app.dto.ordercreate.OrderServiceDetailForm;
import com.laundry.app.dto.serviceall.ServiceAllDto;
import com.laundry.app.dto.sevicedetail.ServiceDetailDto;
import com.laundry.app.dto.sevicedetail.ServicesDetailResponse;
import com.laundry.app.view.adapter.ServiceDetailAdapter;
import com.laundry.app.view.adapter.ServiceDetailDisplay;
import com.laundry.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

public class ServiceDetailFragment extends BaseFragment<ServicesDetailsFragmentBinding> {

    private static final String TAG = "ServiceDetailFragment";
    public static final String KEY_SEND_DATA = "KEY_SEND_DATA";
    private DataController mDataController = new DataController();
    private ServiceAllDto mServiceAllDto;
    private ServiceDetailAdapter mServiceDetailAdapter = new ServiceDetailAdapter();

    @Override
    protected int getLayoutResource() {
        return R.layout.services_details_fragment;
    }

    @Override
    public void onPreInitView() {
        mServiceAllDto = (ServiceAllDto) getArguments().getSerializable(KEY_SEND_DATA);
    }

    @Override
    public void onInitView() {
        binding.servicesDetailRecycle.setAdapter(mServiceDetailAdapter);
        if (mServiceAllDto != null) {
            mDataController.getServicesDetail(mServiceAllDto.getId(), new ApiServiceOperator.OnResponseListener<ServicesDetailResponse>() {
                @Override
                public void onSuccess(ServicesDetailResponse body) {
                    mServiceDetailAdapter.submitList(body.getServiceDetailList());
                }

                @Override
                public void onFailure(Throwable t) {

                }
            });
        }

        List<OrderServiceDetailForm> list = new ArrayList<>();

        list.add(new OrderServiceDetailForm(3,16));

        mDataController.createOrder("11", list, 16, "Ha Noi", new ApiServiceOperator.OnResponseListener<OrderResponse>() {
            @Override
            public void onSuccess(OrderResponse body) {
                Log.d(TAG, "onSuccess: " + body.toString());
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });

    }

    @Override
    public void onViewClick() {

    }
}
