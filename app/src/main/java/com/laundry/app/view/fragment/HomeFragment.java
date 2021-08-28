package com.laundry.app.view.fragment;

import com.laundry.app.R;
import com.laundry.app.databinding.HomeFragmentBinding;
import com.laundry.base.BaseFragment;

public class HomeFragment extends BaseFragment<HomeFragmentBinding> {


    @Override
    protected int getLayoutResource() {
        return R.layout.home_fragment;
    }

    @Override
    public void onPreInitView() {

    }

    @Override
    public void onInitView() {

    }

    @Override
    public void onViewClick() {
        getBinding().tvToSetting.setOnClickListener(v->{
            navigateTo(R.id.action_homeFragment_to_settingFragment);
        });
    }
}
