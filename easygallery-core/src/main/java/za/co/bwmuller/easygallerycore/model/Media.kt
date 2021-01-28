package za.co.bwmuller.easygallerycore.model

import android.database.Cursor
import android.net.Uri
import android.os.Parcel
import android.os.Parcelable.Creator
import android.provider.MediaStore.Files.FileColumns
import android.provider.MediaStore.Images
import android.provider.MediaStore.MediaColumns
import android.provider.MediaStore.Video
import android.text.TextUtils
import za.co.bwmuller.easygallerycore.data.cursor.AlbumMediaCursor
import za.co.bwmuller.easygallerycore.utils.ContentUriUtil.getPath
import java.util.Locale

/**
 * Created by Bernhard Müller on 8/23/2017.
 */
class Media {
    val bucketId: String
    val dbId: String
    val id: Long
    private val displayName: String
    private val mimeType: String
    val contentUri: Uri
    private val size: Long

    // only for video, in ms
    private val duration: Long
    val dateTaken: Long
    private val custom: Boolean

    private constructor(bucketId: String, id: Long, displayName: String, mimeType: String, size: Long, duration: Long, dateTaken: Long) : this(
        bucketId,
        id,
        "",
        displayName,
        mimeType,
        "",
        size,
        duration,
        dateTaken
    )

    private constructor(bucketId: String, id: Long, displayName: String, mimeType: String, uri: Uri?, size: Long, duration: Long, dateTaken: Long) : this(
        bucketId,
        0,
        String.format(Locale.ROOT, "custom_%s_%d", bucketId, id),
        displayName,
        mimeType,
        uri.toString(),
        size,
        duration,
        dateTaken
    )

    constructor(bucketId: String, id: Long, dbId: String, displayName: String, mimeType: String, uri: String?, size: Long, duration: Long, dateTaken: Long) {
        this.bucketId = bucketId
        if (TextUtils.isEmpty(uri)) {
            custom = false
            this.dbId = id.toString()
            contentUri = getPath(mimeType, id)
            this.id = id
        } else {
            custom = true
            this.dbId = dbId
            contentUri = Uri.parse(uri)
            this.id = dbId.hashCode().toLong()
        }
        this.displayName = displayName
        this.mimeType = mimeType
        this.size = size
        this.duration = duration
        this.dateTaken = dateTaken
    }

    protected constructor(`in`: Parcel) {
        bucketId = `in`.readString() ?: ""
        id = `in`.readLong()
        dbId = `in`.readString() ?: ""
        displayName = `in`.readString() ?: ""
        mimeType = `in`.readString() ?: ""
        contentUri = `in`.readParcelable(Uri::class.java.classLoader) ?: Uri.EMPTY
        size = `in`.readLong()
        duration = `in`.readLong()
        dateTaken = `in`.readLong()
        custom = dbId.startsWith("custom")
    }

    override fun hashCode(): Int {
        var result = 1
        result = 31 * result + (bucketId + "").hashCode()
        result = 31 * result + (dbId + "").hashCode()
        result = 31 * result + java.lang.Long.valueOf(id).hashCode()
        result = 31 * result + (displayName + "").hashCode()
        result = 31 * result + (mimeType + "").hashCode()
        result = 31 * result + (contentUri.toString() + "").hashCode()
        result = 31 * result + java.lang.Long.valueOf(size).hashCode()
        result = 31 * result + java.lang.Long.valueOf(duration).hashCode()
        result = 31 * result + java.lang.Long.valueOf(dateTaken).hashCode()
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (other !is Media) {
            return false
        }
        return (id == other.id && (bucketId == other.bucketId)
                && (dbId == other.dbId)
                && (displayName == other.displayName)
                && (mimeType == other.mimeType)
                && (contentUri == other.contentUri)
                && size == other.size && duration == other.duration && dateTaken == other.dateTaken)
    }

