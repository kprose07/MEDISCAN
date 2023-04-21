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
import com.example.mediscan.Data.Notes
import com.example.mediscan.Data.Recents
import com.example.mediscan.Data.Saved
import com.example.mediscan.R

class NotesAdapter(private var notesList: ArrayList<Notes>, private var specialNotes: LinearLayout) :
    RecyclerView.Adapter<NotesAdapter.ViewHolder>() {


    private var nList: ArrayList<Notes> = notesList
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

        var isEmptyCard: Boolean = notesList[position].isnEmpty

        fun changeToEmptyCard(){
            holder.notesmptycard.visibility = View.VISIBLE
            holder.notesfilledcard.visibility = View.GONE
        }
        fun changeToFilled(){
            holder.notesmedtext.text = notesList[position].notestitle
            holder.notesfilledcard.visibility = View.VISIBLE
            holder.notesmptycard.visibility = View.GONE
            holder.notesfilledcard.setOnClickListener{v:View ->
                specialNotes.visibility = View.VISIBLE
            }
        }

           if (isEmptyCard) {
               changeToEmptyCard()
           } else {
               changeToFilled()
           }
    }

    override fun getItemCount(): Int {
        return notesList.size
    }

}