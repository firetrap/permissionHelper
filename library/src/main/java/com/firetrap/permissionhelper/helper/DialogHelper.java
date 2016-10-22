package com.firetrap.permissionhelper.helper;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.firetrap.permissionhelper.fragment.DialogContainerFragment;

/**
 * Created by firetrap on 22/10/2016.
 */

public class DialogHelper {
    private static final String TAG = "DialogHelper";

    private static final DialogInterface.OnClickListener closeButtonListener = new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            try {
                dialog.dismiss();
            } catch (Exception var4) {
                Log.e("DialogHelper", "closeButtonlistener", var4);
            }

        }
    };

    public DialogHelper() {
    }

    public static void showAlertDialog(Activity context, int titleResource, int messageResource, int positiveButtonTextResource,
                                       int negativeButtonTextResource, DialogInterface.OnClickListener positiveClickListener, Integer theme) {
        showAlertDialog(context, titleResource, messageResource, positiveButtonTextResource, negativeButtonTextResource,
                positiveClickListener, closeButtonListener, theme);
    }

    public static void showAlertDialog(Activity context, int titleResource, int messageResource, int positiveButtonTextResource,
                                       int negativeButtonTextResource, DialogInterface.OnClickListener positiveClickListener,
                                       DialogInterface.OnClickListener negativeClickListener, Integer theme) {
        AlertDialog.Builder builder = buildAlertDialogBuilder(context, titleResource, false, null, theme);
        builder.setMessage(messageResource);
        builder.setPositiveButton(positiveButtonTextResource, positiveClickListener);
        builder.setNegativeButton(negativeButtonTextResource, negativeClickListener);
        showDialogFragment(context, createAlertDialogFragment(builder.create()), "");
    }


    public static DialogFragment createAlertDialogFragment(Dialog dialog) {
        DialogContainerFragment dialogFragment = new DialogContainerFragment();
        dialogFragment.setDialog(dialog);
        return dialogFragment;
    }

    public static void showDialogFragment(final Activity currentActivity, final DialogFragment dialog, final String tag) {
        currentActivity.runOnUiThread(new Runnable() {
            public void run() {
                dialog.show(currentActivity.getFragmentManager(), tag);
            }
        });
    }

    public static AlertDialog.Builder buildAlertDialogBuilder(Activity context, int titleResource, boolean addDismissButton, Integer dismissButtonTextResource, Integer theme) {
        AlertDialog.Builder builder = theme == null ? new AlertDialog.Builder(context, android.R.style.Theme_DeviceDefault_Dialog) : new AlertDialog.Builder(context, theme.intValue());
        builder.setCancelable(true);
        builder.setTitle(titleResource);
        if (addDismissButton) {
            builder.setNeutralButton(dismissButtonTextResource, closeButtonListener);
        }

        return builder;
    }
}
