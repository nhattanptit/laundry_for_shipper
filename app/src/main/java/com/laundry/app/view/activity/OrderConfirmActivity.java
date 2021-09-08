package com.laundry.app.view.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.text.TextUtils;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;

import com.laundry.app.R;
import com.laundry.app.constant.Constant;
import com.laundry.app.control.ApiServiceOperator;
import com.laundry.app.control.DataController;
import com.laundry.app.data.APIConstant;
import com.laundry.app.databinding.OrderConfirmActivityBinding;
import com.laundry.app.dto.addressall.AddressListlDto;
import com.laundry.app.dto.maps.MapDirectionResponse;
import com.laundry.app.dto.order.OrderConfirmDto;
import com.laundry.app.dto.ordercreate.OrderRequest;
import com.laundry.app.dto.ordercreate.OrderResponseDto;
import com.laundry.app.dto.ordercreate.OrderServiceDetailForm;
import com.laundry.app.dto.sevicedetail.ServiceDetailDto;
import com.laundry.app.dto.shippingfee.ShippingFeeResponseDto;
import com.laundry.app.utils.ErrorDialog;
import com.laundry.app.utils.MapUtils;
import com.laundry.app.utils.SingleTapListener;
import com.laundry.app.view.adapter.ServicesOrderAdapter;
import com.laundry.base.BaseActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.laundry.app.constant.Constant.PRICE_FORMAT;

public class OrderConfirmActivity extends BaseActivity<OrderConfirmActivityBinding> {

    private static final String TAG = OrderConfirmActivity.class.getSimpleName();
    private final ServicesOrderAdapter mServicesOrderAdapter = new ServicesOrderAdapter();
    private OrderConfirmDto responseDto;
    private final DataController mDataController = new DataController();
    private AddressListlDto addressDto;

    private double subTotal = 0.0;
    private double shippingFee = 0.0;
    private double mDistance = 0.0;
    double longitude = 0;
    double latitude = 0;

    private MapDirectionResponse mMapDirectionResponse;

