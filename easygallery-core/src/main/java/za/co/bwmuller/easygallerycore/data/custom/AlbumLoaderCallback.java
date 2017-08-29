package za.co.bwmuller.easygallerycore.data.custom;

import java.util.ArrayList;

import za.co.bwmuller.easygallerycore.model.Album;

/**
 * Created by Bernhard Müller on 8/24/2017.
 */

public interface AlbumLoaderCallback {
    ArrayList<Album> prefixAlbums();

    ArrayList<Album> postfixAlbums();
}
