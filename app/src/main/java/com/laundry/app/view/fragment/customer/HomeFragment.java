package com.laundry.app.view.fragment.customer;

import android.os.Bundle;
import android.util.Log;

import com.laundry.app.R;
import com.laundry.app.control.ApiServiceOperator;
import com.laundry.app.control.DataController;
import com.laundry.app.databinding.FragmentHomeBinding;
import com.laundry.app.dto.servicelist.ServiceListResponse;
import com.laundry.app.view.adapter.BannerAdapter;
import com.laundry.app.view.adapter.ServiceListAdapter;
import com.laundry.base.BaseFragment;

public class HomeFragment extends BaseFragment<FragmentHomeBinding> {

    private static final String TAG = "HomeFragment";
    private final DataController mController = new DataController();
    private final int[] image = {
            R.drawable.banner1,
            R.drawable.banner2,
            R.drawable.banner3,
            R.drawable.banner4,
            R.drawable.banner5};

    ServiceListAdapter serviceListAdapter = new ServiceListAdapter();

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_home;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onPreInitView() {
        mController.getServicesAll(new ApiServiceOperator.OnResponseListener<ServiceListResponse>() {
            @Override
            public void onSuccess(ServiceListResponse body) {
                serviceListAdapter.submitList(body.getServicesList());
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    @Override
    public void onInitView() {
        // Banner list
        BannerAdapter bannerAdapter = new BannerAdapter(getContext(), image);
        binding.viewPager.setAdapter(bannerAdapter);
        binding.wormDotsIndicator.setViewPager(binding.viewPager);

        // Service all list
        binding.serviceList.setAdapter(serviceListAdapter);
    }

    @Override
    public void onViewClick() {
        serviceListAdapter.setCallback((position, item) -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable(ServiceDetailFragment.KEY_SEND_DATA, item);
            navigateTo(R.id.action_navigation_customer_home_to_navigation_serviceDetailFragment, bundle);
        });
    }

}