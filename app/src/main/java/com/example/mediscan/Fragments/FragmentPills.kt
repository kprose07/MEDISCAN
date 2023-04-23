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
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.google.firebase.database.DatabaseReference
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mediscan.Adapter.NarrowAdapter
import com.example.mediscan.Data.Communicator
import com.example.mediscan.Data.NarrowDownSearch
import com.example.mediscan.Data.SavedMedicine
import com.example.mediscan.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_pillsd.*
import kotlinx.android.synthetic.main.fragment_pillsd.popup_title
import kotlinx.android.synthetic.main.fragment_pillsd.popup_close
import kotlinx.android.synthetic.main.fragment_results.*


class PillsFragment : Fragment()  {

    private lateinit var database: DatabaseReference
    private val TAG = "Home Fragment"
    private var narrowList = mutableListOf<NarrowDownSearch>()
    private val narrowSuggestions = mutableListOf<String>()
    private var medicineName: String? = ""
    private var medicineId: String? = ""
    private var brandName: String? = ""
    private var popupCard: CardView? = null
    private lateinit var comm: Communicator
    private lateinit var firebaseAuth: FirebaseAuth
    var saveToggle: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?{
        val view = inflater.inflate(R.layout.fragment_pillsd, container,false)
        medicineName = arguments?.getString("mdSelected")
        medicineId = arguments?.getString("mdId")
        brandName = arguments?.getString("brandName")
        if (narrowList.isEmpty()) loadDataBase()
        firebaseAuth = FirebaseAuth.getInstance()
        comm = requireActivity() as Communicator
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mdName:TextView = view.findViewById(R.id.medicine)


        mdName.text = medicineName

        //popup card
        popupCard = view.findViewById(R.id.popup_card)

        popupCard = view.findViewById(R.id.popup_card)


        //Toggle for Saved Button
        val ssavedMedicine = SavedMedicine(medicineName.toString(), medicineId.toString(), brandName.toString())
        val medicineDB = FirebaseDatabase.getInstance().getReference("users").child(firebaseAuth.uid!!).child("saved_medicines").orderByChild("id").equalTo(ssavedMedicine.id)

        medicineDB.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if( snapshot!=null && snapshot.getChildren()!=null &&
                    snapshot.getChildren().iterator().hasNext()){
                    savemed_button.setBackgroundResource(R.drawable.ic_savefilled)
                    saveToggle = true
                }else{
                    savemed_button.setBackgroundResource(R.drawable.ic_saveempty)
                    saveToggle = false
                }

            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })
        medicine.setOnClickListener{
            saveToggle=!saveToggle
            if(saveToggle){
                saveMedicine(medicineName.toString(), medicineId.toString(), brandName.toString())
                savemed_button.setBackgroundResource(R.drawable.ic_savefilled)

            }else{
                deleteMedicine(medicineName.toString(), medicineId.toString(), brandName.toString())
                savemed_button.setBackgroundResource(R.drawable.ic_saveempty)

            }

        }








        narrow_down_recycler.layoutManager = GridLayoutManager(activity, 3)
        narrow_down_recycler.adapter = medicineName?.let {
            NarrowAdapter(narrowList, popupCard,popup_title,popup_detail,popup_close, see_more, comm,medicineName.toString(), brandName.toString())
        }



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
         database = FirebaseDatabase.getInstance().getReference("narrow_search").child("$medicineId")
        //val myRef = database.getReference("narrow_search")
        //Toast.makeText(context,"Data from firebase: $myRef",Toast.LENGTH_LONG).show()

        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (medsnapshot in snapshot.children) {
                    narrowList.add(

                        NarrowDownSearch(
                            medsnapshot.key.toString(),
                            medsnapshot.value.toString()
                        )
                    )
                }
                narrow_down_recycler.adapter = NarrowAdapter(narrowList,popupCard,popup_title,popup_detail,popup_close, see_more,comm, medicineName.toString(), brandName.toString())
                for (narrowsearch in narrowList)
                    narrowSuggestions.add(narrowsearch.title)
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })
    }

    // TODO: call this function using onclick listener on saved medicine button

    fun saveMedicine(name: String, id: String, brandName: String) {
        val medicineDB = FirebaseDatabase.getInstance().getReference("users").child(firebaseAuth.uid!!).child("saved_medicines")
        val savedMedicine = SavedMedicine(name, id, brandName)
       // savemed_button.setBackgroundResource(R.drawable.ic_savefilled)

        medicineDB.child(savedMedicine.id).setValue(savedMedicine).addOnSuccessListener {
            Toast.makeText(context,"Saved Medicince", Toast.LENGTH_SHORT).show()

        }.addOnFailureListener {
            Toast.makeText(context, "NOT SAVED", Toast.LENGTH_SHORT).show()

        }
    }
    fun deleteMedicine(name: String, id: String, brandName: String) {
        val medicineDB = FirebaseDatabase.getInstance().getReference("users").child(firebaseAuth.uid!!).child("saved_medicines")
        val savedMedicine = SavedMedicine(name, id, brandName)
       //savemed_button.setBackgroundResource(R.drawable.ic_saveempty)
        medicineDB.child(savedMedicine.id).removeValue().addOnSuccessListener {
            Toast.makeText(context,"Deleted Medicince", Toast.LENGTH_SHORT).show()

        }.addOnFailureListener {
            Toast.makeText(context, "NOT DELETED", Toast.LENGTH_SHORT).show()

        }

    }

}
