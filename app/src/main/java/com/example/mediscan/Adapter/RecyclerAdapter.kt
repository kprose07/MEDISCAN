package com.example.mediscan.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.mediscan.Data.Medicine
import com.example.mediscan.R
import java.util.HashMap


class RecyclerAdapter(private var medicines: List<Medicine>) :
    RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    // TODO: add the rest of the medicines
    // TODO: what to look at for hash map
    private val imageMap: HashMap<String, Int> = hashMapOf(
        "adcirca" to R.drawable.adcirca,
        "alimta" to R.drawable.alimta,
        "amyvid" to R.drawable.avyvid
    )

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val itemTitle: TextView = itemView.findViewById(R.id.md_title)
        val itemImage: ImageView = itemView.findViewById(R.id.md_image)

        init {
            itemView.setOnClickListener { v: View ->
                // TODO: Launch next screen for the medicines
                val position: Int = adapterPosition
                Toast.makeText(
                    itemView.context,
                    "You clicked on item # ${position + 1}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.medicine_layout, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemTitle.text = medicines[position].name
        // TODO: what to look at for hash map
        imageMap[medicines[position].name]?.let { holder.itemImage.setImageResource(it) }
    }

    override fun getItemCount(): Int {
        return medicines.size
    }

}