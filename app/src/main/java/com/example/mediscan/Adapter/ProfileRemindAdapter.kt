package com.example.mediscan.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.mediscan.Data.ProfileRemind
import com.example.mediscan.Data.Recents
import com.example.mediscan.R

class ProfileRemindAdapter(private var remindList: List<ProfileRemind>) :
    RecyclerView.Adapter<ProfileRemindAdapter.ViewHolder>() {


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var profileremind : TextView = itemView.findViewById(R.id.profile_card_title)
        var filledcard : LinearLayout = itemView.findViewById(R.id.plin)
        var emptycard :  LinearLayout = itemView.findViewById(R.id.pelin)



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.remind_card, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        var isEmptyCard: Boolean = remindList[position].isprEmpty

        fun changeToEmptyCard(){
            holder.emptycard.visibility = View.VISIBLE
            holder.filledcard.visibility = View.GONE
        }
        fun changeToFilled(){
            holder.profileremind.text = remindList[position].title
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
        for(rec in remindList) {
            checktog()
        }
    }

    override fun getItemCount(): Int {
        return remindList.size
    }

}