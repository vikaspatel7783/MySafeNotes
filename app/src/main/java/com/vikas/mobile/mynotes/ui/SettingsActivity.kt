package com.vikas.mobile.mynotes.ui

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.vikas.mobile.authandcrypto.BiometricPromptUtils
import com.vikas.mobile.mynotes.BuildConfig
import com.vikas.mobile.mynotes.R
import com.vikas.mobile.mynotes.data.entity.Setting
import dagger.hilt.android.AndroidEntryPoint
import observeOnce


@AndroidEntryPoint
class SettingsActivity : AppCompatActivity() {

    private val settingsViewModel: SettingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_layout)

        settingsViewModel.getAuthSetting().observeOnce(this, { authSetting ->
            getAuthSwitchToggleButton().isChecked =
                Setting.interpretAuthSettingValue(authSetting.value)
        })

        assignVersion()

        onEmailClick()

        onAuthToggle()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun onAuthToggle() {
        getAuthSwitchToggleButton().setOnCheckedChangeListener { toggleButtonView, isChecked ->
            run {
                if (isChecked) {
                    if (!BiometricPromptUtils.isDeviceSecured(this)) {
                        handleIfDeviceNotSecured(toggleButtonView)
                        return@run
                    }
                }
                settingsViewModel.updateAuthSetting(
                    Setting(
                        name = Setting.AUTH_SETTINGS_NAME,
                        value = if (isChecked) Setting.AUTH_ON else Setting.AUTH_OFF
                    )
                )
            }
        }
    }

    private fun handleIfDeviceNotSecured(toggleButtonView: CompoundButton) {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.error_no_authentication_title))
            .setMessage(getString(R.string.error_no_authentication_method))
            .setCancelable(false)
            .setPositiveButton(android.R.string.ok) { dg: DialogInterface, _: Int ->
                toggleButtonView.isChecked = false
                dg.dismiss()
            }
            .show()
    }

    private fun onEmailClick() {
        findViewById<ImageView>(R.id.ic_developer_email).setOnClickListener {
            val email = Intent(Intent.ACTION_SEND)
            email.putExtra(
                Intent.EXTRA_EMAIL,
                arrayOf(getString(R.string.settings_developer_email))
            )
            email.putExtra(Intent.EXTRA_SUBJECT, "MyNotes")
            email.type = "message/rfc822"
            startActivity(Intent.createChooser(email, "Choose an Email client :"))
        }
    }

    private fun assignVersion() {
        findViewById<TextView>(R.id.value_settings_version).text = BuildConfig.VERSION_NAME
    }

    private fun getAuthSwitchToggleButton(): ToggleButton {
        return this.findViewById(R.id.settings_auth_switch)
    }
}