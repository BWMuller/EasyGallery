package za.co.bwmuller.easypermissions;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.StringDef;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Arrays;

public class PermissionsHelper extends PermissionListener {
    public static final String K_READ_CONTACTS = Manifest.permission.READ_CONTACTS;
    public static final String K_RECORD_AUDIO = Manifest.permission.RECORD_AUDIO;
    public static final String K_ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    public static final String K_ACCESS_COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    public static final String K_INTERNET = Manifest.permission.INTERNET;
    public static final String K_ACCESS_NETWORK_STATE = Manifest.permission.ACCESS_NETWORK_STATE;
    public static final String K_VIBRATE = Manifest.permission.VIBRATE;
    public static final String K_GET_ACCOUNTS = Manifest.permission.GET_ACCOUNTS;
    public static final String K_WAKE_LOCK = Manifest.permission.WAKE_LOCK;
    public static final String K_MODIFY_AUDIO_SETTINGS = Manifest.permission.MODIFY_AUDIO_SETTINGS;
    public static final String K_READ_PHONE_STATE = Manifest.permission.READ_PHONE_STATE;
    public static final String K_WRITE_CONTACTS = Manifest.permission.WRITE_CONTACTS;
    public static final String K_READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE;
    public static final String K_WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    public static final String K_CAMERA = Manifest.permission.CAMERA;
    public static final String K_READ_SMS = Manifest.permission.READ_SMS;
    private static ArrayList<PermissionRequest> permissionRequests = new ArrayList<>();

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionRequest requestResult = new PermissionRequest(requestCode);
        if (permissionRequests.contains(requestResult)) {
            PermissionRequest permissionRequest = permissionRequests.get(permissionRequests.indexOf(requestResult));
            if (verifyPermissions(grantResults)) {
                //Permission has been granted
                permissionRequest.getPermissionCallback().permissionGranted();
            } else {
                permissionRequest.getPermissionCallback().permissionRefused();
            }
            permissionRequests.remove(requestResult);
        }
    }

    @Override public Activity getActivity() {
        return null;
    }

    private static boolean checkPermission(Context context, String permission) {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Returns false if the Context does not have access to a all given permission.
     */
    public static boolean needPermission(Context context, @PermissionsHelper.Permission String... permissions) {
        for (String permission : permissions) {
            if (checkPermission(context, permission)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns true if the Context has access to a all given permission.
     */
    public static boolean hasPermission(Context context, @PermissionsHelper.Permission String... permissions) {
        return !needPermission(context, permissions);
    }

    public static void requestPermissions(AppCompatCallbackActivity activity, @PermissionsHelper.Permission String[] permissions, PermissionCallback callback) {
        PermissionsHelper permissionsHelper = new PermissionsHelper();
        activity.addListener(permissionsHelper);
        permissionsHelper.askForPermission(activity, permissions, callback);
    }

    public static void requestPermission(AppCompatCallbackActivity activity, @PermissionsHelper.Permission String permissions, PermissionCallback callback) {
        PermissionsHelper permissionsHelper = new PermissionsHelper();
        activity.addListener(permissionsHelper);
        permissionsHelper.askForPermission(activity, permissions, callback);
    }

    public void askForPermission(Activity activity, @PermissionsHelper.Permission String permission) {
        askForPermission(activity, new String[]{permission}, new PermissionCallback() {
            @Override
            public void permissionGranted() {

            }

            @Override
            public void permissionRefused() {

            }
        });
    }

    public void askForPermission(Activity activity, @PermissionsHelper.Permission String[] permissions) {
        askForPermission(activity, permissions, new PermissionCallback() {
            @Override
            public void permissionGranted() {

            }

            @Override
            public void permissionRefused() {

            }
        });
    }

    public void askForPermission(Activity activity, @PermissionsHelper.Permission String permission, PermissionCallback permissionCallback) {
        askForPermission(activity, new String[]{permission}, permissionCallback);
    }

    public void askForPermission(Activity activity, @PermissionsHelper.Permission String[] permissions, PermissionCallback permissionCallback) {
        if (permissionCallback == null) {
            return;
        }
        if (!needPermission(activity, permissions)) {
            permissionCallback.permissionGranted();
            return;
        }
        PermissionRequest permissionRequest = new PermissionRequest(new ArrayList<>(Arrays.asList(permissions)), permissionCallback);
        permissionRequests.add(permissionRequest);

        requestPermissions(activity,
                permissions,
                permissionRequest.getRequestCode());
    }

    public void askForPermission(Fragment fragment, @PermissionsHelper.Permission String permission) {
        askForPermission(fragment.getActivity(), permission);
    }

    public void askForPermission(Fragment fragment, @PermissionsHelper.Permission String permission, PermissionCallback permissionCallback) {
        askForPermission(fragment.getActivity(), new String[]{permission}, permissionCallback);
    }

    private String[] getFilteredPermissions(Activity activity, @PermissionsHelper.Permission String[] permissions) {
        ArrayList<String> permissionList = new ArrayList<>();
        for (String permission : permissions) {
            if (PermissionsHelper.needPermission(activity, permission))
                permissionList.add(permission);

        }
        return permissionList.toArray(new String[permissionList.size()]);
    }

    private int[] generateFailedGrant(Activity activity, @PermissionsHelper.Permission String[] permissions) {
        int[] results = new int[permissions.length];
        for (int i = 0; i < permissions.length; i++) {
            if (PermissionsHelper.needPermission(activity, permissions[i]))
                results[i] = PackageManager.PERMISSION_DENIED;
            else
                results[i] = PackageManager.PERMISSION_GRANTED;
        }
        return results;
    }

    private void requestPermissions(Activity activity, @PermissionsHelper.Permission String[] permissionsRequired, int requestCode) {
        String[] permissions = getFilteredPermissions(activity, permissionsRequired);
        if (permissions.length > 0)
            ActivityCompat.requestPermissions(activity, permissions, requestCode);
        else
            onRequestPermissionsResult(requestCode, permissionsRequired, generateFailedGrant(activity, permissionsRequired));
    }

    /**
     * Check that all given permissions have been granted by verifying that each entry in the
     * given array is of the value {@link PackageManager#PERMISSION_GRANTED}.
     */
    public boolean verifyPermissions(int[] grantResults) {
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    public void askPermission(Activity activity, int requestCode, @PermissionsHelper.Permission String permission) {
        if (needPermission(activity, permission))
            requestPermissions(activity,
                    new String[]{permission},
                    requestCode);
    }

    /**
     * @hide
     */
    @StringDef({K_READ_CONTACTS, K_RECORD_AUDIO, K_ACCESS_FINE_LOCATION
            , K_INTERNET, K_ACCESS_NETWORK_STATE, K_VIBRATE
            , K_GET_ACCOUNTS, K_WAKE_LOCK, K_MODIFY_AUDIO_SETTINGS
            , K_READ_PHONE_STATE, K_WRITE_CONTACTS, K_READ_EXTERNAL_STORAGE
            , K_WRITE_EXTERNAL_STORAGE, K_CAMERA, K_READ_SMS
            , K_ACCESS_COARSE_LOCATION})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Permission {
    }
}