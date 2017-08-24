package za.co.bwmuller.easypermissions;

import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

/**
 * Created by Bernhard Müller on 8/23/2017.
 */

public class AppCompatCallbackActivity extends AppCompatActivity {
    ArrayList<ActivityListener> listenerList = new ArrayList<>();

    public void addListener(ActivityListener listener) {
        listenerList.add(listener);
    }

    public boolean removeListener(ActivityListener listener) {
        return listenerList.remove(listener);
    }
}
