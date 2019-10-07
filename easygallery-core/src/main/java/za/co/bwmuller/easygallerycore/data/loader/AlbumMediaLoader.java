package za.co.bwmuller.easygallerycore.data.loader;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import java.lang.ref.WeakReference;

import za.co.bwmuller.easygallerycore.data.cursor.AlbumMediaCursor;
import za.co.bwmuller.easygallerycore.model.Album;

/**
 * Created by Bernhard Müller on 8/23/2017.
 */

public class AlbumMediaLoader implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String ARGS_ALBUM = "args_album";
    private WeakReference<Context> mContext;
    private LoaderManager mLoaderManager;
    private AlbumCallback mCallbacks;
    private int LOADER_ID = 2;

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Context context = mContext.get();
        if (context == null) {
            return null;
        }

        Album album = args.getParcelable(ARGS_ALBUM);
        if (album == null) {
            return null;
        }

        return AlbumMediaCursor.newInstance(context, album, mCallbacks.getConfig());
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Context context = mContext.get();
        if (context == null) {
            return;
        }

        mCallbacks.onCursorLoad(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Context context = mContext.get();
        if (context == null) {
            return;
        }

        mCallbacks.onCursorReset();
    }

    private int getLoaderId(String albumId) {
        return AlbumMediaLoader.class.getSimpleName().hashCode() + mCallbacks.getConfig().loaderScope.ordinal() + albumId.hashCode();
    }

    public void onCreate(@NonNull FragmentActivity activity, @NonNull AlbumCallback callbacks) {
        mContext = new WeakReference<Context>(activity);
        mLoaderManager = activity.getSupportLoaderManager();
        mCallbacks = callbacks;
    }

    public void onDestroy() {
        mLoaderManager.destroyLoader(LOADER_ID);
        mCallbacks = null;
    }

    public void load(@Nullable Album target) {
        Bundle args = new Bundle();
        args.putParcelable(ARGS_ALBUM, target);
        LOADER_ID = getLoaderId(target.getId());
        mLoaderManager.initLoader(LOADER_ID, args, this);
    }
}
