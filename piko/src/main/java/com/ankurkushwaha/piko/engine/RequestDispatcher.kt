package com.ankurkushwaha.piko.engine

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.view.ViewTreeObserver
import android.widget.ImageView
import com.ankurkushwaha.piko.R
import com.ankurkushwaha.piko.downloader.Downloader
import com.ankurkushwaha.piko.listener.RequestListener
import com.ankurkushwaha.piko.transformation.ScaleType
import com.ankurkushwaha.piko.transformation.transform
import com.ankurkushwaha.piko.util.hash
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @author Ankur Kushwaha
 * Created on 21/06/2025 at 17:46
 */

class RequestDispatcher(
    private val context: Context,
    private val imageView: ImageView,
    private val placeholderRes: Int?,
    private val errorRes: Int?,
    private val listener: RequestListener?,
    private val scaleType: ScaleType?
) {
    private val diskCache = DiskCache(context)

    fun load(url: String) {
        val key = url.hash()
        imageView.setTag(R.id.piko_tag_url, url)
        MemoryCache.get(key)?.let {
            val currentTag = imageView.getTag(R.id.piko_tag_url) as? String
            if (currentTag == url) {
                // Only set if tag matches
                listener?.onResourceReady(it)
                imageView.setImageBitmap(it)
            }
            return
        }

        imageView.viewTreeObserver.addOnPreDrawListener(object :
            ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                imageView.viewTreeObserver.removeOnPreDrawListener(this)
                actuallyLoad(url, key)
                return true
            }
        })
    }

    private fun actuallyLoad(url: String, key: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val diskBitmap = diskCache.load(key)
            if (diskBitmap != null) {
                val transformed =
                    scaleType?.let { transform(diskBitmap, imageView, it) } ?: diskBitmap
                MemoryCache.put(key, transformed)
                withContext(Dispatchers.Main) {
                    listener?.onResourceReady(transformed)
                    imageView.setImageBitmap(transformed)
                }
                return@launch
            }

            val bytes = Downloader.download(url)
            val bitmap = bytes?.let { ImageDecoder.decode(it) }
            if (bitmap != null) {
                // ✅ Save untransformed to disk and memory cache
                diskCache.save(key, bitmap)
                val transformed = scaleType?.let { transform(bitmap, imageView, it) } ?: bitmap
                MemoryCache.put(key, transformed)
                withContext(Dispatchers.Main) {
                    val currentTag = imageView.getTag(R.id.piko_tag_url) as? String
                    if (currentTag == url) {
                        listener?.onResourceReady(transformed)
                        imageView.setImageBitmap(transformed)
                    }
                }
            } else {
                withContext(Dispatchers.Main) {
                    listener?.onLoadFailed(Exception("Failed to download or decode image"))
                    errorRes?.let { imageView.setImageResource(it) }
                }
            }
        }
    }

    fun load(uri: Uri) {
        val key = uri.toString().hash()
        imageView.setTag(R.id.piko_tag_url, uri.toString())

        // Check memory cache first
        MemoryCache.get(key)?.let {
            val currentTag = imageView.getTag(R.id.piko_tag_url) as? String
            if (currentTag == uri.toString()) {
                listener?.onResourceReady(it)
                imageView.setImageBitmap(it)
            }
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val inputStream = context.contentResolver.openInputStream(uri)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                inputStream?.close()

                if (bitmap != null) {
                    val transformed = scaleType?.let { transform(bitmap, imageView, it) } ?: bitmap
                    MemoryCache.put(key, transformed)

                    withContext(Dispatchers.Main) {
                        val currentTag = imageView.getTag(R.id.piko_tag_url) as? String
                        if (currentTag == uri.toString()) {
                            listener?.onResourceReady(transformed)
                            imageView.setImageBitmap(transformed)
                        }
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        listener?.onLoadFailed(Exception("Failed to decode bitmap from URI"))
                        errorRes?.let { imageView.setImageResource(it) }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    listener?.onLoadFailed(e)
                    errorRes?.let { imageView.setImageResource(it) }
                }
            }
        }
    }
}