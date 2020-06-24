package com.naver.android.helloyako.imagecropsample.imageselect

import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavDirections
import com.seasia.prism.R
import java.io.Serializable
import java.lang.UnsupportedOperationException
import kotlin.Int
import kotlin.Suppress

class ImageSelectFragmentDirections private constructor() {
     data class ActionImageToCrop(val uri: Uri) : NavDirections {
        override fun getActionId(): Int = R.id.action_image_to_crop

        @Suppress("CAST_NEVER_SUCCEEDS")
        override fun getArguments(): Bundle {
            val result = Bundle()
            if (Parcelable::class.java.isAssignableFrom(Uri::class.java)) {
                result.putParcelable("uri", this.uri as Parcelable)
            } else if (Serializable::class.java.isAssignableFrom(Uri::class.java)) {
                result.putSerializable("uri", this.uri as Serializable)
            } else {
                throw UnsupportedOperationException(Uri::class.java.name +
                        " must implement Parcelable or Serializable or must be an Enum.")
            }
            return result
        }
    }

    companion object {
        fun actionImageToCrop(uri: Uri): NavDirections = ActionImageToCrop(uri)
    }
}
