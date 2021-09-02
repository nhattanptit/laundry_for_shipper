package com.laundry.app.view.fragment.customer;

import android.os.Bundle;
import android.util.Log;

import com.laundry.app.R;
import com.laundry.app.control.ApiServiceOperator;
import com.laundry.app.control.DataController;
import com.laundry.app.databinding.FragmentHomeBinding;
import com.laundry.app.dto.serviceall.ServiceAllDto;
import com.laundry.app.dto.serviceall.ServiceAllResponse;
import com.laundry.app.view.adapter.BannerAdapter;
import com.laundry.app.view.adapter.ServiceAllAdapter;
import com.laundry.base.BaseFragment;

public class HomeFragment extends BaseFragment<FragmentHomeBinding> {

    private static final String TAG = "HomeFragment";
    private final DataController mController = new DataController();
    private final int[] image = {
            R.drawable.wash_and_iron_icon,
            R.drawable.ironing_icon,
            R.drawable.dry_cleaning_icon,
            R.drawable.wash_blanket_icon};

    //    BannerAdapter bannerAdapter = new BannerAdapter(getContext(), image);
    ServiceAllAdapter serviceAllAdapter = new ServiceAllAdapter();

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
        mController.getServicesAll(new ApiServiceOperator.OnResponseListener<ServiceAllResponse>() {
            @Override
            public void onSuccess(ServiceAllResponse body) {
                serviceAllAdapter.submitList(body.getServicesAllList());
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
        binding.serviceAllRecycle.setAdapter(serviceAllAdapter);
    }

    @Override
    public void onViewClick() {
        serviceAllAdapter.setCallback(new ServiceAllAdapter.IServiceAllCallback() {
            @Override
            public void onClickItem(int position, ServiceAllDto item) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(ServiceDetailFragment.KEY_SEND_DATA, item);
                navigateTo(R.id.action_navigation_customer_home_to_navigation_serviceDetailFragment, bundle);
            }
        });
    }

}