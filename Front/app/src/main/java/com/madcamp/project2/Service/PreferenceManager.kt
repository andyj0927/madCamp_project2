package com.madcamp.project2.Service

import android.content.Context
import android.content.SharedPreferences

object PreferenceManager {
    val PREFERENCES_NAME = "JWT"
    val DEFAULT_VALUE_STRING = ""

    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
    }

    fun setString(context: Context, key: String, value: String) {
        val prefs: SharedPreferences = getPreferences(context)
        val editor: SharedPreferences.Editor = prefs.edit()
        editor.putString(key, value)
        editor.commit()
    }

    fun getString(context: Context, key: String): String {
        val prefs: SharedPreferences = getPreferences(context)

        return prefs.getString(key, DEFAULT_VALUE_STRING)!!
    }

    fun removeKey(context: Context, key: String) {
        val prefs: SharedPreferences = getPreferences(context)
        val edit: SharedPreferences.Editor = prefs.edit()
        edit.remove(key)
        edit.commit()
    }

    fun clear(context: Context) {
        val prefs: SharedPreferences = getPreferences(context)
        val edit: SharedPreferences.Editor = prefs.edit()
        edit.clear()
        edit.commit()
    }
}