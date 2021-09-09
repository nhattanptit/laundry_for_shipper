package com.laundry.app.view.dialog;

import android.content.Context;

import androidx.annotation.NonNull;

import com.laundry.app.R;
import com.laundry.app.databinding.OrderFailDialogBinding;
import com.laundry.app.utils.SingleTapListener;
import com.laundry.base.BaseDialog;

public class OrderFailDialog extends BaseDialog<OrderFailDialogBinding> {

    private OnDialogDissmissListener mOnDialogDissmissListener;

    public interface OnDialogDissmissListener {
        void onCancel();
        void onAllow();
    }

    public static OrderFailDialog newInstance() {
        OrderFailDialog orderFailDialog = new OrderFailDialog();
        return orderFailDialog;
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            this.mOnDialogDissmissListener = (OnDialogDissmissListener) context;
        } catch (ClassCastException e) {
            throw e;
        }
    }


    @Override
    protected int getLayoutResource() {
        return R.layout.order_fail_dialog;
    }

    @Override
    public void onInitView() {

    }

    @Override
    public void onViewClick() {
        binding.noButton.setOnClickListener(new SingleTapListener(v -> mOnDialogDissmissListener.onCancel()));
        binding.yesButton.setOnClickListener(new SingleTapListener(v -> mOnDialogDissmissListener.onAllow()));
    }
}
