package com.vikas.mobile.mysafenotes.authandcrypto

interface AppAuthCallback {
    fun onAuthenticationSucceeded()
    fun authenticationsNotPresent()
}