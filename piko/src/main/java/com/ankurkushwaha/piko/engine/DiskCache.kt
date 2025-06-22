package com.ankurkushwaha.piko.engine

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.ankurkushwaha.piko.util.hash
import com.jakewharton.disklrucache.DiskLruCache
import java.io.File

/**
 * @author Ankur Kushwaha
 * Created on 21/06/2025 at 17:46
 */

class DiskCache(context: Context) {
    private val diskLruCache: DiskLruCache

    init {
        val cacheDir = File(context.cacheDir, "piko_image_cache")
        val appVersion = 1
        val valueCount = 1
        val maxSize = 50L * 1024 * 1024 // 50MB
        diskLruCache = DiskLruCache.open(cacheDir, appVersion, valueCount, maxSize)
    }

    fun save(key: String, bitmap: Bitmap) {
        val editor = diskLruCache.edit(key.hash()) ?: return
        try {
            editor.newOutputStream(0).use { out ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            }
            editor.commit()
        } catch (e: Exception) {
            editor.abort()
        }
    }

    fun load(key: String): Bitmap? {
        return try {
            val snapshot = diskLruCache.get(key.hash()) ?: return null
            snapshot.getInputStream(0).use { input -> BitmapFactory.decodeStream(input) }
        } catch (e: Exception) {
            null
        }
    }
}