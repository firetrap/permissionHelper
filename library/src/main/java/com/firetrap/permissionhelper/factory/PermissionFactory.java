package com.firetrap.permissionhelper.factory;

import android.app.Activity;
import android.app.Fragment;

import com.firetrap.permissionhelper.helper.PermissionHelper;

/**
 * Created by firetrap on 28/02/2016.
 */
public class PermissionFactory {

    private Activity activity;
    private Fragment fragment;

    public PermissionFactory(Activity activity) {
        this.activity = activity;
    }

    public PermissionFactory(Fragment fragment) {
        this.fragment = fragment;
    }

    public PermissionHelper.PermissionBuilder build(String permissionName) {

        if (activity != null) {

            return new PermissionHelper.PermissionBuilder(activity, new String[]{permissionName});
        } else {

            return new PermissionHelper.PermissionBuilder(fragment, new String[]{permissionName});
        }
    }

    public PermissionHelper.PermissionBuilder build(String... permissionNames) {

        if (activity != null) {

            return new PermissionHelper.PermissionBuilder(activity, permissionNames);
        } else {

            return new PermissionHelper.PermissionBuilder(fragment, permissionNames);
        }
    }

}