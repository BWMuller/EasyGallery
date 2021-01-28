package za.co.bwmuller.easygallerycore.data.custom

import za.co.bwmuller.easygallerycore.model.Album
import java.util.ArrayList

/**
 * Created by Bernhard Müller on 8/24/2017.
 */
interface AlbumLoaderCallback {

    fun prefixAlbums(): ArrayList<Album>?
    fun postfixAlbums(): ArrayList<Album>?
}