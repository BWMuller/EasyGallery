package za.co.bwmuller.easygallerycore.data.loader

import android.database.Cursor
import za.co.bwmuller.easygallerycore.Config

/**
 * Created by Bernhard Müller on 8/23/2017.
 */
interface AlbumCallback {

    fun onCursorLoad(cursor: Cursor)
    fun onCursorReset()
    val config: Config
}