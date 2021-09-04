package com.laundry.app.view.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ViewDataBinding;

import com.laundry.app.view.activity.HomeActivity;
import com.laundry.base.BaseFragment;

/**
 * Tất cả những fragment "KHÔNG MUỐN hiển thị tab" thì kế thừa từ LaundryFragment
 */
public abstract class LaundryFragment<DB extends ViewDataBinding> extends BaseFragment<DB> {


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (isHideTab()) {
            ((HomeActivity) getMyActivity()).hideMenu();
        } else {
            ((HomeActivity) getMyActivity()).showMenu();
        }
    }

    boolean isHideTab() {
        return true;
    }
}
