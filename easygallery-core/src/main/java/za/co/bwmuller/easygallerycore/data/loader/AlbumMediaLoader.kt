package za.co.bwmuller.easygallerycore.data.loader

import android.content.Context
import android.database.Cursor
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.loader.app.LoaderManager
import androidx.loader.app.LoaderManager.LoaderCallbacks
import androidx.loader.content.Loader
import za.co.bwmuller.easygallerycore.data.cursor.AlbumMediaCursor
import za.co.bwmuller.easygallerycore.model.Album
import java.lang.ref.WeakReference

/**
 * Created by Bernhard Müller on 8/23/2017.
 */
class AlbumMediaLoader(val activity: FragmentActivity, val callbacks: AlbumCallback) : LoaderCallbacks<Cursor> {

    private var mContext: WeakReference<Context> = WeakReference(activity)
    private var mLoaderManager: LoaderManager = LoaderManager.getInstance(activity)
    private var mCallbacks: AlbumCallback? = callbacks
    private var LOADER_ID = 2

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        val context = mContext.get()
        val album: Album? = args?.getParcelable(ARGS_ALBUM)
        return AlbumMediaCursor.newInstance(context!!, album!!, mCallbacks?.config!!)
    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor) {
        mContext.get() ?: return
        mCallbacks?.onCursorLoad(data)
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
        mContext.get() ?: return
        mCallbacks?.onCursorReset()
    }

    private fun getLoaderId(albumId: String): Int {
        return AlbumMediaLoader::class.java.simpleName.hashCode() + (mCallbacks?.config?.loaderScope?.ordinal ?: 0) + albumId.hashCode()
    }

    fun onDestroy() {
        mLoaderManager.destroyLoader(LOADER_ID)
        mCallbacks = null
    }

    fun load(target: Album) {
        val args = Bundle()
        args.putParcelable(ARGS_ALBUM, target)
        LOADER_ID = getLoaderId(target.bucketId)
        mLoaderManager.initLoader(LOADER_ID, args, this)
    }

    companion object {

        private const val ARGS_ALBUM = "args_album"
    }
}