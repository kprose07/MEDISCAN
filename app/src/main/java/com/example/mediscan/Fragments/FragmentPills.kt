package com.example.mediscan.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.mediscan.R

import kotlinx.android.synthetic.main.fragment_pillsd.*

class PillsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_pillsd, container,false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        iv_pillsd.setOnClickListener {
            Toast.makeText(context, "You clicked on the pills image", Toast.LENGTH_SHORT).show()
        }
    }

}