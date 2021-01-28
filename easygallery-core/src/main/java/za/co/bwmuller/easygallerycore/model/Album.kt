package za.co.bwmuller.easygallerycore.model

import android.database.Cursor
import android.net.Uri
import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator
import android.provider.MediaStore.Files.FileColumns
import android.provider.MediaStore.Images.Media
import android.provider.MediaStore.MediaColumns
import kotlinx.parcelize.Parcelize
import za.co.bwmuller.easygallerycore.Config
import za.co.bwmuller.easygallerycore.data.cursor.AlbumCursor
import za.co.bwmuller.easygallerycore.utils.ContentUriUtil.getPath

/**
 * Created by Bernhard Müller on 8/23/2017.
 */
@Parcelize
class Album(
    val dbId: String,
    val bucketId: String,
    val name: String?,
    var coverUri: Uri? = null,
    var coverMimeType: String = "",
    var coverId: Long = 0L,
    val dateTaken: Long = 0L,
    var count: Long = 0L,
    val customAlbum: Boolean = false,
    val video: Boolean = false,
) : Parcelable {

    init {
        if (coverUri == null) {
            this.coverMimeType = coverMimeType ?: ""
            this.coverId = coverId ?: 0L
            if (customAlbum || ALBUM_ALL_ID == bucketId) {
                this.coverUri = Uri.parse(coverMimeType)
            } else {
                this.coverUri = getPath(coverMimeType, coverId)
            }
        } else {
            this.coverMimeType = coverUri.toString()
            this.coverId = 0
            this.coverUri = coverUri ?: Uri.EMPTY
        }
    }

    val isCustomAlbum: Boolean
        get() = customAlbum
    val isAllMedia: Boolean
        get() = ALBUM_ALL_ID == bucketId

    fun getDisplayName(mConfig: Config): String {
        return if (isAllMedia) mConfig.allMedia else name ?: ""
    }

    fun toCursorData(): Array<String?> {
        return arrayOf(
            coverId.toString(),
            bucketId,
            name,
            coverMimeType, dateTaken.toString(), count.toString(), customAlbum.toString()
        )
    }

    val coverPath: String
        get() = coverUri.toString()

    fun addCount() {
        count++
    }

    class Builder {

        private var dbId = ""
        private var bucketId = ""
        private var coverUri: Uri? = null
        private var count: Long = 0
        private var name: String? = null
        private var video = false
        private var coverMimeType: String = ""
        private var coverId: Long = 0
        private var dateTaken: Long = 0
        private var customAlbum = false
        fun build(): Album {
            return Album(dbId, bucketId, name, coverMimeType = coverMimeType, coverId = coverId, dateTaken = dateTaken, count = count, customAlbum = customAlbum, video = video)
        }

        fun setBucketId(id: String): Builder {
            dbId = id + "_custom"
            bucketId = id
            return this
        }

        fun setDbId(dbId: String): Builder {
            this.dbId = dbId
            return this
        }

        fun setCoverUri(coverUri: Uri?): Builder {
            this.coverUri = coverUri
            return this
        }

        fun setCount(count: Long): Builder {
            this.count = count
            return this
        }

        fun setName(name: String?): Builder {
            this.name = name
            return this
        }

        fun setVideo(video: Boolean): Builder {
            this.video = video
            return this
        }

        fun setCoverMimeType(coverMimeType: String): Builder {
            this.coverMimeType = coverMimeType
            return this
        }

        fun setCoverId(coverId: Long): Builder {
            this.coverId = coverId
            return this
        }

        fun setDateTaken(dateTaken: Long): Builder {
            this.dateTaken = dateTaken
            return this
        }

        fun setCustomAlbum(customAlbum: Boolean): Builder {
            this.customAlbum = customAlbum
            return this
        }
    }

    companion object {

        val ALBUM_ALL_ID: String = "-1"

        fun createAllAlbumEntry(coverPath: String, coverId: Long, dateTaken: Long, count: Int): Array<String> {
            return arrayOf(
                coverId.toString(),
                ALBUM_ALL_ID,
                "",
                coverPath, dateTaken.toString(), count.toString(),
                java.lang.Boolean.FALSE.toString()
            )
        }

        @JvmStatic
        fun from(cursor: Cursor): Album {
            return Album(
                cursor.getString(cursor.getColumnIndex(FileColumns._ID)) ?: "",
                cursor.getString(cursor.getColumnIndex(AlbumCursor.BUCKET_ID)) ?: "",
                cursor.getString(cursor.getColumnIndex(AlbumCursor.BUCKET_DISPLAY_NAME)),
                coverMimeType = cursor.getString(cursor.getColumnIndex(MediaColumns.DATA)) ?: "",
                coverId = cursor.getLong(cursor.getColumnIndex(MediaColumns._ID)),
                dateTaken = cursor.getLong(cursor.getColumnIndex(Media.DATE_TAKEN)),
                count = if (cursor.getColumnIndex(AlbumCursor.COLUMN_COUNT) >= 0) cursor.getLong(cursor.getColumnIndex(AlbumCursor.COLUMN_COUNT)) else 0,
                customAlbum = cursor.getColumnIndex(AlbumCursor.CUSTOM_ALBUM) >= 0 && java.lang.Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(AlbumCursor.CUSTOM_ALBUM)))
            )
        }

        @JvmStatic
        fun createCustom(id: String, displayName: String?, contentUri: Uri, dateTaken: Long, mediaCount: Int): Album {
            return Album(id, id, displayName, contentUri, dateTaken = dateTaken, count = mediaCount.toLong(), customAlbum = true)
        }
    }
}