package com.laundry.app.view.fragment.shipper;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

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
import com.laundry.app.view.activity.OrderDetailShipperActivity;
import com.laundry.app.view.adapter.BannerAdapter;
import com.laundry.app.view.adapter.HomeOrderAdapter;
import com.laundry.app.view.adapter.HomeOrderAreShippingAdapter;
import com.laundry.base.BaseFragment;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ShipperHomeFragment extends BaseFragment<ShipperFragmentHomeBinding> implements ViewPager.OnPageChangeListener
        , SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

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

    OnClickCallPhone mOnClickCallPhone;

    private DataController mDataController = new DataController();

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
        mHomeOrderAreShippingAdapter = new HomeOrderAreShippingAdapter(this);
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
        binding.homeStaffButtonLogin.setOnClickListener(v -> {
            navigateTo(R.id.action_home_staff_to_navigation_login_account_dialog);
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
        mDataController.getOrderListShipper(getMyActivity(), Constant.SHIPPER_ACCEPTED_ORDER, 0, 50, new OrderAreShippingCallBack());
        mHomeOrderAreShippingAdapter.submitList(mListOrderAreShipping);
        updateList();
    }

    /**
     * Call list new order api
     */
    public void callListNewOrderApi() {
        mDataController.getOrderListNewShipper(getMyActivity(),0, 50, new NewOrderCallBack());

    }

    /**
     * Call list history order api
     */
    public void callListHistoryOrderApi() {
        mDataController.getOrderListShipper(getMyActivity(),Constant.COMPLETE_ORDER,0, 50, new HistoryOrderCallBack());
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
        if (UserInfo.getInstance().isLogin(getActivity())) {
            binding.homeStaffButtonLogin.setVisibility(View.GONE);
            if (mListOrderAreShipping != null && !mListOrderAreShipping.isEmpty()) {
                binding.homeStaffOrderDeliveringRcv.setVisibility(View.VISIBLE);
                binding.orderDeliveringHeadingLayout.setVisibility(View.VISIBLE);
                binding.noHaveOrderNoticeLayout.setVisibility(View.GONE);
            } else {
                binding.noHaveOrderNoticeLayout.setVisibility(View.VISIBLE);
                binding.homeStaffOrderDeliveringRcv.setVisibility(View.GONE);
                binding.orderDeliveringHeadingLayout.setVisibility(View.GONE);
            }
        } else {
            binding.homeStaffButtonLogin.setVisibility(View.VISIBLE);
            binding.noHaveOrderNoticeLayout.setVisibility(View.GONE);
            binding.orderDeliveringHeadingLayout.setVisibility(View.GONE);
        }

        binding.noneLoginView.setVisibility(binding.homeStaffButtonLogin.getVisibility() == View.GONE
                    && binding.homeStaffOrderDeliveringRcv.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
        int paddingTop = 0;
        if (binding.noneLoginView.getVisibility() == View.GONE) {
            paddingTop = getResources().getDimensionPixelOffset(R.dimen.dp_32);
        } else {
            paddingTop = getResources().getDimensionPixelOffset(R.dimen.dp_110);
        }
        binding.containerStaffHome.setPadding(binding.containerStaffHome.getPaddingLeft(),paddingTop, binding.containerStaffHome.getPaddingRight(), binding.containerStaffHome.getPaddingBottom() );
    }

    @Override
    public void onClick(View v) {
        OrderListShipperDto item = (OrderListShipperDto) v.getTag();
        switch (v.getId()) {
            case R.id.home_staff_derivering_order_call_button:
                onClickCallButton(item);
                break;
            case R.id.home_staff_derivering_order_done_button:
                onClickDoneButton();
                break;
            case R.id.order_home_item_layout:
            case R.id.order_are_shipping_item:
                onClickOrderItem(item);
                break;
            case R.id.home_staff_item_accept_button:
                onClickAcceptButton(item);
                break;
            default:
                break;
        }
    }

    /**
     * Handle click accept button
     */
    private void onClickAcceptButton(OrderListShipperDto item) {
        beforeCallApi();
        mDataController.acceptOrder(getMyActivity(), String.valueOf(item.id), new ApiServiceOperator.OnResponseListener<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse body) {
                if (TextUtils.equals(APIConstant.STATUS_CODE_SUCCESS, body.statusCd)) {
                    Toast.makeText(getMyActivity(), "Accept order successful!", Toast.LENGTH_LONG).show();
                    loadData();
                } else {
                    Toast.makeText(getMyActivity(), "Accept order fail!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(getMyActivity(), "Accept order fail!", Toast.LENGTH_LONG).show();
            }
        });
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
                mListOrderAreShipping.clear();
                mListOrderAreShipping.addAll(body.orderListShipperDtos);
                mHomeOrderAreShippingAdapter.submitList(mListOrderAreShipping);
            } else {

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
            } else {

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
            } else {

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