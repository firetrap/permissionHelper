package com.firetrap.permissionhelper.fragment;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

/**
 * Created by firetrap on 22/10/2016.
 */

public class DialogContainerFragment extends DialogFragment {

    // Global field to contain the error dialog
    private Dialog mDialog;

    // Default constructor. Sets the dialog field to null
    public DialogContainerFragment() {

        super();
        mDialog = null;
    }

    // Set the dialog to display
    public void setDialog(Dialog dialog) {

        mDialog = dialog;
    }

    // Return a Dialog to the DialogFragment.
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        return mDialog;
    }
}
