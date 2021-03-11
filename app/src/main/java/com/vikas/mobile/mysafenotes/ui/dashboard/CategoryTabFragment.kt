package com.vikas.mobile.mysafenotes.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vikas.mobile.mysafenotes.R
import com.vikas.mobile.mysafenotes.adapter.NoteListAdapter
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

        categoryTabViewModel.getNotes(categoryId).observe(viewLifecycleOwner, {
            val noteListAdapter = NoteListAdapter(it) { note ->
                Toast.makeText(
                    root.context.applicationContext,
                    note.noteContent,
                    Toast.LENGTH_SHORT
                ).show()
            }
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