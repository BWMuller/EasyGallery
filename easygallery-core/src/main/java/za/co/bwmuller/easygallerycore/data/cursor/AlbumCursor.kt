/*
 * Copyright (C) 2014 nohana, Inc.
 * Copyright 2017 Zhihu Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an &quot;AS IS&quot; BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package za.co.bwmuller.easygallerycore.data.cursor

import android.content.Context
import android.database.Cursor
import android.database.MatrixCursor
import android.provider.MediaStore.Files
import android.provider.MediaStore.Files.FileColumns
import android.provider.MediaStore.Images.Media
import android.provider.MediaStore.MediaColumns
import android.webkit.MimeTypeMap
import androidx.loader.content.CursorLoader
import za.co.bwmuller.easygallerycore.Config
import za.co.bwmuller.easygallerycore.Config.Scope.IMAGES
import za.co.bwmuller.easygallerycore.Config.Scope.VIDEOS
import za.co.bwmuller.easygallerycore.model.Album
import java.util.ArrayList
import java.util.HashMap

/**
 * Load all albums (grouped by bucket_id) into a single cursor.
 */
class AlbumCursor private constructor(
    context: Context, selection: String, selectionArgs: Array<String?>,
    private val mConfig: Config
) : CursorLoader(context, QUERY_URI, PROJECTION, selection, selectionArgs, BUCKET_ORDER_BY) {

    override fun loadInBackground(): Cursor? {
        val albumCursor = super.loadInBackground()
        val prefixAlbums = mConfig.albumLoader?.prefixAlbums()?.takeIf { it.isNotEmpty() } ?: ArrayList<Album>()
        val postfixAlbums = mConfig.albumLoader?.postfixAlbums()?.takeIf { it.isNotEmpty() } ?: ArrayList<Album>()

        val sortedAlbums = MatrixCursor(COLUMNS)
        var totalCount = 0
        var allAlbumCoverId: Long = -1
        var allAlbumCoverPath = ""
        var dateTaken: Long = 0
        val albums = ArrayList<Album>()
        if (albumCursor != null) {
            val albumList = HashMap<String, Album>()
            while (albumCursor.moveToNext()) {
                val cursorAlbum = Album.from(albumCursor)
                var album = albumList[cursorAlbum.id]
                if (album == null) {
                    album = cursorAlbum
                    albumList[album.id] = album
                    val date = album.dateTaken
                    if (dateTaken < date) {
                        dateTaken = date
                        allAlbumCoverId = album.coverId
                        allAlbumCoverPath = album.coverPath
                    }
                }
                album?.addCount()
            }
            for (item in albumList.values) {
                albums.add(item)
                totalCount += item.count.toInt()
            }
        }
        for (prefixAlbum in prefixAlbums) {
            totalCount += prefixAlbum.count.toInt()
            val date = prefixAlbum.dateTaken
            if (dateTaken < date) {
                dateTaken = date
                allAlbumCoverId = prefixAlbum.coverId
                allAlbumCoverPath = prefixAlbum.coverPath
            }
        }
        for (postfixAlbum in postfixAlbums) {
            totalCount += postfixAlbum.count.toInt()
            val date = postfixAlbum.dateTaken
            if (dateTaken < date) {
                dateTaken = date
                allAlbumCoverId = postfixAlbum.coverId
                allAlbumCoverPath = postfixAlbum.coverPath
            }
        }
        albums.sortWith { o1, o2 -> o1.name?.compareTo(o2.name ?: "")?.coerceIn(-1, 1) ?: 0 }
        sortedAlbums.addRow(Album.createAllAlbumEntry(allAlbumCoverPath, allAlbumCoverId, dateTaken, totalCount))
        for (customAlbum in prefixAlbums) {
            sortedAlbums.addRow(customAlbum.toCursorData())
        }
        for (album in albums) {
            sortedAlbums.addRow(album.toCursorData())
        }
        for (customAlbum in postfixAlbums) {
            sortedAlbums.addRow(customAlbum.toCursorData())
        }
        return sortedAlbums
    }

    override fun onContentChanged() {
        // FIXME a dirty way to fix loading multiple times
    }

    companion object {

        const val CUSTOM_ALBUM = "custom_album"
        const val COLUMN_COUNT = "count"
        const val BUCKET_ID = Media.BUCKET_ID
        const val BUCKET_DISPLAY_NAME = Media.BUCKET_DISPLAY_NAME
        private val QUERY_URI = Files.getContentUri("external")
        private const val BUCKET_ORDER_BY = Media.DATE_TAKEN + " DESC"
        private val COLUMNS = arrayOf(
            FileColumns._ID,
            BUCKET_ID,
            BUCKET_DISPLAY_NAME,
            MediaColumns.DATA,
            Media.DATE_TAKEN,
            COLUMN_COUNT,
            CUSTOM_ALBUM
        )
        private val PROJECTION = arrayOf(
            FileColumns._ID,
            BUCKET_ID,
            BUCKET_DISPLAY_NAME,
            MediaColumns.DATA,
            Media.DATE_TAKEN
        )

        // === params for showSingleMediaType: false ===
        private const val SELECTION = (" ( "
                + " ( " + FileColumns.MEDIA_TYPE + "=?"
                + " AND NOT " + FileColumns.MEDIA_TYPE + "=?)"
                + " OR " + FileColumns.MEDIA_TYPE + "=?)"
                + " AND " + MediaColumns.SIZE + ">0")
        private val SELECTION_ARGS = arrayOf(
            FileColumns.MEDIA_TYPE_IMAGE.toString(),
            MimeTypeMap.getSingleton().getMimeTypeFromExtension("gif"), FileColumns.MEDIA_TYPE_VIDEO.toString()
        )

        // =============================================
        // === params for showSingleMediaType: true ===
        private const val SELECTION_FOR_IMAGE_MEDIA_TYPE = (" ( "
                + FileColumns.MEDIA_TYPE + "=?"
                + " AND NOT " + Media.MIME_TYPE + "=?"
                + " ) AND " + MediaColumns.SIZE + ">0")
        private const val SELECTION_FOR_VIDEO_MEDIA_TYPE = (FileColumns.MEDIA_TYPE + "=?"
                + " AND " + MediaColumns.SIZE + ">0")

        private fun getSelectionArgsForSingleMediaType(mediaType: Int): Array<String?> {
            return if (FileColumns.MEDIA_TYPE_IMAGE == mediaType) {
                arrayOf(
                    mediaType.toString(),
                    MimeTypeMap.getSingleton().getMimeTypeFromExtension("gif")
                )
            } else {
                arrayOf(mediaType.toString())
            }
        }

        @JvmStatic
        fun newInstance(context: Context, config: Config): CursorLoader {
            var selection: String
            val selectionArgs: Array<String?>
            if (config.loaderScope == IMAGES) {
                selection = SELECTION_FOR_IMAGE_MEDIA_TYPE
                selectionArgs = getSelectionArgsForSingleMediaType(FileColumns.MEDIA_TYPE_IMAGE)
            } else if (config.loaderScope == VIDEOS) {
                selection = SELECTION_FOR_VIDEO_MEDIA_TYPE
                selectionArgs = getSelectionArgsForSingleMediaType(FileColumns.MEDIA_TYPE_VIDEO)
            } else {
                selection = SELECTION
                selectionArgs = SELECTION_ARGS
            }
            if (!config.excludeDirectories.isEmpty()) {
                var tmp = ""
                for (excludeDirectory in config.excludeDirectories) {
                    tmp += (if (tmp.isEmpty()) "" else " AND ") + Media.DATA + " not like '%" + excludeDirectory + "%' "
                }
                selection = " ( $tmp ) AND $selection"
            }
            return AlbumCursor(context, selection, selectionArgs, config)
        }
    }
}