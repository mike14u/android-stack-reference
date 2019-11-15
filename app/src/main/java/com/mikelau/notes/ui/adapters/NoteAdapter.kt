package com.mikelau.notes.ui.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mikelau.notes.R
import com.mikelau.notes.data.models.Note
import com.mikelau.notes.util.getColorPriority
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_note.*

class NoteAdapter : ListAdapter<Note, NoteAdapter.NoteHolder>(DiffCallback()) {

    var onItemClick: ((Note) -> Unit)? = null

    override fun onBindViewHolder(holder: NoteHolder, position: Int) {
        val currentNote: Note = getItem(position)
        holder.tvTitle.text = currentNote.title
        holder.tvDescription.text = currentNote.description
        holder.viewPriority.setBackgroundColor(Color.parseColor(getColorPriority(currentNote.priority)))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteHolder {
        return NoteHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_note,
                parent,
                false
            )
        )
    }

    fun getNote(position: Int): Note {
        return getItem(position)
    }

    class DiffCallback : DiffUtil.ItemCallback<Note>() {
        override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem.title == newItem.title
                    && oldItem.description == newItem.description
                    && oldItem.priority == newItem.priority
        }
    }

    inner class NoteHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer {
        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(getItem(adapterPosition))
            }
        }
    }
}
