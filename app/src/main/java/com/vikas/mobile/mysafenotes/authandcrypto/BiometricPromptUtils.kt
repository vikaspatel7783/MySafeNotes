package com.vikas.mobile.mysafenotes.authandcrypto
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_WEAK
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import com.vikas.mobile.mysafenotes.R

/**
 * Usage
 * ----------------------------
 * val crypto = Crypto.INSTANCE
 * val encodedCypherText = crypto.encrypt("ICICI Bank, Account Id: 123456, password: 1234")
 * System.out.println("-----> EncodedCypherText: ${encodedCypherText}")
 * val originalText = crypto.decrypt(encodedCypherText)
 * System.out.println("-----> Original text: ${originalText}")
 *
 */

object BiometricPromptUtils {

    fun showUserAuthentication(activity: AppCompatActivity, appAuthCallback: AppAuthCallback) {
        if (BiometricManager.from(activity).canAuthenticate(BIOMETRIC_WEAK or DEVICE_CREDENTIAL) == BiometricManager.BIOMETRIC_SUCCESS) {
            val biometricPrompt = createBiometricPrompt(activity, authCallback = appAuthCallback)
            val promptInfo = createPromptInfo(activity.applicationContext)
            biometricPrompt.authenticate(promptInfo)
        } else {
            appAuthCallback.authenticationsNotPresent()
        }
    }

    private fun createBiometricPrompt(activity: AppCompatActivity, authCallback: AppAuthCallback): BiometricPrompt {
        val executor = ContextCompat.getMainExecutor(activity)

        val callback = object : BiometricPrompt.AuthenticationCallback() {

            override fun onAuthenticationError(errCode: Int, errString: CharSequence) {
                // TODO: check need
            }

            override fun onAuthenticationFailed() {
                // TODO: check need
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                authCallback.onAuthenticationSucceeded()
            }
        }
        return BiometricPrompt(activity, executor, callback)
    }

    private fun createPromptInfo(context: Context): BiometricPrompt.PromptInfo =
        BiometricPrompt.PromptInfo.Builder().apply {
            setTitle(context.getString(R.string.prompt_info_title))
            setSubtitle(context.getString(R.string.prompt_info_subtitle))
            setDescription(context.getString(R.string.prompt_info_description))
            setConfirmationRequired(false)
            setAllowedAuthenticators(BIOMETRIC_WEAK or DEVICE_CREDENTIAL)
        }.build()

}