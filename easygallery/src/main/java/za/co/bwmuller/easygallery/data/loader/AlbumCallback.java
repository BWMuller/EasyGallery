package za.co.bwmuller.easygallery.data.loader;

import android.database.Cursor;

import za.co.bwmuller.easygallery.Config;

/**
 * Created by Bernhard Müller on 8/23/2017.
 */

public interface AlbumCallback {
    void onCursorLoad(Cursor cursor);

    void onCursorReset();

    Config getConfig();
}
