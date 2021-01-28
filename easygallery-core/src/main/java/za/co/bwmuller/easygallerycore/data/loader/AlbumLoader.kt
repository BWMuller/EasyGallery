package za.co.bwmuller.easygallerycore.data.loader

import android.content.Context
import android.database.Cursor
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.loader.app.LoaderManager
import androidx.loader.app.LoaderManager.LoaderCallbacks
import androidx.loader.content.Loader
import za.co.bwmuller.easygallerycore.data.cursor.AlbumCursor.Companion.newInstance
import java.lang.ref.WeakReference

/**
 * Created by Bernhard Müller on 8/23/2017.
 */
class AlbumLoader(activity: FragmentActivity, albumCallback: AlbumCallback?) : LoaderCallbacks<Cursor> {

    private var mContext: WeakReference<Context> = WeakReference(activity)
    private var mLoaderManager: LoaderManager = LoaderManager.getInstance(activity)
    private var mAlbumCallback: AlbumCallback? = albumCallback

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        return newInstance(mContext.get()!!, mAlbumCallback?.config!!)
    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor) {
        val context = mContext.get() ?: return
        mAlbumCallback?.onCursorLoad(data)
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
        val context = mContext.get() ?: return
        mAlbumCallback?.onCursorReset()
    }

    fun onDestroy() {
        mLoaderManager.destroyLoader(loaderId)
        mAlbumCallback = null
    }

    private val loaderId: Int
        get() = AlbumLoader::class.java.simpleName.hashCode() + (mAlbumCallback?.config?.loaderScope?.ordinal ?: 0)

    fun loadAlbums() {
        mLoaderManager.initLoader(loaderId, null, this)
    }
}