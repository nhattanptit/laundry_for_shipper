package com.laundry.base;

public abstract class BaseDisplay<T> {

    public static final int INVALID_RESOURCE = -1;

    public T data;

    protected abstract int getIcon();
}


