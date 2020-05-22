package com.e.seasianoticeboard.utils

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.google.gson.Gson

class PrefStore(private val mAct: Context) {

    private val pref: SharedPreferences
        get() = PreferenceManager.getDefaultSharedPreferences(mAct)

    fun cleanPref() {
        val settings = pref
        settings.edit().clear().apply()
    }


    fun saveString(key: String, value: String) {
        val settings = pref
        val editor = settings.edit()
        editor.putString(key, value)
        editor.apply()
    }

    @JvmOverloads
    fun getString(key: String, defaultVal: String? = null): String? {
        val settings = pref
        return settings.getString(key, defaultVal)
    }

    // to save object in prefrence
    fun save(key: String?, `object`: Any?) {
        if (`object` == null) {
            throw IllegalArgumentException("object is null")
        }

        if (key == "" || key == null) {
            throw IllegalArgumentException("key is empty or null")
        }
        val settings = pref
        val editor = settings.edit()
        editor.putString(key, GSON.toJson(`object`)).apply()
    }

    companion object {

        private val GSON = Gson()
        private val gson = Gson()
    }
}