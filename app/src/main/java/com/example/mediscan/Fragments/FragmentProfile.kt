package com.example.mediscan.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mediscan.Adapter.ProfileRemindAdapter
import com.example.mediscan.Adapter.RecentsAdapter
import com.example.mediscan.Data.ProfileRemind
import com.example.mediscan.Data.Recents
import com.example.mediscan.R

import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_saved.*

class ProfileFragment : Fragment() {
    val remindList = ArrayList<ProfileRemind>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_profile, container,false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        profile_reminder.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        profile_reminder.adapter = ProfileRemindAdapter(remindList)
        reminddata()
    }
    private fun reminddata(){
        remindList.add(
            ProfileRemind(
                "Rose",
                1,
                false

            )
        )
        remindList.add(
            ProfileRemind(
                "Humilin",
                1,
                true

            )
        )
        remindList.add(
            ProfileRemind(
                "Humilin",
                1,
                true

            )
        )
    }
}