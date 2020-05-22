package com.e.seasianoticeboard.camera.audio

import android.content.Context
import android.database.Cursor
import android.net.Uri

class UtilKotlin {

    companion object{
        fun getAbsolutePath(activity: Context, uri: Uri): String {

            if ("content".equals(uri.scheme, ignoreCase = true)) {
                val projection = arrayOf("_data")
                var cursor: Cursor? = null
                try {
                    cursor = activity.contentResolver.query(uri, projection, null, null, null)
                    val column_index = cursor!!.getColumnIndexOrThrow("_data")
                    if (cursor.moveToFirst()) {
                        return cursor.getString(column_index)
                    }
                } catch (e: Exception) {
                    // Eat it
                    e.printStackTrace()
                }

            } else if ("file".equals(uri.scheme, ignoreCase = true)) {
                return uri.path!!
            }

            return ""
        }

    }


}