package com.mikelau.notes.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.mikelau.notes.R
import com.mikelau.notes.ui.adapters.NoteAdapter
import com.mikelau.notes.viewmodels.NoteViewModel
import kotlinx.android.synthetic.main.fragment_note_list.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class NoteListFragment : Fragment(R.layout.fragment_note_list) {

    private val noteViewModel: NoteViewModel by viewModel()
    private lateinit var adapter: NoteAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvMain.layoutManager = LinearLayoutManager(activity)
        rvMain.setHasFixedSize(true)

        adapter = NoteAdapter()
        rvMain.adapter = adapter

        ItemTouchHelper(object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                noteViewModel.delete(adapter.getNote(viewHolder.adapterPosition))
                Snackbar.make(view, R.string.message_note_deleted, Snackbar.LENGTH_LONG).show()
            }
        }).attachToRecyclerView(rvMain)

        adapter.onItemClick = { note ->
            findNavController().navigate(NoteListFragmentDirections.createNote(
                note.id,
                note.title,
                note.description,
                note.priority
            ))
        }

        initViewModel()
    }

    private fun initViewModel() {
        // Show Database List
        noteViewModel.getAllNotes().observe(activity!!, Observer {
            adapter.submitList(it)
        })

        // Show Progress Bar
        noteViewModel.showLoading.observe(activity!!, Observer { showLoading ->
            mainProgressBar?.visibility =
                if (showLoading!!) View.VISIBLE else View.GONE
        })

        // Show Error
        noteViewModel.showError.observe(activity!!, Observer { showError ->
            Snackbar.make(view!!, showError, Snackbar.LENGTH_LONG).show()
        })
    }
}

