package com.e.seasianoticeboard

import android.app.Application

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        app = this
    }

    companion object {

        @get:Synchronized
        lateinit var app: App
    }
}