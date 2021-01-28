package za.co.bwmuller.easypermissions

import android.os.Bundle

/**
 * Created by Bernhard Müller on 10/14/2016.
 */
abstract class PermissionListener : ActivityListener {

    override fun onCreate(savedInstanceState: Bundle?) {}
    override fun onPostCreate(savedInstanceState: Bundle?) {}
    override fun onResume() {}
    override fun onPostResume() {}
    override fun onStart() {}
    override fun onStop() {}
    override fun onPause() {}
    override fun onDestroy() {}
}