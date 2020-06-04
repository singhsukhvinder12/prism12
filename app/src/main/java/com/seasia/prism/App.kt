package com.seasia.prism

import android.app.Application
import com.seasia.prism.util.MediaLoader
import com.yanzhenjie.album.Album
import com.yanzhenjie.album.AlbumConfig
import java.util.*


class App : Application() {

    override fun onCreate() {
        super.onCreate()
        app = this

        //application class oncreate
        //application class oncreate
        Album.initialize(
            AlbumConfig.newBuilder(this)
                .setAlbumLoader(MediaLoader())
                .setLocale(Locale.getDefault())
                .build()
        )
    }

    companion object {

        @get:Synchronized
        lateinit var app: App
    }
}