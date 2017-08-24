package za.co.bwmuller.easygallery.model;

import android.database.Cursor;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.MediaStore;

import za.co.bwmuller.easygallery.Config;
import za.co.bwmuller.easygallery.data.cursor.AlbumCursor;
import za.co.bwmuller.easygallery.internal.utils.ContentUriUtil;

/**
 * Created by Bernhard Müller on 8/23/2017.
 */

public class Album implements Parcelable {
    public static final String ALBUM_ALL_ID = String.valueOf(-1);
    public static final Parcelable.Creator<Album> CREATOR = new Parcelable.Creator<Album>() {
        @Override public Album createFromParcel(Parcel source) {
            return new Album(source);
        }

        @Override public Album[] newArray(int size) {
            return new Album[size];
        }
    };
    private String id;
    private Uri coverUri;
    private long count;
    private String name;
    private boolean video;
    private String coverMimeType;

    public Album(String id, String name, String coverMimeType, long coverId, long count) {
        this.id = id;
        this.name = name;
        this.coverMimeType = coverMimeType;
        this.coverUri = ContentUriUtil.getPath(coverMimeType, coverId);
        this.count = count;
    }

    public Album(String id, String name, String coverMimeType, long coverId, long count, boolean video) {
        this.id = id;
        this.coverMimeType = coverMimeType;
        this.coverUri = ContentUriUtil.getPath(coverMimeType, coverId);
        this.count = count;
        this.name = name;
        this.video = video;
    }

    public Album(String coverMimeType, long coverId, long count) {
        this.id = ALBUM_ALL_ID;
        this.name = "All Media";
        this.coverMimeType = coverMimeType;
        this.coverUri = ContentUriUtil.getPath(coverMimeType, coverId);
        this.count = count;
    }

    protected Album(Parcel in) {
        this.id = in.readString();
        this.coverUri = in.readParcelable(Uri.class.getClassLoader());
        this.count = in.readLong();
        this.name = in.readString();
        this.video = in.readByte() != 0;
        this.coverMimeType = in.readString();
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeParcelable(this.coverUri, flags);
        dest.writeLong(this.count);
        dest.writeString(this.name);
        dest.writeByte(this.video ? (byte) 1 : (byte) 0);
        dest.writeString(this.coverMimeType);
    }

    public static String[] createAllAlbumEntry(String coverPath, long coverId, long dateTaken, int count) {
        return new String[]{
                String.valueOf(coverId),
                ALBUM_ALL_ID,
                "",
                coverPath,
                String.valueOf(dateTaken),
                String.valueOf(count)
        };
    }

    public static Album from(Cursor cursor) {
        return new Album(
                cursor.getString(cursor.getColumnIndex(AlbumCursor.BUCKET_ID)),
                cursor.getString(cursor.getColumnIndex(AlbumCursor.BUCKET_DISPLAY_NAME)),
                cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA)),
                cursor.getLong(cursor.getColumnIndex(MediaStore.MediaColumns._ID)),
                cursor.getLong(cursor.getColumnIndex(AlbumCursor.COLUMN_COUNT)));
    }

    public String getId() {
        return id;
    }

    public long getCount() {
        return count;
    }

    public String getName() {
        return name;
    }

    public Uri getCoverUri() {
        return coverUri;
    }

    public boolean isAllMedia() {
        return ALBUM_ALL_ID.equals(id);
    }

    public boolean isVideo() {
        return video;
    }

    public String getDisplayName(Config mConfig) {
        return isAllMedia() ? (isVideo() ? mConfig.allVideos : mConfig.allImages) : getName();
    }
}
