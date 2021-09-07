package com.laundry.app.view.activity;

import android.annotation.SuppressLint;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;
import android.view.View;

import com.laundry.app.R;
import com.laundry.app.constant.Constant;
import com.laundry.app.control.ApiServiceOperator;
import com.laundry.app.control.DataController;
import com.laundry.app.data.APIConstant;
import com.laundry.app.databinding.OrderConfirmActivityBinding;
import com.laundry.app.dto.BaseResponse;
import com.laundry.app.dto.addressaccount.AddressRegisteredDto;
import com.laundry.app.dto.addressaccount.AddressRegisteredResponse;
import com.laundry.app.dto.addressaccount.User;
import com.laundry.app.dto.maps.MapDirectionResponse;
import com.laundry.app.dto.order.OrderConfirmDto;
import com.laundry.app.dto.order.OrderConfirmResponseDto;
import com.laundry.app.dto.sevicedetail.ServiceDetailDto;
import com.laundry.app.dto.shippingfee.ShippingFeeResponseDto;
import com.laundry.app.utils.MapUtils;
import com.laundry.app.utils.SingleTapListener;
import com.laundry.app.view.adapter.ServicesOrderAdapter;
import com.laundry.app.view.fragment.customer.HomeFragment;
import com.laundry.base.BaseActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.laundry.app.constant.Constant.PRICE_FORMAT;

public class OrderConfirmActivity extends BaseActivity<OrderConfirmActivityBinding> {

    private static final String TAG = OrderConfirmActivity.class.getSimpleName();
    private final ServicesOrderAdapter mServicesOrderAdapter = new ServicesOrderAdapter();
    private List<ServiceDetailDto> mServiceDetails = new ArrayList<>();
    private OrderConfirmDto responseDto;
    private DataController mDataController = new DataController();
    private double subTotal;
    private double shippingFee;
    private double mDistance;

    @Override
    protected int getLayoutResource() {
        return R.layout.order_confirm_activity;
    }

    @Override
    public void onPreInitView() {
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onInitView() {
        responseDto = (OrderConfirmDto) getIntent().getSerializableExtra(Constant.KEY_ORDER_CONFIRM_DTO);

        binding.toolbar.setTitle(getString(R.string.order_confirm));

        createDisplay();
    }

    @Override
    public void onViewClick() {
        binding.toolbar.setToolbarListener(view -> {
            onBackPressed();
        });

        binding.momoButton.setOnClickListener(new SingleTapListener(view -> {
            binding.momoButton.setChecked(true);
            binding.cashPaymentButton.setChecked(false);

        }));

        binding.cashPaymentButton.setOnClickListener(new SingleTapListener(view -> {
            binding.cashPaymentButton.setChecked(true);
            binding.momoButton.setChecked(false);

        }));

        binding.shippingAddressCardView.setOnClickListener(new SingleTapListener(view -> {

        }));
    }

    private void createDisplay() {
        subTotal = responseDto.totalServicesFee;
        mServicesOrderAdapter.typeService = ServicesOrderAdapter.SERVICES_DETAIL_VIEW_TYPE.ORDER;
        mServiceDetails = responseDto.products;
        mServicesOrderAdapter.submitList(mServiceDetails);
        binding.orderedList.setAdapter(mServicesOrderAdapter);
        binding.subTotalFee.setText(String.format(PRICE_FORMAT, subTotal));
        binding.vatFee.setText(String.format(PRICE_FORMAT, (subTotal + shippingFee) * 0.1));

        getShippingFee();
    }

    private void beforeCallApi() {
        binding.progressBar.maskviewLayout.setVisibility(View.VISIBLE);
    }

    private void afterCallApi() {
        binding.progressBar.maskviewLayout.setVisibility(View.GONE);
    }

    private void getShippingFee() {
        getLatLong();
        getDistance();
    }

    private void getDistance() {
        mDataController.getDirectionMaps(MapUtils.getCoordinate(Constant.LONG_START, Constant.LAT_START, longitude, latitude), Constant.GEOMETRIES,
                APIConstant.MAPBOX_ACCESS_TOKEN, new MapDirectionCallback());
    }

    double longitude = 0;
    double latitude = 0;

    private void getLatLong() {
        Geocoder coder = new Geocoder(this);
        try {
            ArrayList<Address> adresses = (ArrayList<Address>) coder.getFromLocationName("17, Duy Tân, Dịch Vọng Hậu, Cầu Giấy, Hà Nội", 1);
            for (Address add : adresses) {
                longitude = add.getLongitude();
                latitude = add.getLatitude();
                break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private class MapDirectionCallback implements ApiServiceOperator.OnResponseListener<MapDirectionResponse> {
        @Override
        public void onSuccess(MapDirectionResponse body) {
            Log.d(TAG, "Distance: " + body.getRoutes().get(0).getDistance() / 1000 + " km");
            mDistance = (body.getRoutes().get(0).getDistance() / 1000);
            mDataController.getShippingFee(OrderConfirmActivity.this, String.valueOf(mDistance), new ShippingFeeCallback());
        }

        @Override
        public void onFailure(Throwable t) {

        }
    }

    private class ShippingFeeCallback implements ApiServiceOperator.OnResponseListener<ShippingFeeResponseDto> {
        @Override
        public void onSuccess(ShippingFeeResponseDto body) {
            
        }

        @Override
        public void onFailure(Throwable t) {

        }
    }
}
