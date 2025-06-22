package com.ankurkushwaha.piko.downloader

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import java.io.IOException

/**
 * @author Ankur Kushwaha
 * Created on 21/06/2025 at 17:42
 */

object Downloader {
    private val client: OkHttpClient

    init {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    fun download(url: String): ByteArray? {
        val request = Request.Builder()
            .url(url)
            .build()

        return try {
            client.newCall(request).execute().use { response ->
                if (response.isSuccessful) {
                    response.body?.bytes()
                } else {
                    null
                }
            }
        } catch (e: IOException) {
            null
        }
    }
}