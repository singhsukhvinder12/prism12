package com.naver.android.helloyako.imagecropsample.crop

import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavArgs
import java.io.Serializable
import java.lang.IllegalArgumentException
import java.lang.UnsupportedOperationException
import kotlin.Suppress
import kotlin.jvm.JvmStatic

data class CropFragmentArgs(val uri: Uri) : NavArgs {
    @Suppress("CAST_NEVER_SUCCEEDS")
    fun toBundle(): Bundle {
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

    companion object {
        @JvmStatic
        fun fromBundle(bundle: Bundle): CropFragmentArgs {
            bundle.setClassLoader(CropFragmentArgs::class.java.classLoader)
            val __uri : Uri?
            if (bundle.containsKey("uri")) {
                if (Parcelable::class.java.isAssignableFrom(Uri::class.java) ||
                        Serializable::class.java.isAssignableFrom(Uri::class.java)) {
                    __uri = bundle.get("uri") as Uri?
                } else {
                    throw UnsupportedOperationException(Uri::class.java.name +
                            " must implement Parcelable or Serializable or must be an Enum.")
                }
                if (__uri == null) {
                    throw IllegalArgumentException("Argument \"uri\" is marked as non-null but was passed a null value.")
                }
            } else {
                throw IllegalArgumentException("Required argument \"uri\" is missing and does not have an android:defaultValue")
            }
            return CropFragmentArgs(__uri)
        }
    }
}
