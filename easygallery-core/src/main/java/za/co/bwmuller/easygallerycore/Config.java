package za.co.bwmuller.easygallerycore;

import android.support.annotation.Nullable;

import java.util.HashSet;

import za.co.bwmuller.easygallerycore.data.custom.AlbumLoader;
import za.co.bwmuller.easygallerycore.data.custom.MediaLoader;

/**
 * Created by Bernhard Müller on 8/23/2017.
 */

public class Config {
    public String allImages = "All images";
    public String allVideos = "All videos";
    public int albumGridColumnCount = 2;
    public int mediaGridColumnCount = 3;
    public boolean autoRotateImages = false;
    public Scope loaderScope;
    public boolean enableCameraCapture = false;
    public HashSet<String> excludeDirectories = new HashSet<>();
    public
    @Nullable
    AlbumLoader albumLoader;
    public
    @Nullable
    MediaLoader mediaLoader;

    public Config addExcludedDirectory(String directory) {
        excludeDirectories.add(directory);
        return this;
    }

    public Config setCustomAlbum(AlbumLoader albumLoader) {
        this.albumLoader = albumLoader;
        return this;
    }

    public Config setCustomMedia(MediaLoader mediaLoader) {
        this.mediaLoader = mediaLoader;
        return this;
    }

    public enum Scope {
        ALL,
        IMAGES,
        VIDEOS
    }
}
