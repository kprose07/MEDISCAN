package com.example.mediscan.Adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.SpinnerAdapter
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.mediscan.Data.ProfileRemind
import com.example.mediscan.R

class ProfileRemindAdapter(private var remindList: List<ProfileRemind>, private var remindPopupCard: CardView?, private var itemtx: Spinner,  private var close: View) :
    RecyclerView.Adapter<ProfileRemindAdapter.ViewHolder>() {


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var profileremind: TextView = itemView.findViewById(R.id.remind_card_title)
        var filledcard: LinearLayout = itemView.findViewById(R.id.plin)
        var emptycard: LinearLayout = itemView.findViewById(R.id.pelin)
        var rcTitle: TextView = itemView.findViewById(R.id.remind_card_title)
        //var isEmptyCard: Boolean = remindList[position].isprEmpty



        //remindPopupCard?.visibility = View.GONE
        init {

        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.remind_card, parent, false)

        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        var dataClassProf: ProfileRemind = remindList[position]

        if (remindList[position].isprEmpty) {
            holder.emptycard.visibility = View.VISIBLE
            holder.filledcard.visibility = View.GONE

        } else {
            //holder.profileremind.text = remindList[position].title
            holder.filledcard.visibility = View.VISIBLE
            holder.emptycard.visibility = View.GONE

        }
        holder.itemView.setOnClickListener { v: View ->
            // TODO: Launch next screen for the medicines
            // val position: Int = adapterPosition

            remindPopupCard?.visibility = View.VISIBLE


            close.setOnClickListener {

                if(itemtx.selectedItem.toString() != "Select Medicine") {
                    remindList[position].isprEmpty = false
                    remindList[position].title = itemtx.selectedItem.toString()
                    holder.profileremind.text = remindList[position].title
                }else {
                    remindList[position].isprEmpty = true
                    remindList[position].title = "Empty"
                    holder.profileremind.text = remindList[position].title
                }
                remindPopupCard?.visibility = View.GONE
                notifyItemChanged(position)
            }
            notifyItemChanged(position)
        }
    }



    override fun getItemCount(): Int {
        return remindList.size
    }

}
