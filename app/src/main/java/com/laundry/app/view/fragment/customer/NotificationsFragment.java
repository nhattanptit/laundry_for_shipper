package com.laundry.app.view.fragment.customer;

import com.laundry.app.R;
import com.laundry.app.databinding.FragmentNotificationsBinding;
import com.laundry.base.BaseFragment;

public class NotificationsFragment extends BaseFragment<FragmentNotificationsBinding> {

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_notifications;
    }

    @Override
    public void onInitView() {

    }

    @Override
    public void onViewClick() {
        binding.textNotifications.setOnClickListener(view -> {
//            navigateTo(R.id.action_navigation_notifications_to_navigation_RegisterOrLoginFragment);
        });
    }
}