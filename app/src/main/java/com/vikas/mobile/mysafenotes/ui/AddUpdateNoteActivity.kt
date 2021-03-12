package com.vikas.mobile.mysafenotes.ui

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.vikas.mobile.mysafenotes.R
import com.vikas.mobile.mysafenotes.data.entity.Note
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class AddUpdateNoteActivity : AppCompatActivity() {

    private val addUpdateNoteViewModel: AddUpdateNoteViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_note_layout)

        findViewById<TextView>(R.id.labelCategory).text = getCategoryName()!!.toUpperCase(Locale.ROOT)

        if (this.getNoteId() != -1L) {
            addUpdateNoteViewModel.getNote(getNoteId()).observe(this, {
                findViewById<EditText>(R.id.edtTextNote).setText(it.noteContent)
            })
        }

        findViewById<Button>(R.id.buttonAddNoteCancel).setOnClickListener {
            finish()
        }

        findViewById<Button>(R.id.buttonAddUpdateNote).setOnClickListener {

            val noteContent = findViewById<EditText>(R.id.edtTextNote).text.toString()

            val noteObj = Note(categoryId = getCategoryId(), noteContent = noteContent)
            if (isNoteExist()) {
                noteObj.id = getNoteId()
            }
            addUpdateNoteViewModel.addUpdateNote(noteObj)
            finish()
        }
    }

    private fun isNoteExist(): Boolean {
        return getNoteId() != -1L
    }

    private fun getCategoryId() = intent.getLongExtra(KEY_CATEGORY_ID, -1)

    private fun getCategoryName() = intent.getStringExtra(KEY_CATEGORY_NAME)

    private fun getNoteId() = intent.getLongExtra(KEY_NOTE_ID, -1)

    companion object {
        const val KEY_CATEGORY_ID: String = "KEY_CATEGORY_ID"
        const val KEY_CATEGORY_NAME: String = "KEY_CATEGORY_NAME"
        const val KEY_NOTE_ID: String = "KEY_NOTE_ID"
    }
}