package com.laundry.app.view.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.databinding.DataBindingUtil;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.laundry.app.R;
import com.laundry.app.constant.Constant;
import com.laundry.app.control.ApiServiceOperator;
import com.laundry.app.control.DataController;
import com.laundry.app.data.APIConstant;
import com.laundry.app.databinding.ActivityOrderDetailShipperBinding;
import com.laundry.app.dto.BaseResponse;
import com.laundry.app.dto.maps.MapDirectionResponse;
import com.laundry.app.dto.ordercreate.OrderResponseDto;
import com.laundry.app.dto.sevicedetail.ServiceDetailDto;
import com.laundry.app.utils.MapUtils;
import com.laundry.app.utils.SingleTapListener;
import com.laundry.app.view.adapter.ServicesOrderAdapter;
import com.laundry.base.BaseActivity;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static com.laundry.app.constant.Constant.GEOMETRIES;
import static com.laundry.app.constant.Constant.ICON_ID;
import static com.laundry.app.constant.Constant.LAT_START;
import static com.laundry.app.constant.Constant.LAYER_ID;
import static com.laundry.app.constant.Constant.LONG_START;
import static com.laundry.app.constant.Constant.SOURCE_ID;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;

public class OrderDetailShipperActivity extends BaseActivity<ActivityOrderDetailShipperBinding> implements BaseActivity.ConfigPermission
        , OnMapReadyCallback
        , View.OnClickListener
