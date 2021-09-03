
package com.laundry.app.utils;

import android.view.View;
import android.view.View.OnClickListener;

public class SingleTapListener implements OnClickListener {

    private static final int THRESHOLD = 500;

    private long timestamp = 0;

    private OnClickListener singleTapListener;


    public SingleTapListener(OnClickListener listener) {
        this.singleTapListener = listener;
    }

    @Override
    public void onClick(View v) {
        long now = System.currentTimeMillis();
        if (isOverThreashold(now)) {
            this.singleTapListener.onClick(v);
        }
        this.timestamp = now;
    }

    private boolean isOverThreashold(long now) {
        return now - timestamp > THRESHOLD;
    }

}
