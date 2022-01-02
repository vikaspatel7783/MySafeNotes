package com.vikas.mobile.mynotes.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.vikas.mobile.mynotes.R
import com.vikas.mobile.mynotes.data.entity.MaskedData
import com.vikas.mobile.mynotes.data.entity.Note
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class AddUpdateNoteActivity : AppCompatActivity() {

    private val addUpdateNoteViewModel: AddUpdateNoteViewModel by viewModels()
    private lateinit var buttonAddUpdateNote: Button
    private lateinit var editTextNoteContent: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE)
        setContentView(R.layout.add_note_layout)

        actionBar?.setDisplayHomeAsUpEnabled(true)
        title = getCategoryName().toUpperCase(Locale.ROOT)

        buttonAddUpdateNote = findViewById(R.id.buttonAddUpdateNote)
        editTextNoteContent = findViewById(R.id.edtTextNote)

        if (this.getNoteId() != -1L) {
            addUpdateNoteViewModel.getNote(getNoteId()).observe(this, {
                findViewById<EditText>(R.id.edtTextNote).setText(it.noteContent.content)
            })
        }

        editTextNoteContent.addTextChangedListener {
            buttonAddUpdateNote.isEnabled = it.toString().isNotEmpty()
        }

        buttonAddUpdateNote.setOnClickListener {

            val noteContent = editTextNoteContent.text.toString()

            val noteObj = Note(categoryId = getCategoryId(), noteContent = MaskedData(noteContent))
            if (isNoteExist()) {
                noteObj.id = getNoteId()
            }
            addUpdateNoteViewModel.addUpdateNote(noteObj)
            finish()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun isNoteExist(): Boolean {
        return getNoteId() != -1L
    }

    private fun getCategoryId() = intent.getLongExtra(KEY_CATEGORY_ID, -1)

    private fun getCategoryName() = intent.getStringExtra(KEY_CATEGORY_NAME)!!

    private fun getNoteId() = intent.getLongExtra(KEY_NOTE_ID, -1)

    companion object {
        private const val KEY_CATEGORY_ID: String = "KEY_CATEGORY_ID"
        private const val KEY_CATEGORY_NAME: String = "KEY_CATEGORY_NAME"
        private const val KEY_NOTE_ID: String = "KEY_NOTE_ID"

        fun createIntent(context: Context, categoryId: Long, categoryName: String, noteId: Long? = -1): Intent {
            return Intent(context.applicationContext, AddUpdateNoteActivity::class.java).apply {
                this.putExtra(KEY_CATEGORY_ID, categoryId)
                this.putExtra(KEY_CATEGORY_NAME, categoryName)
                this.putExtra(KEY_NOTE_ID, noteId)
            }
        }
    }
}