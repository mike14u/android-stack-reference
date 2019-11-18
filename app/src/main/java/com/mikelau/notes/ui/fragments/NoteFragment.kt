package com.mikelau.notes.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.mikelau.notes.R
import com.mikelau.notes.viewmodels.NoteViewModel
import kotlinx.android.synthetic.main.fragment_note.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class NoteFragment : Fragment(R.layout.fragment_note) {

    private val noteViewModel: NoteViewModel by viewModel()
    private val args: NoteFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(args.NOTEID > 0) {
            etTitle.setText(args.NOTETITLE)
            etDescription.setText(args.NOTEDESCRIPTION)
            etImage.setText(args.NOTEIMAGE)
            npPriority.value = args.NOTEPRIORITY
        }

        buttonSave.setOnClickListener {
            (activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(getView()?.windowToken, 0)
            if(args.NOTEID > 0) {
                noteViewModel.modifyNote(etTitle.text.toString(),
                    etDescription.text.toString(), etImage.text.toString(),
                    npPriority.value,
                    args.NOTEID)
                Snackbar.make(view, R.string.message_note_updated, Snackbar.LENGTH_LONG).show()
            } else  {
                noteViewModel.saveNote(etTitle.text.toString(), etDescription.text.toString(), etImage.text.toString(), npPriority.value)
                Snackbar.make(view, R.string.message_note_saved, Snackbar.LENGTH_LONG).show()
            }
            findNavController().popBackStack()
        }
    }

}

