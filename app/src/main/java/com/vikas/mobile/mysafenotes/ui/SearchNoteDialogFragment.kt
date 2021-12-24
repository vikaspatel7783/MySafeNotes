package com.vikas.mobile.mysafenotes.ui

import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.vikas.mobile.mysafenotes.R
import com.vikas.mobile.mysafenotes.adapter.NoteListAdapter
import com.vikas.mobile.mysafenotes.ui.dashboard.DashboardActivity
import com.vikas.mobile.mysafenotes.ui.dashboard.SearchNotesViewModel
import dagger.hilt.android.AndroidEntryPoint


/**
 *
 * A fragment that shows a list of items as a modal bottom sheet.
 *
 * You can show this modal bottom sheet from your activity like this:
 */
@AndroidEntryPoint
class SearchNoteDialogFragment : BottomSheetDialogFragment() {

    private val searchViewModel: SearchNotesViewModel by viewModels()

    lateinit var editTextSearchNote: EditText
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchCountView: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val inflatedView = inflater.inflate(R.layout.fragment_search_notes, container, false)
        recyclerView = inflatedView.findViewById(R.id.recyclerview_search_notes)
        editTextSearchNote = inflatedView.findViewById(R.id.edtTextSearchNote)
        searchCountView = inflatedView.findViewById(R.id.search_count)
        return inflatedView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        editTextSearchNote.addTextChangedListener { searchText ->
            val keyedString = searchText.toString().trim()

            searchViewModel.searchNotes(keyedString).observe(this, { notes ->

                searchCountView.text = notes.size.toString()

                val noteListAdapter = NoteListAdapter(
                        dataSet = notes,
                        onClick = { note ->
                            (activity as DashboardActivity).onNoteClicked(note)
                            dismiss()
                        },
                        onDelete = { note ->
                            Snackbar.make(view.rootView, "Delete note ?", Snackbar.LENGTH_LONG)
                                    .setAction("YES") { _ ->
                                        searchViewModel.deleteNote(note)
                                        dismiss()
                                    }.show()
                        },
                        onShare = { note ->
                            Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_TEXT, note.noteContent.content)
                                type = "text/plain"
                            }.let { shareIntent ->
                                startActivity(Intent.createChooser(shareIntent, null))
                                dismiss()
                            }
                        })

                recyclerView.layoutManager = LinearLayoutManager(context)
                recyclerView.adapter = noteListAdapter

            })
        }
    }

    companion object {

        fun newInstance(): SearchNoteDialogFragment =
                SearchNoteDialogFragment()

    }
}