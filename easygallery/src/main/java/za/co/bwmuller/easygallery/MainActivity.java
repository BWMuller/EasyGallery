package za.co.bwmuller.easygallery;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import za.co.bwmuller.easygallery.ui.EasyGalleryActivty;
import za.co.bwmuller.easypermissions.AppCompatCallbackActivity;
import za.co.bwmuller.easypermissions.PermissionCallback;
import za.co.bwmuller.easypermissions.PermissionsHelper;

public class MainActivity extends AppCompatCallbackActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.example_button).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                PermissionsHelper.requestPermission(MainActivity.this, PermissionsHelper.K_READ_EXTERNAL_STORAGE, new PermissionCallback() {
                    @Override public void permissionGranted() {
                        startActivity(new Intent(getApplicationContext(), EasyGalleryActivty.class));
                    }

                    @Override public void permissionRefused() {

                    }
                });
            }
        });
    }
}
