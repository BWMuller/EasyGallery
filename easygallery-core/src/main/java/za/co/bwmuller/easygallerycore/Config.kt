package za.co.bwmuller.easygallerycore

import za.co.bwmuller.easygallerycore.data.custom.AlbumLoaderCallback
import za.co.bwmuller.easygallerycore.data.custom.MediaLoaderCallback
import java.util.HashSet

/**
 * Created by Bernhard Müller on 8/23/2017.
 */
open class Config {

    @JvmField
    var allMedia = "All media"

    @JvmField
    var albumGridColumnCount = 2

    @JvmField
    var mediaGridColumnCount = 3

    @JvmField
    var autoRotateImages = false

    @JvmField
    var loaderScope: Scope = Scope.ALL
    var enableCameraCapture = false

    @JvmField
    var excludeDirectories = HashSet<String>()
    var albumLoader: AlbumLoaderCallback? = null

    @JvmField
    var mediaLoader: MediaLoaderCallback? = null
    fun addExcludedDirectory(directory: String): Config {
        excludeDirectories.add(directory)
        return this
    }

    fun setCustomAlbum(albumLoader: AlbumLoaderCallback): Config {
        this.albumLoader = albumLoader
        return this
    }

    fun setCustomMedia(mediaLoader: MediaLoaderCallback): Config {
        this.mediaLoader = mediaLoader
        return this
    }

    enum class Scope {
        ALL, IMAGES, VIDEOS
    }

    class Builder {

        private val config = Config()
        fun setAllMedia(allMedia: String): Builder {
            config.allMedia = allMedia
            return this
        }

        fun setAlbumGridColumnCount(albumGridColumnCount: Int): Builder {
            config.albumGridColumnCount = albumGridColumnCount
            return this
        }

        fun setMediaGridColumnCount(mediaGridColumnCount: Int): Builder {
            config.mediaGridColumnCount = mediaGridColumnCount
            return this
        }

        fun setAutoRotateImages(autoRotateImages: Boolean): Builder {
            config.autoRotateImages = autoRotateImages
            return this
        }

        fun setLoaderScope(loaderScope: Scope): Builder {
            config.loaderScope = loaderScope
            return this
        }

        fun setEnableCameraCapture(enableCameraCapture: Boolean): Builder {
            config.enableCameraCapture = enableCameraCapture
            return this
        }

        fun setExcludeDirectories(excludeDirectories: HashSet<String>): Builder {
            config.excludeDirectories = excludeDirectories
            return this
        }

        fun addExcludedDirectory(directory: String): Builder {
            config.excludeDirectories.add(directory)
            return this
        }

        fun setAlbumLoader(albumLoader: AlbumLoaderCallback): Builder {
            config.albumLoader = albumLoader
            return this
        }

        fun setMediaLoader(mediaLoader: MediaLoaderCallback): Builder {
            config.mediaLoader = mediaLoader
            return this
        }

        fun build(): Config {
            return config
        }
    }

    companion object {

        fun newBuilder(): Builder {
            return Builder()
        }
    }
}