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
import android.support.v4.content.CursorLoader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import za.co.bwmuller.easygallerycore.Config;
import za.co.bwmuller.easygallerycore.model.Album;

/**
 * Load all albums (grouped by bucket_id) into a single cursor.
 */
public class AlbumCursor extends CursorLoader {
    public static final String CUSTOM_ALBUM = "custom_album";
    public static final String COLUMN_COUNT = "count";
    public static final String BUCKET_ID = MediaStore.Images.Media.BUCKET_ID;
    public static final String BUCKET_DISPLAY_NAME = MediaStore.Images.Media.BUCKET_DISPLAY_NAME;

    private static final Uri QUERY_URI = MediaStore.Files.getContentUri("external");
    private static final String[] COLUMNS = {
            MediaStore.Files.FileColumns._ID,
            BUCKET_ID,
            BUCKET_DISPLAY_NAME,
            MediaStore.MediaColumns.DATA,
            MediaStore.Images.Media.DATE_TAKEN,
            COLUMN_COUNT,
            CUSTOM_ALBUM};
    private static final String[] PROJECTION = {
            MediaStore.Files.FileColumns._ID,
            BUCKET_ID,
            BUCKET_DISPLAY_NAME,
            MediaStore.MediaColumns.DATA,
            MediaStore.Images.Media.DATE_TAKEN,
            "COUNT(*) AS " + COLUMN_COUNT,
            "0 > 0 AS " + CUSTOM_ALBUM};

    // === params for showSingleMediaType: false ===
    private static final String SELECTION =
            "(" + MediaStore.Files.FileColumns.MEDIA_TYPE + "=?"
                    + " OR "
                    + MediaStore.Files.FileColumns.MEDIA_TYPE + "=?)"
                    + " AND " + MediaStore.MediaColumns.SIZE + ">0"
                    + ") GROUP BY (" + BUCKET_ID;
    private static final String[] SELECTION_ARGS = {
            String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE),
            String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO),
    };
    // =============================================

    // === params for showSingleMediaType: true ===
    private static final String SELECTION_FOR_SINGLE_MEDIA_TYPE =
            MediaStore.Files.FileColumns.MEDIA_TYPE + "=?"
                    + " AND " + MediaStore.MediaColumns.SIZE + ">0"
                    + ") GROUP BY (" + BUCKET_ID;
    private static final String BUCKET_ORDER_BY = MediaStore.Images.Media.DATE_TAKEN + " DESC";
    // =============================================
    private Config mConfig;

    private AlbumCursor(Context context, String selection, String[] selectionArgs, Config config) {
        super(context, QUERY_URI, PROJECTION, selection, selectionArgs, BUCKET_ORDER_BY);
        this.mConfig = config;
    }

    @Override
    public Cursor loadInBackground() {
        Cursor albumCursor = super.loadInBackground();
        ArrayList<Album> prefixAlbums = new ArrayList<>();
        ArrayList<Album> postfixAlbums = new ArrayList<>();

        if (mConfig.albumLoader != null) {
            ArrayList<Album> customAlbums = mConfig.albumLoader.prefixAlbums();
            if (customAlbums != null && !customAlbums.isEmpty()) {
                prefixAlbums = customAlbums;
            }
        }
        if (mConfig.albumLoader != null) {
            ArrayList<Album> customAlbums = mConfig.albumLoader.postfixAlbums();
            if (customAlbums != null && !customAlbums.isEmpty()) {
                for (Album customAlbum : customAlbums) {
                    postfixAlbums = customAlbums;
                }
            }
        }

        MatrixCursor sortedAlbums = new MatrixCursor(COLUMNS);
        int totalCount = 0;
        long allAlbumCoverId = -1;
        String allAlbumCoverPath = "";
        long dateTaken = 0;
        ArrayList<Album> albums = new ArrayList<>();
        if (albumCursor != null) {
            while (albumCursor.moveToNext()) {
                Album album = Album.from(albumCursor);
                albums.add(album);
                totalCount += album.getCount();
                long date = album.getDateTaken();
                if (dateTaken < date) {
                    allAlbumCoverId = album.getCoverId();
                    allAlbumCoverPath = album.getCoverPath();
                }
            }
        }
        for (Album prefixAlbum : prefixAlbums) {
            totalCount += prefixAlbum.getCount();
            long date = prefixAlbum.getDateTaken();
            if (dateTaken < date) {
                allAlbumCoverId = prefixAlbum.getCoverId();
                allAlbumCoverPath = prefixAlbum.getCoverPath();
            }
        }

        for (Album postfixAlbum : postfixAlbums) {
            totalCount += postfixAlbum.getCount();
            long date = postfixAlbum.getDateTaken();
            if (dateTaken < date) {
                allAlbumCoverId = postfixAlbum.getCoverId();
                allAlbumCoverPath = postfixAlbum.getCoverPath();
            }
        }

        Collections.sort(albums, new Comparator<Album>() {
            @Override public int compare(Album o1, Album o2) {
                return Math.min(Math.max(o1.getName().compareTo(o2.getName()), -1), 1);
            }
        });

        sortedAlbums.addRow(Album.createAllAlbumEntry(allAlbumCoverPath, allAlbumCoverId, dateTaken, totalCount));

        for (Album customAlbum : prefixAlbums) {
            sortedAlbums.addRow(customAlbum.toCursorData());
        }
        for (Album album : albums) {
            sortedAlbums.addRow(album.toCursorData());
        }
        for (Album customAlbum : postfixAlbums) {
            sortedAlbums.addRow(customAlbum.toCursorData());
        }
        return sortedAlbums;
    }

    @Override
    public void onContentChanged() {
        // FIXME a dirty way to fix loading multiple times
    }

    private static String[] getSelectionArgsForSingleMediaType(int mediaType) {
        return new String[]{String.valueOf(mediaType)};
    }

    public static CursorLoader newInstance(Context context, Config config) {
        String selection;
        String[] selectionArgs;
        if (config.loaderScope == Config.Scope.IMAGES) {
            selection = SELECTION_FOR_SINGLE_MEDIA_TYPE;
            selectionArgs = getSelectionArgsForSingleMediaType(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE);
        } else if (config.loaderScope == Config.Scope.VIDEOS) {
            selection = SELECTION_FOR_SINGLE_MEDIA_TYPE;
            selectionArgs = getSelectionArgsForSingleMediaType(MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO);
        } else {
            selection = SELECTION;
            selectionArgs = SELECTION_ARGS;
        }

        if (!config.excludeDirectories.isEmpty()) {
            String tmp = "";
            for (String excludeDirectory : config.excludeDirectories) {
                tmp += (tmp.isEmpty() ? "" : " AND ") + MediaStore.Images.Media.DATA + " not like '%" + excludeDirectory + "%' ";
            }
            selection = " ( " + tmp + " ) AND " + selection;
        }
        return new AlbumCursor(context, selection, selectionArgs, config);
    }
}