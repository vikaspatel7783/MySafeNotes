package com.vikas.mobile.mysafenotes.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vikas.mobile.mysafenotes.R
import com.vikas.mobile.mysafenotes.data.entity.Note

class NoteListAdapter(private val dataSet: List<Note>, private val onClick: (Note) -> Unit) :
    RecyclerView.Adapter<NoteListAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val noteContainerView: ViewGroup = view.findViewById(R.id.containerNoteListItem)
        val noteContentView: TextView = view.findViewById(R.id.note_content_item)

    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.note_list_item, viewGroup, false)
        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.noteContentView.text = dataSet[position].noteContent
        viewHolder.noteContainerView.setOnClickListener { onClick(dataSet[position]) }
    }

    override fun getItemCount() = dataSet.size

}
