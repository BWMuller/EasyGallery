/*
 * Copyright 2017 Zhihu Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package za.co.bwmuller.easygallerycore.utils

import android.media.ExifInterface
import android.text.TextUtils
import android.util.Log
import java.io.IOException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

/**
 * Bug fixture for ExifInterface constructor.
 */
internal object ExifInterfaceCompat {

    private val TAG = ExifInterfaceCompat::class.java.simpleName
    private const val EXIF_DEGREE_FALLBACK_VALUE = -1

    /**
     * Creates new instance of [ExifInterface].
     * Original constructor won't check filename value, so if null value has been passed,
     * the process will be killed because of SIGSEGV.
     * Google Play crash report system cannot perceive this crash, so this method will throw
     * [NullPointerException] when the filename is null.
     *
     * @param filename a JPEG filename.
     * @return [ExifInterface] instance.
     * @throws IOException something wrong with I/O.
     */
    @JvmStatic
    @Throws(IOException::class)
    fun newInstance(filename: String): ExifInterface {
        return ExifInterface(filename)
    }

    private fun getExifDateTime(filepath: String): Date? {
        val exif: ExifInterface
        exif = try {
            // ExifInterface does not check whether file path is null or not,
            // so passing null file path argument to its constructor causing SIGSEGV.
            // We should avoid such a situation by checking file path string.
            newInstance(filepath)
        } catch (ex: IOException) {
            Log.e(TAG, "cannot read exif", ex)
            return null
        }
        val date = exif.getAttribute(ExifInterface.TAG_DATETIME)
        if (date.isNullOrBlank()) {
            return null
        }
        try {
            val formatter = SimpleDateFormat("yyyy:MM:dd HH:mm:ss", Locale.getDefault())
            formatter.timeZone = TimeZone.getTimeZone("UTC")
            return formatter.parse(date)
        } catch (e: ParseException) {
            Log.d(TAG, "failed to parse date taken", e)
        }
        return null
    }

    /**
     * Read exif info and get orientation value of the photo.
     *
     * @param filepath to get exif.
     * @return exif orientation value
     */
    fun getExifOrientation(filepath: String): Int {
        val exif: ExifInterface
        exif = try {
            // ExifInterface does not check whether file path is null or not,
            // so passing null file path argument to its constructor causing SIGSEGV.
            // We should avoid such a situation by checking file path string.
            newInstance(filepath)
        } catch (ex: IOException) {
            Log.e(TAG, "cannot read exif", ex)
            return EXIF_DEGREE_FALLBACK_VALUE
        }
        val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, EXIF_DEGREE_FALLBACK_VALUE)
        return if (orientation == EXIF_DEGREE_FALLBACK_VALUE) {
            0
        } else when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> 90
            ExifInterface.ORIENTATION_ROTATE_180 -> 180
            ExifInterface.ORIENTATION_ROTATE_270 -> 270
            else -> 0
        }
    }
}