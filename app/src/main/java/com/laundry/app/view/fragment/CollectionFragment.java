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
        binding.tvCollection.setOnClickListener(v ->{
            navigateTo(R.id.action_collectionFragment_to_loginDialog);
        });

        binding.tvBackToSetting.setOnClickListener( v -> {
            onBackPressed();
        });
        binding.tvBackToHome.setOnClickListener(v -> {
            popBackStack(R.id.homeFragment);
        });
    }


}
