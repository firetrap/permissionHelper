package com.firetrap.permissionhelper.helper;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.firetrap.permissionhelper.BuildConfig;
import com.firetrap.permissionhelper.action.OnDenyAction;
import com.firetrap.permissionhelper.action.OnGrantAction;
import com.firetrap.permissionhelper.factory.PermissionFactory;
import com.firetrap.permissionhelper.model.SinglePermission;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by firetrap on 28/02/2016.
 */
public class PermissionHelper {

    private static final String TAG = "PermissionHelper";
    private static Fragment mFragment;
    private static Activity mActivity;
    private static Context context;

    public static PermissionFactory with(Fragment fragment) {
        mFragment = fragment;
        context = fragment.getActivity();
        return new PermissionFactory();
    }

    public static PermissionFactory with(Activity activity) {
        mActivity = activity;
        context = activity;

        return new PermissionFactory();
    }

    public static boolean checkPermissions(Fragment fragment, String... permissionNames) {
        boolean permissionGranted = true;

        for (String permission : permissionNames) {

            int permissionCheck = ContextCompat.checkSelfPermission(fragment.getActivity(), permission);

            if (permissionCheck == PackageManager.PERMISSION_DENIED) {

                permissionGranted = false;

            }
        }

        return permissionGranted;
    }

    public static boolean checkPermissions(Activity activity, String... permissionNames) {
        boolean permissionGranted = true;

        for (String permission : permissionNames) {

            int permissionCheck = ContextCompat.checkSelfPermission(activity, permission);

            if (permissionCheck == PackageManager.PERMISSION_DENIED) {

                permissionGranted = false;

            }
        }

        return permissionGranted;
    }

    static public class PermissionBuilder {

        private static final String TAG = "PermissionBuilder";

        private ArrayList<SinglePermission> notGrantedPermissionsList = new ArrayList<>();
        private int mRequestCode;
        private ArrayList<String> mRequestPermissions;
        private OnGrantAction mGrantAction;
        private OnDenyAction mDenyAction;

        public PermissionBuilder(String[] permissionNames) {

            mRequestPermissions = new ArrayList<>(Arrays.asList(permissionNames));
        }


        public ArrayList<SinglePermission> getPermissionsList() {
            return notGrantedPermissionsList;
        }

        /**
         * Execute the permission build with the given Request Code
         *
         * @param reqCode a unique build code in your activity or fragment
         */
        public PermissionBuilder request(int reqCode) {
            mRequestCode = reqCode;

            for (String permission : mRequestPermissions) {

                int permissionCheck = ContextCompat.checkSelfPermission(context, permission);

                //if the permission is already granted don't need to ask
                switch (permissionCheck) {

                    case PackageManager.PERMISSION_GRANTED:
                        //do nothing
                        break;

                    case PackageManager.PERMISSION_DENIED:
                        notGrantedPermissionsList.add(new SinglePermission(permission));
                        break;

                }
            }

            buildRequest();

            return this;
        }


        public PermissionBuilder retry() {
            buildRequest();

            return this;
        }

        private void buildRequest() {
            boolean requestPermissions = !notGrantedPermissionsList.isEmpty();

            if (requestPermissions) {

                try {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                        ArrayList<String> permissionsList = new ArrayList<>();

                        for (SinglePermission singlePermission : notGrantedPermissionsList) {

                            permissionsList.add(singlePermission.getPermissionName());
                        }

                        if (mFragment != null) {

                            mFragment.requestPermissions(permissionsList.toArray(new String[permissionsList.size()]), mRequestCode);
                        } else {

                            mActivity.requestPermissions(permissionsList.toArray(new String[permissionsList.size()]), mRequestCode);
                        }
                    }

                } catch (NullPointerException e) {

                    Log.e(TAG, "The activity or fragment is null", e);
                }

            } else {
                //if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M ) {
                mGrantAction.call(0);
                //}

            }
        }

        /**
         * Called if all the permissions were granted
         */
        public PermissionBuilder onPermissionsGranted(OnGrantAction grantAction) {
            mGrantAction = grantAction;

            return this;
        }

        /**
         * Called if there is at least one denied permission
         */
        public PermissionBuilder onPermissionsDenied(OnDenyAction denyAction) {
            mDenyAction = denyAction;

            return this;
        }

        /**
         * One approach you might use is to provide an explanation only if the user
         * has already turned down that permission build. If a user keeps trying
         * to use functionality that requires a permission, but keeps turning down
         * the permission build, that probably shows that the user doesn't understand
         * why the app needs the permission to provide that functionality.
         * In a situation like that, it's probably a good idea to show an explanation.
         *
         * @param titleResx
         * @param descriptionResx
         * @param styleResx
         */
        public void showRational(int titleResx, int descriptionResx, int styleResx) {
            int mTitle = titleResx != 0 ? titleResx : com.firetrap.permissionhelper.R.string.rationale_title;

            DialogHelper.showAlertDialog((Activity) context, mTitle, descriptionResx, com.firetrap.permissionhelper.R.string.rationale_retry, com.firetrap.permissionhelper.R.string.rationale_dismiss, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    retry();
                }
            }, styleResx);
        }

        /**
         * This Method should be called from {@link AppCompatActivity#onRequestPermissionsResult(int, String[], int[])
         * onRequestPermissionsResult} with all the same incoming operands
         * <pre>
         * {@code
         *
         * public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
         *      if (mStoragePermissionRequest != null)
         *          mStoragePermissionRequest.onRequestPermissionsResult(requestCode, permissions,grantResults);
         * }
         * }
         * </pre>
         */
        public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

            if (mRequestCode == requestCode) {

                for (int i = 0; i < permissions.length; i++) {

                    if (grantResults[i] == PackageManager.PERMISSION_DENIED) {


                        boolean shouldShowRational = ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, notGrantedPermissionsList.get(i).getPermissionName());

                        if (BuildConfig.DEBUG) {

                            Log.i(TAG, "Calling Deny action");
                        }

                        if (mDenyAction != null) {

                            mDenyAction.call(requestCode, shouldShowRational);
                        } else {

                            Log.i(TAG, "Error calling OnDenyAction, had you implemented OnDenyAction?");
                        }

                        // Terminate if there is at least one deny
                        return;
                    }
                }

                // there has not been any deny
                if (mGrantAction != null) {

                    mGrantAction.call(requestCode);
                } else {

                    Log.i(TAG, "Error calling onGrantAction, had you implemented onGrantAction?");
                }

            }
        }
    }
}

