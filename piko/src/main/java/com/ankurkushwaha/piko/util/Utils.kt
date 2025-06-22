package com.ankurkushwaha.piko.util

import java.security.MessageDigest

/**
 * @author Ankur Kushwaha
 * Created on 21/06/2025 at 17:41
 */

fun String.hash(): String {
    val digest = MessageDigest.getInstance("MD5")
    val result = digest.digest(toByteArray())
    return result.joinToString("") { "%02x".format(it) }
}