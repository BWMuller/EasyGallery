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
package za.co.bwmuller.easygallerycore.data.cursor;

import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.loader.content.CursorLoader;
import android.webkit.MimeTypeMap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import za.co.bwmuller.easygallerycore.Config;
import za.co.bwmuller.easygallerycore.model.Album;
import za.co.bwmuller.easygallerycore.model.Media;

/**
 * Load images and videos into a single cursor.
 */
public class AlbumMediaCursor extends CursorLoader {
    public static final String COLUMN_URL = "custom_url";
    public static final String CUSTOM_ID = "custom_id";
    private static final Uri QUERY_URI = MediaStore.Files.getContentUri("external");
    private static final String[] COLUMNS = {
            MediaStore.Images.Media.BUCKET_ID,
            MediaStore.Files.FileColumns._ID,
            CUSTOM_ID,
            MediaStore.MediaColumns.DISPLAY_NAME,
            COLUMN_URL,
            MediaStore.MediaColumns.MIME_TYPE,
            MediaStore.MediaColumns.SIZE,
            MediaStore.Video.VideoColumns.DURATION,
            MediaStore.Images.Media.DATE_TAKEN};
    private static final String[] PROJECTION = {
            MediaStore.Images.Media.BUCKET_ID,
            MediaStore.Files.FileColumns._ID,
            MediaStore.MediaColumns.DISPLAY_NAME,
            MediaStore.MediaColumns.MIME_TYPE,
            MediaStore.MediaColumns.SIZE,
            MediaStore.Video.VideoColumns.DURATION,
            MediaStore.Images.Media.DATE_TAKEN};

