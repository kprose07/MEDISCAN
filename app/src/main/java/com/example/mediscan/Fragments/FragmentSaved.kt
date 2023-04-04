package com.example.mediscan.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mediscan.Adapter.NotesAdapter
import com.example.mediscan.Adapter.RecentsAdapter
import com.example.mediscan.Adapter.SavedAdapter
import com.example.mediscan.Data.Notes
import com.example.mediscan.Data.Recents
import com.example.mediscan.Data.Saved
import com.example.mediscan.R
import kotlinx.android.synthetic.main.fragment_saved.*


class SavedFragment : Fragment() {
    val recentList = ArrayList<Recents>()
    val savedList = ArrayList<Saved>()
    val notesList = ArrayList<Notes>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_saved, container,false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recent_medication.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recent_medication.adapter = RecentsAdapter(recentList)
        saved_medication.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        saved_medication.adapter = SavedAdapter(savedList)
        notes_medication.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        notes_medication.adapter = NotesAdapter(notesList)
        recentdata()
        saveddata()
        notesddata()
    }
    private fun recentdata(){
        recentList.add(
            Recents(
                "Rose",
                1,
                false

            )
        )
        recentList.add(
            Recents(
                "Humilin",
                1,
                true

            )
        )
        recentList.add(
            Recents(
                "Humilin",
                1,
                true

            )
        )
        recentList.add(
            Recents(
                "Humilin",
                1,
                false

            )
        )
        recentList.add(
            Recents(
                "Monjoro",
                1,
                true

            )
        )

    }
    private fun saveddata(){
        savedList.add(
            Saved(
                "Monjoro",
                1,
                false

            )
        )
        savedList.add(
            Saved(
                "Humilin",
                1,
                true

            )
        )
    }
    private fun notesddata(){
        notesList.add(
            Notes(
                "Monjoro",
                1,
                true

            )
        )
        notesList.add(
            Notes(
                "Humilin",
                1,
                false

            )
        )
    }
}