package com.laundry.app.view.activity;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;
import com.laundry.app.R;
import com.laundry.app.constant.Constant;
import com.laundry.app.control.ApiServiceOperator;
import com.laundry.app.control.DataController;
import com.laundry.app.data.APIConstant;
import com.laundry.app.databinding.ActivityOrderSuccessBinding;
import com.laundry.app.dto.AddressInfo;
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
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static com.laundry.app.constant.Constant.ICON_ID;
import static com.laundry.app.constant.Constant.KEY_BUNDLE_IS_CASH_PAYMENT_METHOD;
import static com.laundry.app.constant.Constant.LAT_START;
import static com.laundry.app.constant.Constant.LAYER_ID;
import static com.laundry.app.constant.Constant.LONG_START;
import static com.laundry.app.constant.Constant.SOURCE_ID;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;

public class OrderSuccessActivity extends BaseActivity<ActivityOrderSuccessBinding> implements OnMapReadyCallback {
    ActivityOrderSuccessBinding binding;

    String geometries = "";

    private MapDirectionResponse mMapDirectionResponse;

    private OrderResponseDto mOrderResponseDto;

    private final ServicesOrderAdapter mServicesOrderAdapter = new ServicesOrderAdapter();

    private boolean mIsCashPayment;

    private final DataController mDataController = new DataController();

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_order_success;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent() != null) {
            mMapDirectionResponse = (MapDirectionResponse) getIntent().getSerializableExtra(Constant.KEY_BUNDLE_MAP_DIRECTION_RESPONSE);
            mOrderResponseDto = (OrderResponseDto) getIntent().getSerializableExtra(Constant.KEY_BUNDLE_ORDER_RESPONSE);
            mIsCashPayment = getIntent().getBooleanExtra(KEY_BUNDLE_IS_CASH_PAYMENT_METHOD, true);
        }

        // Mapbox access token is configured here. This needs to be called either in your application
        // object or in the same activity which contains the mapview.
        Mapbox.getInstance(this, APIConstant.MAPBOX_ACCESS_TOKEN);

        // This contains the MapView in XML and needs to be called after the access token is configured.
        binding = DataBindingUtil.setContentView(this, getLayoutResource());
        binding.setLifecycleOwner(this);
        createDisplay();
        binding.mapView.onCreate(savedInstanceState);
        binding.toolbar.bringToFront();
        binding.toolbar.setTitle(getString(R.string.order_success));
        binding.toolbar.setToolbarListener(view -> {
            onBackPressed();
        });

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


        // Handle click button
        binding.cancelOrderButton.setOnClickListener(new SingleTapListener(v -> {
            callCancelOrderApi();
        }));

        binding.doneButton.setOnClickListener(new SingleTapListener(v -> {
            finish();
        }));

        // get call back of mapview
        binding.mapView.getMapAsync(this);

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
    }

    /**
     * Create display order success layout
     */
    private void createDisplay() {
        // Get polyline point
        geometries = new Gson().toJson(mMapDirectionResponse.getRoutes().get(0).getGeometry());

        // Bind info
        binding.shippingAddressText.setText(mOrderResponseDto.data.shippingAddress);
        binding.orderSuccessPhoneNumber.setText(mOrderResponseDto.data.shippingPersonPhoneNumber);
        binding.orderSuccessPaymentMethod.setText(mIsCashPayment ? getResources().getString(R.string.cash_payment) : getResources().getString(R.string.momo_wallet));
        binding.orderSuccessTotalPrice.setText("$ " + mOrderResponseDto.data.totalBill + "");
        if (mOrderResponseDto.data.isPaid) {
            binding.cancelOrderButton.setEnabled(false);
            binding.cancelOrderButton.setBackgroundDrawable(getDrawable(R.drawable.shaper_button_green_big_disable));
        }

        // Create order list
        mServicesOrderAdapter.typeService = ServicesOrderAdapter.SERVICES_DETAIL_VIEW_TYPE.ORDER;
        List<ServiceDetailDto> products = mOrderResponseDto.data.serviceDetails;
        mServicesOrderAdapter.submitList(products);
        binding.orderedList.setAdapter(mServicesOrderAdapter);
    }

    /**
     * Cancel order api
     */
    private void callCancelOrderApi() {
        beforeCallApi();
        mDataController.cancelOrder(this, String.valueOf(mOrderResponseDto.data.id), new CancelOrderCallBack());
    }

    private void beforeCallApi() {
        binding.progressBar.maskviewLayout.setVisibility(View.VISIBLE);
    }

    private void afterCallApi() {
        binding.progressBar.maskviewLayout.setVisibility(View.GONE);
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
    public void onInitView() {


    }

    @Override
    public void onViewClick() {

    }

    /**
     * Call back for cancel order api
     */
    private class CancelOrderCallBack implements ApiServiceOperator.OnResponseListener<BaseResponse> {
        @Override
        public void onSuccess(BaseResponse body) {
            if (TextUtils.equals(APIConstant.STATUS_CODE_SUCCESS, body.statusCd)) {
                Toast.makeText(OrderSuccessActivity.this, getResources().getString(R.string.cancel_order_successful), Toast.LENGTH_LONG).show();
                finish();
            } else {
                Toast.makeText(OrderSuccessActivity.this, getResources().getString(R.string.cancel_order_fail), Toast.LENGTH_LONG).show();
            }
            afterCallApi();
        }

        @Override
        public void onFailure(Throwable t) {
            Toast.makeText(OrderSuccessActivity.this, getResources().getString(R.string.cancel_order_fail), Toast.LENGTH_LONG).show();
            afterCallApi();
        }
    }

}