package com.example.mediscan.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.mediscan.Accordresults
import com.example.mediscan.Data.Medicine
import com.example.mediscan.Data.Recents
import com.example.mediscan.R
import java.util.HashMap

class RecentsAdapter(private var recentList: List<Recents>) :
    RecyclerView.Adapter<RecentsAdapter.ViewHolder>() {


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var recmedtext : TextView = itemView.findViewById(R.id.nsc_title)
        // var linearlayout : LinearLayout = itemView.findViewById(R.id.linear)
        //var emptycard : CardView = itemView.findViewById(R.id.rec_empty_card)
       //var filledcard : CardView = itemView.findViewById(R.id.rec_filled_card)



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.reccent_card, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.recmedtext.text = recentList[position].reccentMedicine
        //var isEmptyCard: Boolean= recentList[position].isrEmpty

       /* for(rec in recentList) {
            if (rec.isrEmpty) {
                holder.emptycard.visibility = VISIBLE
                holder.filledcard.visibility = GONE
            } else {
                holder.emptycard.visibility = GONE
                holder.filledcard.visibility = VISIBLE
            }
        }*/
    }

    override fun getItemCount(): Int {
        return recentList.size
    }

}