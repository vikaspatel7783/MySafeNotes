package com.vikas.mobile.mynotes.ui

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import android.widget.ToggleButton
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
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

        findViewById<TextView>(R.id.value_settings_version).text = BuildConfig.VERSION_NAME

        findViewById<ImageButton>(R.id.ic_developer_email).setOnClickListener {
            val email = Intent(Intent.ACTION_SEND)
            email.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.settings_developer_email)))
            email.putExtra(Intent.EXTRA_SUBJECT, "MyNotes")
            email.type = "message/rfc822"
            startActivity(Intent.createChooser(email, "Choose an Email client :"))
        }

        getAuthSwitchToggleButton().setOnCheckedChangeListener { buttonView, isChecked ->
            run {
                    settingsViewModel.updateAuthSetting(
                        Setting(
                            name = Setting.AUTH_SETTINGS_NAME,
                            value = if (isChecked) Setting.AUTH_ON else Setting.AUTH_OFF
                        )
                    )
            }
        }
    }

    private fun getAuthSwitchToggleButton(): ToggleButton {
        return this.findViewById(R.id.settings_auth_switch)
    }
}