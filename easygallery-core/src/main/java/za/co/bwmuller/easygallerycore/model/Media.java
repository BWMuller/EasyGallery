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
    public final long id;
    public final String mimeType;
    public final Uri uri;
    public final long size;
    public final long duration; // only for video, in ms

    private Media(long id, String mimeType, long size, long duration) {
        this.id = id;
        this.mimeType = mimeType;
        this.uri = ContentUriUtil.getPath(mimeType, id);
        this.size = size;
        this.duration = duration;
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = 31 * result + Long.valueOf(id).hashCode();
        result = 31 * result + mimeType.hashCode();
        result = 31 * result + uri.hashCode();
        result = 31 * result + Long.valueOf(size).hashCode();
        result = 31 * result + Long.valueOf(duration).hashCode();
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Media)) {
            return false;
        }

        Media other = (Media) obj;
        return id == other.id
                && (mimeType != null && mimeType.equals(other.mimeType)
                || (mimeType == null && other.mimeType == null))
                && (uri != null && uri.equals(other.uri)
                || (uri == null && other.uri == null))
                && size == other.size
                && duration == other.duration;
    }

    public static Media from(Cursor cursor) {
        return new Media(cursor.getLong(cursor.getColumnIndex(MediaStore.Files.FileColumns._ID)),
                cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.MIME_TYPE)),
                cursor.getLong(cursor.getColumnIndex(MediaStore.MediaColumns.SIZE)),
                cursor.getLong(cursor.getColumnIndex("duration")));
    }

    public Uri getContentUri() {
        return uri;
    }

    public boolean isCapture() {
        return id == ITEM_ID_CAPTURE;
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.mimeType);
        dest.writeParcelable(this.uri, flags);
        dest.writeLong(this.size);
        dest.writeLong(this.duration);
    }

    protected Media(Parcel in) {
        this.id = in.readLong();
        this.mimeType = in.readString();
        this.uri = in.readParcelable(Uri.class.getClassLoader());
        this.size = in.readLong();
        this.duration = in.readLong();
    }

    public static final Parcelable.Creator<Media> CREATOR = new Parcelable.Creator<Media>() {
        @Override public Media createFromParcel(Parcel source) {
            return new Media(source);
        }

        @Override public Media[] newArray(int size) {
            return new Media[size];
        }
    };
}
