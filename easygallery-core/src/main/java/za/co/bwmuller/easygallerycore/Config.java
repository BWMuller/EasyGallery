package za.co.bwmuller.easygallerycore;

import androidx.annotation.Nullable;

import java.util.HashSet;

import za.co.bwmuller.easygallerycore.data.custom.AlbumLoaderCallback;
import za.co.bwmuller.easygallerycore.data.custom.MediaLoaderCallback;

/**
 * Created by Bernhard Müller on 8/23/2017.
 */

public class Config {
    public String allMedia = "All media";
    public int albumGridColumnCount = 2;
    public int mediaGridColumnCount = 3;
    public boolean autoRotateImages = false;
    public Scope loaderScope;
    public boolean enableCameraCapture = false;
    public HashSet<String> excludeDirectories = new HashSet<>();
    public
    @Nullable
    AlbumLoaderCallback albumLoader;
    public
    @Nullable
    MediaLoaderCallback mediaLoader;

    public static Builder newBuilder() {
        return new Builder();
    }

    public Config addExcludedDirectory(String directory) {
        excludeDirectories.add(directory);
        return this;
    }

    public Config setCustomAlbum(AlbumLoaderCallback albumLoader) {
        this.albumLoader = albumLoader;
        return this;
    }

    public Config setCustomMedia(MediaLoaderCallback mediaLoader) {
        this.mediaLoader = mediaLoader;
        return this;
    }

    public enum Scope {
        ALL,
        IMAGES,
        VIDEOS
    }

    public static class Builder {
        private Config config = new Config();

        public Builder setAllMedia(String allMedia) {
            config.allMedia = allMedia;
            return this;
        }

        public Builder setAlbumGridColumnCount(int albumGridColumnCount) {
            config.albumGridColumnCount = albumGridColumnCount;
            return this;
        }

        public Builder setMediaGridColumnCount(int mediaGridColumnCount) {
            config.mediaGridColumnCount = mediaGridColumnCount;
            return this;
        }

        public Builder setAutoRotateImages(boolean autoRotateImages) {
            config.autoRotateImages = autoRotateImages;
            return this;
        }

        public Builder setLoaderScope(Scope loaderScope) {
            config.loaderScope = loaderScope;
            return this;
        }

        public Builder setEnableCameraCapture(boolean enableCameraCapture) {
            config.enableCameraCapture = enableCameraCapture;
            return this;
        }

        public Builder setExcludeDirectories(HashSet<String> excludeDirectories) {
            config.excludeDirectories = excludeDirectories;
            return this;
        }

        public Builder addExcludedDirectory(String directory) {
            config.excludeDirectories.add(directory);
            return this;
        }

        public Builder setAlbumLoader(AlbumLoaderCallback albumLoader) {
            config.albumLoader = albumLoader;
            return this;
        }

        public Builder setMediaLoader(MediaLoaderCallback mediaLoader) {
            config.mediaLoader = mediaLoader;
            return this;
        }

        public Config build() {
            return config;
        }
    }
}
