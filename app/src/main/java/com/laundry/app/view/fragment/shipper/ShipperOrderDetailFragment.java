package com.laundry.app.view.fragment.shipper;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.laundry.app.R;
import com.laundry.app.databinding.ShipperOrderDetailFragmentBinding;
import com.laundry.base.BaseFragment;

public class ShipperOrderDetailFragment extends BaseFragment<ShipperOrderDetailFragmentBinding> implements SwipeRefreshLayout.OnRefreshListener {
    @Override
    protected int getLayoutResource() {
        return R.layout.shipper_order_detail_fragment;
    }

    @Override
    public void onInitView() {
        binding.pullToRefresh.setOnRefreshListener(this);
    }

    @Override
    public void onViewClick() {

    }

    @Override
    public void onRefresh() {
        binding.pullToRefresh.setRefreshing(false);
    }
}
