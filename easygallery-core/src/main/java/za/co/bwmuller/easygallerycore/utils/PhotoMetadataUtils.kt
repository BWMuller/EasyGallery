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
package za.co.bwmuller.easygallerycore.utils

import android.app.Activity
import android.content.ContentResolver
import android.database.Cursor
import android.graphics.BitmapFactory
import android.graphics.BitmapFactory.Options
import android.graphics.Point
import android.media.ExifInterface
import android.net.Uri
import android.provider.MediaStore.Images.ImageColumns
import android.util.DisplayMetrics
import android.util.Log
import za.co.bwmuller.easygallerycore.utils.ExifInterfaceCompat.newInstance
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStream
import java.text.DecimalFormat

object PhotoMetadataUtils {

    private val TAG = PhotoMetadataUtils::class.java.simpleName
    private const val MAX_WIDTH = 1600
    private const val SCHEME_CONTENT = "content"

    fun getPixelsCount(resolver: ContentResolver, uri: Uri): Int {
        val size = getBitmapBound(resolver, uri)
        return size.x * size.y
    }

    fun getBitmapSize(uri: Uri, activity: Activity): Point {
        val resolver = activity.contentResolver
        val imageSize = getBitmapBound(resolver, uri)
        var w = imageSize.x
        var h = imageSize.y
        if (shouldRotate(resolver, uri)) {
            w = imageSize.y
            h = imageSize.x
        }
        if (h == 0) return Point(MAX_WIDTH, MAX_WIDTH)
        val metrics = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(metrics)
        val screenWidth = metrics.widthPixels.toFloat()
        val screenHeight = metrics.heightPixels.toFloat()
        val widthScale = screenWidth / w
        val heightScale = screenHeight / h
        return if (widthScale > heightScale) {
            Point((w * widthScale).toInt(), (h * heightScale).toInt())
        } else Point((w * widthScale).toInt(), (h * heightScale).toInt())
    }

    fun getBitmapBound(resolver: ContentResolver, uri: Uri): Point {
        var stream: InputStream? = null
        return try {
            val options = Options()
            options.inJustDecodeBounds = true
            stream = resolver.openInputStream(uri)
            BitmapFactory.decodeStream(stream, null, options)
            val width = options.outWidth
            val height = options.outHeight
            Point(width, height)
        } catch (e: FileNotFoundException) {
            Point(0, 0)
        } finally {
            try {
                stream?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    fun getPath(resolver: ContentResolver, uri: Uri?): String? {
        if (uri == null) {
            return null
        }
        if (SCHEME_CONTENT == uri.scheme) {
            var cursor: Cursor? = null
            return try {
                cursor = resolver.query(
                    uri, arrayOf(ImageColumns.DATA),
                    null, null, null
                )
                if (cursor == null || !cursor.moveToFirst()) {
                    null
                } else cursor.getString(cursor.getColumnIndex(ImageColumns.DATA))
            } finally {
                cursor?.close()
            }
        }
        return uri.path
    }

    private fun shouldRotate(resolver: ContentResolver, uri: Uri): Boolean {
        val exif: ExifInterface
        exif = try {
            val path = getPath(resolver, uri) ?: return false
            newInstance(path)
        } catch (e: IOException) {
            Log.e(TAG, "could not read exif info of the image: $uri")
            return false
        }
        val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1)
        return (orientation == ExifInterface.ORIENTATION_ROTATE_90
                || orientation == ExifInterface.ORIENTATION_ROTATE_270)
    }

    fun getSizeInMB(sizeInBytes: Long): Float {
        return java.lang.Float.valueOf(DecimalFormat("0.0").format((sizeInBytes.toFloat() / 1024 / 1024).toDouble()))
    }
}