package com.vikas.mobile.mynotes.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.vikas.mobile.mynotes.R
import com.vikas.mobile.mynotes.adapter.NoteListAdapter
import dagger.hilt.android.AndroidEntryPoint

/**
 * A placeholder fragment containing a simple view.
 */
@AndroidEntryPoint
class CategoryTabFragment(private val categoryId: Long) : Fragment() {

    private val categoryTabViewModel: CategoryTabViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        categoryTabViewModel = ViewModelProvider(this, CategoryTabViewModel.CategoryTabViewModelFactory()).get(CategoryTabViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_category, container, false)
        val recyclerView: RecyclerView = root.findViewById(R.id.recyclerview_notes)

        categoryTabViewModel.getNotes(categoryId).observe(viewLifecycleOwner, { it ->

            val noteListAdapter = NoteListAdapter(
                root.context,
                    dataSet = it,

            onClick = {
                (activity as DashboardActivity).onNoteClicked(it)
            },

            onDelete = { note ->
                val dialog = BottomSheetDialog(requireContext())
                val dialogView = layoutInflater.inflate(R.layout.confirm_note_category_deletion_layout, null)
                dialog.setContentView(dialogView)

                dialogView.findViewById<TextView>(R.id.labelConfirmDeletion).text = getString(R.string.prompt_confirm_delete_note)
                dialogView.findViewById<Button>(R.id.buttonYes).setOnClickListener {
                    categoryTabViewModel.deleteNote(note)
                    dialog.dismiss()
                }
                dialogView.findViewById<Button>(R.id.buttonCancel).setOnClickListener {
                    dialog.dismiss()
                }
                dialog.show()
            },

            onShare = { note ->
                Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, note.noteContent.content)
                    type = "text/plain"
                }.let { shareIntent ->
                    startActivity(Intent.createChooser(shareIntent, null))
                }
            })

            recyclerView.layoutManager = LinearLayoutManager(context)
            recyclerView.adapter = noteListAdapter
        })

        return root
    }

    companion object {

        @JvmStatic
        fun newInstance(categoryId: Long): CategoryTabFragment {
            return CategoryTabFragment(categoryId)
        }
    }
}