package za.co.bwmuller.easygallerycore.model;

import android.database.Cursor;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.text.TextUtils;

import java.util.Locale;

import za.co.bwmuller.easygallerycore.data.cursor.AlbumMediaCursor;
import za.co.bwmuller.easygallerycore.utils.ContentUriUtil;

/**
 * Created by Bernhard Müller on 8/23/2017.
 */

public class Media implements Parcelable {
    public static final long ITEM_ID_CAPTURE = -1;
    public static final Parcelable.Creator<Media> CREATOR = new Parcelable.Creator<Media>() {
        @Override public Media createFromParcel(Parcel source) {
            return new Media(source);
        }

        @Override public Media[] newArray(int size) {
            return new Media[size];
        }
    };
    private final String bucketId;
    private final String dbId;
    private final long id;
    private final String displayName;
    private final String mimeType;
    private final Uri uri;
    private final long size;
    private final long duration; // only for video, in ms
    private final long dateTaken;
    private final boolean custom;

    private Media(String bucketId, long id, String displayName, String mimeType, long size, long duration, long dateTaken) {
        this.custom = false;
        this.bucketId = bucketId;
        this.dbId = String.valueOf(id);
        this.id = id;
        this.displayName = displayName;
        this.mimeType = mimeType;
        this.uri = ContentUriUtil.getPath(mimeType, id);
        this.size = size;
        this.duration = duration;
        this.dateTaken = dateTaken;
    }

    private Media(String bucketId, long id, String displayName, String mimeType, Uri uri, long size, long duration, long dateTaken) {
        this.custom = true;
        this.bucketId = bucketId;
        this.dbId = String.format(Locale.getDefault(), "custom_%s_%d", bucketId, id);
        this.id = dbId.hashCode();
        this.displayName = displayName;
        this.mimeType = mimeType;
        this.uri = uri;
        this.size = size;
        this.duration = duration;
        this.dateTaken = dateTaken;
    }

    public Media(String bucketId, long id, String dbId, String displayName, String mimeType, String uri, long size, long duration, long dateTaken) {
        this.bucketId = bucketId;
        if (TextUtils.isEmpty(uri)) {
            this.custom = false;
            this.dbId = String.valueOf(id);
            this.uri = ContentUriUtil.getPath(mimeType, id);
            this.id = id;
        } else {
            this.custom = true;
            this.dbId = dbId;
            this.uri = Uri.parse(uri);
            this.id = dbId.hashCode();
        }
        this.displayName = displayName;
        this.mimeType = mimeType;
        this.size = size;
        this.duration = duration;
        this.dateTaken = dateTaken;
    }

