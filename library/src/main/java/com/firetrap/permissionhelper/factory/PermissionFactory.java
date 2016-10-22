package com.firetrap.permissionhelper.factory;

import com.firetrap.permissionhelper.helper.PermissionHelper;

/**
 * Created by firetrap on 28/02/2016.
 */
public class PermissionFactory {

	public PermissionHelper.PermissionBuilder build(String permissionName) {

		return new PermissionHelper.PermissionBuilder(new String[]{permissionName});
	}

	public PermissionHelper.PermissionBuilder build(String... permissionNames) {

		return new PermissionHelper.PermissionBuilder(permissionNames);
	}

}