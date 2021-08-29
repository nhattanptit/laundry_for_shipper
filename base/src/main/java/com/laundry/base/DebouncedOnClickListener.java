package com.laundry.base;

import static java.lang.System.currentTimeMillis;

import android.view.View;

public abstract class DebouncedOnClickListener implements View.OnClickListener {

    private static final long DEFAULT_DEBOUNCE_INTERVAL = 350L;
    private long lastClickTimestampGlobal = -1L;

    abstract void onDebouncedClick(View view);

    @Override
    public void onClick(View v) {
        long now = currentTimeMillis();
        if (lastClickTimestampGlobal == -1L || now >= (lastClickTimestampGlobal + DEFAULT_DEBOUNCE_INTERVAL)) {
            onDebouncedClick(v);
        }
        lastClickTimestampGlobal = now;
    }
}
