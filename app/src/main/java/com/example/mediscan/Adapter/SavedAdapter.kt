package com.example.mediscan.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.mediscan.Data.Communicator
import com.example.mediscan.Data.Recents
import com.example.mediscan.Data.Saved
import com.example.mediscan.Data.SavedMedicine
import com.example.mediscan.R
import com.google.firebase.database.DatabaseReference

class SavedAdapter(private var savedList: List<SavedMedicine>, communicator: Communicator, savedMedicineDB: DatabaseReference) :
    RecyclerView.Adapter<SavedAdapter.ViewHolder>() {

    private val comm:Communicator = communicator
    private val db: DatabaseReference = savedMedicineDB

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var savemedtext : TextView = itemView.findViewById(R.id.savetextid)
        var savefilledcard : RelativeLayout = itemView.findViewById(R.id.savedfill)
        var saveemptycard :  LinearLayout = itemView.findViewById(R.id.emptysaved)



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.saved_card, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        var isEmptyCard: Boolean = false

//        fun changeToEmptyCard(){
//            holder.saveemptycard.visibility = View.VISIBLE
//            holder.savefilledcard.visibility = View.GONE
//        }
        fun changeToFilled(){
            holder.savemedtext.text = savedList[position].name
            holder.savefilledcard.visibility = View.VISIBLE
            holder.savefilledcard.setOnClickListener { v: View ->
                comm.passDataCom(savedList[position].name, savedList[position].id, savedList[position].brandName)
            }

    // TODO: Add delete functions for saved items
//            holder.savefilledcard.setOnLongClickListener (
//                comm.deleteFromDB(db, savedList[position].id)
//            )
            holder.saveemptycard.visibility = View.GONE
        }
        fun checktog(){
           if (isEmptyCard) {
//               changeToEmptyCard()
           } else {
               changeToFilled()
           }
        }
        for(rec in savedList) {
            checktog()
        }
    }

    override fun getItemCount(): Int {
        return savedList.size
    }

}