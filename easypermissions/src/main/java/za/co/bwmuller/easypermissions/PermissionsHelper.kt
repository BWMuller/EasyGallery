package za.co.bwmuller.easypermissions

import android.Manifest.permission
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import androidx.annotation.StringDef
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import java.util.ArrayList

class PermissionsHelper : PermissionListener() {

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>, grantResults: IntArray) {
        val requestResult = PermissionRequest(requestCode)
        if (permissionRequests.contains(requestResult)) {
            val permissionRequest = permissionRequests[permissionRequests.indexOf(requestResult)]
            if (verifyPermissions(grantResults)) {
                //Permission has been granted
                permissionRequest.permissionCallback?.permissionGranted()
            } else {
                permissionRequest.permissionCallback?.permissionRefused()
            }
            permissionRequests.remove(requestResult)
        }
    }

    override val activity: Activity?
        get() = null

    fun askForPermission(
        activity: Activity, permission: String, permissionCallback: PermissionCallback = object : PermissionCallback {
            override fun permissionGranted() {}
            override fun permissionRefused() {}
        }
    ) {
        askForPermission(activity, arrayOf(permission), permissionCallback)
    }

    fun askForPermission(
        activity: Activity, permissions: Array<String>, permissionCallback: PermissionCallback = object : PermissionCallback {
            override fun permissionGranted() {}
            override fun permissionRefused() {}
        }
    ) {
        if (!needPermission(activity, *permissions)) {
            permissionCallback.permissionGranted()
            return
        }
        val permissionRequest = PermissionRequest(permissions.toMutableList(), permissionCallback)
        permissionRequests.add(permissionRequest)
        requestPermissions(
            activity,
            permissions,
            permissionRequest.requestCode
        )
    }

    fun askForPermission(
        fragment: Fragment, permission: String, permissionCallback: PermissionCallback = object : PermissionCallback {
            override fun permissionGranted() {}
            override fun permissionRefused() {}
        }
    ) {
        askForPermission(fragment.requireActivity(), arrayOf(permission), permissionCallback)
    }

    private fun getFilteredPermissions(activity: Activity, permissions: Array<String>): Array<String> {
        return permissions.filter { needPermission(activity, it) }.toTypedArray()
    }

    private fun generateFailedGrant(activity: Activity, permissions: Array<String>): IntArray {
        val results = IntArray(permissions.size)
        for (i in permissions.indices) {
            if (needPermission(activity, permissions[i])) results[i] = PackageManager.PERMISSION_DENIED else results[i] = PackageManager.PERMISSION_GRANTED
        }
        return results
    }

    private fun requestPermissions(activity: Activity, permissionsRequired: Array<String>, requestCode: Int) {
        val permissions = getFilteredPermissions(activity, permissionsRequired)
        if (permissions.isNotEmpty()) ActivityCompat.requestPermissions(activity, permissions, requestCode) else onRequestPermissionsResult(
            requestCode,
            permissionsRequired.mapTo(ArrayList<String?>()) { it }.toTypedArray(),
            generateFailedGrant(activity, permissionsRequired)
        )
    }

    /**
     * Check that all given permissions have been granted by verifying that each entry in the
     * given array is of the value [PackageManager.PERMISSION_GRANTED].
     */
    fun verifyPermissions(grantResults: IntArray): Boolean {
        for (result in grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    fun askPermission(activity: Activity, requestCode: Int, permission: String) {
        if (needPermission(activity, permission)) requestPermissions(
            activity, arrayOf(permission),
            requestCode
        )
    }

    /**
     * @hide
     */
    @StringDef(
        K_READ_CONTACTS,
        K_RECORD_AUDIO,
        K_ACCESS_FINE_LOCATION,
        K_INTERNET,
        K_ACCESS_NETWORK_STATE,
        K_VIBRATE,
        K_GET_ACCOUNTS,
        K_WAKE_LOCK,
        K_MODIFY_AUDIO_SETTINGS,
        K_READ_PHONE_STATE,
        K_WRITE_CONTACTS,
        K_READ_EXTERNAL_STORAGE,
        K_WRITE_EXTERNAL_STORAGE,
        K_CAMERA,
        K_READ_SMS,
        K_ACCESS_COARSE_LOCATION
    )
    annotation class Permission
    companion object {

        const val K_READ_CONTACTS = permission.READ_CONTACTS
        const val K_RECORD_AUDIO = permission.RECORD_AUDIO
        const val K_ACCESS_FINE_LOCATION = permission.ACCESS_FINE_LOCATION
        const val K_ACCESS_COARSE_LOCATION = permission.ACCESS_COARSE_LOCATION
        const val K_INTERNET = permission.INTERNET
        const val K_ACCESS_NETWORK_STATE = permission.ACCESS_NETWORK_STATE
        const val K_VIBRATE = permission.VIBRATE
        const val K_GET_ACCOUNTS = permission.GET_ACCOUNTS
        const val K_WAKE_LOCK = permission.WAKE_LOCK
        const val K_MODIFY_AUDIO_SETTINGS = permission.MODIFY_AUDIO_SETTINGS
        const val K_READ_PHONE_STATE = permission.READ_PHONE_STATE
        const val K_WRITE_CONTACTS = permission.WRITE_CONTACTS
        const val K_READ_EXTERNAL_STORAGE = permission.READ_EXTERNAL_STORAGE
        const val K_WRITE_EXTERNAL_STORAGE = permission.WRITE_EXTERNAL_STORAGE
        const val K_CAMERA = permission.CAMERA
        const val K_READ_SMS = permission.READ_SMS
        private val permissionRequests = ArrayList<PermissionRequest>()

        private fun checkPermission(context: Context, permission: String): Boolean {
            return VERSION.SDK_INT >= VERSION_CODES.M && ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED
        }

        /**
         * Returns false if the Context does not have access to a all given permission.
         */
        @JvmStatic
        fun needPermission(context: Context, vararg permissions: String): Boolean {
            for (permission in permissions) {
                if (checkPermission(context, permission)) {
                    return true
                }
            }
            return false
        }

        /**
         * Returns true if the Context has access to a all given permission.
         */
        @JvmStatic
        fun hasPermission(context: Context, vararg permissions: String): Boolean {
            return !needPermission(context, *permissions)
        }

        @JvmStatic
        fun requestPermissions(
            activity: AppCompatCallbackActivity, permissions: Array<String>, callback: PermissionCallback = object : PermissionCallback {
                override fun permissionGranted() {}
                override fun permissionRefused() {}
            }
        ) {
            val permissionsHelper = PermissionsHelper()
            activity.addListener(permissionsHelper)
            permissionsHelper.askForPermission(activity, permissions, callback)
        }

        @JvmStatic
        fun requestPermission(
            activity: AppCompatCallbackActivity, permissions: String, callback: PermissionCallback = object : PermissionCallback {
                override fun permissionGranted() {}
                override fun permissionRefused() {}
            }
        ) {
            val permissionsHelper = PermissionsHelper()
            activity.addListener(permissionsHelper)
            permissionsHelper.askForPermission(activity, permissions, callback)
        }
    }
}