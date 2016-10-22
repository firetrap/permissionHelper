PermissionHelper
=====

dependencies {

compile 'itpeople:pt.itpeople.permissionhelper:2.0.1'
  
compile 'itpeople:pt.itpeople.app:3.1.1'

compile 'itpeople:pt.itpeople.common:3.1.1'

compile 'com.android.support:appcompat-v7:23.2.0'

}

How do I use PermissionHelper?
-------------------

Simple use cases will look something like this:

```java
private PermissionHelper.PermissionBuilder permissionRequest;
private static final int REQUEST_STORAGE_CONTACTS = 2;

	View.OnClickListener onPermissionRequestClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {


			permissionRequest = PermissionHelper.with(mFragment)
					.request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.WRITE_CONTACTS)
					.onPermissionsDenied(onDenyAction)
					.onPermissionsGranted(onGrantAction)
					.request(REQUEST_STORAGE_CONTACTS);

		}
	};

	private OnDenyAction onDenyAction = new OnDenyAction() {
		@Override
		public void call(int requestCode, boolean shouldShowRequestPermissionRationale) {

			// Should we show an explanation?
			if (shouldShowRequestPermissionRationale) {

				permissionRequest.showRational(R.string.rationale_description, R.style.DialogTheme);
			}
		}
	};

	private OnGrantAction onGrantAction = new OnGrantAction() {
		@Override
		public void call(int requestCode) {
			//do something
		}
	};

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		permissionRequest.onRequestPermissionsResult(requestCode, permissions, grantResults);

		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
	}

```