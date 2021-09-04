package com.laundry.base;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

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

    protected DB binding;
    private OnBackPressedCallback mCallback;
    private BaseActivity mActivity;
    protected LinearLayout mProgressBarView;


    protected abstract int getLayoutResource();

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mActivity = (BaseActivity) context;
        } catch (ClassCastException e) {
            throw e;
        }
    }

    public BaseActivity getMyActivity() {
        return this.mActivity != null ? this.mActivity : (BaseActivity) getActivity();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onPreInitView();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(getActivity()), getLayoutResource(), container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        onInitBinding();
        return binding.getRoot();
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

    protected final void onBackPressed() {
        backScreen();
    }

    protected final boolean isHandleBackPressByFragment() {
        return false;
    }

    protected final void navigateTo(int actionId) {
        NavController controller = NavHostFragment.findNavController(this);
        NavDestination destination = controller.getCurrentDestination();
        if (destination != null && destination.getAction(actionId) != null) {
            NavAction action = destination.getAction(actionId);
            if (action != null && destination.getId() != action.getDestinationId()) {
                controller.navigate(actionId);
            }
        }
    }

    protected final void navigateTo(int actionId, Bundle bundle) {
        NavController controller = NavHostFragment.findNavController(this);
        NavDestination destination = controller.getCurrentDestination();
        if (destination != null && destination.getAction(actionId) != null) {
            NavAction action = destination.getAction(actionId);
            if (action != null && destination.getId() != action.getDestinationId()) {
                controller.navigate(actionId, bundle);
            }
        }
    }

    protected final void popBackStack(int idOfFragment) {
        try {
            NavHostFragment.findNavController(this).popBackStack(idOfFragment, false);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    protected final void popBackStack(int idOfFragment, boolean inclusive) {
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

    protected final void setStatusColor(int color, boolean state) {

    }

    protected final void setFullScreenWithStatusBar(boolean isFull) {

    }

    protected final void setFullScreen(boolean isFull) {

    }
}
