package com.sockytest.db

import android.content.Context
import com.sockytest.App

class SharePreferenceCache : LocalCache {
    companion object {
        private const val PREF_NAME = "SharePreferenceCache"
    }

    private val preferenceCache =
        App.getInstance()?.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    private val preferenceCacheEditor = preferenceCache?.edit()

    override fun getValue(id: String): Int {
        return preferenceCache?.getInt(id, 0) ?: 0
    }

    override fun storeValue(id: String, value: Int) {
        preferenceCacheEditor?.putInt(id, value)
        preferenceCacheEditor?.apply()
    }
}