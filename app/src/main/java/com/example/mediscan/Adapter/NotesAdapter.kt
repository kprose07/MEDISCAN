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

class NotesAdapter(private var notesList: ArrayList<Note>) :
    RecyclerView.Adapter<NotesAdapter.ViewHolder>() {


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var notesmedtext : TextView = itemView.findViewById(R.id.noteid)
        var notesfilledcard : RelativeLayout = itemView.findViewById(R.id.notesfill)
        var notesmptycard :  LinearLayout = itemView.findViewById(R.id.emptynotes)
//        var notesTitle: TextInputEditText = itemView.findViewById(R.id.noteTitleInput)
//        var notesBody: TextInputEditText = itemView.findViewById(R.id.notesBodyInput)
//        var savedCard: LinearLayout = itemView.findViewById(R.id.specialNotes)
//        var saveNote: Button = itemView.findViewById(R.id.notesSave)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.notes_card, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


            holder.notesmedtext.text = notesList[position].title
            holder.notesfilledcard.visibility = View.VISIBLE
            holder.notesmptycard.visibility = View.GONE
//            holder.notesfilledcard.setOnClickListener{v: View ->
//                holder.notesTitle.setText(notesList[position].title)
//                holder.notesBody.setText(notesList[position].body)
//                holder.saveNote.text = "Delete"
//                holder.savedCard.visibility = View.VISIBLE
//
//            }

    }

    override fun getItemCount(): Int {
        return notesList.size
    }

}