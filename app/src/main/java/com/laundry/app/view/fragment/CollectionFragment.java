package com.laundry.app.view.fragment;

import com.laundry.app.R;
import com.laundry.app.databinding.CollectionFragmentBinding;
import com.laundry.base.BaseFragment;

public class CollectionFragment extends BaseFragment<CollectionFragmentBinding>{

    @Override
    protected int getLayoutResource() {
        return R.layout.collection_fragment;
    }

    @Override
    public void onPreInitView() {

    }

    @Override
    public void onInitView() {

    }

    @Override
    public void onViewClick() {
        getBinding().tvBackToSetting.setOnClickListener( v -> {
            onBackPressed();
        });
        getBinding().tvBackToHome.setOnClickListener(v -> {
            popBackStack(R.id.homeFragment);
        });
    }

}
