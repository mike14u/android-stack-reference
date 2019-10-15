package com.mikelau.notes.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.mikelau.notes.R
import com.mikelau.notes.data.models.Note
import com.mikelau.notes.ui.notes.*
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel

const val ADD_NOTE_REQUEST = 1
const val EDIT_NOTE_REQUEST = 2

class MainActivity : AppCompatActivity() {

    private val noteViewModel: NoteViewModel by viewModel()
    private lateinit var adapter: NoteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fabAddNote.setOnClickListener {
            val intent = Intent(this@MainActivity, NoteActivity::class.java)
            startActivityForResult(intent, ADD_NOTE_REQUEST)
        }

        rvMain.layoutManager = LinearLayoutManager(this)
        rvMain.setHasFixedSize(true)

        adapter = NoteAdapter()
        rvMain.adapter = adapter

        ItemTouchHelper(object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: ViewHolder,
                target: ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: ViewHolder, direction: Int) {
                noteViewModel.delete(adapter.getNote(viewHolder.adapterPosition))
                Toast.makeText(this@MainActivity, "Note deleted", Toast.LENGTH_SHORT).show()
            }
        }).attachToRecyclerView(rvMain)

        adapter.onItemClick = { note ->
            val intent = Intent(this@MainActivity, NoteActivity::class.java)
            intent.putExtra(EXTRA_ID, note.id)
            intent.putExtra(EXTRA_TITLE, note.title)
            intent.putExtra(EXTRA_DESCRIPTION, note.description)
            intent.putExtra(EXTRA_PRIORITY, note.priority)
            startActivityForResult(intent, EDIT_NOTE_REQUEST)
        }

        initViewModel()
    }

    private fun initViewModel() {
        // Show Database List
        noteViewModel.getAllNotes().observe(this, Observer {
            adapter.submitList(it)
        })

        // Show Progress Bar
        noteViewModel.showLoading.observe(this, Observer { showLoading ->
            mainProgressBar.visibility = if (showLoading!!) View.VISIBLE else View.GONE
        })

        // Show Error
        noteViewModel.showError.observe(this, Observer { showError ->
            Toast.makeText(this, showError, Toast.LENGTH_SHORT).show()
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == ADD_NOTE_REQUEST && resultCode == RESULT_OK) {
            val title = data?.getStringExtra(EXTRA_TITLE) ?: ""
            val description = data?.getStringExtra(EXTRA_DESCRIPTION) ?: ""
            val priority = data?.getIntExtra(EXTRA_PRIORITY, 1) ?: 1

            val note = Note(title, description, priority)
            noteViewModel.insert(note)

            Toast.makeText(this, "Note saved", Toast.LENGTH_SHORT).show()
        } else if (requestCode == EDIT_NOTE_REQUEST && resultCode == RESULT_OK) {
            val id = data?.getIntExtra(EXTRA_ID, -1) ?: -1

            if(id == -1)  {
                Toast.makeText(this, "Note can't be updated", Toast.LENGTH_SHORT).show()
                return
            }

            val title = data?.getStringExtra(EXTRA_TITLE) ?: ""
            val description = data?.getStringExtra(EXTRA_DESCRIPTION) ?: ""
            val priority = data?.getIntExtra(EXTRA_PRIORITY, 1) ?: 1

            val note = Note(title, description, priority)
            note.id = id
            noteViewModel.update(note)

            Toast.makeText(this, "Note updated", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Note not saved", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.menu_delete_all, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.delete_all_notes -> {
                noteViewModel.deleteAllNotes()
                Toast.makeText(this, "All notes deleted", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
