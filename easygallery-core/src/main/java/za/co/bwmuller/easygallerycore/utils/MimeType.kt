package za.co.bwmuller.easygallerycore.utils

import android.content.ContentResolver
import android.net.Uri
import android.webkit.MimeTypeMap
import java.util.EnumSet
import java.util.HashSet
import java.util.Locale

/**
 * MIME Type enumeration to restrict selectable media on the selection activity.
 *
 *
 * Good example of mime types Android supports:
 * https://android.googlesource.com/platform/frameworks/base/+/refs/heads/master/media/java/android/media/MediaFile.java
 */
enum class MimeType(private val mMimeTypeName: String, private val mExtensions: Set<String>) {

    // ============== images ==============
    JPEG("image/jpeg", object : HashSet<String>() {
        init {
            add("jpg")
            add("jpeg")
        }
    }),
    PNG("image/png", object : HashSet<String>() {
        init {
            add("png")
        }
    }),
    GIF("image/gif", object : HashSet<String>() {
        init {
            add("gif")
        }
    }),
    BMP("image/x-ms-bmp", object : HashSet<String>() {
        init {
            add("bmp")
        }
    }),
    WEBP("image/webp", object : HashSet<String>() {
        init {
            add("webp")
        }
    }),  // ============== videos ==============
    MPEG("video/mpeg", object : HashSet<String>() {
        init {
            add("mpeg")
            add("mpg")
        }
    }),
    MP4("video/mp4", object : HashSet<String>() {
        init {
            add("mp4")
            add("m4v")
        }
    }),
    QUICKTIME("video/quicktime", object : HashSet<String>() {
        init {
            add("mov")
        }
    }),
    THREEGPP("video/3gpp", object : HashSet<String>() {
        init {
            add("3gp")
            add("3gpp")
        }
    }),
    THREEGPP2("video/3gpp2", object : HashSet<String>() {
        init {
            add("3g2")
            add("3gpp2")
        }
    }),
    MKV("video/x-matroska", object : HashSet<String>() {
        init {
            add("mkv")
        }
    }),
    WEBM("video/webm", object : HashSet<String>() {
        init {
            add("webm")
        }
    }),
    TS("video/mp2ts", object : HashSet<String>() {
        init {
            add("ts")
        }
    }),
    AVI("video/avi", object : HashSet<String>() {
        init {
            add("avi")
        }
    });

    override fun toString(): String {
        return mMimeTypeName
    }

    fun checkType(resolver: ContentResolver, uri: Uri?): Boolean {
        val map = MimeTypeMap.getSingleton()
        if (uri == null) {
            return false
        }
        val type = map.getExtensionFromMimeType(resolver.getType(uri))
        for (extension in mExtensions) {
            if (extension == type) {
                return true
            }
            val path = PhotoMetadataUtils.getPath(resolver, uri)
            if (path != null && path.toLowerCase(Locale.US).endsWith(extension)) {
                return true
            }
        }
        return false
    }

    companion object {

        fun ofAll(): Set<MimeType> {
            return EnumSet.allOf(MimeType::class.java)
        }

        fun of(type: MimeType, vararg rest: MimeType): Set<MimeType> {
            return EnumSet.of(type, *rest)
        }

        fun ofImage(): Set<MimeType> {
            return EnumSet.of(JPEG, PNG, GIF, BMP, WEBP)
        }

        fun ofVideo(): Set<MimeType> {
            return EnumSet.of(MPEG, MP4, QUICKTIME, THREEGPP, THREEGPP2, MKV, WEBM, TS, AVI)
        }
    }
}