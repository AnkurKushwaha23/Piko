package com.ankurkushwaha.piko.core

import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.core.graphics.toColorInt
import com.ankurkushwaha.piko.engine.RequestDispatcher
import com.ankurkushwaha.piko.listener.RequestListener
import com.ankurkushwaha.piko.transformation.ScaleType
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerDrawable

/**
 * @author Ankur Kushwaha
 * Created on 21/06/2025 at 17:43
 */

class RequestBuilder(
    private val context: Context,
    private val url: String? = null,
    private val uri: Uri? = null
) {
    private var placeholderRes: Int? = null
    private var errorRes: Int? = null
    private var contentDesc: String? = null
    private var requestListener: RequestListener? = null
    private var scaleType: ScaleType? = null

    private var useShimmer: Boolean = false
    private var shimmerBackgroundColor: Int = Color.LTGRAY
    private var shimmerForegroundColor: Int = Color.LTGRAY
    private var shimmerDirection : Int = Shimmer.Direction.LEFT_TO_RIGHT
    private var shimmerDuration : Long = 1000

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

    fun showShimmer(
        @ColorInt foregroundColor: Int = Color.LTGRAY,
        @ColorInt backgroundColor: Int = "#E0E0E0".toColorInt(),
        shimmerDirection : Int = Shimmer.Direction.LEFT_TO_RIGHT,
        shimmerDuration : Long = 1000
    ): RequestBuilder {
        this.useShimmer = true
        this.shimmerForegroundColor = foregroundColor
        this.shimmerBackgroundColor = backgroundColor
        this.shimmerDirection = shimmerDirection
        this.shimmerDuration = shimmerDuration
        return this
    }

    fun into(imageView: ImageView) {
        if (useShimmer) {
            imageView.setImageDrawable(createShimmerDrawable())
        } else {
            placeholderRes?.let { imageView.setImageResource(it) }
        }

        val dispatcher = RequestDispatcher(
            context = context,
            imageView = imageView,
            placeholderRes = placeholderRes,
            errorRes = errorRes,
            listener = requestListener,
            scaleType = scaleType
        )

        when {
            url != null -> dispatcher.load(url)
            uri != null -> dispatcher.load(uri)
            else -> throw IllegalArgumentException("Image source must not be null")
        }
    }

    private fun createShimmerDrawable(): ShimmerDrawable {
        val shimmer = Shimmer.ColorHighlightBuilder()
            .setBaseColor(shimmerBackgroundColor)
            .setHighlightColor(shimmerForegroundColor)
            .setDirection(shimmerDirection)
            .setDuration(shimmerDuration)
            .build()

        return ShimmerDrawable().apply {
            setShimmer(shimmer)
        }
    }
}