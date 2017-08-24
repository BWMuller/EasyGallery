package za.co.bwmuller.easypermissions;

/**
 * Created by Bernhard Müller on 10/14/2016.
 */
public interface PermissionCallback {
    void permissionGranted();

    void permissionRefused();
}