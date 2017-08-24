package za.co.bwmuller.easypermissions;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Bernhard Müller on 10/14/2016.
 */
public class PermissionRequest {
    private static Random random;
    private ArrayList<String> permissions;
    private int requestCode;
    private PermissionCallback permissionCallback;

    public PermissionRequest(int requestCode) {
        this.requestCode = requestCode;
    }

    public PermissionRequest(ArrayList<String> permissions, PermissionCallback permissionCallback) {
        this.permissions = permissions;
        this.permissionCallback = permissionCallback;
        if (random == null) {
            random = new Random();
        }
        this.requestCode = random.nextInt(255);
    }

    @Override
    public int hashCode() {
        return requestCode;
    }

    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        return object instanceof PermissionRequest && ((PermissionRequest) object).requestCode == this.requestCode;
    }

    public ArrayList<String> getPermissions() {
        return permissions;
    }

    public int getRequestCode() {
        return requestCode;
    }

    public PermissionCallback getPermissionCallback() {
        return permissionCallback;
    }
}