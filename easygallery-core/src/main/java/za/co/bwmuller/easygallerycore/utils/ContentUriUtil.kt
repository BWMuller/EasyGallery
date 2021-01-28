package za.co.bwmuller.easygallerycore.utils

import android.content.ContentUris
import android.net.Uri
import android.provider.MediaStore.Files
import android.provider.MediaStore.Images.Media
import android.provider.MediaStore.Video
import za.co.bwmuller.easygallerycore.utils.MimeType.AVI
import za.co.bwmuller.easygallerycore.utils.MimeType.BMP
import za.co.bwmuller.easygallerycore.utils.MimeType.GIF
import za.co.bwmuller.easygallerycore.utils.MimeType.JPEG
import za.co.bwmuller.easygallerycore.utils.MimeType.MKV
import za.co.bwmuller.easygallerycore.utils.MimeType.MP4
import za.co.bwmuller.easygallerycore.utils.MimeType.MPEG
import za.co.bwmuller.easygallerycore.utils.MimeType.PNG
import za.co.bwmuller.easygallerycore.utils.MimeType.QUICKTIME
import za.co.bwmuller.easygallerycore.utils.MimeType.THREEGPP
import za.co.bwmuller.easygallerycore.utils.MimeType.THREEGPP2
import za.co.bwmuller.easygallerycore.utils.MimeType.TS
import za.co.bwmuller.easygallerycore.utils.MimeType.WEBM
import za.co.bwmuller.easygallerycore.utils.MimeType.WEBP

/**
 * Created by Bernhard Müller on 8/23/2017.
 */
object ContentUriUtil {

    fun isImage(mimeType: String): Boolean {
        return mimeType == JPEG.toString() || mimeType == PNG.toString() || mimeType == GIF.toString() || mimeType == BMP.toString() || mimeType == WEBP.toString()
    }

    fun isGif(mimeType: String): Boolean {
        return mimeType == GIF.toString()
    }

    fun isVideo(mimeType: String): Boolean {
        return mimeType == MPEG.toString() || mimeType == MP4.toString() || mimeType == QUICKTIME.toString() || mimeType == THREEGPP.toString() || mimeType == THREEGPP2.toString() || mimeType == MKV.toString() || mimeType == WEBM.toString() || mimeType == TS.toString() || mimeType == AVI.toString()
    }

    @JvmStatic
    fun getPath(mimeType: String, id: Long): Uri {
        val contentUri: Uri
        contentUri = if (isImage(mimeType)) {
            Media.EXTERNAL_CONTENT_URI
        } else if (isVideo(mimeType)) {
            Video.Media.EXTERNAL_CONTENT_URI
        } else {
            Files.getContentUri("external")
        }
        return ContentUris.withAppendedId(contentUri, id)
    }
}