, SwipeRefreshLayout.OnRefreshListener {
    //----------------------------------------------------------------------------------------------
    //------------ instance variable
    //----------------------------------------------------------------------------------------------
    private DataController mDataController = new DataController();

    private int mOrderId;

    public static Intent getNewActivityStartIntent(Context context, int orderId) {
        Intent intent = new Intent(context, OrderDetailShipperActivity.class);
        intent.putExtra(Constant.KEY_BUNDLE_ORDER_ID, orderId);
        return intent;
    }

    private ArrayList<String> listPermission = new ArrayList<>();

    private OrderResponseDto mOrderResponseDto;

    private final ServicesOrderAdapter mServicesOrderAdapter = new ServicesOrderAdapter();

    public void setGeometries(String geometries) {
        this.geometries = geometries;
    }

    private String geometries;

    public void setOrderResponseDto(OrderResponseDto orderResponseDto) {
        this.mOrderResponseDto = orderResponseDto;
    }

    //----------------------------------------------------------------------------------------------
    //------------ override method
    //----------------------------------------------------------------------------------------------

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_order_detail_shipper;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() != null) {
            mOrderId = getIntent().getIntExtra(Constant.KEY_BUNDLE_ORDER_ID, -1);
        }

        //Mapbox access token is configured here. This needs to be called either in your application
        //object or in the same activity which contains the mapview.
        //Mapbox init before inflate view xml
        Mapbox.getInstance(this, APIConstant.MAPBOX_ACCESS_TOKEN);


        //This contains the MapView in XML and needs to be called after the access token is configured.
        binding = DataBindingUtil.setContentView(this, getLayoutResource());
        binding.setLifecycleOwner(this);
        binding.pullToRefresh.setOnRefreshListener(this);

        binding.mapView.onCreate(savedInstanceState);

        // Add permission
        listPermission.add(Manifest.permission.CALL_PHONE);

        initToolbar();

        handleSwipeBetweenMapAndScrollView();

        loadData();
    }


    @Override
    public void onInitView() {

    }

    @Override
    public void onViewClick() {

    }

    @Override
    public void onResume() {
        super.onResume();
        binding.mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        binding.mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        binding.mapView.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        binding.mapView.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        binding.mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        binding.mapView.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding.mapView.onDestroy();
    }


    @Override
    public void onAllow() {
        callNow(mOrderResponseDto.data.shipperDto.phoneNumber);
    }

    @Override
    public void onDenied() {

    }

    @Override
    public void onMapReady(@NonNull @NotNull MapboxMap mapboxMap) {
        List<Feature> symbolLayerIconFeatureList = new ArrayList<>();
        symbolLayerIconFeatureList.add(Feature.fromGeometry(
                Point.fromLngLat(LONG_START, LAT_START)));
        symbolLayerIconFeatureList.add(Feature.fromGeometry(
                Point.fromLngLat(mOrderResponseDto.data.longitude, mOrderResponseDto.data.latitude)));

        mapboxMap.setStyle(new Style.Builder().fromUri(Style.MAPBOX_STREETS)

                // Add the SymbolLayer icon image to the map style
                .withImage(ICON_ID, BitmapFactory.decodeResource(
                        getResources(), R.drawable.mapbox_marker_icon_default))

                // Adding a GeoJson source for the SymbolLayer icons.
                .withSource(new GeoJsonSource(SOURCE_ID,
                        FeatureCollection.fromFeatures(symbolLayerIconFeatureList)))

                // Adding the actual SymbolLayer to the map style. An offset is added that the bottom of the red
                // marker icon gets fixed to the coordinate, rather than the middle of the icon being fixed to
                // the coordinate point. This is offset is not always needed and is dependent on the image
                // that you use for the SymbolLayer icon.
                .withLayer(new SymbolLayer(LAYER_ID, SOURCE_ID)
                        .withProperties(
                                iconImage(ICON_ID),
                                iconAllowOverlap(true),
                                iconIgnorePlacement(true)
                        )
                ), style -> {
            // Draw polyline between user & store
            MapUtils.drawPolylines(MapUtils.convertFeatureCollection(geometries), mapboxMap);
        });

        // Move camera map to position of customer
        CameraPosition position = new CameraPosition.Builder()
                .target(new LatLng(mOrderResponseDto.data.latitude, mOrderResponseDto.data.longitude))
                .zoom(18)
                .build();
        mapboxMap.moveCamera(CameraUpdateFactory.newCameraPosition(position));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.order_detail_call_shipper_button:
                onClickCallCustomer();
                break;
            case R.id.order_detail_staff_item_accept_button:
                onClickAcceptButton();
                break;
            case R.id.parcel_picked_up_button:
                updateStatus(Constant.SHIPPER_RECEIVED_ORDER);
                break;
            case R.id.parcel_deliver_order_button:
                updateStatus(Constant.SHIPPER_DELIVER_ORDER);
                break;
            case R.id.order_delivered_button:
                updateStatus(Constant.COMPLETE_ORDER);
                break;
            default:
                break;
        }
    }

    @Override
    public void onRefresh() {
        loadData();
    }

    /**
     * Update status order
     * @param shipperReceivedOrder status order
     */
    private void updateStatus(String shipperReceivedOrder) {
        beforeCallApi();
        mDataController.updateStatusOrder(this, shipperReceivedOrder, String.valueOf(mOrderResponseDto.data.id), new ApiServiceOperator.OnResponseListener<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse body) {
                if (TextUtils.equals(APIConstant.STATUS_CODE_SUCCESS, body.statusCd)) {
                    loadData();
                } else {
                    Toast.makeText(OrderDetailShipperActivity.this, "Something went wrong!", Toast.LENGTH_LONG).show();
                    afterCallApi();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(OrderDetailShipperActivity.this, "Something went wrong!", Toast.LENGTH_LONG).show();
                afterCallApi();
            }
        });
    }

    /**
     * Handle click accept button
     */
    private void onClickAcceptButton() {
        beforeCallApi();
        mDataController.acceptOrder(this, String.valueOf(mOrderResponseDto.data.id), new ApiServiceOperator.OnResponseListener<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse body) {
                if (TextUtils.equals(APIConstant.STATUS_CODE_SUCCESS, body.statusCd)) {
                    Toast.makeText(OrderDetailShipperActivity.this, "Accept order successful!", Toast.LENGTH_LONG).show();
                    loadData();
                } else {
                    Toast.makeText(OrderDetailShipperActivity.this, "Accept order fail!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(OrderDetailShipperActivity.this, "Accept order fail!", Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * init toolbar
     */
    private void initToolbar() {
        binding.toolbar.bringToFront();
        binding.toolbar.setTitle(getString(R.string.order_detail));
        binding.toolbar.setToolbarListener(view -> {
            onBackPressed();
        });
    }

    /**
     * Handle fix swipe between map and scrollview
     */
    private void handleSwipeBetweenMapAndScrollView() {
        // Handle fixing conflict swipe between mapview & nested scrollview
        NestedScrollView scrollView = findViewById(R.id.nested_layout);
        binding.mapView.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_MOVE:
                    scrollView.requestDisallowInterceptTouchEvent(true);
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    scrollView.requestDisallowInterceptTouchEvent(false);
                    break;
            }
            return binding.mapView.onTouchEvent(event);
        });
    }

    /**
     * Load data of screen
     */
    private void loadData() {
        beforeCallApi();
        callOrderDetailApi();
    }

    /**
     * Call order detail api
     */
    private void callOrderDetailApi() {
        if (mOrderId != -1) {
            mDataController.getOrderDetailShipper(this, mOrderId, new OrderDetailCallBack());
        } else {
            Toast.makeText(this, getResources().getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Update view after fetching data
     */
    private void updateView(OrderResponseDto orderResponseDto) {

        if (orderResponseDto != null) {
            binding.nestedLayout.setVisibility(View.VISIBLE);
            binding.orderDetailStatusImage.setImageResource(orderResponseDto.data.getIconByStatus());
            binding.orderDetailStatusNotice.setText(orderResponseDto.data.getStatusContent());
            binding.orderDetailItemPhonenumber.setText(orderResponseDto.data.shippingPersonPhoneNumber);
            binding.orderDetailItemCustomerName.setText(orderResponseDto.data.shippingPersonName);
            binding.homeOrderDetailOrderName.setText(orderResponseDto.data.serviceName);
            binding.orderDetailPickupAddress.setText(orderResponseDto.data.pickUpAddress);
            binding.orderDetailDeliveryAddress.setText(orderResponseDto.data.shippingAddress);
            binding.orderDetailSubTotalPrice.setText(orderResponseDto.data.totalServiceFee + "$");
            binding.orderDetailDeliveryPrice.setText(orderResponseDto.data.totalShipFee + "$");

            if (TextUtils.equals(Constant.NEW, orderResponseDto.data.status)) {
                binding.orderDetailStaffItemAcceptButton.setVisibility(View.VISIBLE);
                binding.orderDetailStaffItemAcceptButton.setOnClickListener(new SingleTapListener(this));
                binding.statusLayout.setVisibility(View.GONE);
                binding.orderDetailCallShipperButton.setOnClickListener(null);
            } else {
                binding.orderDetailStaffItemAcceptButton.setVisibility(View.GONE);
                binding.statusLayout.setVisibility(View.VISIBLE);
                binding.orderDetailStaffItemAcceptButton.setOnClickListener(null);
                binding.orderDetailCallShipperButton.setOnClickListener(new SingleTapListener(this));
            }

            binding.orderDetailPaymentMethod.setText(getResources().getString(orderResponseDto.data.isCashPay ? R.string.cash_payment : R.string.momo_wallet));
            binding.orderDetailTotalPrice.setText(orderResponseDto.data.totalBill + "$");
            binding.shippingAddressText.setText(orderResponseDto.data.shippingAddress);

            if (TextUtils.equals(Constant.SHIPPER_ACCEPTED_ORDER, orderResponseDto.data.status)) {
                binding.parcelPickedUpButton.setVisibility(View.VISIBLE);
                binding.parcelPickedUpButton.setOnClickListener(new SingleTapListener(this));
            } else {
                binding.parcelPickedUpButton.setVisibility(View.GONE);
                binding.parcelPickedUpButton.setOnClickListener(null);
            }

            if (TextUtils.equals(Constant.STORE_DONE_ORDER, orderResponseDto.data.status)) {
                binding.parcelDeliverOrderButton.setVisibility(View.VISIBLE);
                binding.parcelDeliverOrderButton.setOnClickListener(new SingleTapListener(this));
            } else {
                binding.parcelDeliverOrderButton.setVisibility(View.GONE);
                binding.parcelDeliverOrderButton.setOnClickListener(null);
            }
            binding.orderDeliveredButton.setOnClickListener(new SingleTapListener(this));
            if (TextUtils.equals(Constant.SHIPPER_DELIVER_ORDER, orderResponseDto.data.status)) {
                binding.orderDeliveredButton.setBackground(getResources().getDrawable(R.drawable.shape_border_button_green));
                binding.orderDeliveredButton.setTextColor(getResources().getColor(R.color.alpha_8cc63e));
                binding.orderDeliveredButton.setEnabled(true);
            } else {
                binding.orderDeliveredButton.setBackground(getResources().getDrawable(R.drawable.shape_border_button_green_disable));
                binding.orderDeliveredButton.setTextColor(getResources().getColor(R.color.green_disable));
                binding.orderDeliveredButton.setEnabled(false);
            }

            // Create order list
            mServicesOrderAdapter.typeService = ServicesOrderAdapter.SERVICES_DETAIL_VIEW_TYPE.ORDER;
            List<ServiceDetailDto> products = mOrderResponseDto.data.serviceDetails;
            mServicesOrderAdapter.submitList(products);
            binding.orderedList.setAdapter(mServicesOrderAdapter);

            getDirectionApi();
        } else {
            binding.nestedLayout.setVisibility(View.GONE);
        }

    }

    /**
     * Call phone
     *
     * @param phoneNumber Phone number
     */
    @SuppressLint("MissingPermission")
    private void callNow(String phoneNumber) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + phoneNumber));
        try {
            this.startActivity(callIntent);
        } catch (Exception ex) {
            Toast.makeText(this, "Your call failed... " + ex.getMessage(),
                    Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    /**
     * Handle click call shipper
     */
    private void onClickCallCustomer() {
        String[] simpleArray = new String[listPermission.size()];
        listPermission.toArray(simpleArray);
        // Request permission
        doRequestPermission(simpleArray, this);
    }

    /**
     * Get direction api
     */
    private void getDirectionApi() {

        mDataController.getDirectionMaps(MapUtils.getCoordinate(LONG_START,
                LAT_START,
                mOrderResponseDto.data.longitude,
                mOrderResponseDto.data.latitude)
                , GEOMETRIES, APIConstant.MAPBOX_ACCESS_TOKEN, new MapDirectionCallback(this));
    }

    private void beforeCallApi() {
        binding.progressBar.maskviewLayout.setVisibility(View.VISIBLE);
    }

    private void afterCallApi() {
        binding.progressBar.maskviewLayout.setVisibility(View.GONE);
        binding.pullToRefresh.setRefreshing(false);
    }


    //----------------------------------------------------------------------------------------------
    //------------ Inner class
    //----------------------------------------------------------------------------------------------

    private class OrderDetailCallBack implements ApiServiceOperator.OnResponseListener<OrderResponseDto> {

        @Override
        public void onSuccess(OrderResponseDto body) {
            if (TextUtils.equals(APIConstant.STATUS_CODE_SUCCESS, body.statusCd)) {
                setOrderResponseDto(body);
                updateView(body);
            } else {
                // Do something went wrong
                Toast.makeText(OrderDetailShipperActivity.this, getResources().getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                afterCallApi();
            }

        }

        @Override
        public void onFailure(Throwable t) {
            // Do something went wrong
            Toast.makeText(OrderDetailShipperActivity.this, getResources().getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
            afterCallApi();
        }
    }

    /**
     * ServiceListCallBack
     */
    private class MapDirectionCallback implements ApiServiceOperator.OnResponseListener<MapDirectionResponse> {
        OnMapReadyCallback onMapReadyCallback;

        public MapDirectionCallback(OnMapReadyCallback onMapReadyCallback) {
            this.onMapReadyCallback = onMapReadyCallback;
        }

        @Override
        public void onSuccess(MapDirectionResponse body) {
            // get call back of mapview
            // Get polyline point
            setGeometries(new Gson().toJson(body.getRoutes().get(0).getGeometry()));

            binding.mapView.getMapAsync(onMapReadyCallback);
            afterCallApi();
        }

        @Override
        public void onFailure(Throwable t) {
            afterCallApi();
        }
    }
}