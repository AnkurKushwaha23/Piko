package com.ankurkushwaha.piko.transformation

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.widget.ImageView
import androidx.core.graphics.createBitmap
import androidx.core.graphics.scale

/**
 * @author Ankur Kushwaha
 * Created on 21/06/2025 at 17:48
 */

fun transform(bitmap: Bitmap, imageView: ImageView, scaleType: ScaleType): Bitmap {
    return when (scaleType) {
        ScaleType.CENTER_CROP -> centerCrop(bitmap, imageView)
        ScaleType.FIT_CENTER -> fitCenter(bitmap, imageView)
        ScaleType.CENTER_INSIDE -> centerInside(bitmap, imageView)
        ScaleType.CIRCLE_CROP -> circleCrop(bitmap)
    }
}

fun centerCrop(src: Bitmap, imageView: ImageView): Bitmap {
    val viewW = imageView.width
    val viewH = imageView.height
    val srcRatio = src.width.toFloat() / src.height
    val dstRatio = viewW.toFloat() / viewH
    val scaled = if (srcRatio > dstRatio) {
        val height = viewH
        val width = (height * srcRatio).toInt()
        src.scale(width, height)
    } else {
        val width = viewW
        val height = (width / srcRatio).toInt()
        src.scale(width, height)
    }
    val xOffset = (scaled.width - viewW) / 2
    val yOffset = (scaled.height - viewH) / 2
    return Bitmap.createBitmap(scaled, xOffset, yOffset, viewW, viewH)
}

fun fitCenter(src: Bitmap, imageView: ImageView): Bitmap {
    val widthRatio = imageView.width.toFloat() / src.width
    val heightRatio = imageView.height.toFloat() / src.height
    val scale = minOf(widthRatio, heightRatio)
    val newWidth = (src.width * scale).toInt()
    val newHeight = (src.height * scale).toInt()
    return src.scale(newWidth, newHeight)
}

fun centerInside(src: Bitmap, imageView: ImageView): Bitmap {
    return fitCenter(src, imageView) // for simplicity
}

fun circleCrop(src: Bitmap): Bitmap {
    val size = minOf(src.width, src.height)
    val x = (src.width - size) / 2
    val y = (src.height - size) / 2
    val squared = Bitmap.createBitmap(src, x, y, size, size)
    val output = createBitmap(size, size)
    val canvas = Canvas(output)
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val rect = Rect(0, 0, size, size)
    canvas.drawCircle(size / 2f, size / 2f, size / 2f, paint)
    paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
    canvas.drawBitmap(squared, rect, rect, paint)
    return output
}