package com.example.mediscan.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.database.DatabaseReference
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mediscan.Adapter.NarrowAdapter
import com.example.mediscan.Data.Medicine
import com.example.mediscan.Data.NarrowDownSearch
import com.example.mediscan.R
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_pillsd.*



class PillsFragment : androidx.fragment.app.Fragment()  {

    private lateinit var database: DatabaseReference
    private val TAG = "Pills Fragment"
    private var narrowList = mutableListOf<NarrowDownSearch>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_pillsd, container,false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(narrowList.isEmpty()) {dataBase() }
        narrow_down_recycler.layoutManager = GridLayoutManager(activity, 3)
        narrow_down_recycler.adapter = NarrowAdapter(narrowList)
    }

    private fun dataBase() {
        var database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("narrow_search")

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                for (snapshot in dataSnapshot.children) {
                    // Toast.makeText(context,"${snapshot.child("name").value.toString()}",Toast.LENGTH_SHORT).show()
                    narrowList.add(
                        NarrowDownSearch(
                            snapshot.child("title").value.toString(),
                            snapshot.child("img_url").value.toString(),
                            snapshot.child("common_perscribe").value.toString()
                        )
                    )
                }

            }


            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })
    }
}
