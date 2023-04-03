package com.example.mediscan.Fragments

//import com.example.mediscan.hideKeyboard

import android.annotation.SuppressLint
import android.app.SearchManager
import android.database.Cursor
import android.database.MatrixCursor
import android.os.Bundle
import android.provider.BaseColumns
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CursorAdapter
import android.widget.SearchView
import android.widget.SimpleCursorAdapter
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mediscan.Adapter.NarrowAdapter
import com.example.mediscan.Adapter.RecyclerAdapter
import com.example.mediscan.Data.Communicator
import com.example.mediscan.Data.Medicine
import com.example.mediscan.R
import com.example.mediscan.hideKeyboard
import com.google.firebase.database.*
import com.google.firebase.firestore.auth.User
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_pillsd.*
import kotlinx.android.synthetic.main.fragment_results.*


class HomeFragment : androidx.fragment.app.Fragment() {
    private lateinit var database: DatabaseReference
    private val TAG = "Home Fragment"
    private val medicineSuggestions = mutableListOf<String>()

    private var medicineList = mutableListOf<Medicine>()

    private lateinit var comm:Communicator

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val rootView = inflater.inflate(R.layout.fragment_home, container, false)
        comm = requireActivity() as Communicator
        if (medicineList.isEmpty()) loadDataFromDatabase()
        return rootView
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        hideKeyboard()
        super.onViewCreated(view, savedInstanceState)
        val medicineListAdapter = RecyclerAdapter(medicineList, comm)
        recycler_view.layoutManager = GridLayoutManager(activity, 3)
        recycler_view.adapter = medicineListAdapter

        // TODO: Create function for search view code below
        val searchView: SearchView = view.findViewById(R.id.idSV)
        searchView.isFocusable = false
        val from = arrayOf(SearchManager.SUGGEST_COLUMN_TEXT_1)
        val to = intArrayOf(R.id.searchItemID)
        val cursorAdapter = SimpleCursorAdapter(
            context,
            R.layout.suggestion_item_layout,
            null,
            from,
            to,
            CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER
        )
        searchView.suggestionsAdapter = cursorAdapter
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                val cursor =
                    MatrixCursor(arrayOf(BaseColumns._ID, SearchManager.SUGGEST_COLUMN_TEXT_1))
                newText?.let {

                    medicineSuggestions.forEachIndexed { index, suggestion ->
                        if (suggestion.contains(newText, true))
                            cursor.addRow(
                                arrayOf(
                                    index,
                                    suggestion.replaceFirstChar { ch -> ch.uppercaseChar() })
                            )
                    }
                }

                cursorAdapter.changeCursor(cursor)
                return true
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
        })

        searchView.setOnSuggestionListener(object : SearchView.OnSuggestionListener {
            @SuppressLint("Range")
            override fun onSuggestionClick(position: Int): Boolean {
//                hideKeyboard()

                val cursor = searchView.suggestionsAdapter.getItem(position) as Cursor
                val selection =
                    cursor.getString(cursor.getColumnIndex(SearchManager.SUGGEST_COLUMN_TEXT_1))
                searchView.setQuery(selection, false)

                Toast.makeText(context, "You have selected $selection", Toast.LENGTH_LONG).show()
                comm.passDataCom(selection)
                return true
            }

            override fun onSuggestionSelect(position: Int): Boolean {
                return false
            }
        })
    }


    private fun loadDataFromDatabase()  {
        database = FirebaseDatabase.getInstance().getReference("medicines")

        //Toast.makeText(context,"Data from firebase: $myRef",Toast.LENGTH_LONG).show()

        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                for (medsnapshot in dataSnapshot.children) {
                    // Toast.makeText(context,"${snapshot.child("name").value.toString()}",Toast.LENGTH_SHORT).show()
                    medicineList.add(
                        Medicine(
                            medsnapshot.child("name").value.toString(),
                            medsnapshot.child("img_url").value.toString(),
                            medsnapshot.child("common_presc").value.toString()
                        )
                    )
                }
                recycler_view.adapter = RecyclerAdapter(medicineList,comm)
                for (medicine in medicineList)
                    medicineSuggestions.add(medicine.name)
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })
    }

}