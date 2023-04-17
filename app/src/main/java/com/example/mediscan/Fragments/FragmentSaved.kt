package com.example.mediscan.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mediscan.Adapter.NotesAdapter
import com.example.mediscan.Adapter.RecentsAdapter
import com.example.mediscan.Adapter.SavedAdapter
import com.example.mediscan.Data.*
import com.example.mediscan.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_saved.*


class SavedFragment : Fragment() {
    private var TAG = "Saved Fragment"
    private lateinit var savedMedicineDB: DatabaseReference
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var comm:Communicator

    private val savedMedicineList = mutableListOf<SavedMedicine>()
    val recentList = ArrayList<Recents>()
    val notesList = ArrayList<Notes>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_saved, container,false)
        comm = requireActivity() as Communicator
        firebaseAuth = FirebaseAuth.getInstance()
        loadSavedMedicines()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recent_medication.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recent_medication.adapter = RecentsAdapter(recentList)
        saved_medication.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        saved_medication.adapter = SavedAdapter(savedMedicineList,comm, savedMedicineDB)
        notes_medication.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        notes_medication.adapter = NotesAdapter(notesList)
        recentdata()
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

    private fun loadSavedMedicines() {
        savedMedicineList.clear()
        savedMedicineDB = FirebaseDatabase.getInstance().getReference("users").child(firebaseAuth.uid!!).child("saved_medicines")
        //val myRef = database.getReference("narrow_search")
        //Toast.makeText(context,"Data from firebase: $myRef",Toast.LENGTH_LONG).show()


        savedMedicineDB.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (mdsnapshot in snapshot.children) {
                    savedMedicineList.add(
                        SavedMedicine(
                            mdsnapshot.child("name").value.toString(),
                            mdsnapshot.child("id").value.toString(),
                            mdsnapshot.child("brandName").value.toString())
                        )

                }
                saved_medication.adapter = SavedAdapter(savedMedicineList,comm, savedMedicineDB)
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })
    }
}