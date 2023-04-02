package com.example.mediscan.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.mediscan.Data.Recents
import com.example.mediscan.R

class RecentsAdapter(private var recentList: List<Recents>) :
    RecyclerView.Adapter<RecentsAdapter.ViewHolder>() {


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var recmedtext : TextView = itemView.findViewById(R.id.nsc_title)
        var filledcard : LinearLayout = itemView.findViewById(R.id.rlin)
        var emptycard :  LinearLayout = itemView.findViewById(R.id.emptylin)



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.reccent_card, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        var isEmptyCard: Boolean = recentList[position].isrEmpty

        fun changeToEmptyCard(){
            holder.emptycard.visibility = View.VISIBLE
            holder.filledcard.visibility = View.GONE
        }
        fun changeToFilled(){
            holder.recmedtext.text = recentList[position].reccentMedicine
            holder.filledcard.visibility = View.VISIBLE
            holder.emptycard.visibility = View.GONE
        }
        fun checktog(){
           if (isEmptyCard) {
               changeToEmptyCard()
           } else {
               changeToFilled()
           }
        }
        for(rec in recentList) {
            checktog()
        }
    }

    override fun getItemCount(): Int {
        return recentList.size
    }

}