    val customId: Long
        get() = if (dbId.startsWith("custom")) dbId.substring(String.format(Locale.ROOT, "custom_%s_", bucketId).length).toLong() else id
    val isCapture: Boolean
        get() = id == ITEM_ID_CAPTURE

    fun toCursorData(): Array<String?> {
        return arrayOf(
            bucketId, id.toString(), dbId,
            displayName,
            if (custom) contentUri.toString() else "",
            mimeType, size.toString(), duration.toString(), dateTaken.toString()
        )
    }

    class Builder(private val bucketId: String) {

        private var id: Long = 0
        private var displayName: String = ""
        private var mimeType: String = ""
        private var uri: Uri = Uri.EMPTY
        private var size: Long = 0

        // only for video, in ms
        private var duration: Long = 0
        private var dateTaken: Long = 0
        fun build(): Media {
            return Media(bucketId, id, displayName, mimeType, uri, size, duration, dateTaken)
        }

        fun setId(id: Long): Builder {
            this.id = id
            return this
        }

        fun setDisplayName(displayName: String): Builder {
            this.displayName = displayName
            return this
        }

        fun setMimeType(mimeType: String): Builder {
            this.mimeType = mimeType
            return this
        }

        fun setUri(uri: Uri): Builder {
            this.uri = uri
            return this
        }

        fun setUri(uriString: String?): Builder {
            uri = Uri.parse(uriString)
            return this
        }

        fun setSize(size: Long): Builder {
            this.size = size
            return this
        }

        fun setDuration(duration: Long): Builder {
            this.duration = duration
            return this
        }

        fun setDateTaken(dateTaken: Long): Builder {
            this.dateTaken = dateTaken
            return this
        }
    }

    companion object {

        const val ITEM_ID_CAPTURE: Long = -1
        val CREATOR: Creator<Media> = object : Creator<Media> {
            override fun createFromParcel(source: Parcel): Media {
                return Media(source)
            }

            override fun newArray(size: Int): Array<Media?> {
                return arrayOfNulls(size)
            }
        }

        @JvmStatic
        fun from(cursor: Cursor): Media {
            return Media(
                cursor.getString(cursor.getColumnIndex(Images.Media.BUCKET_ID)) ?: "",
                cursor.getLong(cursor.getColumnIndex(FileColumns._ID)),
                cursor.getString(cursor.getColumnIndex(AlbumMediaCursor.CUSTOM_ID)) ?: "",
                cursor.getString(cursor.getColumnIndex(MediaColumns.DISPLAY_NAME)) ?: "",
                cursor.getString(cursor.getColumnIndex(MediaColumns.MIME_TYPE)) ?: "",
                cursor.getString(cursor.getColumnIndex(AlbumMediaCursor.COLUMN_URL)) ?: "",
                cursor.getLong(cursor.getColumnIndex(MediaColumns.SIZE)),
                cursor.getLong(cursor.getColumnIndex(Video.Media.DURATION)),
                cursor.getLong(cursor.getColumnIndex(Images.Media.DATE_TAKEN))
            )
        }

        fun fromDevice(cursor: Cursor): Media {
            return Media(
                cursor.getString(cursor.getColumnIndex(Images.Media.BUCKET_ID)) ?: "",
                cursor.getLong(cursor.getColumnIndex(FileColumns._ID)),
                "",
                cursor.getString(cursor.getColumnIndex(MediaColumns.DISPLAY_NAME)) ?: "",
                cursor.getString(cursor.getColumnIndex(MediaColumns.MIME_TYPE)) ?: "",
                "",
                cursor.getLong(cursor.getColumnIndex(MediaColumns.SIZE)),
                cursor.getLong(cursor.getColumnIndex(Video.Media.DURATION)),
                cursor.getLong(cursor.getColumnIndex(Images.Media.DATE_TAKEN))
            )
        }

        fun newBuilder(bucketId: Long): Builder {
            return Builder(bucketId.toString())
        }

        @JvmStatic
        fun newBuilder(bucketId: String): Builder {
            return Builder(bucketId)
        }
    }
}