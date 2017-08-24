package za.co.bwmuller.easygallery.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.facebook.drawee.backends.pipeline.Fresco;

import za.co.bwmuller.easygallery.R;
import za.co.bwmuller.easygallery.ui.album.AlbumFragment;
import za.co.bwmuller.easygallery.ui.media.MediaFragment;
import za.co.bwmuller.easygallerycore.Config;
import za.co.bwmuller.easygallerycore.model.Album;
import za.co.bwmuller.easygallerycore.model.Media;

public class EasyGalleryActivty extends AppCompatActivity implements AlbumFragment.OnAlbumFragmentListener, MediaFragment.OnMediaFragmentListener {

    @Override public Config getConfig() {
        return new Config() {{
            mediaGridColumnCount = 3;
            albumGridColumnCount = 2;
            loaderScope = Scope.IMAGES;
        }}.addExcludedDirectory("/Kalido/");
    }

    @Override public void onAlbumSelected(Album item) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content, MediaFragment.newInstance(item), item.getId())
                .commit();
    }

    @Override public void onBackPressed() {
        if (!getSupportFragmentManager().popBackStackImmediate())
            super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_easy_gallery_activty);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content, AlbumFragment.newInstance(), AlbumFragment.class.getSimpleName())
                .commit();
    }

    @Override public void onMediaSelected(Media item) {
    }
}