    protected Media(Parcel in) {
        this.bucketId = in.readString();
        this.id = in.readLong();
        this.dbId = in.readString();
        this.displayName = in.readString();
        this.mimeType = in.readString();
        this.uri = in.readParcelable(Uri.class.getClassLoader());
        this.size = in.readLong();
        this.duration = in.readLong();
        this.dateTaken = in.readLong();
        this.custom = this.dbId.startsWith("custom");
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.bucketId);
        dest.writeLong(this.id);
        dest.writeString(this.dbId);
        dest.writeString(this.displayName);
        dest.writeString(this.mimeType);
        dest.writeParcelable(this.uri, flags);
        dest.writeLong(this.size);
        dest.writeLong(this.duration);
        dest.writeLong(this.dateTaken);
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = 31 * result + (bucketId + "").hashCode();
        result = 31 * result + (dbId + "").hashCode();
        result = 31 * result + Long.valueOf(id).hashCode();
        result = 31 * result + (displayName + "").hashCode();
        result = 31 * result + (mimeType + "").hashCode();
        result = 31 * result + (uri + "").hashCode();
        result = 31 * result + Long.valueOf(size).hashCode();
        result = 31 * result + Long.valueOf(duration).hashCode();
        result = 31 * result + Long.valueOf(dateTaken).hashCode();
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Media)) {
            return false;
        }

        Media other = (Media) obj;
        return id == other.id
                && (bucketId != null && bucketId.equals(other.bucketId)
                || (bucketId == null && other.bucketId == null))
                && (dbId != null && dbId.equals(other.dbId)
                || (dbId == null && other.dbId == null))
                && (displayName != null && displayName.equals(other.displayName)
                || (displayName == null && other.displayName == null))
                && (mimeType != null && mimeType.equals(other.mimeType)
                || (mimeType == null && other.mimeType == null))
                && (uri != null && uri.equals(other.uri)
                || (uri == null && other.uri == null))
                && size == other.size
                && duration == other.duration
                && dateTaken == other.dateTaken;
    }

    public static Media from(Cursor cursor) {
        return new Media(cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_ID)),
                cursor.getLong(cursor.getColumnIndex(MediaStore.Files.FileColumns._ID)),
                cursor.getString(cursor.getColumnIndex(AlbumMediaCursor.CUSTOM_ID)),
                cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME)),
                cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.MIME_TYPE)),
                cursor.getString(cursor.getColumnIndex(AlbumMediaCursor.COLUMN_URL)),
                cursor.getLong(cursor.getColumnIndex(MediaStore.MediaColumns.SIZE)),
                cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.DURATION)),
                cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.DATE_TAKEN)));
    }

    public static Media fromDevice(Cursor cursor) {
        return new Media(cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_ID)),
                cursor.getLong(cursor.getColumnIndex(MediaStore.Files.FileColumns._ID)),
                "",
                cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME)),
                cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.MIME_TYPE)),
                "",
                cursor.getLong(cursor.getColumnIndex(MediaStore.MediaColumns.SIZE)),
                cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.DURATION)),
                cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.DATE_TAKEN)));
    }

    public static Builder newBuilder(long bucketId) {
        return new Builder(String.valueOf(bucketId));
    }

    public static Builder newBuilder(String bucketId) {
        return new Builder(bucketId);
    }

    public String getDbId() {
        return dbId;
    }

    public long getCustomId() {
        return (getDbId().startsWith("custom")) ? Long.valueOf(getDbId().substring(String.format(Locale.getDefault(), "custom_%s_", bucketId).length())) : getId();
    }

    public long getId() {
        return id;
    }

    public Uri getContentUri() {
        return uri;
    }

    public boolean isCapture() {
        return id == ITEM_ID_CAPTURE;
    }

    public long getDateTaken() {
        return dateTaken;
    }

    public String[] toCursorData() {
        return new String[]{
                String.valueOf(bucketId),
                String.valueOf(id),
                String.valueOf(dbId),
                displayName,
                custom ? uri.toString() : "",
                mimeType,
                String.valueOf(size),
                String.valueOf(duration),
                String.valueOf(dateTaken)
        };
    }

    public String getBucketId() {
        return bucketId;
    }

    public static class Builder {
        private final String bucketId;
        private long id;
        private String displayName;
        private String mimeType;
        private Uri uri;
        private long size;
        private long duration; // only for video, in ms
        private long dateTaken;

        public Builder(String bucketId) {
            this.bucketId = bucketId;
        }

        public Media build() {
            return new Media(bucketId, id, displayName, mimeType, uri, size, duration, dateTaken);
        }

        public Builder setId(long id) {
            this.id = id;
            return this;
        }

        public Builder setDisplayName(String displayName) {
            this.displayName = displayName;
            return this;
        }

        public Builder setMimeType(String mimeType) {
            this.mimeType = mimeType;
            return this;
        }

        public Builder setUri(Uri uri) {
            this.uri = uri;
            return this;
        }

        public Builder setUri(String uriString) {
            this.uri = Uri.parse(uriString);
            return this;
        }

        public Builder setSize(long size) {
            this.size = size;
            return this;
        }

        public Builder setDuration(long duration) {
            this.duration = duration;
            return this;
        }

        public Builder setDateTaken(long dateTaken) {
            this.dateTaken = dateTaken;
            return this;
        }
    }
}
