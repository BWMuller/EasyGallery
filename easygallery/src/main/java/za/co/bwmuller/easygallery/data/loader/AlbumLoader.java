package za.co.bwmuller.easygallery.data.loader;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;

import java.lang.ref.WeakReference;

import za.co.bwmuller.easygallery.data.cursor.AlbumCursor;

/**
 * Created by Bernhard Müller on 8/23/2017.
 */

public class AlbumLoader implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int LOADER_ID = AlbumLoader.class.getSimpleName().hashCode();
    private WeakReference<Context> mContext;
    private LoaderManager mLoaderManager;
    private AlbumCallback mAlbumCallback;

    @Override public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Context context = mContext.get();
        if (context == null) {
            return null;
        }
        return AlbumCursor.newInstance(context, mAlbumCallback.getConfig());
    }

    @Override public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Context context = mContext.get();
        if (context == null) {
            return;
        }

        mAlbumCallback.onCursorLoad(data);
    }

    @Override public void onLoaderReset(Loader<Cursor> loader) {
        Context context = mContext.get();
        if (context == null) {
            return;
        }

        mAlbumCallback.onCursorReset();
    }

    public void onCreate(FragmentActivity activity, AlbumCallback albumCallback) {
        mContext = new WeakReference<Context>(activity);
        mAlbumCallback = albumCallback;
        mLoaderManager = activity.getSupportLoaderManager();
    }

    public void onDestroy() {
        mLoaderManager.destroyLoader(LOADER_ID);
        mAlbumCallback = null;
    }

    public void loadAlbums() {
        mLoaderManager.initLoader(LOADER_ID, null, this);
    }

//    public int getCurrentSelection() {
//        return mCurrentSelection;
//    }
//
//    public void setStateCurrentSelection(int currentSelection) {
//        mCurrentSelection = currentSelection;
//    }
}
