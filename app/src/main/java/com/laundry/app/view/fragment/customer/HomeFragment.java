package com.laundry.app.view.fragment.customer;

import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.laundry.app.R;
import com.laundry.app.control.ApiServiceOperator;
import com.laundry.app.control.DataController;
import com.laundry.app.databinding.FragmentHomeBinding;
import com.laundry.app.dto.UserInfo;
import com.laundry.app.dto.servicelist.ServiceListResponse;
import com.laundry.app.utils.SingleTapListener;
import com.laundry.app.view.activity.ServicesDetailsActivity;
import com.laundry.app.view.adapter.BannerAdapter;
import com.laundry.app.view.adapter.ServiceListAdapter;
import com.laundry.app.view.dialog.LoginDialog;
import com.laundry.app.view.dialog.RegisterAccountDialog;
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

    }

    @Override
    public void onInitView() {
        // Banner list
        BannerAdapter bannerAdapter = new BannerAdapter(getContext(), image);
        binding.viewPager.setAdapter(bannerAdapter);
        binding.wormDotsIndicator.setViewPager(binding.viewPager);

        // Service all list
        binding.serviceList.setAdapter(serviceListAdapter);

        getServiceList();
        createLoginLayout();
    }

    @Override
    public void onViewClick() {
        serviceListAdapter.setCallback((position, item) -> {
            Intent intent = new Intent(getActivity(), ServicesDetailsActivity.class);
            intent.putExtra(ServicesDetailsActivity.KEY_SEND_DATA, item);
            startActivity(intent);
        });

        binding.login.setOnClickListener(new SingleTapListener(view -> {
            LoginDialog loginDialog = LoginDialog.newInstance(HomeFragment.class.getSimpleName());
            loginDialog.show(getMyActivity().getSupportFragmentManager(), LoginDialog.class.getSimpleName());
        }));

        binding.signUp.setOnClickListener(new SingleTapListener(view -> {
            RegisterAccountDialog registerAccountDialog = RegisterAccountDialog.newInstance(HomeFragment.class.getSimpleName());
            registerAccountDialog.show(getMyActivity().getSupportFragmentManager(), RegisterAccountDialog.class.getSimpleName());
        }));
    }

    /**
     * ServiceListCallBack
     */
    private class ServiceListCallBack implements ApiServiceOperator.OnResponseListener<ServiceListResponse> {

        @Override
        public void onSuccess(ServiceListResponse body) {
            serviceListAdapter.submitList(body.servicesList);
            afterCallApi();
        }

        @Override
        public void onFailure(Throwable t) {
            Log.e(TAG, "onFailure: " + t.getMessage());
            afterCallApi();
        }
    }

    private void createDisplay() {

    }

    private void getServiceList() {
        beforeCallApi();
        mController.getServicesAll(new ServiceListCallBack());
    }

    /**
     * Create login or register are
     */
    private void createLoginLayout() {
        binding.registerLoginLayout.setVisibility(UserInfo.getInstance().isLogin(getMyActivity()) ? View.GONE : View.VISIBLE);
    }

    private void beforeCallApi() {
        binding.progressBar.maskviewLayout.setVisibility(View.VISIBLE);
    }

    private void afterCallApi() {
        binding.progressBar.maskviewLayout.setVisibility(View.GONE);
        binding.homeContent.setVisibility(View.VISIBLE);
    }

}