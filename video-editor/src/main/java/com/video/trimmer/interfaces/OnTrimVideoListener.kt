package com.video.trimmer.interfaces

import android.net.Uri

public interface OnTrimVideoListener {
    fun onTrimStarted()
    fun getResult(uri: Uri)
    fun cancelAction()
    fun onError(message: String)
}