    public void setMapDirectionResponse(MapDirectionResponse mapDirectionResponse) {
        this.mMapDirectionResponse = mapDirectionResponse;
    }

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
            binding.momoButton.setChecked(false);
            binding.cashPaymentButton.setChecked(true);
        }));

        binding.shippingAddressCardView.setOnClickListener(new SingleTapListener(view -> {
            Intent intent = new Intent(this, BillingAddressActivity.class);
            someActivityResultLauncher.launch(intent);

        }));

        binding.placeOrderButton.setOnClickListener(new SingleTapListener(view -> {


            if (addressDto == null || TextUtils.isEmpty(addressDto.city) || TextUtils.isEmpty(addressDto.district)
                    || TextUtils.isEmpty(addressDto.ward) || TextUtils.isEmpty(addressDto.city)) {
                AlertDialog alertDialog = ErrorDialog.buildPopupOnlyPositive(OrderConfirmActivity.this, getString(R.string.please_select_shipping_address), R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                alertDialog.show();
                return;
            }
            String address = String.format(getString(R.string.address_format), addressDto.address, addressDto.ward, addressDto.district, addressDto.city);
            OrderRequest request = new OrderRequest();
            request.distance = mDistance;
            request.orderServiceDetails = formatProductList();
            request.serviceId = responseDto.serviceParentId;
            request.shippingPersonName = addressDto.receiverName;
            request.shippingAddress = address;
            request.totalServiceFee = subTotal;
            request.totalShipFee = shippingFee;
            request.shippingPersonPhoneNumber = addressDto.receiverPhoneNumber;
            request.longShipping = longitude +"";
            request.latShipping = latitude +"";

            mDataController.createOrder(this, request, new OrderCreateCallback());

        }));
    }

    private void createDisplay() {
        subTotal = responseDto.totalServicesFee;
        mServicesOrderAdapter.typeService = ServicesOrderAdapter.SERVICES_DETAIL_VIEW_TYPE.ORDER;
        List<ServiceDetailDto> products = responseDto.products;
        mServicesOrderAdapter.submitList(products);
        binding.orderedList.setAdapter(mServicesOrderAdapter);
        binding.subTotalFee.setText(String.format(PRICE_FORMAT, subTotal));
        binding.totalPriceFee.setText(String.format(PRICE_FORMAT, (subTotal + shippingFee)));
        binding.shippingFeeMoney.setText(String.format(PRICE_FORMAT, shippingFee));

    }

    private final ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    addressDto = (AddressListlDto) data.getSerializableExtra(BillingAddressActivity.RESULT_CODE_ADDRESS);
                    updateView();
                }
            });

    private void updateView() {
        if (addressDto != null && addressDto.user != null) {

            String fullAddress = String.format(getString(R.string.address_format), addressDto.address, addressDto.ward, addressDto.district, addressDto.city);
            binding.nameAndPhoneText.setText(String.format(getString(R.string.name_and_phone_format),
                    addressDto.receiverName, addressDto.receiverPhoneNumber));
            binding.shippingAddressText.setText(fullAddress);

            binding.nameAndPhoneText.setVisibility(View.VISIBLE);
            binding.shippingAddressText.setVisibility(View.VISIBLE);
            binding.addressNoContentText.setVisibility(View.GONE);

            getShippingFee(fullAddress);
        } else {
            binding.nameAndPhoneText.setVisibility(View.GONE);
            binding.shippingAddressText.setVisibility(View.GONE);
            binding.addressNoContentText.setVisibility(View.VISIBLE);
        }
    }

    private void beforeCallApi() {
        binding.progressBar.maskviewLayout.setVisibility(View.VISIBLE);
    }

    private void afterCallApi() {
        binding.progressBar.maskviewLayout.setVisibility(View.GONE);
    }

    private void getShippingFee(String fullAddress) {
        beforeCallApi();
        getLatLong(fullAddress);
        getDistance();
    }

    private void getDistance() {
        mDataController.getDirectionMaps(MapUtils.getCoordinate(Constant.LONG_START, Constant.LAT_START, longitude, latitude), Constant.GEOMETRIES,
                APIConstant.MAPBOX_ACCESS_TOKEN, new MapDirectionCallback(this));
    }

    private void getLatLong(String fullAddress) {
        Geocoder coder = new Geocoder(this);
        try {
            ArrayList<Address> address = (ArrayList<Address>) coder.getFromLocationName(fullAddress, 1);
            for (Address add : address) {
                longitude = add.getLongitude();
                latitude = add.getLatitude();
                break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<OrderServiceDetailForm> formatProductList() {
        List<OrderServiceDetailForm> list = new ArrayList<>();
        for (ServiceDetailDto dto : responseDto.products) {
            list.add(new OrderServiceDetailForm(dto.serviceDetailId, dto.quantity));
        }
        return list;
    }

    private class MapDirectionCallback implements ApiServiceOperator.OnResponseListener<MapDirectionResponse> {
        OrderConfirmActivity mActivity;

        public MapDirectionCallback(OrderConfirmActivity mActivity) {
            this.mActivity = mActivity;
        }

        @Override
        public void onSuccess(MapDirectionResponse body) {
            mDistance = (body.getRoutes().get(0).getDistance() / 1000);
            mActivity.setMapDirectionResponse(body);
            mDataController.getShippingFee(OrderConfirmActivity.this, String.valueOf(mDistance), new ShippingFeeCallback());
        }

        @Override
        public void onFailure(Throwable t) {

        }
    }

    private class ShippingFeeCallback implements ApiServiceOperator.OnResponseListener<ShippingFeeResponseDto> {
        @Override
        public void onSuccess(ShippingFeeResponseDto body) {

            shippingFee = body.data;
            binding.shippingFeeMoney.setText(String.format(PRICE_FORMAT, shippingFee));
            binding.totalPriceFee.setText(String.format(PRICE_FORMAT, (subTotal + body.data)));

            afterCallApi();
        }

        @Override
        public void onFailure(Throwable t) {

        }
    }

    private class OrderCreateCallback implements ApiServiceOperator.OnResponseListener<OrderResponseDto> {
        @Override
        public void onSuccess(OrderResponseDto body) {
            body.data.latitude = latitude;
            body.data.longitude = longitude;


            // Move to order success
            if (TextUtils.equals("200", body.statusCd)) {
                Intent intent = new Intent(OrderConfirmActivity.this, OrderSuccessActivity.class);
                intent.putExtra(Constant.KEY_BUNDLE_MAP_DIRECTION_RESPONSE, mMapDirectionResponse);
                intent.putExtra(Constant.KEY_BUNDLE_ORDER_RESPONSE, body);
                intent.putExtra(Constant.KEY_BUNDLE_IS_CASH_PAYMENT_METHOD, binding.cashPaymentButton.isChecked());
                startActivity(intent);
                finish();
            } else {
                // Go to order fail
            }
        }

        @Override
        public void onFailure(Throwable t) {
                // Go to order fail
        }
    }
}
