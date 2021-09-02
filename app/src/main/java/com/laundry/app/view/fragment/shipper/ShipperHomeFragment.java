package com.laundry.app.view.fragment.shipper;

import android.os.Handler;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.laundry.app.R;
import com.laundry.app.databinding.ShipperFragmentHomeBinding;
import com.laundry.app.dto.order.OrderItem;
import com.laundry.app.view.adapter.BannerAdapter;
import com.laundry.app.view.adapter.HomeOrderAdapter;
import com.laundry.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ShipperHomeFragment extends BaseFragment<ShipperFragmentHomeBinding> implements ViewPager.OnPageChangeListener, SwipeRefreshLayout.OnRefreshListener {

    int[] mResources = {R.drawable.wash_and_iron_icon,
            R.drawable.ironing_icon,
            R.drawable.dry_cleaning_icon,
            R.drawable.wash_blanket_icon};
    int currentPage = 0;

    private List<Object> mListNewOrder;
    private HomeOrderAdapter mNewOrderAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;

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
        BannerAdapter bannerAdapter = new BannerAdapter(getContext(), mResources);
        binding.viewPager.setAdapter(bannerAdapter);
        binding.wormDotsIndicator.setViewPager(binding.viewPager);
        binding.pullToRefresh.setOnRefreshListener(this);


        autoSwipeBanner();

        initNewOrder();

        callListNewOrderApi();

    }

    @Override
    public void onViewClick() {

    }



    /**
     * Auto swipe banner top
     */
    private void autoSwipeBanner() {
        Timer timer;
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            public void run() {
                if (currentPage == mResources.length) {
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
        int pageCount = mResources.length;
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
        binding.homeStaffNewOrderRcv.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        binding.homeStaffNewOrderRcv.setLayoutManager(linearLayoutManager);
        binding.homeStaffNewOrderRcv.setAdapter(mNewOrderAdapter);
        mNewOrderAdapter.setDataList(mListNewOrder);
        binding.homeStaffNewOrderRcv.bringToFront();
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
        mNewOrderAdapter.setDataList(mListNewOrder);
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
    }

}