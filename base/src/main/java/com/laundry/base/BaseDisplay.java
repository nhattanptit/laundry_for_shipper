package com.laundry.base;

public abstract class BaseDisplay<T> {

    public static final int INVALID_RESOURCE = -1;

    protected T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    protected abstract int getIcon();
}


