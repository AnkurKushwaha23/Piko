package com.ankurkushwaha.piko.engine

import android.graphics.Bitmap
import androidx.collection.LruCache

/**
 * @author Ankur Kushwaha
 * Created on 21/06/2025 at 17:47
 */

object MemoryCache {
    private val lruCache: LruCache<String, Bitmap> =
        object : LruCache<String, Bitmap>(calculateMaxSize()) {
            override fun sizeOf(key: String, value: Bitmap): Int {
                return value.byteCount / 1024
            }
        }

    private fun calculateMaxSize(): Int = (Runtime.getRuntime().maxMemory() / 1024 / 8).toInt()

    fun get(key: String): Bitmap? = lruCache.get(key)
    fun put(key: String, bitmap: Bitmap) = lruCache.put(key, bitmap)
}