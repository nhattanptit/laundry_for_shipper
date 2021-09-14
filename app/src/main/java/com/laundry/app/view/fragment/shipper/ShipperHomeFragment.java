package com.laundry.app.view.fragment.shipper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.freshchat.consumer.sdk.Freshchat;
import com.freshchat.consumer.sdk.FreshchatConfig;
import com.freshchat.consumer.sdk.exception.MethodNotAllowedException;
import com.laundry.app.R;
import com.laundry.app.constant.Constant;
import com.laundry.app.control.ApiServiceOperator;
import com.laundry.app.control.DataController;
import com.laundry.app.data.APIConstant;
import com.laundry.app.databinding.ShipperFragmentHomeBinding;
import com.laundry.app.dto.BaseResponse;
import com.laundry.app.dto.UserInfo;
import com.laundry.app.dto.orderlistshipper.OrderListShipperDto;
import com.laundry.app.dto.orderlistshipper.OrderListShipperResponse;
import com.laundry.app.utils.SingleTapListener;
import com.laundry.app.view.activity.OrderDetailShipperActivity;
import com.laundry.app.view.adapter.BannerAdapter;
import com.laundry.app.view.adapter.HomeOrderAdapter;
import com.laundry.app.view.adapter.HomeOrderAreShippingAdapter;
import com.laundry.base.BaseFragment;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class ShipperHomeFragment extends BaseFragment<ShipperFragmentHomeBinding> implements ViewPager.OnPageChangeListener
        , SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    private static final String TAG = ShipperHomeFragment.class.getSimpleName();
    int[] mResourcesBanner = {R.drawable.banner1,
            R.drawable.banner2,
            R.drawable.banner3,
            R.drawable.banner4,
            R.drawable.banner5};
    int currentPage = 0;

    private List<OrderListShipperDto> mListNewOrder;
    private HomeOrderAdapter mNewOrderAdapter;

    private List<OrderListShipperDto> mListHistoryOrder;
    private HomeOrderAdapter mHistoryOrderAdapter;

    private List<OrderListShipperDto> mListOrderAreShipping;
    private HomeOrderAreShippingAdapter mHomeOrderAreShippingAdapter;

    private OrderListShipperDto mOrderListShipperDto;

    OnClickCallPhone mOnClickCallPhone;

    private DataController mDataController = new DataController();

    private String mRestoreId;

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
        try {
            mOnClickCallPhone = (OnClickCallPhone) context;
        } catch (ClassCastException e) {

        }
    }


    @Override
    protected int getLayoutResource() {
        return R.layout.shipper_fragment_home;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onInitView() {
        BannerAdapter bannerAdapter = new BannerAdapter(getContext(), mResourcesBanner);
        binding.viewPager.setAdapter(bannerAdapter);
        binding.wormDotsIndicator.setViewPager(binding.viewPager);
        binding.pullToRefresh.setOnRefreshListener(this);

        autoSwipeBanner();


        initOrderAreShipping();
        initNewOrder();
        initHistoryOrder();

        loadData();
        createFreshChat();
    }

    /**
     * Load data
     */
    private void loadData() {
        callOrderAreShippingApi();
    }

    /**
     * Set visibility 0 order notice layout
     */
    private void setVisibilityNoOrderNoticeLayout() {

    }

    /**
     * List order are shipping
     */
    private void initOrderAreShipping() {
        mListOrderAreShipping = new ArrayList<>();
        mHomeOrderAreShippingAdapter = new HomeOrderAreShippingAdapter(getMyActivity(), this);
        binding.homeStaffOrderDeliveringRcv.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        binding.homeStaffOrderDeliveringRcv.setLayoutManager(linearLayoutManager);
        binding.homeStaffOrderDeliveringRcv.setAdapter(mHomeOrderAreShippingAdapter);
        mHomeOrderAreShippingAdapter.submitList(mListOrderAreShipping);
        binding.homeStaffOrderDeliveringRcv.bringToFront();
    }

    private void beforeCallApi() {
        binding.progressBar.maskviewLayout.setVisibility(View.VISIBLE);
    }

    private void afterCallApi() {
        binding.progressBar.maskviewLayout.setVisibility(View.GONE);
    }

    @Override
    public void onViewClick() {
        binding.chatIcon.setOnClickListener(view -> {
            Freshchat.showConversations(getMyActivity());
        });
    }


    /**
     * Auto swipe banner top
     */
    private void autoSwipeBanner() {
        Timer timer;
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            public void run() {
                if (currentPage == mResourcesBanner.length) {
                    currentPage = 0;
                }
                binding.viewPager.setCurrentItem(currentPage++, true);
            }
        };

        timer = new Timer(); // This will create a new Thread
        timer.schedule(new TimerTask() { // task to be scheduled
            @Override
            public void run() {
                handler.post(runnable);
            }
        }, 500, 3000);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        int pageCount = mResourcesBanner.length;
        if (position == 0) {
            binding.viewPager.setCurrentItem(pageCount - 2, false);
        } else if (position == pageCount - 1) {
            binding.viewPager.setCurrentItem(1, false);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void createFreshChat() {
        FreshchatConfig config = new FreshchatConfig(getString(R.string.id_fresh_chat), getString(R.string.key_fresh_chat));
        config.setDomain(getString(R.string.domain_fresh_chat));
        config.setCameraCaptureEnabled(false);
        config.setGallerySelectionEnabled(true);
        Freshchat.getInstance(getMyActivity()).init(config);

        //Set properties
        Map<String, String> userMeta = new HashMap<>();
        userMeta.put(getString(R.string.shipper), UserInfo.getInstance().getUsername(getMyActivity()));
        try {
            Freshchat.getInstance(getMyActivity()).setUserProperties(userMeta);
        } catch (MethodNotAllowedException e) {
            e.printStackTrace();
        }

        //Save conversation refer 3.5 link above
        try {
            Freshchat.getInstance(getMyActivity()).identifyUser(UserInfo.getInstance().getUsername(getMyActivity())
                    , null);
        } catch (MethodNotAllowedException e) {
            e.printStackTrace();
        }

        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                mRestoreId = Freshchat.getInstance(getMyActivity()).getUser().getRestoreId();
            }
        };

        IntentFilter intentFilter = new IntentFilter(Freshchat.FRESHCHAT_USER_RESTORE_ID_GENERATED);
        LocalBroadcastManager.getInstance(getMyActivity()).registerReceiver(broadcastReceiver, intentFilter);

        try {
            Freshchat.getInstance(getMyActivity()).identifyUser(UserInfo.getInstance().getUsername(getMyActivity())
                    , mRestoreId);
        } catch (MethodNotAllowedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRefresh() {
        loadData();
        binding.pullToRefresh.setRefreshing(false);
    }

    /**
     * Init list order
     */
    private void initNewOrder() {
        mListNewOrder = new ArrayList<>();
        mNewOrderAdapter = new HomeOrderAdapter(true);
        mNewOrderAdapter.setOnClickListener(this);
        binding.homeStaffNewOrderRcv.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        binding.homeStaffNewOrderRcv.setLayoutManager(linearLayoutManager);
        binding.homeStaffNewOrderRcv.setAdapter(mNewOrderAdapter);
        mNewOrderAdapter.submitList(mListNewOrder);
        binding.homeStaffNewOrderRcv.bringToFront();
    }

    /**
     * Init history order list
     */
    private void initHistoryOrder() {
        mListHistoryOrder = new ArrayList<>();
        mHistoryOrderAdapter = new HomeOrderAdapter(false);
        mHistoryOrderAdapter.setOnClickListener(this);
        binding.homeStaffHistoryOrderRcv.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        binding.homeStaffHistoryOrderRcv.setLayoutManager(linearLayoutManager);
        binding.homeStaffHistoryOrderRcv.setAdapter(mHistoryOrderAdapter);
        mHistoryOrderAdapter.submitList(mListHistoryOrder);
        binding.homeStaffHistoryOrderRcv.bringToFront();
    }

    /**
     * call order are shipping api
     */
    private void callOrderAreShippingApi() {
        beforeCallApi();
        mDataController.getOrderListShipper(getMyActivity(), Constant.INCOMPLETE_ORDER, 0, 50,
                new OrderAreShippingCallBack());
        updateList();
    }

    /**
     * Call list new order api
     */
    public void callListNewOrderApi() {
        mDataController.getOrderListNewShipper(getMyActivity(), 0, 50, new NewOrderCallBack());

    }

    /**
     * Call list history order api
     */
    public void callListHistoryOrderApi() {
        mDataController.getOrderListShipper(getMyActivity(), Constant.COMPLETE_ORDER, 0, 50,
                new HistoryOrderCallBack());
    }

    /**
     * Set visibility list
     */
    private void updateList() {
        if (mListNewOrder != null && !mListNewOrder.isEmpty()) {
            binding.homeStaffNewOrderRcv.setVisibility(View.VISIBLE);
            binding.newOrderHeadingLayout.setVisibility(View.VISIBLE);
        } else {
            binding.homeStaffNewOrderRcv.setVisibility(View.GONE);
            binding.newOrderHeadingLayout.setVisibility(View.GONE);
        }
        if (mListHistoryOrder != null && !mListHistoryOrder.isEmpty()) {
            binding.homeStaffHistoryOrderRcv.setVisibility(View.VISIBLE);
            binding.historyOrderHeadingLayout.setVisibility(View.VISIBLE);
        } else {
            binding.homeStaffHistoryOrderRcv.setVisibility(View.GONE);
            binding.historyOrderHeadingLayout.setVisibility(View.GONE);
        }
        if (mListOrderAreShipping != null && !mListOrderAreShipping.isEmpty()) {
            binding.homeStaffOrderDeliveringRcv.setVisibility(View.VISIBLE);
            binding.orderDeliveringHeadingLayout.setVisibility(View.VISIBLE);
        } else {
            binding.homeStaffOrderDeliveringRcv.setVisibility(View.GONE);
            binding.orderDeliveringHeadingLayout.setVisibility(View.GONE);
        }

        if (mOrderListShipperDto != null) {
            binding.orderItem.orderTopDeliveryAddressContent.setText(mOrderListShipperDto.deliverAddress);
            binding.orderItem.orderTopPhonenumberContent.setText(mOrderListShipperDto.shippingPhoneNumber);
            binding.orderItem.statusIcon.setBackgroundResource(mOrderListShipperDto.getIconByStatus());
            binding.orderItem.statusContent.setText(mOrderListShipperDto.getStatusContent());

            binding.orderItem.orderTopCallButton.setOnClickListener(ShipperHomeFragment.this);
            binding.orderItem.orderTopDoneButton.setOnClickListener(ShipperHomeFragment.this);
            binding.orderItem.orderTopCancelButton.setOnClickListener(ShipperHomeFragment.this);
            binding.orderItem.orderTopCallButton.setTag(mOrderListShipperDto);
            binding.orderItem.orderTopDoneButton.setTag(mOrderListShipperDto);
            binding.orderItem.orderTopCancelButton.setTag(mOrderListShipperDto);

            binding.orderItem.orderTopContent.setOnClickListener(new SingleTapListener(ShipperHomeFragment.this));
            binding.orderItem.orderTopContent.setTag(mOrderListShipperDto);

            if (TextUtils.equals(Constant.SHIPPER_ACCEPTED_ORDER, mOrderListShipperDto.status)) {
                binding.orderItem.orderTopCancelButton.setVisibility(View.VISIBLE);
                binding.orderItem.orderTopCancelButton.setEnabled(true);
                binding.orderItem.orderTopDoneButton.setVisibility(View.VISIBLE);
                binding.orderItem.orderTopDoneButton.setEnabled(true);

                binding.orderItem.orderTopDoneButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11f);
                binding.orderItem.orderTopDoneButton.setBackground(ContextCompat.getDrawable(getMyActivity(),
                        R.drawable.shaper_button_green_big));
                binding.orderItem.orderTopDoneButton.setText(R.string.order_receivered);
            } else if (TextUtils.equals(Constant.SHIPPER_RECEIVED_ORDER, mOrderListShipperDto.status)) {
                binding.orderItem.orderTopCancelButton.setVisibility(View.GONE);

                binding.orderItem.orderTopDoneButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f);
                binding.orderItem.orderTopDoneButton.setText(R.string.order_delivered);
                binding.orderItem.orderTopDoneButton.setEnabled(false);
                binding.orderItem.orderTopDoneButton.setBackground(ContextCompat.getDrawable(getMyActivity(),
                        R.drawable.shaper_button_green_disable));
            } else if (TextUtils.equals(Constant.STORE_RECEIVED_ORDER, mOrderListShipperDto.status)
                    || TextUtils.equals(Constant.STORE_DONE_ORDER, mOrderListShipperDto.status)) {
                binding.orderItem.orderTopCancelButton.setVisibility(View.GONE);
                binding.orderItem.orderTopDoneButton.setEnabled(true);
                binding.orderItem.orderTopDoneButton.setBackground(ContextCompat.getDrawable(getMyActivity(),
                        R.drawable.shaper_button_green_big));

                binding.orderItem.orderTopDoneButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f);
                binding.orderItem.orderTopDoneButton.setText(R.string.order_delivered);
            } else if (TextUtils.equals(Constant.SHIPPER_DELIVER_ORDER, mOrderListShipperDto.status)) {
                binding.orderItem.orderTopCancelButton.setVisibility(View.GONE);

                binding.orderItem.orderTopDoneButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f);
                binding.orderItem.orderTopDoneButton.setText(R.string.order_complete);
            } else {
                binding.orderItem.orderTopCancelButton.setVisibility(View.GONE);
            }
            binding.noHaveOrderNoticeLayout.setVisibility(View.GONE);
            binding.orderItem.orderTop.setVisibility(View.VISIBLE);
        } else {
            binding.noHaveOrderNoticeLayout.setVisibility(View.VISIBLE);
            binding.orderItem.orderTop.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        OrderListShipperDto item = (OrderListShipperDto) v.getTag();
        switch (v.getId()) {
            case R.id.home_staff_derivering_order_call_button:
            case R.id.order_top_call_button:
                onClickCallButton(item);
                break;
            case R.id.order_home_item_layout:
            case R.id.order_are_shipping_item:
            case R.id.order_top_content:
                onClickOrderItem(item);
                break;
            case R.id.home_staff_item_accept_button:
            case R.id.home_staff_derivering_order_done_button:
            case R.id.order_top_done_button:
                onClickAcceptButton(item);
                break;
            case R.id.order_top_cancel_button:
            case R.id.home_staff_derivering_order_cancel_button:
                cancelOrder(item);
                break;
            default:
                break;
        }
    }

    /**
     * Handle click accept button
     */
    private void onClickAcceptButton(OrderListShipperDto item) {
        if (item == null) {
            return;
        }
        beforeCallApi();
        mDataController.updateStatusOrder(getMyActivity(), item.status, String.valueOf(item.id), new OrderStatusCallback());
    }

    /**
     * Cancel order
     *
     * @param item OrderListShipperDto
     */
    private void cancelOrder(OrderListShipperDto item) {
        if (item == null) {
            return;
        }
        beforeCallApi();
        mDataController.shipperCancelOrder(getMyActivity(), String.valueOf(item.id), new OrderStatusCallback());
    }

    /**
     * OrderStatusCallback
     */
    private class OrderStatusCallback implements ApiServiceOperator.OnResponseListener<BaseResponse> {
        @Override
        public void onSuccess(BaseResponse body) {
            if (TextUtils.equals(APIConstant.STATUS_CODE_SUCCESS, body.statusCd)) {
                Toast.makeText(getMyActivity(), body.message, Toast.LENGTH_LONG).show();
                loadData();
            } else {
                Toast.makeText(getMyActivity(), body.message, Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onFailure(Throwable t) {
            Toast.makeText(getMyActivity(), getMyActivity().getText(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Handle click order item
     */
    private void onClickOrderItem(OrderListShipperDto item) {
        startActivity(OrderDetailShipperActivity.getNewActivityStartIntent(getMyActivity(), item.id));
    }

    /**
     * Handle click call button in list order are shipping
     */
    private void onClickCallButton(OrderListShipperDto item) {
        mOnClickCallPhone.onCall(item.shippingPhoneNumber);
    }

    /**
     * Handle click done button in list order are shipping
     */
    private void onClickDoneButton() {

    }

    public interface OnClickCallPhone {
        void onCall(String phoneNumber);
    }

    private class OrderAreShippingCallBack implements ApiServiceOperator.OnResponseListener<OrderListShipperResponse> {

        @Override
        public void onSuccess(OrderListShipperResponse body) {
            if (TextUtils.equals(APIConstant.STATUS_CODE_SUCCESS, body.statusCd)) {
                if (body.orderListShipperDtos != null && !body.orderListShipperDtos.isEmpty()) {
                    Collections.shuffle(body.orderListShipperDtos);
                    mListOrderAreShipping.clear();
                    mListOrderAreShipping.addAll(body.orderListShipperDtos);
                    mHomeOrderAreShippingAdapter.submitList(mListOrderAreShipping);
                    mOrderListShipperDto = body.orderListShipperDtos.get(0);
                }
            }
            callListNewOrderApi();
        }

        @Override
        public void onFailure(Throwable t) {
            callListNewOrderApi();
        }
    }

    private class NewOrderCallBack implements ApiServiceOperator.OnResponseListener<OrderListShipperResponse> {

        @Override
        public void onSuccess(OrderListShipperResponse body) {

            if (TextUtils.equals(APIConstant.STATUS_CODE_SUCCESS, body.statusCd)) {
                mListNewOrder.clear();
                mListNewOrder.addAll(body.orderListShipperDtos);
                mNewOrderAdapter.submitList(mListNewOrder);
            }
            callListHistoryOrderApi();
        }

        @Override
        public void onFailure(Throwable t) {
            callListHistoryOrderApi();
        }
    }

    private class HistoryOrderCallBack implements ApiServiceOperator.OnResponseListener<OrderListShipperResponse> {

        @Override
        public void onSuccess(OrderListShipperResponse body) {
            if (TextUtils.equals(APIConstant.STATUS_CODE_SUCCESS, body.statusCd)) {
                mListHistoryOrder.clear();
                mListHistoryOrder.addAll(body.orderListShipperDtos);
                mHistoryOrderAdapter.submitList(mListHistoryOrder);
            }
            updateList();
            afterCallApi();
        }

        @Override
        public void onFailure(Throwable t) {
            updateList();
            afterCallApi();
        }
    }

}