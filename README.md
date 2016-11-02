PermissionHelper
=====
How do I add PermissionHelper to my project?
------------------- 
Add it in your root build.gradle at the end of repositories:
```
allprojects {
		repositories {
			...
			maven { url "https://jitpack.io" }
		}
}
```
And in your module gradle build file:
```
dependencies {
		compile 'com.github.firetrap:permissionHelper:1.0.1'
	}
```
How do I use PermissionHelper?
-------------------

Simple use cases will look something like this:

Manifest:

```xml
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
```

```java
private PermissionHelper.PermissionBuilder permissionRequest;
private static final int REQUEST_STORAGE_CONTACTS = 2;

	View.OnClickListener onPermissionRequestClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {


			permissionRequest = PermissionHelper.with(YOUR_FRAGMENT_OR_ACTIVITY)
					.build(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.WRITE_CONTACTS)
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

Extras:
-------------------

You can create a dialog theme to show the rational dialog (If you want to use the system default pass null), for this you need to create a "DialogTheme":
```xml
 <style name="DialogTheme" parent="Theme.AppCompat.Light.Dialog.Alert">
        <!--buttons color-->
        <item name="colorAccent">@color/colorPrimary</item>
        <!--title and message color-->
        <item name="android:textColorPrimary">@color/title_text_color</item>
        <!--dialog background-->
        <item name="android:windowBackground">@drawable/dialog_background</item>
    </style>
```

