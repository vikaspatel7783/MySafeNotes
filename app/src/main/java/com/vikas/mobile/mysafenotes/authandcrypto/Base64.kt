package com.vikas.mobile.mysafenotes.authandcrypto

import android.util.Base64

object Base64Helper {


    fun encode(data: ByteArray, flag: Int = Base64.NO_WRAP): String? {
        return Base64.encodeToString(data, flag)
    }

    fun decode(data: String, flag: Int = Base64.NO_WRAP): ByteArray? {
        return Base64.decode(data, flag)
    }

}