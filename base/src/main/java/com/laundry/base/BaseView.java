package com.laundry.base;

public interface BaseView {

    default void onPreInitView() {
    }

    default void onInitBinding() {
    }

    void onInitView();

    void onViewClick();
}
