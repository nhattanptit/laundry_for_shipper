package com.laundry.app.view.fragment.customer;

import android.util.Log;

import com.laundry.app.R;
import com.laundry.app.control.ApiServiceOperator;
import com.laundry.app.control.DataController;
import com.laundry.app.databinding.FragmentHomeBinding;
import com.laundry.app.dto.serviceall.ServiceAllBody;
import com.laundry.app.model.Product;
import com.laundry.app.view.adapter.BannerAdapter;
import com.laundry.app.view.adapter.ProductionAdapter;
import com.laundry.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends BaseFragment<FragmentHomeBinding> {

    private static final String TAG = "HomeFragment";
    private List<Object> mProducts = new ArrayList<>();
    private int[] image = {R.drawable.wash_and_iron_icon,
            R.drawable.ironing_icon,
            R.drawable.dry_cleaning_icon,
            R.drawable.wash_blanket_icon};
    private String[] product = {"Wash & Iron", "Ironing", "Dry cleaning", "Wash blankets"};
    private DataController controller = new DataController();

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
        for (int i = 0; i < image.length; i++) {
            mProducts.add(new Product(image[i], product[i]));
        }

        controller.getServicesAll(new ApiServiceOperator.OnResponseListener<ServiceAllBody>() {
            @Override
            public void onSuccess(ServiceAllBody body) {
                Log.d(TAG, "onSuccess: " + body);
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d(TAG, "onFailure: ");

            }
        });
    }

    @Override
    public void onInitView() {
        // Banner list
        BannerAdapter bannerAdapter = new BannerAdapter(getContext(), image);
        binding.viewPager.setAdapter(bannerAdapter);
        binding.wormDotsIndicator.setViewPager(binding.viewPager);

        // Product list
        ProductionAdapter productionAdapter = new ProductionAdapter();
        binding.productionRecycle.setAdapter(productionAdapter);
        productionAdapter.setDataList(mProducts);
    }

    @Override
    public void onViewClick() {

    }

}