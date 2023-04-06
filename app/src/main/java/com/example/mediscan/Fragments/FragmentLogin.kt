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


class LoginFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_login, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}
