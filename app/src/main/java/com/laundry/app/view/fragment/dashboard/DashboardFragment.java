package com.laundry.app.view.fragment.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.laundry.app.R;
import com.laundry.app.databinding.FragmentDashboardBinding;
import com.laundry.app.databinding.FragmentHomeBinding;
import com.laundry.base.BaseFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

public class DashboardFragment extends BaseFragment<FragmentDashboardBinding> {


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_dashboard;
    }

    @Override
    public void onInitView() {

    }

    @Override
    public void onViewClick() {

    }
}