package za.co.bwmuller.easypermissions

/**
 * Created by Bernhard Müller on 10/14/2016.
 */
class PermissionRequest {

    val permissions: List<String>
    val requestCode: Int
    val permissionCallback: PermissionCallback?

    constructor(requestCode: Int) {
        permissions = emptyList()
        this.requestCode = requestCode
        permissionCallback = null
    }

    constructor(permissions: List<String>, permissionCallback: PermissionCallback?) {
        this.permissions = permissions
        this.permissionCallback = permissionCallback
        requestCode = 5000 + (System.currentTimeMillis().toInt() % 60000)
    }

    override fun hashCode(): Int {
        return requestCode
    }

    override fun equals(other: Any?): Boolean {
        return if (other == null) {
            false
        } else other is PermissionRequest && other.requestCode == requestCode
    }
}