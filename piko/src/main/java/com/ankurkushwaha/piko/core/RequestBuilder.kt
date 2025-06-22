package com.ankurkushwaha.piko.core

import android.content.Context
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.ankurkushwaha.piko.engine.RequestDispatcher
import com.ankurkushwaha.piko.listener.RequestListener
import com.ankurkushwaha.piko.transformation.ScaleType

/**
 * @author Ankur Kushwaha
 * Created on 21/06/2025 at 17:43
 */

class RequestBuilder(
    private val context: Context,
    private val url: String
) {
    private var placeholderRes: Int? = null
    private var errorRes: Int? = null
    private var contentDesc: String? = null
    private var requestListener: RequestListener? = null
    private var scaleType: ScaleType? = null

    fun description(contentDesc: String): RequestBuilder {
        this.contentDesc = contentDesc
        return this
    }

    fun placeholder(@DrawableRes resId: Int): RequestBuilder {
        this.placeholderRes = resId
        return this
    }

    fun error(@DrawableRes resId: Int): RequestBuilder {
        this.errorRes = resId
        return this
    }

    fun listener(listener: RequestListener): RequestBuilder {
        this.requestListener = listener
        return this
    }

    fun centerCrop() = apply { scaleType = ScaleType.CENTER_CROP }
    fun fitCenter() = apply { scaleType = ScaleType.FIT_CENTER }
    fun centerInside() = apply { scaleType = ScaleType.CENTER_INSIDE }
    fun circleCrop() = apply { scaleType = ScaleType.CIRCLE_CROP }

    fun into(imageView: ImageView) {
        placeholderRes?.let { imageView.setImageResource(it) }
        contentDesc?.let { imageView.contentDescription = it }

        RequestDispatcher(
            context = context,
            imageView = imageView,
            placeholderRes = placeholderRes,
            errorRes = errorRes,
            listener = requestListener,
            scaleType = scaleType
        ).load(url)
    }
}