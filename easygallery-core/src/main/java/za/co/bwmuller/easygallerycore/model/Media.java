package za.co.bwmuller.easygallerycore.model;

import android.database.Cursor;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.MediaStore;

import za.co.bwmuller.easygallerycore.utils.ContentUriUtil;

/**
 * Created by Bernhard Müller on 8/23/2017.
 */

public class Media implements Parcelable {
    public static final long ITEM_ID_CAPTURE = -1;
    public static final String ITEM_DISPLAY_NAME_CAPTURE = "Capture";
    public static final Parcelable.Creator<Media> CREATOR = new Parcelable.Creator<Media>() {
        @Override public Media createFromParcel(Parcel source) {
            return new Media(source);
        }

        @Override public Media[] newArray(int size) {
            return new Media[size];
        }
    };
    private final long id;
    private final String displayName;
    private final String mimeType;
    private final Uri uri;
    private final long size;
    private final long duration; // only for video, in ms
    private final long dateTaken;

    private Media(long id, String displayName, String mimeType, long size, long duration, long dateTaken) {
        this.id = id;
        this.displayName = displayName;
        this.mimeType = mimeType;
        this.uri = ContentUriUtil.getPath(mimeType, id);
        this.size = size;
        this.duration = duration;
        this.dateTaken = dateTaken;
    }

    protected Media(Parcel in) {
        this.id = in.readLong();
        this.displayName = in.readString();
        this.mimeType = in.readString();
        this.uri = in.readParcelable(Uri.class.getClassLoader());
        this.size = in.readLong();
        this.duration = in.readLong();
        this.dateTaken = in.readLong();
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
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
        result = 31 * result + Long.valueOf(id).hashCode();
        result = 31 * result + displayName.hashCode();
        result = 31 * result + mimeType.hashCode();
        result = 31 * result + uri.hashCode();
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
        return new Media(cursor.getLong(cursor.getColumnIndex(MediaStore.Files.FileColumns._ID)),
                cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME)),
                cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.MIME_TYPE)),
                cursor.getLong(cursor.getColumnIndex(MediaStore.MediaColumns.SIZE)),
                cursor.getLong(cursor.getColumnIndex(MediaStore.Video.VideoColumns.DURATION)),
                cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.DATE_TAKEN)));
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
                String.valueOf(id),
                displayName,
                mimeType,
                String.valueOf(size),
                String.valueOf(duration),
                String.valueOf(dateTaken)
        };
    }
}
