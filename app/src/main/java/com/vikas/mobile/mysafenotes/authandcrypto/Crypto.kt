package com.vikas.mobile.mysafenotes.authandcrypto

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties.*
import java.security.*
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

class Crypto private constructor() {

    fun encrypt(data: String): String {
        KeystoreHelper.loadKeystore()
        val cipher = Cipher.getInstance(AES_CIPHER)
        cipher.init(Cipher.ENCRYPT_MODE, KeystoreHelper.getKey(), IvParameterSpec(ByteArray(16)))
        cipher.doFinal(data.toByteArray()).let {
            return Base64Helper.encode(it)!!
        }
    }

    fun decrypt(data: String): String {
        KeystoreHelper.loadKeystore()
        Base64Helper.decode(data).let {
            val cipher = Cipher.getInstance(AES_CIPHER)
            cipher.init(Cipher.DECRYPT_MODE, KeystoreHelper.getKey(), IvParameterSpec(ByteArray(16)))
            return String(cipher.doFinal(it))
        }
    }

    companion object {
        private const val AES_CIPHER = "$KEY_ALGORITHM_AES/$BLOCK_MODE_CBC/$ENCRYPTION_PADDING_PKCS7"
        val INSTANCE: Crypto = Crypto()
    }

     object KeystoreHelper {

        private const val ANDROID_KEY_STORE = "AndroidKeyStore"
        private const val KEY_NAME = "VikasSafeNotesKey"

        init {
            createSecretKey()
        }

        fun loadKeystore(): KeyStore {
            KeyStore.getInstance(ANDROID_KEY_STORE).apply {
                load(null)
                return this
            }
        }

        private fun isSecretKeyGenerated(): Boolean {
            try {
                loadKeystore().let {
                    return it.containsAlias(KEY_NAME)
                }
            } catch (e: KeyStoreException) {
                throw RuntimeException("Failed to get an instance of KeyStore", e)
            }
        }

        private fun createSecretKey() {

            if (isSecretKeyGenerated()) {
                return
            }

            try {
                loadKeystore()
                val keyGenerator: KeyGenerator = KeyGenerator.getInstance(KEY_ALGORITHM_AES, ANDROID_KEY_STORE)

                val keyProperties = PURPOSE_ENCRYPT or PURPOSE_DECRYPT
                val builder = KeyGenParameterSpec.Builder(KEY_NAME, keyProperties)
                    .setBlockModes(BLOCK_MODE_CBC)
                    .setEncryptionPaddings(ENCRYPTION_PADDING_PKCS7)
                    .setRandomizedEncryptionRequired(false)

                keyGenerator.run {
                    init (builder.build())
                    generateKey()
                }

            } catch (e: Exception) {
                when (e) {
                    is NoSuchAlgorithmException,
                    is NoSuchProviderException ->
                        throw RuntimeException("Failed to get an instance of KeyGenerator", e)
                    else -> throw e
                }
            }
        }

        fun getKey(): Key {
            loadKeystore().let {
                return it.getKey(KEY_NAME, null) as SecretKey
            }
        }

        fun removeKey() {
            loadKeystore().let {
                it.deleteEntry(KEY_NAME)
            }
        }

    }
}