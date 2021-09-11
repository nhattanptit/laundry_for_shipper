package com.laundry.app.view.fragment.customer;

import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.laundry.app.R;
import com.laundry.app.control.ApiServiceOperator;
import com.laundry.app.control.DataController;
import com.laundry.app.data.APIConstant;
import com.laundry.app.databinding.FragmentHomeBinding;
import com.laundry.app.dto.UserInfo;
import com.laundry.app.dto.maps.MapDirectionResponse;
import com.laundry.app.dto.orderincompletelist.OrderListIncompleteCustomerDto;
import com.laundry.app.dto.orderincompletelist.OrderListIncompleteCustomerResponse;
import com.laundry.app.dto.servicelist.ServiceListResponse;
import com.laundry.app.utils.MapUtils;
import com.laundry.app.utils.SingleTapListener;
import com.laundry.app.view.activity.OrderDetailCustomerActivity;
import com.laundry.app.view.activity.ServicesDetailsActivity;
import com.laundry.app.view.adapter.BannerAdapter;
import com.laundry.app.view.adapter.OrderListIncompleteAdapter;
import com.laundry.app.view.adapter.ServiceListAdapter;
import com.laundry.app.view.dialog.LoginDialog;
import com.laundry.app.view.dialog.RegisterAccountDialog;
import com.laundry.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends BaseFragment<FragmentHomeBinding> implements OrderListIncompleteAdapter.ISOrderListCallBack {

    private static final String TAG = "HomeFragment";
    private final DataController mController = new DataController();
    private final OrderListIncompleteAdapter mIncompleteAdapter = new OrderListIncompleteAdapter();
    private final int[] image = {
            R.drawable.banner1,
            R.drawable.banner2,
            R.drawable.banner3,
            R.drawable.banner4,
            R.drawable.banner5};

    ServiceListAdapter serviceListAdapter = new ServiceListAdapter();

    private static final double LONG_START = 105.80816275382553;
    private static final double LAT_START = 20.99954902778333;
    private static final double LONG_END = 105.80974926197536;
    private static final double LAT_END = 20.99936771818483;
    private static final String GEOMETRIES = "geojson";

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
        createOrderListIncompleteLayout();
        getDirectionApi();
        mIncompleteAdapter.setIsOrderListCallBack(this);
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

    @Override
    public void onClickItem(int id) {
        startActivity(OrderDetailCustomerActivity.getNewActivityStartIntent(getMyActivity(), id));
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

    /**
     * ServiceListCallBack
     */
    private class MapDirectionCallback implements ApiServiceOperator.OnResponseListener<MapDirectionResponse> {

        @Override
        public void onSuccess(MapDirectionResponse body) {
            Log.d(TAG, "onSuccess: " + body.toString());
            Log.d(TAG, "Distance: " + body.getRoutes().get(0).getDistance() / 1000 + " km");
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
     * Get direction api
     */
    private void getDirectionApi() {

        mController.getDirectionMaps(MapUtils.getCoordinate(LONG_START, LAT_START, LONG_END, LAT_END), GEOMETRIES, APIConstant.MAPBOX_ACCESS_TOKEN, new MapDirectionCallback());
    }

    /**
     * Create login or register are
     */
    private void createLoginLayout() {
        binding.registerLoginLayout.setVisibility(UserInfo.getInstance().isLogin(getMyActivity()) ? View.GONE : View.VISIBLE);
    }

    /**
     * Create llay out order complete
     */
    private void createOrderListIncompleteLayout() {
        if (UserInfo.getInstance().isLogin(getMyActivity())) {
            mController.getOrderListIncompleteCustomer(getMyActivity(), 0, 1000, new OrderListIncompleteCallBack());
        } else {
            binding.orderListLayout.setVisibility(View.GONE);
        }
    }

    private void beforeCallApi() {
        binding.progressBar.maskviewLayout.setVisibility(View.VISIBLE);
    }

    private void afterCallApi() {
        binding.progressBar.maskviewLayout.setVisibility(View.GONE);
        binding.homeContent.setVisibility(View.VISIBLE);
    }

    private class OrderListIncompleteCallBack implements ApiServiceOperator.OnResponseListener<OrderListIncompleteCustomerResponse> {
        @Override
        public void onSuccess(OrderListIncompleteCustomerResponse body) {
            if (body != null && body.data != null && body.data.size() > 0) {
                if (isCheckStatusIncomplete(body.data)) {
                    List<Object> objectList = new ArrayList<>(body.data);
                    binding.orderListIncomplete.setAdapter(mIncompleteAdapter);
                    mIncompleteAdapter.setDataList(objectList);
                    binding.orderListLayout.setVisibility(View.VISIBLE);
                }
            }
        }

        @Override
        public void onFailure(Throwable t) {
        }
    }

    private boolean isCheckStatusIncomplete(List<OrderListIncompleteCustomerDto> dtoes) {
        for (OrderListIncompleteCustomerDto dto : dtoes) {
            if (dto.getIconByStatus() != 0) {
                return true;
            }
        }
        return false;
    }
}