package com.example.mediscan.Fragments

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
import androidx.fragment.app.Fragment
import com.google.firebase.database.DatabaseReference
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mediscan.Adapter.NarrowAdapter
import com.example.mediscan.Data.NarrowDownSearch
import com.example.mediscan.R
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_pillsd.*



class PillsFragment : Fragment()  {

    private lateinit var database: DatabaseReference
    private val TAG = "Home Fragment"
    private var narrowList = mutableListOf<NarrowDownSearch>()
    private val narrowSuggestions = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?{
        val view = inflater.inflate(R.layout.fragment_pillsd, container,false)
        if (narrowList.isEmpty()) loadDataBase()
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       // if (narrowList.isEmpty()) loadDataBase()
        narrow_down_recycler.layoutManager = GridLayoutManager(activity, 3)
        narrow_down_recycler.adapter = NarrowAdapter(narrowList)

        // TODO: Create function for search view code below
        val searchView: SearchView = view.findViewById(R.id.narrowsearch)
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

                    narrowSuggestions.forEachIndexed { index, suggestion ->
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
                //hideKeyboard()

                val cursor = searchView.suggestionsAdapter.getItem(position) as Cursor
                val selection =
                    cursor.getString(cursor.getColumnIndex(SearchManager.SUGGEST_COLUMN_TEXT_1))
                searchView.setQuery(selection, false)

                Toast.makeText(context, "You have selected $selection", Toast.LENGTH_LONG).show()

                return true
            }

            override fun onSuggestionSelect(position: Int): Boolean {
                return false
            }
        })
    }

    private fun loadDataBase() {
        var database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("narrow_search")
        //Toast.makeText(context,"Data from firebase: $myRef",Toast.LENGTH_LONG).show()

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
                for (narrowsearch in narrowList)
                    narrowSuggestions.add(narrowsearch.title)
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })
    }

}
