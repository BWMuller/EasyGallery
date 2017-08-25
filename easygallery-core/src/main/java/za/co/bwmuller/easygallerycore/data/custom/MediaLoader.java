package za.co.bwmuller.easygallerycore.data.custom;

import android.support.annotation.NonNull;

import java.util.ArrayList;

import za.co.bwmuller.easygallerycore.model.Album;
import za.co.bwmuller.easygallerycore.model.Media;

/**
 * Created by Bernhard Müller on 8/24/2017.
 */

public interface MediaLoader {
    @NonNull ArrayList<Media> allMedia();

    @NonNull ArrayList<Media> customAlbumMedia(Album album);
}
