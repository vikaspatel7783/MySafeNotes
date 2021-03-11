package com.vikas.mobile.mysafenotes.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.MainThread
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import com.vikas.mobile.mysafenotes.R
import com.vikas.mobile.mysafenotes.data.entity.Category
import com.vikas.mobile.mysafenotes.ui.AddCategoryDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class AddNoteActivity : AppCompatActivity() {

    companion object {
        const val KEY_CATEGORY_NAME: String = "KEY_CATEGORY_NAME"
        const val KEY_NOTE_CONTENT: String = "KEY_NOTE_CONTENT"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_note_layout)

        findViewById<TextView>(R.id.labelCategory).text = intent.getStringExtra(KEY_CATEGORY_NAME)!!.toUpperCase(Locale.ROOT)

        findViewById<Button>(R.id.buttonAddCategoryCancel).setOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }

        findViewById<Button>(R.id.buttonAddCategoryAdd).setOnClickListener {

            val noteContent = findViewById<EditText>(R.id.edtTextNote).text

            Intent().putExtra(KEY_NOTE_CONTENT, noteContent).run {
                setResult(RESULT_OK, this)
                finish()
            }
        }

    }

}