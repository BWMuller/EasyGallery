package za.co.bwmuller.easygallerycore.data.custom

import za.co.bwmuller.easygallerycore.model.Album
import za.co.bwmuller.easygallerycore.model.Media
import java.util.ArrayList

/**
 * Created by Bernhard Müller on 8/24/2017.
 */
interface MediaLoader {

    fun allMedia(): ArrayList<Media>
    fun customAlbumMedia(album: Album): ArrayList<Media>
}