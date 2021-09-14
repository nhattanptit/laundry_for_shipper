package com.laundry.base;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

public abstract class BaseDialog<DB extends ViewDataBinding> extends DialogFragment implements BaseView {

    protected DB binding;
    private BaseActivity mActivity;
    protected LinearLayout mProgressBarView;

    protected abstract int getLayoutResource();

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mActivity = (BaseActivity) context;
        } catch (ClassCastException e) {
            throw e;
        }
    }

    public BaseActivity getMyActivity() {
        return this.mActivity != null ? this.mActivity : (BaseActivity) getActivity();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onPreInitView();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), getLayoutResource(), container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        onInitBinding();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Bắt buộc phải đặt id layout parent của dialog giống hệt tên như này
        View root = view.findViewById(R.id.backgroundDialog);
        root.setOnClickListener(v -> {
            if (dismissByTouchOutside()) {
                dismissDialog();
            }
        });
        onInitView();
        onViewClick();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        RelativeLayout layout = new RelativeLayout(requireActivity());
        layout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        Dialog dialog = new Dialog(requireActivity()) {
            @Override
            public void onBackPressed() {
                if (dismissByOnBackPress()) {
                    dismissDialog();
                }
            }
        };

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(layout);
        Window window = dialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        wlp.height = WindowManager.LayoutParams.MATCH_PARENT;
        wlp.gravity = Gravity.CENTER;
        configDialog(dialog);
        return dialog;
    }

    public final void showDialog(FragmentManager manager, String tag) {
        if (!this.isAdded()) {
            show(manager, tag);
        }
    }

    public final void dismissDialog() {
        if (this.isAdded()) {
            dismiss();
        }
    }

    protected boolean dismissByTouchOutside() {
        return true;
    }

    protected boolean dismissByOnBackPress() {
        return true;
    }

    private void configDialog(Dialog dialog) {
        dialog.setCanceledOnTouchOutside(dismissByTouchOutside());
    }
}
