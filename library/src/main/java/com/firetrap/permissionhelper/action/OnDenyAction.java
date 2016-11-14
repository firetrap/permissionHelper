package com.firetrap.permissionhelper.action;

/**
 * Created by firetrap on 28/02/2016.
 */
public abstract class OnDenyAction {

    public abstract void call(int requestCode, boolean shouldShowRationale);
}

