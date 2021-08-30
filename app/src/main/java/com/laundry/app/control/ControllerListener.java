package com.laundry.app.control;

public interface ControllerListener<T> {
    void onSuccess(T t);

    void onFailure(Throwable throwable);
}
