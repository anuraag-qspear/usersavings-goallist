package com.sockytest.db

interface LocalCache {
    fun getValue(id: String): Int

    fun storeValue(id: String, value: Int)
}