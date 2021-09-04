package com.laundry.app.view.fragment;

import androidx.databinding.ViewDataBinding;

/**
 * Tất cả những fragment "MUỐN hiển thị tab" thì kế thừa từ LaundryFragment
 */
public abstract class LaundryTabFragment<DB extends ViewDataBinding> extends LaundryFragment<DB>{

    @Override
    boolean isHideTab() {
        return false;
    }

}
