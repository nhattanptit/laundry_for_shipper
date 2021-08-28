package com.laundry.base;

public interface BaseView {

    default void onPreInitView() {
    }

    void onInitView();

    void onViewClick();
}
