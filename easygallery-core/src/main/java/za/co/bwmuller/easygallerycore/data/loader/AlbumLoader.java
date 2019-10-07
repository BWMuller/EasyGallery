package za.co.bwmuller.easygallerycore.data.loader;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import java.lang.ref.WeakReference;

import za.co.bwmuller.easygallerycore.data.cursor.AlbumCursor;

/**
 * Created by Bernhard Müller on 8/23/2017.
 */

public class AlbumLoader implements LoaderManager.LoaderCallbacks<Cursor> {
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
        mLoaderManager.destroyLoader(getLoaderId());
        mAlbumCallback = null;
    }

    private int getLoaderId() {
        return AlbumLoader.class.getSimpleName().hashCode() + mAlbumCallback.getConfig().loaderScope.ordinal();
    }

    public void loadAlbums() {
        mLoaderManager.initLoader(getLoaderId(), null, this);
    }

//    public int getCurrentSelection() {
//        return mCurrentSelection;
//    }
//
//    public void setStateCurrentSelection(int currentSelection) {
//        mCurrentSelection = currentSelection;
//    }
}
