package com.vikas.mobile.mysafenotes.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vikas.mobile.mysafenotes.R
import com.vikas.mobile.mysafenotes.data.entity.Note

class NoteListAdapter(private val dataSet: List<Note>,
                      private val onClick: (Note) -> Unit,
                      private val onDelete: (Note) -> Unit) :
    RecyclerView.Adapter<NoteListAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val noteContainerView: ViewGroup = view.findViewById(R.id.containerNoteListItem)
        val noteDeleteView: ImageButton = view.findViewById(R.id.note_delete)
        val noteHeaderView: TextView = view.findViewById(R.id.note_content_header)
        val noteContentView: TextView = view.findViewById(R.id.note_content_item)
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.note_list_item, viewGroup, false)
        val viewHolder = ViewHolder(view)

        viewHolder.noteContainerView.setOnClickListener {
            onClick(it.tag as Note)
        }

        viewHolder.noteDeleteView.setOnClickListener {
            onDelete(it.tag as Note)
        }
        return viewHolder
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val note = dataSet[position]
        viewHolder.noteHeaderView.text = getHeaderText(note.noteContent.content)
        viewHolder.noteContentView.text = getContentText(note.noteContent.content)
        viewHolder.noteContainerView.tag = note
        viewHolder.noteDeleteView.tag = note
    }

    private fun getHeaderText(noteContent: String): String {
        val defaultHeaderCharsCountLength = 35
        return noteContent.substring(startIndex = 0,
                endIndex = if (noteContent.indexOf("\n") == -1) defaultHeaderCharsCountLength else noteContent.indexOf("\n"))
    }

    private fun getContentText(noteContent: String): String {
        return noteContent.substring(startIndex = if (noteContent.indexOf("\n") == -1) 0 else noteContent.indexOf("\n")+1,
            endIndex = noteContent.length)
    }

    override fun getItemCount() = dataSet.size

}
