package com.example.mediscan.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mediscan.Adapter.RecentsAdapter
import com.example.mediscan.Data.Recents
import com.example.mediscan.R
import kotlinx.android.synthetic.main.fragment_pillsd.*
import kotlinx.android.synthetic.main.fragment_saved.*


class SavedFragment : Fragment() {
    val recentList = ArrayList<Recents>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_saved, container,false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recent_medication.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recent_medication.adapter = RecentsAdapter(recentList)
        recentList.add(
            Recents(
                "Monjoro",
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
        recentList.add(
            Recents(
                "Monjoro",
                1,
                true

            )
        )
        recentList.add(
                Recents(
                    "Monjoro",
                    1,
                    true

                )
                )
        recentList.add(
                Recents(
                    "Monjoro",
                    1,
                    true

                )
                )
        recentList.add(
                Recents(
                    "Monjoro",
                    1,
                    true

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

}