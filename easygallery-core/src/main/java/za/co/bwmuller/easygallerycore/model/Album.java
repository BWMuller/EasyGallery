package za.co.bwmuller.easygallerycore.model;

import android.database.Cursor;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.MediaStore;

import za.co.bwmuller.easygallerycore.Config;
import za.co.bwmuller.easygallerycore.data.cursor.AlbumCursor;
import za.co.bwmuller.easygallerycore.utils.ContentUriUtil;

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
    private String dbId;
    private String id;
    private Uri coverUri;
    private long count;
    private String name;
    private boolean video;
    private String coverMimeType;
    private long coverId;
    private long dateTaken;
    private boolean customAlbum;

    public Album(String dbId, String id, String name, String coverUri, long dateTaken, long count) {
        this.dbId = dbId;
        this.id = id;
        this.name = name;
        this.coverMimeType = coverUri;
        this.coverId = 0;
        this.coverUri = Uri.parse(coverUri);
        this.dateTaken = dateTaken;
        this.count = count;
        this.customAlbum = true;
    }

    Album(String dbId, String id, String name, String coverMimeType, long coverId, long dateTaken, long count, boolean customAlbum) {
        this.dbId = dbId;
        this.id = id;
        this.name = name;
        this.coverMimeType = coverMimeType;
        this.coverId = coverId;
        this.dateTaken = dateTaken;
        this.count = count;
        this.customAlbum = customAlbum;
        if (customAlbum || ALBUM_ALL_ID.equals(id))
            this.coverUri = Uri.parse(coverMimeType);
        else
            this.coverUri = ContentUriUtil.getPath(coverMimeType, coverId);
    }

    Album(String dbId, String id, String name, String coverMimeType, long coverId, long dateTaken, long count, boolean customAlbum, boolean video) {
        this.dbId = dbId;
        this.id = id;
        this.coverMimeType = coverMimeType;
        this.coverId = coverId;
        this.dateTaken = dateTaken;
        this.count = count;
        this.name = name;
        this.video = video;
        this.customAlbum = customAlbum;
        if (customAlbum || ALBUM_ALL_ID.equals(id))
            this.coverUri = Uri.parse(coverMimeType);
        else
            this.coverUri = ContentUriUtil.getPath(coverMimeType, coverId);
    }

    protected Album(Parcel in) {
        this.dbId = in.readString();
        this.id = in.readString();
        this.coverId = in.readLong();
        this.coverUri = in.readParcelable(Uri.class.getClassLoader());
        this.dateTaken = in.readLong();
        this.count = in.readLong();
        this.name = in.readString();
        this.video = in.readByte() != 0;
        this.coverMimeType = in.readString();
        this.customAlbum = in.readByte() != 0;
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.dbId);
        dest.writeString(this.id);
        dest.writeLong(this.coverId);
        dest.writeParcelable(this.coverUri, flags);
        dest.writeLong(this.dateTaken);
        dest.writeLong(this.count);
        dest.writeString(this.name);
        dest.writeByte(this.video ? (byte) 1 : (byte) 0);
        dest.writeString(this.coverMimeType);
        dest.writeByte(this.customAlbum ? (byte) 1 : (byte) 0);
    }

    public static String[] createAllAlbumEntry(String coverPath, long coverId, long dateTaken, int count) {
        return new String[]{
                String.valueOf(coverId),
                ALBUM_ALL_ID,
                "",
                coverPath,
                String.valueOf(dateTaken),
                String.valueOf(count),
                Boolean.FALSE.toString()
        };
    }

    public static Album from(Cursor cursor) {
        return new Album(
                cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns._ID)),
                cursor.getString(cursor.getColumnIndex(AlbumCursor.BUCKET_ID)),
                cursor.getString(cursor.getColumnIndex(AlbumCursor.BUCKET_DISPLAY_NAME)),
                cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA)),
                cursor.getLong(cursor.getColumnIndex(MediaStore.MediaColumns._ID)),
                cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.DATE_TAKEN)),
                cursor.getLong(cursor.getColumnIndex(AlbumCursor.COLUMN_COUNT)),
                Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(AlbumCursor.CUSTOM_ALBUM))));
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

    public String[] toCursorData() {
        return new String[]{
                String.valueOf(coverId),
                id,
                name,
                coverMimeType,
                String.valueOf(dateTaken),
                String.valueOf(count),
                String.valueOf(customAlbum)
        };
    }

    public long getDateTaken() {
        return dateTaken;
    }

    public long getCoverId() {
        return coverId;
    }

    public String getCoverPath() {
        return coverUri.toString();
    }

    public boolean isCustomAlbum() {
        return customAlbum;
    }
}
