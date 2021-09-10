package com.laundry.app.view.toolbar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.laundry.app.R;

public class LaundryToolbar extends FrameLayout {

    private LaundryToolbarListener mToolbarListener;
    private View view;
    private ImageView ivBack;
    private TextView tvTitle;

    public LaundryToolbar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.layout_toolbar, this);
        ivBack = view.findViewById(R.id.toolbar_Back);
        tvTitle = view.findViewById(R.id.toolbar_title);
        ivBack.setOnClickListener(v -> {
            mToolbarListener.onBack(v);
        });
    }

    public void setToolbarListener(LaundryToolbarListener toolbarListener) {
        this.mToolbarListener = toolbarListener;
    }

    public void setTitle(String title) {
        tvTitle.setText(title);
        requestLayout();
    }

    public void setHideButtonBack() {
        ivBack.setVisibility(View.GONE);
    }

    public interface LaundryToolbarListener {
        void onBack(View view);
    }
}

