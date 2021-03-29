package com.vikas.mobile.mysafenotes.authandcrypto

import java.security.MessageDigest

fun hash256(input: String): String = hashString(input, "SHA-256")

private fun hashString(input: String, algorithm: String): String {
    return MessageDigest
            .getInstance(algorithm)
            .digest(input.toByteArray())
            .fold("", { str, it -> str + "%02x".format(it) })
}