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
import android.provider.MediaStore.Images
import android.provider.MediaStore.MediaColumns
import android.provider.MediaStore.Video.VideoColumns
import android.webkit.MimeTypeMap
import androidx.loader.content.CursorLoader
import za.co.bwmuller.easygallerycore.Config
import za.co.bwmuller.easygallerycore.Config.Scope.IMAGES
import za.co.bwmuller.easygallerycore.Config.Scope.VIDEOS
import za.co.bwmuller.easygallerycore.model.Album
import za.co.bwmuller.easygallerycore.model.Media
import java.util.ArrayList

/**
 * Load images and videos into a single cursor.
 */
class AlbumMediaCursor private constructor(
    context: Context, selection: String, selectionArgs: Array<String>,   // ===============================================================
    private val mIsAll: Boolean, private val mConfig: Config, private val mAlbum: Album
) : CursorLoader(context, QUERY_URI, PROJECTION, selection, selectionArgs, ORDER_BY) {

    // ===============================================================
    override fun loadInBackground(): Cursor {
        return if (mAlbum.isCustomAlbum) {
            val mediaList = ArrayList<Media>()
            mediaList.addAll(mConfig.mediaLoader?.customAlbumMedia(mAlbum) ?: emptyList())
            mediaList.sortWith { o1, o2 -> Math.min(Math.max(o2.dateTaken.toDouble().compareTo(o1.dateTaken.toDouble()), -1), 1) }
            val sortedMedia = MatrixCursor(COLUMNS)
            for (media in mediaList) {
                sortedMedia.addRow(media.toCursorData())
            }
            sortedMedia
        } else {
            val mediaCursor = super.loadInBackground()
            val mediaList = ArrayList<Media>()
            if (mIsAll) {
                mediaList.addAll(mConfig.mediaLoader?.allMedia() ?: emptyList())
            }
            if (mediaCursor != null) {
                while (mediaCursor.moveToNext()) {
                    mediaList.add(Media.fromDevice(mediaCursor))
                }
            }
            mediaList.sortWith { o1, o2 -> Math.min(Math.max(o2.dateTaken.toDouble().compareTo(o1.dateTaken.toDouble()), -1), 1) }
            val sortedMedia = MatrixCursor(COLUMNS)
            for (media in mediaList) {
                sortedMedia.addRow(media.toCursorData())
            }
            sortedMedia
        }
    }

    override fun onContentChanged() {
    }

    companion object {

        const val COLUMN_URL = "custom_url"
        const val CUSTOM_ID = "custom_id"
        private val QUERY_URI = Files.getContentUri("external")
        private val COLUMNS = arrayOf(
            Images.Media.BUCKET_ID,
            FileColumns._ID,
            CUSTOM_ID,
            MediaColumns.DISPLAY_NAME,
            COLUMN_URL,
            MediaColumns.MIME_TYPE,
            MediaColumns.SIZE,
            VideoColumns.DURATION,
            Images.Media.DATE_TAKEN
        )
        private val PROJECTION = arrayOf(
            Images.Media.BUCKET_ID,
            FileColumns._ID,
            MediaColumns.DISPLAY_NAME,
            MediaColumns.MIME_TYPE,
            MediaColumns.SIZE,
            VideoColumns.DURATION,
            Images.Media.DATE_TAKEN
        )

        // === params for album ALL && showSingleMediaType: false ===
        private const val SELECTION_ALL = ("(" + FileColumns.MEDIA_TYPE + "=?"
                + " OR "
                + FileColumns.MEDIA_TYPE + "=?)"
                + " AND " + MediaColumns.SIZE + ">0")
        private val SELECTION_ALL_ARGS = arrayOf(FileColumns.MEDIA_TYPE_IMAGE.toString(), FileColumns.MEDIA_TYPE_VIDEO.toString())
        private val SELECTION_NO_ARGS = arrayOf<String>()

        // ===========================================================
        // === album custom ===
        private const val SELECTION_ALL_FOR_CUSTOM_ALBUM = (MediaColumns.SIZE + "== 0"
                + " AND " + MediaColumns.SIZE + ">0")

        // === params for album ALL && showSingleMediaType: true ===
        private const val SELECTION_ALL_FOR_IMAGE_MEDIA_TYPE = (" ( "
                + FileColumns.MEDIA_TYPE + "=?"
                + " AND NOT " + Images.Media.MIME_TYPE + "=?"
                + " ) AND " + MediaColumns.SIZE + ">0")
        private const val SELECTION_ALL_FOR_VIDEO_MEDIA_TYPE = (FileColumns.MEDIA_TYPE + "=?"
                + " AND " + MediaColumns.SIZE + ">0")

        // === params for ordinary album && showSingleMediaType: false ===
        private const val SELECTION_ALBUM = ("("
                + " ( " + FileColumns.MEDIA_TYPE + "=?"
                + " AND NOT " + Images.Media.MIME_TYPE + "=?)"
                + " OR "
                + FileColumns.MEDIA_TYPE + "=?)"
                + " AND "
                + " " + AlbumCursor.BUCKET_ID + "=?"
                + " AND " + MediaColumns.SIZE + ">0")

        // =========================================================
        // === params for ordinary album && showSingleMediaType: true ===
        private const val SELECTION_ALBUM_FOR_IMAGE_MEDIA_TYPE = (SELECTION_ALL_FOR_IMAGE_MEDIA_TYPE
                + " AND " + AlbumCursor.BUCKET_ID + "=?")
        private const val SELECTION_ALBUM_FOR_VIDEO_MEDIA_TYPE = (SELECTION_ALL_FOR_VIDEO_MEDIA_TYPE
                + " AND " + AlbumCursor.BUCKET_ID + "=?")
        private const val ORDER_BY = Images.Media.DATE_TAKEN + " DESC"
        private fun getSelectionArgsForSingleMediaType(mediaType: Int): Array<String> {
            return if (FileColumns.MEDIA_TYPE_IMAGE == mediaType) {
                arrayOf(
                    mediaType.toString(),
                    MimeTypeMap.getSingleton().getMimeTypeFromExtension("gif") ?: ""
                )
            } else {
                arrayOf(mediaType.toString())
            }
        }

        private fun getSelectionAlbumArgs(albumId: String): Array<String> {
            return arrayOf(
                FileColumns.MEDIA_TYPE_IMAGE.toString(),
                MimeTypeMap.getSingleton().getMimeTypeFromExtension("gif") ?: "", FileColumns.MEDIA_TYPE_VIDEO.toString(),
                albumId
            )
        }

        private fun getSelectionAlbumArgsForSingleMediaType(mediaType: Int, albumId: String): Array<String> {
            return if (FileColumns.MEDIA_TYPE_IMAGE == mediaType) {
                arrayOf(
                    mediaType.toString(),
                    MimeTypeMap.getSingleton().getMimeTypeFromExtension("gif") ?: "",
                    albumId
                )
            } else {
                arrayOf(mediaType.toString(), albumId)
            }
        }

        fun newInstance(context: Context, album: Album, config: Config): CursorLoader {
            var selection: String
            val selectionArgs: Array<String>
            if (album.isAllMedia) {
                when {
                    config.loaderScope === IMAGES -> {
                        selection = SELECTION_ALL_FOR_IMAGE_MEDIA_TYPE
                        selectionArgs = getSelectionArgsForSingleMediaType(FileColumns.MEDIA_TYPE_IMAGE)
                    }
                    config.loaderScope === VIDEOS -> {
                        selection = SELECTION_ALL_FOR_VIDEO_MEDIA_TYPE
                        selectionArgs = getSelectionArgsForSingleMediaType(FileColumns.MEDIA_TYPE_VIDEO)
                    }
                    else -> {
                        selection = SELECTION_ALL
                        selectionArgs = SELECTION_ALL_ARGS
                    }
                }
            } else {
                when {
                    album.isCustomAlbum -> {
                        selection = SELECTION_ALL_FOR_CUSTOM_ALBUM
                        selectionArgs = SELECTION_NO_ARGS
                    }
                    config.loaderScope === IMAGES -> {
                        selection = SELECTION_ALBUM_FOR_IMAGE_MEDIA_TYPE
                        selectionArgs = getSelectionAlbumArgsForSingleMediaType(
                            FileColumns.MEDIA_TYPE_IMAGE,
                            album.id
                        )
                    }
                    config.loaderScope === VIDEOS -> {
                        selection = SELECTION_ALBUM_FOR_VIDEO_MEDIA_TYPE
                        selectionArgs = getSelectionAlbumArgsForSingleMediaType(
                            FileColumns.MEDIA_TYPE_VIDEO,
                            album.id
                        )
                    }
                    else -> {
                        selection = SELECTION_ALBUM
                        selectionArgs = getSelectionAlbumArgs(album.id)
                    }
                }
            }
            if (config.excludeDirectories.isNotEmpty()) {
                var tmp = ""
                for (excludeDirectory in config.excludeDirectories) {
                    tmp += (if (tmp.isEmpty()) "" else " AND ") + Images.Media.DATA + " not like '%" + excludeDirectory + "%' "
                }
                selection = " ( $tmp ) AND $selection"
            }
            return AlbumMediaCursor(context, selection, selectionArgs, album.isAllMedia, config, album)
        }
    }
}