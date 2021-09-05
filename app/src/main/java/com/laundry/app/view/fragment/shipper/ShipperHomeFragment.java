package com.laundry.app.view.fragment.shipper;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.maps.model.LatLng;
import com.laundry.app.R;
import com.laundry.app.databinding.ShipperFragmentHomeBinding;
import com.laundry.app.dto.UserInfo;
import com.laundry.app.dto.order.OrderItem;
import com.laundry.app.view.adapter.BannerAdapter;
import com.laundry.app.view.adapter.HomeOrderAdapter;
import com.laundry.app.view.adapter.HomeOrderAreShippingAdapter;
import com.laundry.base.BaseFragment;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
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

    private List<Object> mListNewOrder;
    private HomeOrderAdapter mNewOrderAdapter;

    private List<Object> mListHistoryOrder;
    private HomeOrderAdapter mHistoryOrderAdapter;

    private List<Object> mListOrderAreShipping;
    private HomeOrderAreShippingAdapter mHomeOrderAreShippingAdapter;

    OnClickCallPhone mOnClickCallPhone;

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
        if (UserInfo.getInstance().isLogin(getActivity())) {
            callOrderAreShippingApi();
            callListHistoryOrderApi();
        }
        callListNewOrderApi();


    }

    /**
     * Set visibility 0 order notice layout
     */
    private void setVisibilityNoOrderNoticeLayout() {

    }

    /**
     * call order are shipping api
     */
    private void callOrderAreShippingApi() {
        mListOrderAreShipping.add(new OrderItem("Số 91-93 Đường số 5, Phường An Phú, Tp. Thủ Đức, Thành phố Hồ Chí Minh", "0984622312"));
        mListOrderAreShipping.add(new OrderItem("Số 91-93 Đường số 5, Phường An Phú, Tp. Thủ Đức, Thành phố Hồ Chí Minh", "0984622312"));
        mListOrderAreShipping.add(new OrderItem("Số 91-93 Đường số 5, Phường An Phú, Tp. Thủ Đức, Thành phố Hồ Chí Minh", "0984622312"));
        mListOrderAreShipping.add(new OrderItem("Số 91-93 Đường số 5, Phường An Phú, Tp. Thủ Đức, Thành phố Hồ Chí Minh", "0984622312"));
        mListOrderAreShipping.add(new OrderItem("Số 91-93 Đường số 5, Phường An Phú, Tp. Thủ Đức, Thành phố Hồ Chí Minh", "0984622312"));
        mListOrderAreShipping.add(new OrderItem("Số 91-93 Đường số 5, Phường An Phú, Tp. Thủ Đức, Thành phố Hồ Chí Minh", "0984622312"));
        mListOrderAreShipping.add(new OrderItem("Số 91-93 Đường số 5, Phường An Phú, Tp. Thủ Đức, Thành phố Hồ Chí Minh", "0984622312"));
        mListOrderAreShipping.add(new OrderItem("Số 91-93 Đường số 5, Phường An Phú, Tp. Thủ Đức, Thành phố Hồ Chí Minh", "0984622312"));
        mHomeOrderAreShippingAdapter.submitList(mListOrderAreShipping);
        updateList();
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
        callListNewOrderApi();
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
     * Call list new order api
     */
    public void callListNewOrderApi() {
        mListNewOrder.add(new OrderItem("1"
                , "https://image.thanhnien.vn/2048/uploaded/thanhlongn/2021_07_17/ngoctrinh_gejd.jpg"
                , "Ngọc Trinh"
                , "#AH1234"
                , "Wash & Iron"
                , "12 Jan 2021, 10:30 AM"
                , "B 250 Basker Street ABC XYZ"
                , "13 Jan 2021, 10:30 AM"
                , "B 250 Basker Street ABC XYZ"));
        mListNewOrder.add(new OrderItem("1"
                , "https://image.thanhnien.vn/2048/uploaded/thanhlongn/2021_07_17/ngoctrinh_gejd.jpg"
                , "Ngọc Trinh"
                , "#AH1234"
                , "Wash & Iron"
                , "12 Jan 2021, 10:30 AM"
                , "B 250 Basker Street ABC XYZ"
                , "13 Jan 2021, 10:30 AM"
                , "B 250 Basker Street ABC XYZ"));
        mListNewOrder.add(new OrderItem("1"
                , "https://image.thanhnien.vn/2048/uploaded/thanhlongn/2021_07_17/ngoctrinh_gejd.jpg"
                , "Ngọc Trinh"
                , "#AH1234"
                , "Wash & Iron"
                , "12 Jan 2021, 10:30 AM"
                , "B 250 Basker Street ABC XYZ"
                , "13 Jan 2021, 10:30 AM"
                , "B 250 Basker Street ABC XYZ"));
        mListNewOrder.add(new OrderItem("1"
                , "https://image.thanhnien.vn/2048/uploaded/thanhlongn/2021_07_17/ngoctrinh_gejd.jpg"
                , "Ngọc Trinh"
                , "#AH1234"
                , "Wash & Iron"
                , "12 Jan 2021, 10:30 AM"
                , "B 250 Basker Street ABC XYZ"
                , "13 Jan 2021, 10:30 AM"
                , "B 250 Basker Street ABC XYZ"));
        mListNewOrder.add(new OrderItem("1"
                , "https://image.thanhnien.vn/2048/uploaded/thanhlongn/2021_07_17/ngoctrinh_gejd.jpg"
                , "Ngọc Trinh"
                , "#AH1234"
                , "Wash & Iron"
                , "12 Jan 2021, 10:30 AM"
                , "B 250 Basker Street ABC XYZ"
                , "13 Jan 2021, 10:30 AM"
                , "B 250 Basker Street ABC XYZ"));
        mListNewOrder.add(new OrderItem("1"
                , "https://image.thanhnien.vn/2048/uploaded/thanhlongn/2021_07_17/ngoctrinh_gejd.jpg"
                , "Ngọc Trinh"
                , "#AH1234"
                , "Wash & Iron"
                , "12 Jan 2021, 10:30 AM"
                , "B 250 Basker Street ABC XYZ"
                , "13 Jan 2021, 10:30 AM"
                , "B 250 Basker Street ABC XYZ"));
        mListNewOrder.add(new OrderItem("1"
                , "https://image.thanhnien.vn/2048/uploaded/thanhlongn/2021_07_17/ngoctrinh_gejd.jpg"
                , "Ngọc Trinh"
                , "#AH1234"
                , "Wash & Iron"
                , "12 Jan 2021, 10:30 AM"
                , "B 250 Basker Street ABC XYZ"
                , "13 Jan 2021, 10:30 AM"
                , "B 250 Basker Street ABC XYZ"));
        mListNewOrder.add(new OrderItem("1"
                , "https://image.thanhnien.vn/2048/uploaded/thanhlongn/2021_07_17/ngoctrinh_gejd.jpg"
                , "Ngọc Trinh"
                , "#AH1234"
                , "Wash & Iron"
                , "12 Jan 2021, 10:30 AM"
                , "B 250 Basker Street ABC XYZ"
                , "13 Jan 2021, 10:30 AM"
                , "B 250 Basker Street ABC XYZ"));
        mListNewOrder.add(new OrderItem("1"
                , "https://image.thanhnien.vn/2048/uploaded/thanhlongn/2021_07_17/ngoctrinh_gejd.jpg"
                , "Ngọc Trinh"
                , "#AH1234"
                , "Wash & Iron"
                , "12 Jan 2021, 10:30 AM"
                , "B 250 Basker Street ABC XYZ"
                , "13 Jan 2021, 10:30 AM"
                , "B 250 Basker Street ABC XYZ"));
        mNewOrderAdapter.submitList(mListNewOrder);
        updateList();
    }

    /**
     * Call list history order api
     */
    public void callListHistoryOrderApi() {
        mListHistoryOrder.add(new OrderItem("1"
                , "https://image.thanhnien.vn/2048/uploaded/thanhlongn/2021_07_17/ngoctrinh_gejd.jpg"
                , "Ngọc Trinh"
                , "#AH1234"
                , "Wash & Iron"
                , "12 Jan 2021, 10:30 AM"
                , "B 250 Basker Street ABC XYZ"
                , "13 Jan 2021, 10:30 AM"
                , "B 250 Basker Street ABC XYZ"));
        mListHistoryOrder.add(new OrderItem("1"
                , "https://image.thanhnien.vn/2048/uploaded/thanhlongn/2021_07_17/ngoctrinh_gejd.jpg"
                , "Ngọc Trinh"
                , "#AH1234"
                , "Wash & Iron"
                , "12 Jan 2021, 10:30 AM"
                , "B 250 Basker Street ABC XYZ"
                , "13 Jan 2021, 10:30 AM"
                , "B 250 Basker Street ABC XYZ"));
        mListHistoryOrder.add(new OrderItem("1"
                , "https://image.thanhnien.vn/2048/uploaded/thanhlongn/2021_07_17/ngoctrinh_gejd.jpg"
                , "Ngọc Trinh"
                , "#AH1234"
                , "Wash & Iron"
                , "12 Jan 2021, 10:30 AM"
                , "B 250 Basker Street ABC XYZ"
                , "13 Jan 2021, 10:30 AM"
                , "B 250 Basker Street ABC XYZ"));
        mListHistoryOrder.add(new OrderItem("1"
                , "https://image.thanhnien.vn/2048/uploaded/thanhlongn/2021_07_17/ngoctrinh_gejd.jpg"
                , "Ngọc Trinh"
                , "#AH1234"
                , "Wash & Iron"
                , "12 Jan 2021, 10:30 AM"
                , "B 250 Basker Street ABC XYZ"
                , "13 Jan 2021, 10:30 AM"
                , "B 250 Basker Street ABC XYZ"));
        mListHistoryOrder.add(new OrderItem("1"
                , "https://image.thanhnien.vn/2048/uploaded/thanhlongn/2021_07_17/ngoctrinh_gejd.jpg"
                , "Ngọc Trinh"
                , "#AH1234"
                , "Wash & Iron"
                , "12 Jan 2021, 10:30 AM"
                , "B 250 Basker Street ABC XYZ"
                , "13 Jan 2021, 10:30 AM"
                , "B 250 Basker Street ABC XYZ"));
        mListHistoryOrder.add(new OrderItem("1"
                , "https://image.thanhnien.vn/2048/uploaded/thanhlongn/2021_07_17/ngoctrinh_gejd.jpg"
                , "Ngọc Trinh"
                , "#AH1234"
                , "Wash & Iron"
                , "12 Jan 2021, 10:30 AM"
                , "B 250 Basker Street ABC XYZ"
                , "13 Jan 2021, 10:30 AM"
                , "B 250 Basker Street ABC XYZ"));
        mListHistoryOrder.add(new OrderItem("1"
                , "https://image.thanhnien.vn/2048/uploaded/thanhlongn/2021_07_17/ngoctrinh_gejd.jpg"
                , "Ngọc Trinh"
                , "#AH1234"
                , "Wash & Iron"
                , "12 Jan 2021, 10:30 AM"
                , "B 250 Basker Street ABC XYZ"
                , "13 Jan 2021, 10:30 AM"
                , "B 250 Basker Street ABC XYZ"));
        mListHistoryOrder.add(new OrderItem("1"
                , "https://image.thanhnien.vn/2048/uploaded/thanhlongn/2021_07_17/ngoctrinh_gejd.jpg"
                , "Ngọc Trinh"
                , "#AH1234"
                , "Wash & Iron"
                , "12 Jan 2021, 10:30 AM"
                , "B 250 Basker Street ABC XYZ"
                , "13 Jan 2021, 10:30 AM"
                , "B 250 Basker Street ABC XYZ"));
        mListHistoryOrder.add(new OrderItem("1"
                , "https://image.thanhnien.vn/2048/uploaded/thanhlongn/2021_07_17/ngoctrinh_gejd.jpg"
                , "Ngọc Trinh"
                , "#AH1234"
                , "Wash & Iron"
                , "12 Jan 2021, 10:30 AM"
                , "B 250 Basker Street ABC XYZ"
                , "13 Jan 2021, 10:30 AM"
                , "B 250 Basker Street ABC XYZ"));
        mHistoryOrderAdapter.submitList(mListHistoryOrder);
        updateList();
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
        OrderItem item = (OrderItem) v.getTag();
        switch (v.getId()) {
            case R.id.home_staff_derivering_order_call_button:
                onClickCallButton(item);
                break;
            case R.id.home_staff_derivering_order_done_button:
                onClickDoneButton();
                break;
            case R.id.order_home_item_layout:
                onClickOrderItem();
                break;
            default:
                break;
        }
    }

    /**
     * Handle click order item
     */
    private void onClickOrderItem() {
        navigateTo(R.id.action_home_staff_to_navigation_order_detail);
    }

    /**
     * Handle click call button in list order are shipping
     */
    private void onClickCallButton(OrderItem item) {
        mOnClickCallPhone.onCall(item.getPhoneNumber());
    }

    /**
     * Handle click done button in list order are shipping
     */
    private void onClickDoneButton() {

    }

    public interface OnClickCallPhone {
        void onCall(String phoneNumber);
    }

    public double calculationByDistance(double latStart, double longStart, double latEnd, double longEnd) {
        int Radius = 6371;// radius of earth in Km
        double lat1 = latStart;
        double lat2 = latEnd;
        double lon1 = longStart;
        double lon2 = longEnd;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.valueOf(newFormat.format(km));
        double meter = valueResult % 1000;
        int meterInDec = Integer.valueOf(newFormat.format(meter));
        Log.i("Radius Value", "" + valueResult + "   KM  " + kmInDec
                + " Meter   " + meterInDec);

        return Radius * c;
    }

}