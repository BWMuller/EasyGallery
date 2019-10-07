package za.co.bwmuller.easypermissions;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by Bernhard Müller on 10/14/2016.
 */
public interface ActivityListener {
    void onCreate(@Nullable Bundle savedInstanceState);

    void onPostCreate(@Nullable Bundle savedInstanceState);
    void onResume();
    void onPostResume();
    void onStart();
    void onStop();
    void onPause();
    void onDestroy();

    void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults);

    Activity getActivity();
}