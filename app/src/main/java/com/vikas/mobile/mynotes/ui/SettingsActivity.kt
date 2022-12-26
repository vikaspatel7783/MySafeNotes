package com.vikas.mobile.mynotes.ui

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment.*
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED
import androidx.work.WorkManager
import com.vikas.mobile.authandcrypto.BiometricPromptUtils
import com.vikas.mobile.mynotes.BuildConfig
import com.vikas.mobile.mynotes.R
import com.vikas.mobile.mynotes.utils.NotesImportExportUtil
import com.vikas.mobile.mynotes.data.entity.Setting
import dagger.hilt.android.AndroidEntryPoint
import com.vikas.mobile.mynotes.utils.observeOnce
import java.io.BufferedWriter
import java.io.File
import java.io.FileInputStream
import java.io.FileWriter


@AndroidEntryPoint
class SettingsActivity : AppCompatActivity() {

    private val settingsViewModel: SettingsViewModel by viewModels()
    private val STORAGE_WRITE_PERMISSION_CODE = 1

    private val externalImportExportFile =  lazy {
        getImportExportFilePath()
    }

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

        onExportNotesClick()

        onImportNotesClick()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getImportExportFilePath(): File {
        val externalStoragePublicDirectory = getExternalStoragePublicDirectory(DIRECTORY_DOCUMENTS)
        if (!externalStoragePublicDirectory.exists()) {
            externalStoragePublicDirectory.mkdir()
        }
        val exportJsonFile = File(externalStoragePublicDirectory, "safenotes.json")
        if (!exportJsonFile.exists()) {
            exportJsonFile.createNewFile()
        }
        return exportJsonFile
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

    private fun onExportNotesClick() {
        findViewById<ImageView>(R.id.ic_developer_export).setOnClickListener {
            checkStoragePermissionAndProceed {
                toggleExportProgressView(true)
                writeNotesToExternalStorage()
            }
        }
    }

    private fun checkStoragePermissionAndProceed(executeNext: () -> Unit) {
        when (ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            PackageManager.PERMISSION_GRANTED -> {
                executeNext()
            }
            else -> {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), STORAGE_WRITE_PERMISSION_CODE)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            STORAGE_WRITE_PERMISSION_CODE -> {
                if (grantResults[0] == PERMISSION_GRANTED) {
                    writeNotesToExternalStorage()
                } else {
                    Toast.makeText(this, "Please provide permission in order to proceed", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun writeNotesToExternalStorage() {
        settingsViewModel.getAllNotes().observeOnce(this, { notesExportList ->
            val fileWriter = FileWriter(externalImportExportFile.value)
            val bufferedWriter = BufferedWriter(fileWriter)
            NotesImportExportUtil.writeToFile(bufferedWriter, notesExportList)
            bufferedWriter.close()

            toggleExportProgressView(false)
            Toast.makeText(this, "Notes are exported to\n"+externalImportExportFile.value.canonicalFile.toString(), Toast.LENGTH_LONG).show()
            finish()
        })
    }

    private fun onImportNotesClick() {
        findViewById<ImageView>(R.id.ic_notes_import).setOnClickListener {
            checkStoragePermissionAndProceed {
                toggleImportProgressView(true)
                importNotes()
            }
        }
    }

    private fun importNotes() {
        NotesImportExportUtil.readFromFile(FileInputStream(externalImportExportFile.value)).let { jsonObject ->
            if (jsonObject == null) {
                Toast.makeText(this, "No notes found to import", Toast.LENGTH_LONG).show()
                toggleImportProgressView(false)
                return@let
            }
            settingsViewModel.importNotes(jsonObject).observe(this, { done ->
                if (done) {
                    toggleImportProgressView(false)
                    Toast.makeText(this, "Notes successfully imported", Toast.LENGTH_LONG).show()
                    finish()
                }
            })
        }
    }

    private fun getAuthSwitchToggleButton(): ToggleButton {
        return this.findViewById(R.id.settings_auth_switch)
    }

    private fun toggleExportProgressView(show: Boolean) {
        findViewById<ImageView>(R.id.ic_developer_export).visibility = if (show) View.INVISIBLE else View.VISIBLE
        findViewById<ProgressBar>(R.id.progress_export_note).visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun toggleImportProgressView(show: Boolean) {
        findViewById<ImageView>(R.id.ic_notes_import).visibility = if (show) View.INVISIBLE else View.VISIBLE
        findViewById<ProgressBar>(R.id.progress_import_note).visibility = if (show) View.VISIBLE else View.GONE
    }

}