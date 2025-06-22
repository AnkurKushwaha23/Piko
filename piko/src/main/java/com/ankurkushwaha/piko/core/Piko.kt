package com.ankurkushwaha.piko.core

import android.content.Context

/**
 * @author Ankur Kushwaha
 * Created on 21/06/2025 at 17:42
 */

object Piko {
    fun with(context: Context): RequestManager {
        return RequestManager(context)
    }
}