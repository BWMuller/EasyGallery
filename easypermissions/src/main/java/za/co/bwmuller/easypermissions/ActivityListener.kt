package za.co.bwmuller.easypermissions

import android.os.Bundle
import android.app.Activity

/**
 * Created by Bernhard Müller on 10/14/2016.
 */
interface ActivityListener {

    fun onCreate(savedInstanceState: Bundle?)
    fun onPostCreate(savedInstanceState: Bundle?)
    fun onResume()
    fun onPostResume()
    fun onStart()
    fun onStop()
    fun onPause()
    fun onDestroy()
    fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String?>,
        grantResults: IntArray
    )

    val activity: Activity?
}