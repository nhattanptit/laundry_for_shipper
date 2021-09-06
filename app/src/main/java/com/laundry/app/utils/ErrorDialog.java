package com.laundry.app.utils;

import android.content.Context;
import android.content.DialogInterface;

import com.laundry.app.R;

import androidx.appcompat.app.AlertDialog;

public class ErrorDialog {

    public static AlertDialog buildPopup(Context context, int messageId,
                                         DialogInterface.OnClickListener okListener) {
        AlertDialog.Builder ab = new AlertDialog.Builder(context);
        ab.setMessage(messageId);
        ab.setNegativeButton(R.string.no, new OnCancelListener());
        ab.setPositiveButton(R.string.yes, okListener);

        AlertDialog dialog = ab.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        return dialog;
    }

    public static AlertDialog buildPopupWithTitleOnlyPositive(Context context, int titleId,
                                                              String message, DialogInterface.OnClickListener okListener) {
        AlertDialog.Builder ab = new AlertDialog.Builder(context);
        ab.setTitle(titleId);
        ab.setMessage(message);
        ab.setPositiveButton(R.string.ok, okListener);

        AlertDialog dialog = ab.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        return dialog;
    }

    public static AlertDialog buildPopupOnlyPositive(Context context, String message,
                                                     int positiveButtonId, DialogInterface.OnClickListener positiveButtonListener) {
        AlertDialog.Builder ab = new AlertDialog.Builder(context);

        ab.setMessage(message);
        ab.setPositiveButton(context.getString(positiveButtonId), positiveButtonListener);

        AlertDialog dialog = ab.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        return dialog;
    }

    private static class OnCancelListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
        }
    }
}
