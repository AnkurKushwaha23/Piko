package com.ankurkushwaha.piko.listener

import android.graphics.Bitmap

/**
 * @author Ankur Kushwaha
 * Created on 21/06/2025 at 17:44
 */

interface RequestListener {
    fun onLoadFailed(exception: Exception?)
    fun onResourceReady(bitmap: Bitmap)
}