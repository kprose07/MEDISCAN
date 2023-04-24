package com.example.mediscan.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mediscan.Data.Note
import com.example.mediscan.Data.Notes
import com.example.mediscan.R
import com.google.android.material.textfield.TextInputEditText

class NotesAdapter(private var notesList: ArrayList<Note>,private val onItemClickedListenerNotes:OnItemClickedListener) :
    RecyclerView.Adapter<NotesAdapter.ViewHolder>() {


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var notesmedtext : TextView = itemView.findViewById(R.id.noteid)
        var notesfilledcard : RelativeLayout = itemView.findViewById(R.id.notesfill)
        var notesmptycard :  LinearLayout = itemView.findViewById(R.id.emptynotes)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.notes_card, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


            holder.notesmedtext.text = notesList[position].title
            holder.notesfilledcard.visibility = View.VISIBLE
            holder.notesmptycard.visibility = View.GONE
        holder.itemView.setOnClickListener { v: View ->
            // TODO: Launch next screen for the medicines
            // val position: Int = holder.adapterPosition

            // remindPopupCard?.visibility = View.VISIBLE
            onItemClickedListenerNotes.onCLickNotes(position)
            return@setOnClickListener
            //notifyItemChanged(position)
        }

    }
    interface OnItemClickedListener{
        fun onCLickNotes(position: Int)
    }
    override fun getItemCount(): Int {
        return notesList.size
    }

}