package com.mikelau.notes.ui.notes

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.mikelau.notes.R
import kotlinx.android.synthetic.main.activity_note.*

const val EXTRA_ID: String = "com.mikelau.notestest.ui.notes.EXTRA_ID"
const val EXTRA_TITLE: String = "com.mikelau.notestest.ui.notes.EXTRA_TITLE"
const val EXTRA_DESCRIPTION: String = "com.mikelau.notestest.ui.notes.EXTRA_DESCRIPTION"
const val EXTRA_PRIORITY: String = "com.mikelau.notestest.ui.notes.EXTRA_PRIORITY"

class NoteActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)

        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_close)

        if (intent.hasExtra(EXTRA_ID)) {
            title = "Edit Note"
            etTitle.setText(intent.getStringExtra(EXTRA_TITLE))
            etDescription.setText(intent.getStringExtra(EXTRA_DESCRIPTION))
            npPriority.value = intent.getIntExtra(EXTRA_PRIORITY, 1)
        } else title = "Add Note"
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.menu_note, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.save_note -> {
                saveNote()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun saveNote() {
        val title = etTitle.text.toString()
        val description = etDescription.text.toString()
        val priority = npPriority.value

        if (title.isBlank()) {
            tilTitle.error = "Title is required"
            return
        }

        if (description.isBlank()) {
            tilDescription.error = "Description is required"
            return
        }

        val data = Intent()
        data.putExtra(EXTRA_TITLE, title)
        data.putExtra(EXTRA_DESCRIPTION, description)
        data.putExtra(EXTRA_PRIORITY, priority)

        val id = intent.getIntExtra(EXTRA_ID, -1)
        if (id != -1) data.putExtra(EXTRA_ID, id)

        setResult(RESULT_OK, data)
        finish()
    }
}