    // === params for album ALL && showSingleMediaType: false ===
    private static final String SELECTION_ALL =
            "(" + MediaStore.Files.FileColumns.MEDIA_TYPE + "=?"
                    + " OR "
                    + MediaStore.Files.FileColumns.MEDIA_TYPE + "=?)"
                    + " AND " + MediaStore.MediaColumns.SIZE + ">0";
    private static final String[] SELECTION_ALL_ARGS = {
            String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE),
            String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO),
    };
    private static final String[] SELECTION_NO_ARGS = {};
    // ===========================================================

    // === album custom ===
    private static final String SELECTION_ALL_FOR_CUSTOM_ALBUM =
            MediaStore.MediaColumns.SIZE + "== 0"
                    + " AND " + MediaStore.MediaColumns.SIZE + ">0";
    // === params for album ALL && showSingleMediaType: true ===
    private static final String SELECTION_ALL_FOR_IMAGE_MEDIA_TYPE = " ( "
            + MediaStore.Files.FileColumns.MEDIA_TYPE + "=?"
            + " AND NOT " + MediaStore.Images.Media.MIME_TYPE + "=?"
            + " ) AND " + MediaStore.MediaColumns.SIZE + ">0";


    private static final String SELECTION_ALL_FOR_VIDEO_MEDIA_TYPE =
            MediaStore.Files.FileColumns.MEDIA_TYPE + "=?"
                    + " AND " + MediaStore.MediaColumns.SIZE + ">0";

    // === params for ordinary album && showSingleMediaType: false ===
    private static final String SELECTION_ALBUM = "("
            + " ( " + MediaStore.Files.FileColumns.MEDIA_TYPE + "=?"
            + " AND NOT " + MediaStore.Images.Media.MIME_TYPE + "=?)"
            + " OR "
            + MediaStore.Files.FileColumns.MEDIA_TYPE + "=?)"
            + " AND "
            + " " + AlbumCursor.BUCKET_ID + "=?"
            + " AND " + MediaStore.MediaColumns.SIZE + ">0";
    // =========================================================
    // === params for ordinary album && showSingleMediaType: true ===
    private static final String SELECTION_ALBUM_FOR_IMAGE_MEDIA_TYPE =
            SELECTION_ALL_FOR_IMAGE_MEDIA_TYPE
                    + " AND " + AlbumCursor.BUCKET_ID + "=?";


    private static final String SELECTION_ALBUM_FOR_VIDEO_MEDIA_TYPE =
            SELECTION_ALL_FOR_VIDEO_MEDIA_TYPE
                    + " AND " + AlbumCursor.BUCKET_ID + "=?";
    private static final String ORDER_BY = MediaStore.Images.Media.DATE_TAKEN + " DESC";
    // ===============================================================
    private final boolean mIsAll;
    private final Config mConfig;
    private final Album mAlbum;

    private AlbumMediaCursor(Context context, String selection, String[] selectionArgs, boolean isAll, Config config, Album album) {
        super(context, QUERY_URI, PROJECTION, selection, selectionArgs, ORDER_BY);
        mIsAll = isAll;
        mConfig = config;
        mAlbum = album;
    }
    // ===============================================================

    @Override
    public Cursor loadInBackground() {
        if (mAlbum.isCustomAlbum()) {
            ArrayList<Media> mediaList = new ArrayList<>();
            mediaList.addAll(mConfig.mediaLoader.customAlbumMedia(mAlbum));

            Collections.sort(mediaList, new Comparator<Media>() {
                @Override public int compare(Media o1, Media o2) {
                    return Math.min(Math.max(Double.compare(o2.getDateTaken(), o1.getDateTaken()), -1), 1);
                }
            });

            MatrixCursor sortedMedia = new MatrixCursor(COLUMNS);
            for (Media media : mediaList) {
                sortedMedia.addRow(media.toCursorData());
            }
            return sortedMedia;
        } else {
            Cursor mediaCursor = super.loadInBackground();
            ArrayList<Media> mediaList = new ArrayList<>();
            if (mIsAll && mConfig.mediaLoader != null) {
                mediaList.addAll(mConfig.mediaLoader.allMedia());
            }
            if (mediaCursor != null) {
                while (mediaCursor.moveToNext()) {
                    mediaList.add(Media.fromDevice(mediaCursor));
                }
            }
            Collections.sort(mediaList, new Comparator<Media>() {
                @Override
                public int compare(Media o1, Media o2) {
                    return Math.min(Math.max(Double.compare(o2.getDateTaken(), o1.getDateTaken()), -1), 1);
                }
            });

            MatrixCursor sortedMedia = new MatrixCursor(COLUMNS);
            for (Media media : mediaList) {
                sortedMedia.addRow(media.toCursorData());
            }
            return sortedMedia;
        }
    }

    @Override
    public void onContentChanged() {
        // FIXME a dirty way to fix loading multiple times
    }

    private static String[] getSelectionArgsForSingleMediaType(int mediaType) {
        if (MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE == mediaType) {
            return new String[]{
                    String.valueOf(mediaType),
                    MimeTypeMap.getSingleton().getMimeTypeFromExtension("gif"),
            };
        } else {
            return new String[]{String.valueOf(mediaType)};
        }
    }

    private static String[] getSelectionAlbumArgs(String albumId) {
        return new String[]{
                String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE),
                MimeTypeMap.getSingleton().getMimeTypeFromExtension("gif"),
                String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO),
                albumId
        };
    }

    private static String[] getSelectionAlbumArgsForSingleMediaType(int mediaType, String albumId) {
        if (MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE == mediaType) {
            return new String[]{
                    String.valueOf(mediaType),
                    MimeTypeMap.getSingleton().getMimeTypeFromExtension("gif"),
                    albumId
            };
        } else {
            return new String[]{String.valueOf(mediaType), albumId};
        }
    }

    public static CursorLoader newInstance(@NonNull Context context, @NonNull Album album, @NonNull Config config) {
        String selection;
        String[] selectionArgs;

        if (album.isAllMedia()) {
            if (config.loaderScope == Config.Scope.IMAGES) {
                selection = SELECTION_ALL_FOR_IMAGE_MEDIA_TYPE;
                selectionArgs = getSelectionArgsForSingleMediaType(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE);
            } else if (config.loaderScope == Config.Scope.VIDEOS) {
                selection = SELECTION_ALL_FOR_VIDEO_MEDIA_TYPE;
                selectionArgs = getSelectionArgsForSingleMediaType(MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO);
            } else {
                selection = SELECTION_ALL;
                selectionArgs = SELECTION_ALL_ARGS;
            }
        } else {
            if (album.isCustomAlbum()) {
                selection = SELECTION_ALL_FOR_CUSTOM_ALBUM;
                selectionArgs = SELECTION_NO_ARGS;
            } else if (config.loaderScope == Config.Scope.IMAGES) {
                selection = SELECTION_ALBUM_FOR_IMAGE_MEDIA_TYPE;
                selectionArgs = getSelectionAlbumArgsForSingleMediaType(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE,
                        album.getId());
            } else if (config.loaderScope == Config.Scope.VIDEOS) {
                selection = SELECTION_ALBUM_FOR_VIDEO_MEDIA_TYPE;
                selectionArgs = getSelectionAlbumArgsForSingleMediaType(MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO,
                        album.getId());
            } else {
                selection = SELECTION_ALBUM;
                selectionArgs = getSelectionAlbumArgs(album.getId());
            }
        }

        if (!config.excludeDirectories.isEmpty()) {
            String tmp = "";
            for (String excludeDirectory : config.excludeDirectories) {
                tmp += (tmp.isEmpty() ? "" : " AND ") + MediaStore.Images.Media.DATA + " not like '%" + excludeDirectory + "%' ";
            }
            selection = " ( " + tmp + " ) AND " + selection;
        }
        return new AlbumMediaCursor(context, selection, selectionArgs, album.isAllMedia(), config, album);
    }
}
