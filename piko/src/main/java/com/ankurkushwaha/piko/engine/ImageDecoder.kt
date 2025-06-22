package com.ankurkushwaha.piko.engine

import android.graphics.Bitmap
import android.graphics.BitmapFactory

/**
 * @author Ankur Kushwaha
 * Created on 21/06/2025 at 17:47
 */

object ImageDecoder {
    fun decode(bytes: ByteArray): Bitmap? {
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }
}