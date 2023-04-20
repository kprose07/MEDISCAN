package com.example.mediscan.Fragments

import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mediscan.Adapter.NotesAdapter
import com.example.mediscan.Adapter.RecentsAdapter
import com.example.mediscan.Adapter.SavedAdapter
import com.example.mediscan.Data.*
import com.example.mediscan.R
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_saved.*


class SavedFragment : Fragment() {
    private var TAG = "Saved Fragment"
    private lateinit var savedMedicineDB: DatabaseReference
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var comm:Communicator
    private lateinit var specialNotes: LinearLayout
    private lateinit var saveNote: Button
    private lateinit var notesTitle: TextInputEditText
    private lateinit var notesBody: TextInputEditText
    private lateinit var dbRef: DatabaseReference


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
        specialNotes = view.findViewById(R.id.specialNotes)
        saveNote = view.findViewById(R.id.notesSave)
        notesTitle = view.findViewById(R.id.noteTitleInput)
        notesBody = view.findViewById(R.id.notesBodyInput)
        dbRef = FirebaseDatabase.getInstance().getReference("notes")
        loadSavedMedicines()
        notesddata()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        profile_reminder.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        profile_reminder.adapter = RecentsAdapter(recentList)
        saved_medication.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        saved_medication.adapter = SavedAdapter(savedMedicineList,comm, savedMedicineDB)
        notes_medication.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        notes_medication.adapter = NotesAdapter(notesList, specialNotes)
        recentdata()

        saveNote.setOnClickListener{v: View ->
            saveNote()
            specialNotes.visibility = View.GONE
        }
//        notesddata()
    }

    private fun saveNote() {

        //getting values
        val nTitle = notesTitle.text.toString()
        val nBody = notesBody.text.toString()

        if (nTitle.isEmpty()) {
            notesTitle.error = "Please enter name"
        }
        if (nBody.isEmpty()) {
            notesBody.error = "Please enter age"
        }

        val noteId = dbRef.push().key!!

        val note = Note(noteId, nTitle, nBody)

        dbRef.child(noteId).setValue(note)
            .addOnCompleteListener {
                Toast.makeText(context, "Data inserted successfully", Toast.LENGTH_LONG).show()

                notesTitle.text?.clear()
                notesBody.text?.clear()


            }.addOnFailureListener { err ->
                Toast.makeText(context, "Error ${err.message}", Toast.LENGTH_LONG).show()
            }

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