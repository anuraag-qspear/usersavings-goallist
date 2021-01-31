package com.sockytest

import android.app.Application

class App : Application() {
    companion object {
        private var instance: App? = null

        fun getInstance() = instance
    }

    override fun onCreate() {
        super.onCreate()

        instance = this
    }
}