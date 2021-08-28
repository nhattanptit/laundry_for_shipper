package com.laundry.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavAction;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.fragment.NavHostFragment;

public abstract class BaseFragment<DB extends ViewDataBinding> extends Fragment implements BaseView {

    private DB mBinding;
    private OnBackPressedCallback mCallback;


    public DB getBinding() {
        return mBinding;
    }

    protected abstract int getLayoutResource();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onPreInitView();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(getActivity()), getLayoutResource(), container, false);
        mBinding.setLifecycleOwner(getViewLifecycleOwner());
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (isHandleBackPressByFragment()) {
            setBackPressDispatcher();
        }
        onInitView();
        onViewClick();
    }

    public void onBackPressed() {
        backScreen();
    }

    public boolean isHandleBackPressByFragment() {
        return false;
    }

    public void navigateTo(int actionId) {
        NavController controller = NavHostFragment.findNavController(this);
        NavDestination destination = controller.getCurrentDestination();
        if (destination != null && destination.getAction(actionId) != null) {
            NavAction action = destination.getAction(actionId);
            if (action != null && destination.getId() != action.getDestinationId()) {
                controller.navigate(actionId);
            }
        }
    }

    public void navigateTo(int actionId, Bundle bundle) {
        NavController controller = NavHostFragment.findNavController(this);
        NavDestination destination = controller.getCurrentDestination();
        if (destination != null && destination.getAction(actionId) != null) {
            NavAction action = destination.getAction(actionId);
            if (action != null && destination.getId() != action.getDestinationId()) {
                controller.navigate(actionId, bundle);
            }
        }
    }

    public void popBackStack(int idOfFragment) {
        try {
            NavHostFragment.findNavController(this).popBackStack(idOfFragment, false);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    public void popBackStack(int idOfFragment, boolean inclusive) {
        try {
            NavHostFragment.findNavController(this).popBackStack(idOfFragment, inclusive);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    private void setBackPressDispatcher() {
        mCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                onBackPressed();
            }
        };
    }

    private void backScreen() {
        try {
            NavHostFragment.findNavController(this).navigateUp();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    public void setStatusColor(int color, boolean state) {

    }

    public void setFullScreenWithStatusBar(boolean isFull) {

    }

    public void setFullScreen(boolean isFull) {

    }
}
