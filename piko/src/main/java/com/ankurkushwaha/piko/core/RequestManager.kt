package com.ankurkushwaha.piko.core

import android.content.Context
import android.net.Uri

/**
 * @author Ankur Kushwaha
 * Created on 21/06/2025 at 17:43
 */

class RequestManager(private val context: Context) {
    fun load(url: String): RequestBuilder {
        return RequestBuilder(context, url =  url)
    }

    fun load(uri: Uri): RequestBuilder {
        return RequestBuilder(context, uri =  uri)
    }
}