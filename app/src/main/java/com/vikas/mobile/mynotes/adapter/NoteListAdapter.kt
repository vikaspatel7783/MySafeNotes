package com.vikas.mobile.mynotes.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vikas.mobile.mynotes.R
import com.vikas.mobile.mynotes.data.entity.Note
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class NoteListAdapter(private val context: Context,
                      private val dataSet: List<Note>,
                      private val onClick: (Note) -> Unit,
                      private val onDelete: (Note) -> Unit,
                      private val onShare: (Note) -> Unit) :
    RecyclerView.Adapter<NoteListAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val noteContainerView: ViewGroup = view.findViewById(R.id.containerNoteListItem)
        val noteDeleteView: ImageButton = view.findViewById(R.id.note_delete)
        val noteShareView: ImageButton = view.findViewById(R.id.note_share)
        val noteCloudView: ImageView = view.findViewById(R.id.note_cloud_view)
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
        viewHolder.noteShareView.setOnClickListener {
            onShare(it.tag as Note)
        }
        return viewHolder
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val note = dataSet[position]
        viewHolder.noteHeaderView.text = getHeaderText(note.noteContent.content)
        viewHolder.noteContentView.text = getContentText(note.noteContent.content)
        viewHolder.noteCloudView.background =  context.getDrawable(isCloudSynced(note))
        viewHolder.noteContainerView.tag = note
        viewHolder.noteDeleteView.tag = note
        viewHolder.noteShareView.tag = note
    }

    private fun isCloudSynced(note: Note): Int {
        return if (note.cloudTokenId != null)
            R.drawable.cloud_synced_true
        else
            R.drawable.cloud_synced_false
    }

    private fun getHeaderText(noteContent: String): String {
        val defaultHeaderCharsCountLength = 35
        return noteContent.substring(startIndex = 0,
                endIndex = if (noteContent.indexOf("\n") == -1) if(noteContent.length < defaultHeaderCharsCountLength) noteContent.length
                else defaultHeaderCharsCountLength else noteContent.indexOf("\n"))
    }

    private fun getContentText(noteContent: String): String {
        return noteContent.substring(startIndex = if (noteContent.indexOf("\n") == -1) 0 else noteContent.indexOf("\n")+1,
            endIndex = noteContent.length)
    }

    override fun getItemCount() = dataSet.size

}
