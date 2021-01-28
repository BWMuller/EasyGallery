package za.co.bwmuller.easypermissions

import androidx.appcompat.app.AppCompatActivity
import java.util.ArrayList

/**
 * Created by Bernhard Müller on 8/23/2017.
 */
open class AppCompatCallbackActivity : AppCompatActivity() {

    var listenerList = ArrayList<ActivityListener>()
    fun addListener(listener: ActivityListener) {
        listenerList.add(listener)
    }

    fun removeListener(listener: ActivityListener): Boolean {
        return listenerList.remove(listener)
    }
}